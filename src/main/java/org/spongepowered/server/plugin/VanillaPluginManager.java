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
package org.spongepowered.server.plugin;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.spongepowered.server.plugin.PluginScanner.ARCHIVE_FILTER;

import com.google.common.collect.Maps;
import net.minecraft.launchwrapper.Launch;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.common.Sponge;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class VanillaPluginManager implements PluginManager {

    public static final String SCAN_CLASSPATH_PROPERTY = "sponge.plugins.scanClasspath";
    private static final boolean SCAN_CLASSPATH = Boolean.getBoolean(SCAN_CLASSPATH_PROPERTY);

    private final Map<String, PluginContainer> plugins = Maps.newHashMap();
    private final Map<Object, PluginContainer> pluginInstances = Maps.newIdentityHashMap();

    @Inject
    public VanillaPluginManager(@Named("Sponge") PluginContainer spongePlugin, @Named("Minecraft") PluginContainer minecraftPlugin) {
        registerPlugin(spongePlugin);
        registerPlugin(minecraftPlugin);
    }

    private void registerPlugin(PluginContainer plugin) {
        this.plugins.put(plugin.getId(), plugin);
        this.pluginInstances.put(plugin.getInstance(), plugin);
    }

    public void loadPlugins() throws IOException {
        Set<String> plugins;

        if (SCAN_CLASSPATH) {
            Sponge.getLogger().info("Scanning classpath for plugins...");

            // Find plugins on the classpath
            plugins = PluginScanner.scanClassPath(Launch.classLoader);
            if (!plugins.isEmpty()) {
                loadPlugins("classpath", plugins);
            }
        }

        try (DirectoryStream<Path> dir = Files.newDirectoryStream(Sponge.getPluginsDir(), ARCHIVE_FILTER)) {
            for (Path jar : dir) {
                // Search for plugins in the JAR
                plugins = PluginScanner.scanZip(jar);

                if (!plugins.isEmpty()) {
                    // Add plugin to the classpath
                    Launch.classLoader.addURL(jar.toUri().toURL());

                    // Load the plugins
                    loadPlugins(jar, plugins);
                }
            }
        }
    }

    private void loadPlugins(Object source, Iterable<String> plugins) {
        for (String plugin : plugins) {
            try {
                Class<?> pluginClass = Class.forName(plugin);
                VanillaPluginContainer container = new VanillaPluginContainer(pluginClass);
                registerPlugin(container);
                Sponge.getGame().getEventManager().registerListeners(container, container.getInstance());

                Sponge.getLogger().info("Loaded plugin: {} {} (from {})", container.getName(), container.getVersion(), source);
            } catch (Throwable e) {
                Sponge.getLogger().error("Failed to load plugin: {} (from {})", plugin, source, e);
            }
        }
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        checkNotNull(instance, "instance");

        if (instance instanceof PluginContainer) {
            return Optional.of((PluginContainer) instance);
        }

        return Optional.ofNullable(this.pluginInstances.get(instance));
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        return Optional.ofNullable(this.plugins.get(id));
    }

    @Override
    public Logger getLogger(PluginContainer plugin) {
        return ((VanillaPluginContainer) plugin).getLogger();
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return Collections.unmodifiableCollection(this.plugins.values());
    }

    @Override
    public boolean isLoaded(String id) {
        return this.plugins.containsKey(id);
    }

}
