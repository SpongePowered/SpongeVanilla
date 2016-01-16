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

import com.google.common.base.Splitter;
import org.spongepowered.server.launch.VanillaLaunch;
import org.spongepowered.server.launch.transformer.at.AccessTransformers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public final class PluginAccessTransformers {

    private static final String KEY = "FMLAT";
    private static final Splitter VALUE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();
    private static final String LOCATION = "META-INF/";

    private PluginAccessTransformers() {
    }

    public static void register(Path path, JarFile jar) throws IOException {
        Manifest manifest = jar.getManifest();
        if (manifest == null) {
            return;
        }

        String ats = manifest.getMainAttributes().getValue(KEY);
        if (ats == null) {
            return;
        }

        for (String at : VALUE_SPLITTER.split(ats)) {
            String location = LOCATION + at;
            ZipEntry entry = jar.getEntry(location);
            if (entry != null) {
                VanillaLaunch.getLogger().debug("Applying access transformer from {}!{}", path, location);
                try (InputStream in = jar.getInputStream(entry)) {
                    AccessTransformers.register(in);
                } catch (IOException e) {
                    VanillaLaunch.getLogger().warn("Failed to read access transformer from: {}!{}", path, location, e);
                }
            } else {
                VanillaLaunch.getLogger().warn("Found non-existent access transformer in plugin manifest: {}!{}", path, location);
            }
        }
    }

}
