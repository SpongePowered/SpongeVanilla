/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class GranitePluginManager implements PluginManager {

    Collection<PluginContainer> plugins = new ArrayList<>();

    public void loadPlugins() {
        File[] files = Granite.instance.getServerConfig().getPluginDirectory().listFiles(new FilenameFilter() {
             @Override
             public boolean accept(File arg0, String arg1) {
                 return arg1.endsWith(".jar");
             }
        });

        if (files != null) {
             for (File plugin : files) {
                 Granite.instance.getLogger().info("Loading jarfile plugins/{}", plugin.getName());

                 try {
                     URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{plugin.toURI().toURL()});
                     JarFile jarFile = new JarFile(plugin);

                     Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();

                     while (jarEntryEnumeration.hasMoreElements()) {
                         JarEntry jarEntry = jarEntryEnumeration.nextElement();

                         if (jarEntry.getName().endsWith(".class")) {
                             String className = jarEntry.getName().replaceAll("/", ".").substring(0, jarEntry.getName().length() - ".class".length());

                             try {
                                 Class<?> clazz = classLoader.loadClass(className);

                                 PluginContainer pluginContainer = null;

                                 for (Annotation annotation : clazz.getAnnotations()) {
                                     if (annotation.annotationType().equals(Plugin.class)) {
                                         pluginContainer = new GranitePluginContainer(clazz);
                                     }
                                 }

                                 if (pluginContainer != null) {
                                     plugins.add(pluginContainer);
                                     Granite.instance.getLogger().info("Loaded {} ({})!", pluginContainer.getName(), pluginContainer.getVersion());
                                 }
                             } catch (ClassNotFoundException e) {
                                 e.printStackTrace();
                             }

                         }
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        }
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        for (PluginContainer pluginContainer : plugins) {
            if (pluginContainer.getInstance().equals(instance)) return Optional.of(pluginContainer);
        }
        return Optional.absent();
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        for (PluginContainer pluginContainer : plugins) {
            if (pluginContainer.getId().equals(id)) return Optional.of(pluginContainer);
        }
        return Optional.absent();
    }

    @Override
    public Logger getLogger(PluginContainer plugin) {
        return ((GranitePluginContainer) plugin).getLogger();
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return plugins;
    }
}
