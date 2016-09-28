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

import com.google.common.collect.ImmutableList;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.plugin.AbstractPluginContainer;

import java.util.List;
import java.util.Optional;

class McpPluginContainer extends AbstractPluginContainer {

    // TODO: Convert to mcmod.info

    private static final ImmutableList<String> AUTHORS = ImmutableList.of("Searge", "ProfMobius", "IngisKahn", "Fesh0r", "ZeuX", "R4wk",
            "LexManos", "Bspkrs", "Others");

    McpPluginContainer() {
    }

    @Override
    public String getId() {
        return "mcp";
    }

    @Override
    public String getName() {
        return "Mod Coder Pack";
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.of("9.32");
    }

    @Override
    public Optional<String> getMinecraftVersion() {
        return Optional.of(SpongeImpl.getServer().getMinecraftVersion());
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Modding toolkit to decompile and deobfuscate the Minecraft client and server files.");
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.of("http://www.modcoderpack.com/website/");
    }

    @Override
    public List<String> getAuthors() {
        return AUTHORS;
    }

}
