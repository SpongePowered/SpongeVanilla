/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitepowered.granite.impl.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.GraniteGameRegistry;
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.impl.plugin.GranitePluginManager;
import org.granitepowered.granite.impl.service.event.GraniteEventManager;
import org.granitepowered.granite.impl.service.scheduler.GraniteScheduler;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Server;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.service.scheduler.Scheduler;

import javax.inject.Provider;
import java.io.File;

public class GraniteGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        ConfigDir sharedConfigDir = new ConfigDirAnnotation(true);

        bind(Granite.class).in(Scopes.SINGLETON);

        bind(Game.class).toProvider(GraniteProvider.class).in(Scopes.SINGLETON);
        bind(Server.class).toProvider(GraniteServerProvider.class).in(Scopes.SINGLETON);
        bind(PluginManager.class).to(GranitePluginManager.class).in(Scopes.SINGLETON);
        bind(GameRegistry.class).to(GraniteGameRegistry.class).in(Scopes.SINGLETON);
        bind(EventManager.class).to(GraniteEventManager.class).in(Scopes.SINGLETON);
        bind(Scheduler.class).to(GraniteScheduler.class).in(Scopes.SINGLETON);
        bind(File.class).annotatedWith(sharedConfigDir).toProvider(GlobalPluginDataDirProvider.class).in(Scopes.SINGLETON);
    }

    /**
     * Provides Granite. This is used instead of <code>to(Granite.class)</code>
     * because otherwise it would be immediately loaded by Guice and then class rewriting
     * would fail.
     */
    private static class GraniteProvider implements Provider<Game> {

        @Override
        public Game get() {
            return new Granite();
        }

    }

    private static class GraniteServerProvider implements Provider<Server> {

        @Override
        public Server get() {
            return new GraniteServer();
        }

    }

    private static class GlobalPluginDataDirProvider implements Provider<File> {

        @Override
        public File get() {
            return Granite.getInstance().getServerConfig().getPluginDataDirectory();
        }

    }

}
