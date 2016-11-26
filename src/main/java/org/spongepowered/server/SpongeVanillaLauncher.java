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
package org.spongepowered.server;

import static com.google.common.base.Preconditions.checkState;
import static net.minecraft.server.MinecraftServer.USER_CACHE_FILE;
import static org.spongepowered.server.launch.VanillaCommandLine.BONUS_CHEST;
import static org.spongepowered.server.launch.VanillaCommandLine.PORT;
import static org.spongepowered.server.launch.VanillaCommandLine.WORLD_DIR;
import static org.spongepowered.server.launch.VanillaCommandLine.WORLD_NAME;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import joptsimple.OptionSet;
import net.minecraft.init.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixesManager;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.server.launch.VanillaCommandLine;
import org.spongepowered.server.plugin.MetadataContainer;
import org.spongepowered.server.plugin.MinecraftPluginContainer;

import java.io.File;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

public final class SpongeVanillaLauncher {

    private static final String JAR_EXTENSION = ".jar";

    @Nullable
    private static MinecraftServer server;

    @Nullable
    private static MetadataContainer metadata;

    private SpongeVanillaLauncher() {
    }

    static MinecraftServer getServer() {
        checkState(server != null, "Server not initialized");
        return server;
    }

    public static MetadataContainer getMetadata() {
        checkState(metadata != null, "Metadata not loaded");
        return metadata;
    }

    private static void start(String[] args) throws Exception {
        // Attempt to load metadata
        metadata = MetadataContainer.load();

        // Register Minecraft plugin container
        MinecraftPluginContainer.register();

        OptionSet options = VanillaCommandLine.parse(args);

        // Note: This launches the server instead of MinecraftServer.main
        // Keep command line options up-to-date with Vanilla

        Bootstrap.register();

        File worldDir = options.has(WORLD_DIR) ? options.valueOf(WORLD_DIR) : new File(".");

        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        MinecraftSessionService sessionService = authenticationService.createMinecraftSessionService();
        GameProfileRepository profileRepository = authenticationService.createProfileRepository();
        PlayerProfileCache profileCache = new PlayerProfileCache(profileRepository, new File(worldDir, USER_CACHE_FILE.getName()));

        server = new DedicatedServer(worldDir, DataFixesManager.createFixer(),
                authenticationService, sessionService, profileRepository, profileCache);

        // Initialize SpongeVanilla
        SpongeImpl.getLogger().info("This server is running {} version {}", SpongeVanilla.INSTANCE.getName(),
                SpongeVanilla.INSTANCE.getVersion().orElse("unknown"));

        if (options.has(WORLD_NAME)) {
            server.setFolderName(options.valueOf(WORLD_NAME));
        }

        if (options.has(PORT)) {
            server.setServerPort(options.valueOf(PORT));
        }

        if (options.has(BONUS_CHEST)) {
            server.canCreateBonusChest(true);
        }

        server.startServerThread();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));
    }

    public static void main(String[] args) {
        try {
            start(args);
        } catch (Exception e) {
            SpongeImpl.getLogger().fatal("Failed to start the Minecraft server", e);
            System.exit(1);
        }
    }

    public static Optional<Path> findSource(Class<?> type) {
        CodeSource source = type.getProtectionDomain().getCodeSource();
        if (source == null) {
            return Optional.empty();
        }

        URL location = source.getLocation();
        String path = location.getPath();

        if (location.getProtocol().equals("jar")) {
            // LaunchWrapper returns the exact URL to the class, not the path to the JAR
            if (path.startsWith("file:")) {
                int pos = path.lastIndexOf('!');
                if (pos >= 0) {
                    // Strip path in JAR
                    path = path.substring(0, pos);
                }
            }
        } else if (!location.getProtocol().equals("file")) {
            return Optional.empty();
        }

        if (path.endsWith(JAR_EXTENSION)) {
            try {
                return Optional.of(Paths.get(new URI(path)));
            } catch (URISyntaxException e) {
                throw new InvalidPathException(path, "Not a valid URI");
            }
        }

        return Optional.empty();
    }

}
