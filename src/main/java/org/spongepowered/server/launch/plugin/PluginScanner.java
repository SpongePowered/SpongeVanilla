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

import static java.util.stream.Collectors.joining;
import static org.spongepowered.api.plugin.Plugin.ID_PATTERN;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.spongepowered.plugin.meta.McModInfo;
import org.spongepowered.plugin.meta.PluginMetadata;
import org.spongepowered.server.launch.VanillaLaunch;
import org.spongepowered.server.launch.plugin.asm.PluginClassVisitor;
import org.spongepowered.server.launch.transformer.at.AccessTransformers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import javax.annotation.Nullable;

final class PluginScanner {

    private static final String CLASS_EXTENSION = ".class";
    private static final String JAR_EXTENSION = ".jar";

    private static final PathMatcher CLASS_FILE = path -> path.toString().endsWith(CLASS_EXTENSION);
    private static final PathMatcher JAR_FILE = path -> path.toString().endsWith(JAR_EXTENSION);
    private static final DirectoryStream.Filter<Path> JAR_FILTER = path -> path.toString().endsWith(JAR_EXTENSION);

    private static final String METADATA_FILE = McModInfo.STANDARD_FILENAME;

    private static final String JAVA_HOME = System.getProperty("java.home");
    private static final Logger logger = VanillaLaunch.getLogger();

    private final Map<String, PluginCandidate> plugins = new HashMap<>();
    private final Set<String> pluginClasses = new HashSet<>();

    @Nullable private FileVisitor<Path> classFileVisitor;

    public Map<String, PluginCandidate> getPlugins() {
        return this.plugins;
    }

    void scanClassPath(URLClassLoader loader, boolean scanJars) {
        Set<URI> sources = new HashSet<>();

        for (URL url : loader.getURLs()) {
            if (!url.getProtocol().equals("file")) {
                logger.warn("Skipping unsupported classpath source: {}", url);
                continue;
            }

            if (url.getPath().startsWith(JAVA_HOME)) {
                logger.trace("Skipping JRE classpath entry: {}", url);
                continue;
            }

            if (!scanJars && url.getFile().endsWith(JAR_EXTENSION)) {
                logger.trace("Skipping classpath JAR file: {}", url);
                continue;
            }

            URI source;
            try {
                source = url.toURI();
            } catch (URISyntaxException e) {
                logger.error("Failed to search for classpath plugins in {}", url);
                continue;
            }

            if (sources.add(source)) {
                Path path = Paths.get(source);
                if (Files.isDirectory(path)) {
                    scanClasspathDirectory(path);
                } else if (scanJars && JAR_FILE.matches(path) && Files.exists(path)) {
                    scanJar(path, true);
                }
            }
        }
    }

    private void scanClasspathDirectory(Path dir) {
        logger.trace("Scanning {} for plugins", dir);

        if (this.classFileVisitor == null) {
            this.classFileVisitor = new ClassFileVisitor();
        }

        try {
            Files.walkFileTree(dir, Collections.singleton(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, this.classFileVisitor);
        } catch (IOException e) {
            logger.error("Failed to search for plugins in {}", dir, e);
        }
    }

    void visitClasspathFile(Path path) {
        if (CLASS_FILE.matches(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                PluginCandidate candidate = scanClassFile(in, null);
                if (candidate != null) {
                    addCandidate(candidate);
                }
            } catch (IOException e) {
                logger.error("Failed to search for plugins in {}", path, e);
            }
        }
    }

    private final class ClassFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            visitClasspathFile(file);
            return FileVisitResult.CONTINUE;
        }

    }

    void scanDirectory(Path path) {
        try (DirectoryStream<Path> dir = Files.newDirectoryStream(path, JAR_FILTER)) {
            for (Path jar : dir) {
                scanJar(jar, false);
            }
        } catch (IOException e) {
            logger.error("Failed to search for plugins in {}", path, e);
        }
    }

    void scanJar(Path path, boolean classpath) {
        logger.trace("Scanning {} for plugins", path);

        Set<String> annotationProcessors = Collections.emptySet();
        List<PluginCandidate> candidates = new ArrayList<>();
        List<PluginMetadata> metadata = null;

        // Open the zip file so we can scan it for plugins
        try (JarInputStream jar = new JarInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            ZipEntry entry = jar.getNextEntry();
            if (entry == null) {
                return;
            }

            Manifest manifest = jar.getManifest();
            if (manifest == null) {
                // JarInputStream seems to be only able to find the manifest if it's one of the first entries...
                // Try harder to find it anyway - Wtf Java?
                try (JarFile jarFile = new JarFile(path.toFile())) {
                    manifest = jarFile.getManifest();
                }
            }

            if (manifest != null) {
                annotationProcessors = PluginAccessTransformers.find(manifest);
            } else if (!classpath) {
                logger.warn("Missing JAR manifest in {}", path); // TODO
            }

            do {
                if (entry.isDirectory()) {
                    continue;
                }

                final String name = entry.getName();

                if (!name.endsWith(CLASS_EXTENSION)) {
                    if (name.equals(METADATA_FILE)) {
                        try {
                            metadata = new ArrayList<>(McModInfo.DEFAULT.read(jar));
                        } catch (IOException e) {
                            logger.error("Failed to read plugin metadata from " + METADATA_FILE + " in {}", path, e);
                            return;
                        }
                    } else if (annotationProcessors.remove(name)) {
                        try {
                            AccessTransformers.register(jar);
                        } catch (IOException e) {
                            logger.warn("Failed to read access transformer from: {}!{}", path, name, e);
                        }
                    }

                    continue;
                }

                PluginCandidate candidate = scanClassFile(jar, path);
                if (candidate != null) {
                    candidates.add(candidate);
                }
            } while ((entry = jar.getNextEntry()) != null);
        } catch (IOException e) {
            logger.error("Failed to scan plugin JAR: {}", path, e);
            return;
        }

        // There are some annotation processors left we haven't read yet
        if (!annotationProcessors.isEmpty()) {
            logger.warn("Found non-existent access transformers in plugin manifest of {}: {}", path, annotationProcessors);
        }

        if (!candidates.isEmpty()) {
            for (PluginCandidate candidate : candidates) {
                addCandidate(candidate);

                // Find matching plugin metadata
                if (metadata != null) {
                    boolean found = false;

                    Iterator<PluginMetadata> itr = metadata.iterator();
                    while (itr.hasNext()) {
                        PluginMetadata meta = itr.next();
                        if (candidate.getId().equals(meta.getId())) {
                            found = true;
                            candidate.setMetadata(meta);
                            itr.remove();
                            break;
                        }
                    }

                    if (!found) {
                        logger.warn("No matching metadata found for plugin '{}' in " + METADATA_FILE + " from {}", candidate.getId(), path);
                    }
                }
            }

            if (metadata == null) {
                logger.warn("{} is missing a valid " + METADATA_FILE + " file. This is not a problem when testing plugins, however it is recommended"
                        + " to include one in public plugins. Please see the in-existent SpongeDocs link for details.", path); // TODO
            } else if (!metadata.isEmpty()) {
                logger.warn("Found {} plugins in " + METADATA_FILE + " which were not found in the plugin JAR. Are the plugin classes actually "
                                + "included? Affected plugins: {} from {}",
                        metadata.size(), metadata.stream().map(PluginMetadata::getId).collect(joining(", ")), path);
            }
        } else if (!classpath) {
            logger.error("No valid plugins found in {}. Is the file actually a plugin JAR? Please keep in mind Forge mods can be only loaded on "
                    + "SpongeForge servers, SpongeVanilla supports only Sponge plugins.", path);
        }
    }

    private void addCandidate(PluginCandidate candidate) {
        final String pluginClass = candidate.getPluginClass();
        final String id = candidate.getId();

        if (!ID_PATTERN.matcher(id).matches()) { // TODO
            logger.error("Found plugin with invalid plugin ID '{}' from {}. Plugin IDs should be lowercase, and only contain characters from "
                    + "a-z, dashes, underscores or dots. Please see the in-existent SpongeDocs link for details. The plugin will be still loaded "
                    + "currently, however this will be removed soon. Please notify the author to update the plugin as soon as possible.",
                    id, candidate.getDisplaySource());
            // return; // TODO
        }

        if (this.pluginClasses.add(pluginClass)) {
            if (this.plugins.containsKey(id)) {
                logger.error("Skipping plugin with duplicate plugin ID '{}' from {}", id, candidate.getDisplaySource());
                return;
            }

            this.plugins.put(id, candidate);
            return;
        } else {
            logger.error("Skipping duplicate plugin class {} from {}", pluginClass, candidate.getDisplaySource());
        }
    }

    private PluginCandidate scanClassFile(InputStream in, @Nullable Path source) throws IOException {
        ClassReader reader = new ClassReader(in);
        PluginClassVisitor visitor = new PluginClassVisitor();

        try {
            reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            PluginMetadata metadata = visitor.getMetadata();
            if (metadata == null) {
                return null; // Not a plugin class
            }

            return new PluginCandidate(visitor.getClassName().replace('/', '.'), source, metadata);
        } catch (InvalidPluginException e) {
            logger.error("Skipping invalid plugin {} from {}", visitor.getClassName(), source, e);
        }

        return null;
    }

}
