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

import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.common.Sponge;
import org.spongepowered.server.util.PathMatchers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Nullable;

final class PluginScanner {

    private static final String PLUGIN_DESCRIPTOR = Type.getDescriptor(Plugin.class);

    private static final String JAVA_HOME = StandardSystemProperty.JAVA_HOME.value();

    private static final String CLASS_EXTENSION = ".class";

    private static final PathMatcher CLASS_FILE = PathMatchers.create("glob:*" + CLASS_EXTENSION);

    private static final PathMatcher ARCHIVE = PathMatchers.create("glob:*.{jar,zip}");
    static final DirectoryStream.Filter<Path> ARCHIVE_FILTER = PathMatchers.createFilter(ARCHIVE);

    private PluginScanner() {
    }

    static Set<String> scanClassPath(URLClassLoader loader) {
        Set<URI> sources = Sets.newHashSet();
        Set<String> plugins = Sets.newHashSet();

        for (URL url : loader.getURLs()) {
            if (!url.getProtocol().equals("file")) {
                Sponge.getLogger().warn("Skipping unsupported classpath source: {}", url);
                continue;
            }

            if (url.getPath().startsWith(JAVA_HOME)) {
                Sponge.getLogger().trace("Skipping JRE classpath entry: {}", url);
                continue;
            }

            URI source;
            try {
                source = url.toURI();
            } catch (URISyntaxException e) {
                Sponge.getLogger().error("Failed to search for classpath plugins in {}", url);
                continue;
            }

            if (sources.add(source)) {
                scanPath(Paths.get(source), plugins);
            }
        }

        Sponge.getLogger().trace("Found {} plugin(s): {}", plugins.size(), plugins);
        return plugins;
    }

    private static Set<String> scanPath(Path path) {
        Set<String> plugins = Sets.newHashSet();
        scanPath(path, plugins);
        Sponge.getLogger().trace("Found {} plugin(s): {}", plugins.size(), plugins);
        return plugins;
    }

    private static void scanPath(Path path, Set<String> plugins) {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                scanDirectory(path, plugins);
            } else {
                scanZip(path, plugins);
            }
        }
    }

    private static void scanDirectory(Path dir, final Set<String> plugins) {
        Sponge.getLogger().trace("Scanning {} for plugins", dir);

        try {
            Files.walkFileTree(dir, ImmutableSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    if (CLASS_FILE.matches(path.getFileName())) {
                        try (InputStream in = Files.newInputStream(path)) {
                            String plugin = findPlugin(in);
                            if (plugin != null) {
                                plugins.add(plugin);
                            }
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            Sponge.getLogger().error("Failed to search for plugins in {}", dir, e);
        }
    }

    static Set<String> scanZip(Path path) {
        Set<String> plugins = Sets.newHashSet();
        scanZip(path, plugins);
        Sponge.getLogger().trace("Found {} plugin(s): {}", plugins.size(), plugins);
        return plugins;
    }

    private static void scanZip(Path path, Set<String> plugins) {
        Sponge.getLogger().trace("Scanning {} for plugins", path);

        if (!ARCHIVE.matches(path.getFileName())) {
            return;
        }

        // Open the zip file so we can scan for plugins
        try (ZipFile zip = new ZipFile(path.toFile())) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(CLASS_EXTENSION)) {
                    continue;
                }

                try (InputStream in = zip.getInputStream(entry)) {
                    String plugin = findPlugin(in);
                    if (plugin != null) {
                        plugins.add(plugin);
                    }
                }
            }
        } catch (IOException e) {
            Sponge.getLogger().error("Failed to load plugin JAR: {}", path, e);
        }
    }

    @Nullable
    private static String findPlugin(InputStream in) throws IOException {
        ClassReader reader = new ClassReader(in);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        if (classNode.visibleAnnotations != null) {
            for (AnnotationNode node : classNode.visibleAnnotations) {
                if (node.desc.equals(PLUGIN_DESCRIPTOR)) {
                    return classNode.name.replace('/', '.');
                }
            }
        }

        return null;
    }

}
