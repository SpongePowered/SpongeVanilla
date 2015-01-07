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

package org.granitepowered.granite.impl.plugin;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

public class GranitePluginContainer implements PluginContainer {

    String id;
    String name;
    String version;
    String dependencies;
    Object instance;

    Plugin annotation;
    Class<?> clazz;

    Logger logger;

    public GranitePluginContainer(Class<?> clazz) {
        annotation = clazz.getAnnotation(Plugin.class);
        this.clazz = clazz;
        id = annotation.id();
        name = annotation.name();
        version = annotation.version();
        dependencies = annotation.dependencies();

        logger = LoggerFactory.getLogger(name);

        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Throwables.propagate(e);
        }
    }

    public String getDependencies() {
        return dependencies;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }

    public Plugin getAnnotation() {
        return annotation;
    }
}
