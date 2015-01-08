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
import com.google.inject.Key;
import com.google.inject.Scopes;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.GraniteGameRegistry;
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.impl.plugin.GranitePluginManager;
import org.granitepowered.granite.impl.service.event.GraniteEventManager;
import org.granitepowered.granite.impl.service.scheduler.GraniteScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.service.scheduler.Scheduler;
import org.spongepowered.api.util.config.ConfigFile;

import java.io.File;
import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Provider;

public class GraniteGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        PluginScope pluginScope = new PluginScope();

        DefaultConfig pluginConfig = new ConfigFileAnnotation(true);
        ConfigDir sharedConfigDir = new ConfigDirAnnotation(true);
        ConfigDir privateConfigDir = new ConfigDirAnnotation(false);

        bind(Granite.class).in(Scopes.SINGLETON);

        bindScope(PluginScoped.class, pluginScope);
        bind(PluginScope.class).toInstance(pluginScope);

        bind(Game.class).toProvider(GraniteServerProvider.class).in(Scopes.SINGLETON);
        bind(PluginManager.class).to(GranitePluginManager.class).in(Scopes.SINGLETON);
        bind(GameRegistry.class).to(GraniteGameRegistry.class).in(Scopes.SINGLETON);
        bind(EventManager.class).to(GraniteEventManager.class).in(Scopes.SINGLETON);
        bind(Scheduler.class).to(GraniteScheduler.class).in(Scopes.SINGLETON);
        bind(File.class).annotatedWith(sharedConfigDir).toProvider(GlobalPluginDataDirProvider.class).in(Scopes.SINGLETON);

        bind(PluginContainer.class).toProvider(PluginContainerProvider.class).in(PluginScoped.class);
        bind(Logger.class).toProvider(PluginLoggerProvider.class).in(PluginScoped.class);
        bind(File.class).annotatedWith(privateConfigDir).toProvider(PluginDataDirProvider.class).in(PluginScoped.class);
        bind(File.class).annotatedWith(pluginConfig).toProvider(PluginConfigFileProvider.class).in(PluginScoped.class);
        bind(ConfigFile.class).annotatedWith(pluginConfig).toProvider(PluginHoconConfigProvider.class).in(PluginScoped.class);
    }

    /**
     * Provides GraniteServer. This is used instead of <code>to(GraniteServer.class)</code>
     * because otherwise it would be immediately loaded by Guice and then class rewriting
     * would fail.
     */
    private static class GraniteServerProvider implements Provider<Game> {

        @Override
        public Game get() {
            return new GraniteServer();
        }

    }

    private static class PluginContainerProvider implements Provider<PluginContainer> {

        private final PluginScope scope;

        @Inject
        public PluginContainerProvider(PluginScope scope) {
            this.scope = scope;
        }

        @Override
        public PluginContainer get() {
            return scope.getInstance(Key.get(PluginContainer.class));
        }

    }

    private static class PluginLoggerProvider implements Provider<Logger> {

        private final PluginContainer container;

        @Inject
        public PluginLoggerProvider(PluginContainer container) {
            this.container = container;
        }

        @Override
        public Logger get() {
            return LoggerFactory.getLogger(container.getId());
        }

    }

    private static class PluginConfigFileProvider implements Provider<File> {

        private final PluginContainer container;
        private final File configDir;

        @Inject
        public PluginConfigFileProvider(PluginContainer container, @ConfigDir(sharedRoot = true) File configDir) {
            this.container = container;
            this.configDir = configDir;
        }

        @Override
        public File get() {
            return new File(configDir, container.getId() + ".conf");
        }

    }

    private static class PluginHoconConfigProvider implements Provider<ConfigFile> {

        private final File configFile;

        @Inject
        public PluginHoconConfigProvider(@DefaultConfig(sharedRoot = true) File configFile) {
            this.configFile = configFile;
        }

        @Override
        public ConfigFile get() {
            return ConfigFile.parseFile(configFile);
        }

    }

    private static class GlobalPluginDataDirProvider implements Provider<File> {

        @Override
        public File get() {
            return Granite.getInstance().getServerConfig().getPluginDataDirectory();
        }

    }

    private static class PluginDataDirProvider implements Provider<File> {

        private final PluginContainer container;

        @Inject
        public PluginDataDirProvider(PluginContainer container) {
            this.container = container;
        }

        @Override
        public File get() {
            return new File(Granite.getInstance().getServerConfig().getPluginDataDirectory(), container.getId() + "/");
        }

    }

    private static class ConfigDirAnnotation implements ConfigDir {

        private final boolean isShared;

        public ConfigDirAnnotation(boolean isShared) {
            this.isShared = isShared;
        }

        @Override
        public boolean sharedRoot() {
            return isShared;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return ConfigDir.class;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || !(o instanceof ConfigDir)) {
                return false;
            }

            ConfigDir that = (ConfigDir) o;
            return sharedRoot() == that.sharedRoot();
        }

        @Override
        public int hashCode() {
            return (127 * "sharedRoot".hashCode()) ^ Boolean.valueOf(sharedRoot()).hashCode();
        }

    }

    private static class ConfigFileAnnotation implements DefaultConfig {

        private final boolean shared;

        public ConfigFileAnnotation(boolean isShared) {
            this.shared = isShared;
        }

        @Override
        public boolean sharedRoot() {
            return shared;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return DefaultConfig.class;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof DefaultConfig)) {
                return false;
            }

            DefaultConfig that = (DefaultConfig) o;

            return sharedRoot() == that.sharedRoot();
        }

        @Override
        public int hashCode() {
            return (127 * "sharedRoot".hashCode()) ^ Boolean.valueOf(sharedRoot()).hashCode();
        }

    }


}
