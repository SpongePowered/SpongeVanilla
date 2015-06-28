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
package org.spongepowered.vanilla.registry;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.common.registry.RegistryHelper;
import org.spongepowered.common.registry.SpongeGameRegistry;

import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
public class VanillaGameRegistry extends SpongeGameRegistry {

    private final Set<BlockType> blockTypes = Sets.newHashSet();
    private final Set<ItemType> itemTypes = Sets.newHashSet();

    @Override
    public GameDictionary getGameDictionary() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public void postInit() {
        super.postInit();
        setBlockTypes();
        setItemTypes();
    }

    private void setBlockTypes() {
        for (Object obj : Block.blockRegistry) {
            this.blockTypes.add((BlockType) obj);
        }

        RegistryHelper.mapFields(BlockTypes.class, new Function<String, BlockType>() {
            @Nullable
            @Override
            public BlockType apply(String input) {
                for (BlockType type : VanillaGameRegistry.this.blockTypes) {
                    if (type.getName().equalsIgnoreCase("minecraft:" + input)) {
                        VanillaGameRegistry.this.blockTypeMappings.put(input.toLowerCase(), type);
                        return type;
                    }
                }

                return null;
            }
        });
    }

    private void setItemTypes() {
        for (Object obj : Item.itemRegistry) {
            this.itemTypes.add((ItemType) obj);
        }

        RegistryHelper.mapFields(ItemTypes.class, new Function<String, ItemType>() {
            @Nullable
            @Override
            public ItemType apply(String input) {
                for (ItemType type : VanillaGameRegistry.this.itemTypes) {
                    if (type.getName().equalsIgnoreCase("minecraft:" + input)) {
                        return type;
                    }
                }

                return null;
            }
        });
    }
}
