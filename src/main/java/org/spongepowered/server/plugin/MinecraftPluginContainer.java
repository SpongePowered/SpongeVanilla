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

import static org.spongepowered.common.SpongeImpl.GAME_ID;
import static org.spongepowered.common.SpongeImpl.GAME_NAME;

import com.google.inject.Singleton;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.plugin.AbstractPluginContainer;

import java.util.Optional;

@Singleton
public final class MinecraftPluginContainer extends AbstractPluginContainer {

    MinecraftPluginContainer() {
    }

    @Override
    public String getId() {
        return GAME_ID;
    }

    @Override
    public String getName() {
        return GAME_NAME;
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.of(SpongeImpl.getServer().getMinecraftVersion());
    }

    @Override
    public Optional<String> getMinecraftVersion() {
        return getVersion();
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger(MinecraftServer.class);
    }

    @Override
    public Optional<Object> getInstance() {
        return Optional.of(Sponge.getServer());
    }

}
