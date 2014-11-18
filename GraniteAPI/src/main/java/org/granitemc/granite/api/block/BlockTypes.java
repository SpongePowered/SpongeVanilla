package org.granitemc.granite.api.block;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
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

import com.google.common.collect.BiMap;

public class BlockTypes {
    static BiMap<Integer, BlockType> idMap;
    static BiMap<String, BlockType> nameMap;

    public static BlockType air;
    public static BlockType stone;
    public static BlockType grass;
    public static BlockType dirt;
    public static BlockType cobblestone;
    public static BlockType planks;
    public static BlockType sapling;
    public static BlockType bedrock;
    public static BlockType flowing_water;
    public static BlockType water;
    public static BlockType flowing_lava;
    public static BlockType lava;
    public static BlockType sand;
    public static BlockType gravel;
    public static BlockType gold_ore;
    public static BlockType iron_ore;
    public static BlockType coal_ore;
    public static BlockType log;
    public static BlockType leaves;
    public static BlockType sponge;
    public static BlockType glass;
    public static BlockType lapis_ore;
    public static BlockType lapis_block;
    public static BlockType dispenser;
    public static BlockType sandstone;
    public static BlockType noteblock;
    public static BlockType bed;
    public static BlockType golden_rail;
    public static BlockType detector_rail;
    public static BlockType sticky_piston;
    public static BlockType web;
    public static BlockType tallgrass;
    public static BlockType deadbush;
    public static BlockType piston;
    public static BlockType piston_head;
    public static BlockType wool;
    public static BlockType piston_extension;
    public static BlockType yellow_flower;
    public static BlockType red_flower;
    public static BlockType brown_mushroom;
    public static BlockType red_mushroom;
    public static BlockType gold_block;
    public static BlockType iron_block;
    public static BlockType double_stone_slab;
    public static BlockType stone_slab;
    public static BlockType brick_block;
    public static BlockType tnt;
    public static BlockType bookshelf;
    public static BlockType mossy_cobblestone;
    public static BlockType obsidian;
    public static BlockType torch;
    public static BlockType fire;
    public static BlockType mob_spawner;
    public static BlockType oak_stairs;
    public static BlockType chest;
    public static BlockType redstone_wire;
    public static BlockType diamond_ore;
    public static BlockType diamond_block;
    public static BlockType crafting_table;
    public static BlockType wheat;
    public static BlockType farmland;
    public static BlockType furnace;
    public static BlockType lit_furnace;
    public static BlockType standing_sign;
    public static BlockType wooden_door;
    public static BlockType ladder;
    public static BlockType rail;
    public static BlockType stone_stairs;
    public static BlockType wall_sign;
    public static BlockType lever;
    public static BlockType stone_pressure_plate;
    public static BlockType iron_door;
    public static BlockType wooden_pressure_plate;
    public static BlockType redstone_ore;
    public static BlockType lit_redstone_ore;
    public static BlockType unlit_redstone_torch;
    public static BlockType redstone_torch;
    public static BlockType stone_button;
    public static BlockType snow_layer;
    public static BlockType ice;
    public static BlockType snow;
    public static BlockType cactus;
    public static BlockType clay;
    public static BlockType reeds;
    public static BlockType jukebox;
    public static BlockType fence;
    public static BlockType pumpkin;
    public static BlockType netherrack;
    public static BlockType soul_sand;
    public static BlockType glowstone;
    public static BlockType portal;
    public static BlockType lit_pumpkin;
    public static BlockType cake;
    public static BlockType unpowered_repeater;
    public static BlockType powered_repeater;
    public static BlockType stained_glass;
    public static BlockType trapdoor;
    public static BlockType monster_egg;
    public static BlockType stonebrick;
    public static BlockType brown_mushroom_block;
    public static BlockType red_mushroom_block;
    public static BlockType iron_bars;
    public static BlockType glass_pane;
    public static BlockType melon_block;
    public static BlockType pumpkin_stem;
    public static BlockType melon_stem;
    public static BlockType vine;
    public static BlockType fence_gate;
    public static BlockType brick_stairs;
    public static BlockType stone_brick_stairs;
    public static BlockType mycelium;
    public static BlockType waterlily;
    public static BlockType nether_brick;
    public static BlockType nether_brick_fence;
    public static BlockType nether_brick_stairs;
    public static BlockType nether_wart;
    public static BlockType enchanting_table;
    public static BlockType brewing_stand;
    public static BlockType cauldron;
    public static BlockType end_portal;
    public static BlockType end_portal_frame;
    public static BlockType end_stone;
    public static BlockType dragon_egg;
    public static BlockType redstone_lamp;
    public static BlockType lit_redstone_lamp;
    public static BlockType double_wooden_slab;
    public static BlockType wooden_slab;
    public static BlockType cocoa;
    public static BlockType sandstone_stairs;
    public static BlockType emerald_ore;
    public static BlockType ender_chest;
    public static BlockType tripwire_hook;
    public static BlockType tripwire;
    public static BlockType emerald_block;
    public static BlockType spruce_stairs;
    public static BlockType birch_stairs;
    public static BlockType jungle_stairs;
    public static BlockType command_block;
    public static BlockType beacon;
    public static BlockType cobblestone_wall;
    public static BlockType flower_pot;
    public static BlockType carrots;
    public static BlockType potatoes;
    public static BlockType wooden_button;
    public static BlockType skull;
    public static BlockType anvil;
    public static BlockType trapped_chest;
    public static BlockType light_weighted_pressure_plate;
    public static BlockType heavy_weighted_pressure_plate;
    public static BlockType unpowered_comparator;
    public static BlockType powered_comparator;
    public static BlockType daylight_detector;
    public static BlockType redstone_block;
    public static BlockType quartz_ore;
    public static BlockType hopper;
    public static BlockType quartz_block;
    public static BlockType quartz_stairs;
    public static BlockType activator_rail;
    public static BlockType dropper;
    public static BlockType stained_hardened_clay;
    public static BlockType stained_glass_pane;
    public static BlockType leaves2;
    public static BlockType log2;
    public static BlockType acacia_stairs;
    public static BlockType dark_oak_stairs;
    public static BlockType slime;
    public static BlockType barrier;
    public static BlockType iron_trapdoor;
    public static BlockType prismarine;
    public static BlockType sea_lantern;
    public static BlockType hay_block;
    public static BlockType carpet;
    public static BlockType hardened_clay;
    public static BlockType coal_block;
    public static BlockType packed_ice;
    public static BlockType double_plant;
    public static BlockType standing_banner;
    public static BlockType wall_banner;
    public static BlockType daylight_detector_inverted;
    public static BlockType red_sandstone;
    public static BlockType red_sandstone_stairs;
    public static BlockType double_stone_slab2;
    public static BlockType stone_slab2;
    public static BlockType spruce_fence_gate;
    public static BlockType birch_fence_gate;
    public static BlockType jungle_fence_gate;
    public static BlockType dark_oak_fence_gate;
    public static BlockType acacia_fence_gate;
    public static BlockType spruce_fence;
    public static BlockType birch_fence;
    public static BlockType jungle_fence;
    public static BlockType dark_oak_fence;
    public static BlockType acacia_fence;
    public static BlockType spruce_door;
    public static BlockType birch_door;
    public static BlockType jungle_door;
    public static BlockType acacia_door;
    public static BlockType dark_oak_door;

    /**
     * Gets a {@link org.granitemc.granite.api.block.BlockType} by its technical name
     *
     * @param name The technical name
     */
    public static BlockType getByTechnicalName(String name) {
        name = name.replaceAll(" ", "_");
        name = name.replaceAll("-", "_");
        name = name.toLowerCase();

        return nameMap.get(name);
    }

    /**
     * Gets a {@link org.granitemc.granite.api.block.BlockType} by its numeric ID
     *
     * @param id The numeric ID
     */
    public static BlockType getById(int id) {
        return idMap.get(id);
    }

    /**
     * Gets the numeric ID from a {@link org.granitemc.granite.api.block.BlockType}
     *
     * @param type The {@link org.granitemc.granite.api.block.BlockType}
     */
    public static Integer getIdFromBlock(BlockType type) {
        // The maps store the block types with their default metadata
        // "type" may have non-default metadata, and therefore the lookup fails
        // Therefore, we're getting the default metadata of "type"
        return idMap.inverse().get(getByTechnicalName(type.getTechnicalName()));
    }
}