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
import static org.spongepowered.asm.mixin.MixinEnvironment.CompatibilityLevel.JAVA_8;
import static org.spongepowered.server.plugin.VanillaPluginManager.SCAN_CLASSPATH_PROPERTY;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.launch.SpongeLaunch;
import org.spongepowered.common.launch.transformer.SpongeSuperclassRegistry;
import org.spongepowered.server.launch.console.TerminalConsoleAppender;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class VanillaServerTweaker implements ITweaker {

    private static final Logger logger = LogManager.getLogger(SpongeImpl.ECOSYSTEM_NAME);

    public static Logger getLogger() {
        return logger;
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        VanillaCommandLine.parse(args);
        TerminalConsoleAppender.initialize();

        List<String> unrecognizedOptions = VanillaCommandLine.getUnrecognizedOptions();
        if (!unrecognizedOptions.isEmpty()) {
            logger.warn("Found unrecognized command line option(s): {}", unrecognizedOptions);
        }

        SpongeLaunch.initialize();
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader loader) {
        logger.info("Initializing Sponge...");

        // We shouldn't load these through Launchwrapper as they use native dependencies
        loader.addClassLoaderExclusion("io.netty.");
        loader.addClassLoaderExclusion("jline.");
        loader.addClassLoaderExclusion("org.fusesource.");
        loader.addClassLoaderExclusion("joptsimple.");

        // Sponge Launch
        loader.addTransformerExclusion("org.spongepowered.tools.");
        loader.addClassLoaderExclusion("org.spongepowered.common.launch.");
        loader.addClassLoaderExclusion("org.spongepowered.server.launch.");

        // The server GUI won't work if we don't exclude this: log4j2 wants to have this in the same classloader
        loader.addClassLoaderExclusion("com.mojang.util.QueueLogAppender");

        // Minecraft Server libraries
        loader.addTransformerExclusion("com.google.gson.");
        loader.addTransformerExclusion("org.apache.commons.codec.");
        loader.addTransformerExclusion("org.apache.commons.io.");
        loader.addTransformerExclusion("org.apache.commons.lang3.");

        // SpongeAPI
        loader.addTransformerExclusion("com.flowpowered.noise.");
        loader.addTransformerExclusion("com.flowpowered.math.");
        loader.addTransformerExclusion("org.slf4j.");

        // Guice
        loader.addTransformerExclusion("com.google.inject.");
        loader.addTransformerExclusion("org.aopalliance.");

        // Configurate
        loader.addTransformerExclusion("ninja.leaping.configurate.");
        loader.addTransformerExclusion("com.typesafe.config.");
        loader.addTransformerExclusion("org.yaml.snakeyaml.");

        // Check if we're running in de-obfuscated environment already
        logger.debug("Applying runtime de-obfuscation...");
        if (isObfuscated()) {
            logger.info("De-obfuscation mappings are provided by MCP (http://www.modcoderpack.com)");
            Launch.blackboard.put("vanilla.mappings", getResource("mappings.srg"));
            loader.registerTransformer("org.spongepowered.server.launch.transformer.DeobfuscationTransformer");
            logger.debug("Runtime de-obfuscation is applied.");
        } else {
            logger.debug("Runtime de-obfuscation was not applied. Sponge is being loaded in a de-obfuscated environment.");

            // Enable plugin classpath scanning in deobfuscated environment
            System.setProperty(SCAN_CLASSPATH_PROPERTY, "true");
        }

        logger.debug("Applying access transformer...");
        Launch.blackboard.put("vanilla.at", new URL[]{ getResource("common_at.cfg"), getResource("vanilla_at.cfg") });
        loader.registerTransformer("org.spongepowered.server.launch.transformer.AccessTransformer");

        logger.debug("Initializing Mixin environment...");
        MixinBootstrap.init();
        MixinEnvironment.setCompatibilityLevel(JAVA_8);
        MixinEnvironment env = MixinEnvironment.getDefaultEnvironment()
                .addConfiguration("mixins.common.api.json")
                .addConfiguration("mixins.common.core.json")
                .addConfiguration("mixins.common.bungeecord.json")
                .addConfiguration("mixins.common.timings.json")
                .addConfiguration("mixins.vanilla.json");
        env.setSide(MixinEnvironment.Side.SERVER);

        // Superclass transformer
        loader.registerTransformer("org.spongepowered.common.launch.transformer.SpongeSuperclassTransformer");
        SpongeSuperclassRegistry.registerSuperclassModification("org.spongepowered.api.entity.ai.task.AbstractAITask",
                "org.spongepowered.common.entity.ai.SpongeEntityAICommonSuperclass");
        SpongeSuperclassRegistry.registerSuperclassModification("org.spongepowered.api.event.cause.entity.damage.source.common.AbstractDamageSource",
                "org.spongepowered.common.event.damage.SpongeCommonDamageSource");
        SpongeSuperclassRegistry.registerSuperclassModification(
                "org.spongepowered.api.event.cause.entity.damage.source.common.AbstractEntityDamageSource",
                "org.spongepowered.common.event.damage.SpongeCommonEntityDamageSource");
        SpongeSuperclassRegistry.registerSuperclassModification(
                "org.spongepowered.api.event.cause.entity.damage.source.common.AbstractIndirectEntityDamageSource",
                "org.spongepowered.common.event.damage.SpongeCommonIndirectEntityDamageSource");

        logger.info("Initialization finished. Starting Minecraft server...");
    }

    private static boolean isObfuscated() {
        try {
            return Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
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
