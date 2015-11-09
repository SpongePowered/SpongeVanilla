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

import com.google.common.base.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveFormatOld;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Platform;
import org.spongepowered.api.network.ChannelRegistrar;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.common.SpongeGame;
import org.spongepowered.common.registry.SpongeGameRegistry;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VanillaGame extends SpongeGame {

    private GameState state = GameState.CONSTRUCTION;

    private final Platform platform = new SpongeVanillaPlatform(SpongeGame.MINECRAFT_VERSION,
            SpongeGame.API_VERSION, SpongeGame.IMPLEMENTATION_VERSION);

    @Inject
    public VanillaGame(PluginManager pluginManager, EventManager eventManager, SpongeGameRegistry gameRegistry, ServiceManager serviceManager,
            TeleportHelper teleportHelper) {
        super(pluginManager, eventManager, gameRegistry, serviceManager, teleportHelper);
    }

    @Override
    public File getSavesDirectory() {
        return ((SaveFormatOld) ((MinecraftServer) getServer()).getActiveAnvilConverter()).savesDirectory;
    }

    @Override
    public Platform getPlatform() {
        return this.platform;
    }

    @Override
    public GameDictionary getGameDictionary() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public GameState getState() {
        return this.state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public ChannelRegistrar getChannelRegistrar() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("platform", platform)
                .toString();
    }

}
