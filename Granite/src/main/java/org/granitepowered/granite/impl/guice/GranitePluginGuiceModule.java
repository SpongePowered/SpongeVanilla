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
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.granitepowered.granite.Granite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.config.DefaultConfig;

import java.io.File;
import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Provider;

public class GranitePluginGuiceModule extends AbstractModule {

    private final PluginContainer container;

    public GranitePluginGuiceModule(PluginContainer container) {
        this.container = container;
    }

    @Override
    protected void configure() {
        DefaultConfig pluginSharedDirConfig = new ConfigFileAnnotation(true);
        DefaultConfig pluginPrivateDirConfig = new ConfigFileAnnotation(false);
        ConfigDir privateConfigDir = new ConfigDirAnnotation(false);

        bind(PluginContainer.class).toInstance(container);
        bind(Logger.class).toInstance(LoggerFactory.getLogger(container.getId()));
        bind(File.class).annotatedWith(privateConfigDir).toProvider(PluginDataDirProvider.class).in(Scopes.SINGLETON);
        bind(File.class).annotatedWith(pluginSharedDirConfig).toProvider(PluginSharedDirConfigFileProvider.class).in(Scopes.SINGLETON);
        bind(File.class).annotatedWith(pluginPrivateDirConfig).toProvider(PluginPrivateDirConfigFileProvider.class).in(Scopes.SINGLETON);
        bind(ConfigurationLoader.class).annotatedWith(pluginSharedDirConfig).toProvider(PluginHoconConfigProvider.class);
    }

    private static class PluginSharedDirConfigFileProvider implements Provider<File> {

        private final PluginContainer container;
        private final File configDir;

        @Inject
        public PluginSharedDirConfigFileProvider(PluginContainer container, @ConfigDir(sharedRoot = true) File configDir) {
            this.container = container;
            this.configDir = configDir;
        }

        @Override
        public File get() {
            return new File(configDir, container.getId() + ".conf");
        }

    }

    private static class PluginPrivateDirConfigFileProvider implements Provider<File> {

        private final PluginContainer container;
        private final File configDir;

        @Inject
        public PluginPrivateDirConfigFileProvider(PluginContainer container, @ConfigDir(sharedRoot = false) File configDir) {
            this.container = container;
            this.configDir = configDir;
        }

        @Override
        public File get() {
            return new File(configDir, container.getId() + ".conf");
        }

    }

    private static class PluginHoconConfigProvider implements Provider<ConfigurationLoader> {

        private final File configFile;

        @Inject
        public PluginHoconConfigProvider(@DefaultConfig(sharedRoot = true) File configFile) {
            this.configFile = configFile;
        }

        @Override
        public ConfigurationLoader get() {
            return HoconConfigurationLoader.builder().setFile(configFile).build();
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
