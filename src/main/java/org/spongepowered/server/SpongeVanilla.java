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
package org.spongepowered.server;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Throwables;
import com.google.inject.Guice;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.SpongeEventFactoryUtils;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStateEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.common.SpongeBootstrap;
import org.spongepowered.common.SpongeGame;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeVersion;
import org.spongepowered.common.entity.ai.SpongeEntityAICommonSuperclass;
import org.spongepowered.common.interfaces.IMixinServerCommandManager;
import org.spongepowered.common.registry.RegistryHelper;
import org.spongepowered.common.service.permission.SpongeContextCalculator;
import org.spongepowered.common.service.permission.SpongePermissionService;
import org.spongepowered.common.service.sql.SqlServiceImpl;
import org.spongepowered.common.util.SpongeHooks;
import org.spongepowered.common.util.persistence.SpongeSerializationManager;
import org.spongepowered.server.guice.VanillaGuiceModule;
import org.spongepowered.server.plugin.VanillaPluginManager;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public final class SpongeVanilla implements PluginContainer {

    public static final SpongeVanilla INSTANCE = new SpongeVanilla();

    private final SpongeGame game;

    private SpongeVanilla() {
        Guice.createInjector(new VanillaGuiceModule(this, LogManager.getLogger(SpongeImpl.ECOSYSTEM_NAME))).getInstance(SpongeImpl.class);

        this.game = SpongeImpl.getGame();

        try {
            RegistryHelper.setFinalStatic(Sponge.class, "game", this.game);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        MinecraftServer.main(args);
    }

    public void preInitialize() {
        try {
            SpongeImpl.getLogger().info("Loading Sponge...");

            Files.createDirectories(SpongeImpl.getPluginsDir());

            // Pre-initialize registry
            game.getRegistry().preRegistryInit();
            SpongeBootstrap.initializeServices();
            SpongeImpl.getRegistry().preInit();

            this.game.getEventManager().registerListeners(this, this);

            SpongeImpl.getLogger().info("Loading plugins...");
            ((VanillaPluginManager) this.game.getPluginManager()).loadPlugins();
            postState(GameConstructionEvent.class, GameState.CONSTRUCTION);
            SpongeImpl.getLogger().info("Initializing plugins...");
            postState(GamePreInitializationEvent.class, GameState.PRE_INITIALIZATION);

            checkState(Class.forName("org.spongepowered.api.entity.ai.task.AbstractAITask").getSuperclass()
                    .equals(SpongeEntityAICommonSuperclass.class));

            this.game.getServiceManager().potentiallyProvide(PermissionService.class).executeWhenPresent(
                    input -> input.registerContextCalculator(new SpongeContextCalculator()));

            SpongeHooks.enableThreadContentionMonitoring();
        } catch (IOException | ClassNotFoundException e) {
            throw Throwables.propagate(e);
        }
    }

    public void initialize() {
        SpongeImpl.getRegistry().init();
        postState(GameInitializationEvent.class, GameState.INITIALIZATION);

        if (!this.game.getServiceManager().provide(PermissionService.class).isPresent()) {
            try {
                SpongePermissionService service = new SpongePermissionService(this.game);
                // Setup default permissions
                service.getGroupForOpLevel(2).getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, "minecraft.commandblock", Tristate.TRUE);
                this.game.getServiceManager().setProvider(this, PermissionService.class, service);
            } catch (ProviderExistsException e) {
                // It's a fallback, ignore
            }
        }

        SpongeImpl.getRegistry().postInit();
        SpongeSerializationManager.getInstance().completeRegistration();

        postState(GamePostInitializationEvent.class, GameState.POST_INITIALIZATION);

        SpongeImpl.getLogger().info("Successfully loaded and initialized plugins.");

        postState(GameLoadCompleteEvent.class, GameState.LOAD_COMPLETE);
    }

    @Listener(order = Order.PRE)
    public void onServerAboutToStart(GameAboutToStartServerEvent event) {
        ((IMixinServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerEarlyCommands(this.game);
    }

    @Listener(order = Order.PRE)
    public void onServerStarted(GameStartingServerEvent event) {
        ((IMixinServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerLowPriorityCommands(this.game);
    }

    @Listener(order = Order.PRE)
    public void onServerStopped(GameStoppedServerEvent event) throws IOException {
        ((SqlServiceImpl) this.game.getServiceManager().provideUnchecked(SqlService.class)).close();
    }

    @Override
    public String getId() {
        return SpongeImpl.ECOSYSTEM_ID;
    }

    @Override
    public String getName() {
        return SpongeVersion.IMPLEMENTATION_NAME.orElse("SpongeVanilla");
    }

    @Override
    public String getVersion() {
        return SpongeVersion.IMPLEMENTATION_VERSION;
    }

    @Override
    public Logger getLogger() {
        return SpongeImpl.getSlf4jLogger();
    }

    @Override
    public Optional<Object> getInstance() {
        return Optional.of(this);
    }

    public void postState(Class<? extends GameStateEvent> type, GameState state) {
        ((VanillaGame) SpongeImpl.getGame()).setState(state);
        this.game.getEventManager().post(SpongeEventFactoryUtils.createState(type, this.game));
    }

}
