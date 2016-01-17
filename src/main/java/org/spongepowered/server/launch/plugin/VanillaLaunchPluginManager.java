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
package org.spongepowered.server.launch.plugin;

import static com.google.common.base.Preconditions.checkState;
import static org.spongepowered.server.launch.plugin.PluginScanner.ARCHIVE_FILTER;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import org.spongepowered.common.launch.SpongeLaunch;
import org.spongepowered.server.launch.VanillaLaunch;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;

import javax.annotation.Nullable;

public final class VanillaLaunchPluginManager {

    private VanillaLaunchPluginManager() {
    }

    @Nullable
    private static ImmutableSetMultimap<Object, String> plugins;

    public static void findPlugins(boolean scanClasspath) throws IOException {
        SetMultimap<Object, String> found = LinkedHashMultimap.create();
        Set<String> pluginClasses = null;

        VanillaLaunch.getLogger().info("Searching for plugins...");

        if (scanClasspath) {
            VanillaLaunch.getLogger().info("Scanning classpath for plugins...");

            ClassLoader loader = VanillaLaunch.class.getClassLoader();
            if (loader instanceof URLClassLoader) {
                pluginClasses = PluginScanner.scanClassPath((URLClassLoader) loader);
                found.putAll("classpath", pluginClasses);
            } else {
                VanillaLaunch.getLogger().error("Cannot search for plugins on classpath: Unsupported class loader: {}", loader.getClass());
            }
        }

        Path pluginsDir = SpongeLaunch.getPluginsDir();
        if (Files.exists(pluginsDir)) {
            if (pluginClasses == null) {
                pluginClasses = new HashSet<>();
            }

            try (DirectoryStream<Path> dir = Files.newDirectoryStream(SpongeLaunch.getPluginsDir(), ARCHIVE_FILTER)) {
                for (Path path : dir) {
                    // Search for plugins in the JAR
                    try (JarFile jar = new JarFile(path.toFile())) {
                        Set<String> plugins = PluginScanner.scanZip(path, jar);

                        Iterator<String> itr = plugins.iterator();
                        while (itr.hasNext()) {
                            String plugin = itr.next();
                            if (!pluginClasses.add(plugin)) {
                                VanillaLaunch.getLogger().warn("Skipping duplicate plugin class {} from {}", plugin, path);
                                itr.remove();
                            }
                        }

                        if (!plugins.isEmpty()) {
                            found.putAll(path, plugins);

                            // Look for access transformers
                            PluginAccessTransformers.register(path, jar);
                        }
                    } catch (IOException e) {
                        VanillaLaunch.getLogger().error("Failed to scan plugin JAR: {}", path, e);
                    }
                }
            }
        } else {
            // Create plugin folder
            Files.createDirectories(pluginsDir);
        }

        plugins = ImmutableSetMultimap.copyOf(found);
        VanillaLaunch.getLogger().info("{} plugin(s) found", plugins.size());
    }

    public static ImmutableSetMultimap<Object, String> getPlugins() {
        checkState(plugins != null, "Plugin folder was not scanned yet");
        ImmutableSetMultimap<Object, String> result = plugins;
        plugins = null;
        return result;
    }

}
