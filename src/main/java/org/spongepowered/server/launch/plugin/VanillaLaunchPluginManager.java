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

import org.spongepowered.common.launch.SpongeLaunch;
import org.spongepowered.server.launch.VanillaLaunch;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javax.annotation.Nullable;

public final class VanillaLaunchPluginManager {

    private VanillaLaunchPluginManager() {
    }

    @Nullable
    private static Map<String, PluginCandidate> plugins;

    public static void findPlugins(boolean scanClasspath, boolean scanFullClasspath) throws IOException {
        VanillaLaunch.getLogger().info("Searching for plugins...");

        PluginScanner pluginScanner = new PluginScanner();

        if (scanClasspath || scanFullClasspath) {
            VanillaLaunch.getLogger().info("Scanning classpath for plugins...");

            ClassLoader loader = VanillaLaunch.class.getClassLoader();
            if (loader instanceof URLClassLoader) {
                pluginScanner.scanClassPath((URLClassLoader) loader, scanFullClasspath);
            } else {
                VanillaLaunch.getLogger().error("Cannot search for plugins on classpath: Unsupported class loader: {}", loader.getClass());
            }
        }

        Path pluginsDir = SpongeLaunch.getPluginsDir();
        if (Files.isDirectory(pluginsDir)) {
            pluginScanner.scanDirectory(pluginsDir);
        } else {
            // Create plugin folder
            Files.createDirectories(pluginsDir);
        }

        // Scan additional plugin directory
        pluginsDir = SpongeLaunch.getAdditionalPluginsDir();
        if (Files.isDirectory(pluginsDir)) {
            pluginScanner.scanDirectory(pluginsDir);
        }

        plugins = pluginScanner.getPlugins();
        VanillaLaunch.getLogger().info("{} plugin(s) found", plugins.size());
    }

    public static Map<String, PluginCandidate> getPlugins() {
        checkState(plugins != null, "Plugin folder was not scanned yet");
        Map<String, PluginCandidate> result = plugins;
        plugins = null;
        return result;
    }

}
