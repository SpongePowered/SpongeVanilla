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

package org.granitepowered.granite.plugin;

import com.google.inject.Injector;
import org.granitepowered.granite.guice.GranitePluginGuiceModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

public class GranitePluginContainer implements PluginContainer {

    private final Plugin annotation;

    private final String id;
    private final String name;
    private final String version;
    private final String dependencies;

    private final Logger logger;
    private final Object instance;

    public GranitePluginContainer(Injector injector, Class<?> clazz) {
        this.annotation = clazz.getAnnotation(Plugin.class);

        this.id = annotation.id();
        this.name = annotation.name();
        this.version = annotation.version();
        this.dependencies = annotation.dependencies();

        this.logger = LoggerFactory.getLogger(name);
        this.instance = injector.createChildInjector(new GranitePluginGuiceModule(this)).getInstance(clazz);
    }

    public Logger getLogger() {
        return logger;
    }

    public Plugin getAnnotation() {
        return annotation;
    }

    public String getDependencies() {
        return dependencies;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }
}
