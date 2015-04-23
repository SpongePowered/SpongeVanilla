/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.vanilla;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.inject.Guice;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.ConstructionEvent;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.LoadCompleteEvent;
import org.spongepowered.api.event.state.PostInitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppedEvent;
import org.spongepowered.api.event.state.StateEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.command.SimpleCommandService;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.persistence.SerializationService;
import org.spongepowered.api.service.scheduler.AsynchronousScheduler;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.command.SpongeCommandDisambiguator;
import org.spongepowered.common.interfaces.IMixinServerCommandManager;
import org.spongepowered.common.service.permission.SpongeContextCalculator;
import org.spongepowered.common.service.permission.SpongePermissionService;
import org.spongepowered.common.service.persistence.SpongeSerializationService;
import org.spongepowered.common.service.scheduler.AsyncScheduler;
import org.spongepowered.common.service.scheduler.SyncScheduler;
import org.spongepowered.common.service.sql.SqlServiceImpl;
import org.spongepowered.common.util.SpongeHooks;
import org.spongepowered.vanilla.guice.VanillaGuiceModule;
import org.spongepowered.vanilla.plugin.VanillaPluginManager;

import java.io.File;
import java.io.IOException;

public final class SpongeVanilla implements PluginContainer {

    public static final SpongeVanilla INSTANCE = new SpongeVanilla();

    private final Game game;

    private SpongeVanilla() {

        // Initialize Sponge
        Guice.createInjector(new VanillaGuiceModule(this, LogManager.getLogger("Sponge"))).getInstance(Sponge.class);

        this.game = Sponge.getGame();
    }

    public void load() {
        try {
            Sponge.getLogger().info("Loading Sponge...");

            File gameDir = Sponge.getGameDirectory();
            File pluginsDir = Sponge.getPluginsDirectory();

            if (!gameDir.isDirectory() || !pluginsDir.isDirectory()) {
                if (!pluginsDir.mkdirs()) {
                    throw new IOException("Failed to create plugins folder");
                }
            }

            try {
                SimpleCommandService commandService = new SimpleCommandService(this.game, new SpongeCommandDisambiguator(this.game));
                this.game.getServiceManager().setProvider(this, CommandService.class, commandService);
            } catch (ProviderExistsException e) {
                Sponge.getLogger().warn("Non-Sponge CommandService already registered", e);
            }

            try {
                this.game.getServiceManager().setProvider(this, SqlService.class, new SqlServiceImpl());
            } catch (ProviderExistsException e) {
                Sponge.getLogger().warn("Non-Sponge SqlService already registered", e);
            }

            try {
                this.game.getServiceManager().setProvider(this, SynchronousScheduler.class, SyncScheduler.getInstance());
                this.game.getServiceManager().setProvider(this, AsynchronousScheduler.class, AsyncScheduler.getInstance());
            } catch (ProviderExistsException e) {
                Sponge.getLogger().error("Non-Sponge scheduler already registered", e);
            }

            try {
                SerializationService serializationService = new SpongeSerializationService();
                this.game.getServiceManager().setProvider(this, SerializationService.class, serializationService);
            } catch (ProviderExistsException e) {
                Sponge.getLogger().warn("Non-Sponge SerializationService already registered", e);
            }

            this.game.getEventManager().register(this, this);
            this.game.getEventManager().register(this, this.game.getRegistry());

            Sponge.getLogger().info("Loading plugins...");
            ((VanillaPluginManager) this.game.getPluginManager()).loadPlugins();
            postState(ConstructionEvent.class);
            Sponge.getLogger().info("Initializing plugins...");
            postState(PreInitializationEvent.class);

            this.game.getServiceManager().potentiallyProvide(PermissionService.class).executeWhenPresent(new Predicate<PermissionService>() {

                @Override
                public boolean apply(PermissionService input) {
                    input.registerContextCalculator(new SpongeContextCalculator());
                    return true;
                }
            });

            SpongeHooks.enableThreadContentionMonitoring();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void initialize() {
        postState(InitializationEvent.class);

        if (!this.game.getServiceManager().provide(PermissionService.class).isPresent()) {
            try {
                this.game.getServiceManager().setProvider(this, PermissionService.class, new SpongePermissionService());
            } catch (ProviderExistsException e) {
                // It's a fallback, ignore
            }
        }

        postState(PostInitializationEvent.class);

        SerializationService service = this.game.getServiceManager().provide(SerializationService.class).get();
        ((SpongeSerializationService) service).completeRegistration();

        Sponge.getLogger().info("Successfully loaded and initialized plugins.");

        postState(LoadCompleteEvent.class);
    }

    public void postState(Class<? extends StateEvent> type) {
        this.game.getEventManager().post(SpongeEventFactory.createState(type, this.game));
    }

    @Subscribe
    public void onServerStarting(ServerStartingEvent event) {
        ((IMixinServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerEarlyCommands(this.game);
    }

    @Subscribe
    public void onServerStarted(ServerStartedEvent event) {
        ((IMixinServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerLowPriorityCommands(this.game);
    }

    @Subscribe
    public void onServerStopped(ServerStoppedEvent event) throws IOException {
        ((SqlServiceImpl) this.game.getServiceManager().provideUnchecked(SqlService.class)).close();
    }

    @Override
    public String getId() {
        return "sponge";
    }

    @Override
    public String getName() {
        return "Sponge";
    }

    @Override
    public String getVersion() {
        return this.game.getImplementationVersion();
    }

    @Override
    public Object getInstance() {
        return this;
    }
}
