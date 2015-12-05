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
package org.spongepowered.server.guice;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.SimpleServiceManager;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.event.SpongeEventManager;
import org.spongepowered.common.guice.ConfigDirAnnotation;
import org.spongepowered.common.registry.SpongeGameRegistry;
import org.spongepowered.common.world.SpongeTeleportHelper;
import org.spongepowered.server.SpongeVanilla;
import org.spongepowered.server.VanillaGame;
import org.spongepowered.server.plugin.MinecraftPluginContainer;
import org.spongepowered.server.plugin.VanillaPluginManager;

import java.io.File;
import java.nio.file.Path;

public class VanillaGuiceModule extends AbstractModule {

    private final SpongeVanilla instance;
    private final Logger logger;

    public VanillaGuiceModule(SpongeVanilla instance, Logger logger) {
        this.instance = instance;
        this.logger = logger;
    }

    @Override
    protected void configure() {
        bind(SpongeVanilla.class).toInstance(this.instance);
        bind(Logger.class).toInstance(this.logger);

        bind(PluginContainer.class).annotatedWith(named(SpongeImpl.ECOSYSTEM_NAME)).toInstance(this.instance);
        bind(PluginContainer.class).annotatedWith(named("Minecraft")).to(MinecraftPluginContainer.class).in(Scopes.SINGLETON);

        bind(Game.class).to(VanillaGame.class).in(Scopes.SINGLETON);
        bind(PluginManager.class).to(VanillaPluginManager.class).in(Scopes.SINGLETON);
        bind(EventManager.class).to(SpongeEventManager.class).in(Scopes.SINGLETON);
        bind(GameRegistry.class).to(SpongeGameRegistry.class).in(Scopes.SINGLETON);
        bind(ServiceManager.class).to(SimpleServiceManager.class).in(Scopes.SINGLETON);
        bind(TeleportHelper.class).to(SpongeTeleportHelper.class).in(Scopes.SINGLETON);

        ConfigDirAnnotation sharedRoot = new ConfigDirAnnotation(true);
        bind(Path.class).annotatedWith(sharedRoot).toInstance(SpongeImpl.getConfigDir());
        bind(File.class).annotatedWith(sharedRoot).toInstance(SpongeImpl.getConfigDir().toFile());
    }

}
