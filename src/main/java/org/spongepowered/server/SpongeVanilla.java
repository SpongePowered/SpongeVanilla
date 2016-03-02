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
import static org.spongepowered.server.launch.VanillaCommandLine.BONUS_CHEST;
import static org.spongepowered.server.launch.VanillaCommandLine.PORT;
import static org.spongepowered.server.launch.VanillaCommandLine.WORLD_DIR;
import static org.spongepowered.server.launch.VanillaCommandLine.WORLD_NAME;

import com.google.inject.Guice;
import com.google.inject.Injector;
import joptsimple.OptionSet;
import net.minecraft.init.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.common.SpongeBootstrap;
import org.spongepowered.common.SpongeGame;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeInternalListeners;
import org.spongepowered.common.entity.ai.SpongeEntityAICommonSuperclass;
import org.spongepowered.common.interfaces.IMixinServerCommandManager;
import org.spongepowered.common.network.message.SpongeMessageHandler;
import org.spongepowered.common.plugin.SpongePluginContainer;
import org.spongepowered.common.registry.RegistryHelper;
import org.spongepowered.common.service.permission.SpongeContextCalculator;
import org.spongepowered.common.service.permission.SpongePermissionService;
import org.spongepowered.common.service.sql.SqlServiceImpl;
import org.spongepowered.common.util.SpongeHooks;
import org.spongepowered.common.world.storage.SpongePlayerDataHandler;
import org.spongepowered.server.guice.VanillaGuiceModule;
import org.spongepowered.server.launch.VanillaCommandLine;
import org.spongepowered.server.plugin.VanillaPluginManager;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public final class SpongeVanilla extends SpongePluginContainer {

    public static final SpongeVanilla INSTANCE = new SpongeVanilla();

    private final SpongeGame game;

    private SpongeVanilla() {
        Guice.createInjector(new VanillaGuiceModule(this, LogManager.getLogger(SpongeImpl.ECOSYSTEM_NAME))).getInstance(SpongeImpl.class);

        this.game = SpongeImpl.getGame();

        RegistryHelper.setFinalStatic(Sponge.class, "game", this.game);
    }

    public static void main(String[] args) {
        OptionSet options = VanillaCommandLine.parse(args);

        // Note: This launches the server instead of MinecraftServer.main
        // Keep command line options up-to-date with Vanilla
        try {
            Bootstrap.register();

            File worldDir = options.has(WORLD_DIR) ? options.valueOf(WORLD_DIR) : new File(".");
            final DedicatedServer server = new DedicatedServer(worldDir);

            if (options.has(WORLD_NAME)) {
                server.setFolderName(options.valueOf(WORLD_NAME));
            }

            if (options.has(PORT)) {
                server.setServerPort(options.valueOf(PORT));
            }

            if (options.has(BONUS_CHEST)) {
                server.canCreateBonusChest(true);
            }

            server.startServerThread();
            Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));
        } catch (Exception e) {
            SpongeImpl.getLogger().fatal("Failed to start the Minecraft server", e);
            System.exit(1);
        }
    }

    public void preInitialize() throws Exception {
        SpongeImpl.getLogger().info("Loading Sponge...");

        this.game.getEventManager().registerListeners(this, SpongeInternalListeners.getInstance());

        // Pre-initialize registry
        this.game.getRegistry().preRegistryInit();
        SpongeBootstrap.initializeServices();
        SpongeBootstrap.initializeCommands();
        SpongeImpl.getRegistry().preInit();

        SpongeImpl.getLogger().info("Loading plugins...");
        ((VanillaPluginManager) this.game.getPluginManager()).loadPlugins();
        SpongeImpl.postState(GameConstructionEvent.class, GameState.CONSTRUCTION);
        SpongeImpl.getLogger().info("Initializing plugins...");
        SpongeImpl.postState(GamePreInitializationEvent.class, GameState.PRE_INITIALIZATION);

        checkState(Class.forName("org.spongepowered.api.entity.ai.task.AbstractAITask").getSuperclass()
                .equals(SpongeEntityAICommonSuperclass.class));

        SpongeInternalListeners.getInstance().registerServiceCallback(PermissionService.class,
                input -> input.registerContextCalculator(new SpongeContextCalculator()));

        SpongeHooks.enableThreadContentionMonitoring();

        SpongeMessageHandler.init();
    }

    public void initialize() {
        SpongeImpl.getRegistry().init();

        if (!this.game.getServiceManager().provide(PermissionService.class).isPresent()) {
            SpongePermissionService service = new SpongePermissionService(this.game);
            // Setup default permissions
            service.getGroupForOpLevel(1).getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, "minecraft.selector", Tristate.TRUE);
            service.getGroupForOpLevel(2).getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, "minecraft.commandblock", Tristate.TRUE);
            this.game.getServiceManager().setProvider(this, PermissionService.class, service);
        }

        SpongeImpl.postState(GameInitializationEvent.class, GameState.INITIALIZATION);

        SpongeImpl.getRegistry().postInit();

        SpongeImpl.postState(GamePostInitializationEvent.class, GameState.POST_INITIALIZATION);

        SpongeImpl.getLogger().info("Successfully loaded and initialized plugins.");

        SpongeImpl.postState(GameLoadCompleteEvent.class, GameState.LOAD_COMPLETE);
    }

    public void onServerAboutToStart() {
        ((IMixinServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerEarlyCommands(this.game);
        SpongeImpl.postState(GameAboutToStartServerEvent.class, GameState.SERVER_ABOUT_TO_START);
    }

    public void onServerStarting() {
        SpongeImpl.postState(GameStartingServerEvent.class, GameState.SERVER_STARTING);
        SpongeImpl.postState(GameStartedServerEvent.class, GameState.SERVER_STARTED);
        ((IMixinServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerLowPriorityCommands(this.game);
        SpongePlayerDataHandler.init();
    }

    public void onServerStopping() {
        SpongeImpl.postState(GameStoppingServerEvent.class, GameState.SERVER_STOPPING);
    }

    public void onServerStopped() throws IOException {
        SpongeImpl.postState(GameStoppedServerEvent.class, GameState.SERVER_STOPPED);
        ((SqlServiceImpl) this.game.getServiceManager().provideUnchecked(SqlService.class)).close();
    }

    @Override
    public String getId() {
        return SpongeImpl.ECOSYSTEM_ID;
    }

    @Override
    public String getName() {
        return SpongeImpl.IMPLEMENTATION_NAME.orElse("SpongeVanilla");
    }

    @Override
    public String getVersion() {
        return SpongeImpl.IMPLEMENTATION_VERSION;
    }

    @Override
    public Logger getLogger() {
        return SpongeImpl.getSlf4jLogger();
    }

    @Override
    public Optional<Object> getInstance() {
        return Optional.of(this);
    }

    @Override
    public Injector getInjector() {
        return SpongeImpl.getInjector();
    }

}
