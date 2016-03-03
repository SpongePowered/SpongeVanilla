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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.network.ChannelRegistrar;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.common.SpongeGame;
import org.spongepowered.common.registry.SpongeGameRegistry;

import java.nio.file.Path;

@Singleton
public final class VanillaGame extends SpongeGame {

    private Server server;

    @Inject
    public VanillaGame(Platform platform, PluginManager pluginManager, EventManager eventManager,
            SpongeGameRegistry gameRegistry, ServiceManager serviceManager, TeleportHelper teleportHelper,
            ChannelRegistrar channelRegistrar, Logger logger) {
        super(platform, pluginManager, eventManager, gameRegistry, serviceManager, teleportHelper, channelRegistrar, logger);
    }

    @Override
    public Path getSavesDirectory() {
        return ((MinecraftServer) this.getServer()).anvilFile.toPath();
    }

    @Override
    public boolean isServerAvailable() {
        return this.server != null;
    }

    @Override
    public Server getServer() {
        Preconditions.checkState(this.server != null, "Attempting to get server while it is unavailable!");
        return this.server;
    }

    public void setServer(Server server) {
        Preconditions.checkNotNull(server, "Server cannot be null!");
        if (this.server != null) {
            throw new IllegalStateException("Attempting to set server when it is already set!");
        }
        this.server = server;
    }

    @Override
    public GameDictionary getGameDictionary() {
        throw new NotImplementedException("TODO");
    }

}
