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
package org.spongepowered.server.launch;

import static com.google.common.io.Resources.getResource;
import static org.spongepowered.asm.mixin.MixinEnvironment.Side.SERVER;
import static org.spongepowered.server.launch.VanillaCommandLine.ACCESS_TRANSFORMER;
import static org.spongepowered.server.launch.VanillaCommandLine.SCAN_CLASSPATH;
import static org.spongepowered.server.launch.VanillaCommandLine.SCAN_FULL_CLASSPATH;

import com.google.common.base.Throwables;
import joptsimple.OptionSet;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IRemapper;
import org.spongepowered.common.launch.SpongeLaunch;
import org.spongepowered.server.launch.console.TerminalConsoleAppender;
import org.spongepowered.server.launch.plugin.VanillaLaunchPluginManager;
import org.spongepowered.server.launch.transformer.at.AccessTransformers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class VanillaServerTweaker implements ITweaker {

    private static final String FORGE_GRADLE_CSV_DIR = "net.minecraftforge.gradle.GradleStart.csvDir";

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        VanillaCommandLine.parse(args);
        TerminalConsoleAppender.initialize();

        List<String> unrecognizedOptions = VanillaCommandLine.getUnrecognizedOptions();
        if (!unrecognizedOptions.isEmpty()) {
            VanillaLaunch.getLogger().warn("Found unrecognized command line option(s): {}", unrecognizedOptions);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader loader) {
        VanillaLaunch.getLogger().info("Initializing Sponge...");

        // We shouldn't load these through Launchwrapper as they use native dependencies
        loader.addClassLoaderExclusion("io.netty.");
        loader.addClassLoaderExclusion("jline.");
        loader.addClassLoaderExclusion("org.fusesource.");

        // Sponge Launch
        loader.addClassLoaderExclusion("joptsimple.");
        loader.addClassLoaderExclusion("com.google.common.");
        loader.addClassLoaderExclusion("org.spongepowered.common.launch.");
        loader.addClassLoaderExclusion("org.spongepowered.server.launch.");
        loader.addClassLoaderExclusion("org.spongepowered.plugin.");

        // The server GUI won't work if we don't exclude this: log4j2 wants to have this in the same classloader
        loader.addClassLoaderExclusion("com.mojang.util.QueueLogAppender");

        // Don't allow our libraries to be transformed
        loader.addTransformerExclusion("com.google.");
        loader.addTransformerExclusion("org.apache.");
        loader.addTransformerExclusion("com.flowpowered.");
        loader.addTransformerExclusion("org.slf4j.");
        loader.addTransformerExclusion("gnu.trove.");
        // Guice
        loader.addTransformerExclusion("org.aopalliance.");
        // Configurate
        loader.addTransformerExclusion("ninja.leaping.configurate.");
        loader.addTransformerExclusion("com.typesafe.config.");
        loader.addTransformerExclusion("org.yaml.snakeyaml.");
        // Database connectors
        loader.addTransformerExclusion("com.zaxxer.hikari.");
        loader.addTransformerExclusion("org.h2.");
        loader.addTransformerExclusion("org.mariadb.");
        loader.addTransformerExclusion("org.sqlite.");

        OptionSet options = VanillaCommandLine.getOptions().get();
        boolean scanClasspath = options.has(SCAN_CLASSPATH);

        // Check if we're running in de-obfuscated environment already
        VanillaLaunch.getLogger().debug("Applying runtime de-obfuscation...");
        if (isObfuscated()) {
            // Enable Notch->Searge deobfuscation
            VanillaLaunch.getLogger().info("De-obfuscation mappings are provided by MCP (http://www.modcoderpack.com)");
            Launch.blackboard.put("vanilla.srg_mappings", getResource("mappings.srg"));
            loader.registerTransformer("org.spongepowered.server.launch.transformer.deobf.NotchDeobfuscationTransformer");
        } else {
            // Enable Searge->MCP deobfuscation (if running in ForgeGradle)
            String mcpDir = System.getProperty(FORGE_GRADLE_CSV_DIR);
            if (mcpDir != null) {
                Launch.blackboard.put("vanilla.mcp_mappings", Paths.get(mcpDir));
                loader.registerTransformer("org.spongepowered.server.launch.transformer.deobf.SeargeDeobfuscationTransformer");
            }

            // Enable plugin classpath scanning in deobfuscated environment
            scanClasspath = true;
        }

        try {
            // Apply our access transformers
            AccessTransformers.register(getResource("META-INF/common_at.cfg"));
            AccessTransformers.register(getResource("META-INF/vanilla_at.cfg"));

            // Apply access transformers from command line
            for (String at : options.valuesOf(ACCESS_TRANSFORMER)) {
                // First check if the AT exists as file
                Path path = Paths.get(at);
                if (Files.isReadable(path)) {
                    AccessTransformers.register(path);
                } else {
                    // Try as resource in classpath instead
                    AccessTransformers.register(getResource(at));
                }
            }

            // Search for plugins (and apply access transformers if available)
            VanillaLaunchPluginManager.findPlugins(scanClasspath, options.has(SCAN_FULL_CLASSPATH));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

        VanillaLaunch.getLogger().debug("Applying access transformer...");
        loader.registerTransformer("org.spongepowered.server.launch.transformer.at.AccessTransformer");

        VanillaLaunch.getLogger().debug("Initializing Mixin environment...");
        MixinEnvironment env = SpongeLaunch.setupMixinEnvironment()
                .addConfiguration("mixins.vanilla.core.json")
                .addConfiguration("mixins.vanilla.entityactivation.json")
                .addConfiguration("mixins.vanilla.chunkio.json")
                .setSide(SERVER);

        // Add our remapper to Mixin's remapper chain
        IRemapper remapper = VanillaLaunch.getRemapper();
        if (remapper != null) {
            env.getRemappers().add(remapper);
        }

        // Superclass transformer
        loader.registerTransformer(SpongeLaunch.SUPERCLASS_TRANSFORMER);
        SpongeLaunch.setupSuperClassTransformer();

        VanillaLaunch.getLogger().info("Initialization finished. Starting Minecraft server...");
    }

    private static boolean isObfuscated() {
        try {
            // If the dedicated server class exists in the de-obfuscated name, we're likely in dev env
            return Launch.classLoader.getClassBytes("net.minecraft.server.dedicated.DedicatedServer") == null;
        } catch (IOException ignored) {
            return true;
        }
    }

    @Override
    public String getLaunchTarget() {
        return "org.spongepowered.server.SpongeVanilla";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

}
