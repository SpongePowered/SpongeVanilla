/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitepowered.granite.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.block.GraniteBlockType;
import org.granitepowered.granite.impl.item.GraniteItemBlock;
import org.granitepowered.granite.impl.item.GraniteItemType;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.granitepowered.granite.utils.ReflectionUtils;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemBlock;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public class GraniteGameRegistry implements GameRegistry {
    Map<String, BlockType> blockTypes = Maps.newHashMap();
    Map<String, ItemType> itemTypes = Maps.newHashMap();

    public void register() {
        registerBlocks();
        registerItems();
    }

    private void registerBlocks() {
        Granite.instance.getLogger().info("Registering blocks");

        for (Field field : BlockTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Object mcBlock = Mappings.invokeStatic("Blocks", "getRegisteredBlock", name);

                BlockType block = (BlockType) MinecraftUtils.wrapComposite(mcBlock);
                field.set(null, block);
                blockTypes.put(name, block);

                Granite.getInstance().getLogger().info("Registered block " + block.getId());

                if (name.equals("redstone_wire")) {
                    block.getDefaultState().cycleProperty(block.getDefaultState().getPropertyByName("power").get());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerItems() {
        Granite.instance.getLogger().info("Registering items");

        for (Field field : ItemTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {

                String nameInMc = name;

                // This is needed because of a Sponge API bug, should be fixed soon
                if (name.equals("oak_fence")) {
                    nameInMc = "fence";
                } else if (name.equals("oak_fence_gate")) {
                    nameInMc = "fence_gate";
                } else if (name.equals("slime_block")) {
                    nameInMc = "slime";
                }

                Object mcItem = Mappings.invokeStatic("Items", "getRegisteredItem", nameInMc);

                ItemType item = (ItemType) MinecraftUtils.wrapComposite(mcItem);
                field.set(null, item);
                itemTypes.put(name, item);

                Granite.getInstance().getLogger().info("Registered item " + item.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<BlockType> getBlock(String s) {
        return Optional.fromNullable(blockTypes.get(s));
    }

    @Override
    public List<BlockType> getBlocks() {
        return (List<BlockType>) blockTypes.values();
    }

    @Override
    public Optional<ItemType> getItem(String s) {
        return Optional.fromNullable(itemTypes.get(s));
    }

    @Override
    public List<ItemType> getItems() {
        return (List<ItemType>) itemTypes.values();
    }
}
