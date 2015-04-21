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

import com.google.common.base.Throwables;
import com.google.inject.Guice;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.state.ConstructionEvent;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.LoadCompleteEvent;
import org.spongepowered.api.event.state.PostInitializationEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.StateEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.command.SimpleCommandService;
import org.spongepowered.common.Sponge;
import org.spongepowered.vanilla.guice.VanillaGuiceModule;
import org.spongepowered.vanilla.plugin.VanillaPluginManager;

import java.io.File;
import java.io.IOException;

public final class SpongeVanilla {

    private static final SpongeVanilla instance = new SpongeVanilla();

    public static SpongeVanilla getInstance() {
        return instance;
    }

    private final Plugin plugin;
    private final Game game;

    private SpongeVanilla() {
        this.plugin = new Plugin();

        // Initialize Sponge
        Guice.createInjector(new VanillaGuiceModule(this, LogManager.getLogger())).getInstance(Sponge.class);

        this.game = Sponge.getGame();
    }

    public PluginContainer getPlugin() {
        return this.plugin;
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
                SimpleCommandService commandService = new SimpleCommandService(this.game);
                this.game.getServiceManager().setProvider(this, CommandService.class, commandService);
                this.game.getEventManager().register(this, commandService);
            } catch (ProviderExistsException e) {
                Sponge.getLogger().warn("An unknown CommandService was already registered", e);
            }

            //this.game.getEventManager().register(this, this.game.getRegistry());

            Sponge.getLogger().info("Loading plugins...");
            ((VanillaPluginManager) this.game.getPluginManager()).loadPlugins();
            postState(ConstructionEvent.class);
            Sponge.getLogger().info("Initializing plugins...");
            postState(PreInitializationEvent.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void initialize() {
        postState(InitializationEvent.class);
        postState(PostInitializationEvent.class);
        Sponge.getLogger().info("Successfully loaded and initialized plugins.");

        postState(LoadCompleteEvent.class);
    }

    public void postState(Class<? extends StateEvent> type) {
        this.game.getEventManager().post(SpongeEventFactory.createState(type, this.game));
    }

    private class Plugin implements PluginContainer {

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
            return SpongeVanilla.this.game.getImplementationVersion();
        }

        @Override
        public Object getInstance() {
            return SpongeVanilla.this;
        }

    }

}
