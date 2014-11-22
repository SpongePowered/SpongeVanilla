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

import org.granitemc.granite.api.item.ItemStack;

public interface BlockType {
    /**
     * Returns how much velocity is maintained while moving on top of this block
     */
    float getSlipperiness();

    /**
     * Returns how much light is subtracted for going through this block
     */
    int getLightOpacity();

    /**
     * Returns how much light is emitted from this block
     */
    int getLightValue();

    // TODO
    //int getMapColor();

    /**
     * Returns how many hits it takes to break a block
     */
    float getHardness();

    /**
     * Returns how resistant this block is to explosions
     */
    float getBlastResistance();

    /**
     * Returns whether this block is opaque (non-transparent)
     */
    boolean isOpaque();

    /**
     * Returns whether this block is transparent (non-opaque)
     */
    boolean isTransparent();

    /**
     * Returns whether grass is blocked by this block
     */
    boolean canBlockGrass();

    /**
     * Returns a specific metadata value
     *
     * @param key The metadata key to get (e.g. variant, power, facing)
     */
    Comparable getMetadata(String key);

    /**
     * Creates a *NEW* {@link org.granitemc.granite.api.block.BlockType} with this metadata value set - {@link org.granitemc.granite.api.block.BlockType}s are immutable
     *
     * @param key   The metadata key to set (e.g. variant, power, facing)
     * @param value The metadata value to set to (e.g. spruce, 8, north)
     * @return A new {@link org.granitemc.granite.api.block.BlockType} with this metadata value set
     */
    BlockType setMetadata(String key, Comparable value);

    /**
     * Returns whether this {@link org.granitemc.granite.api.block.BlockType} and its metadata is equal to another block type
     *
     * @param that The other block type
     */
    boolean equals(BlockType that);

    /**
     * Returns whether the type of this {@link org.granitemc.granite.api.block.BlockType} (and not its metadata) is equal to another block type
     *
     * @param that The other block type
     */
    boolean typeEquals(BlockType that);

    /**
     * Returns the human name of this block type (i.e. what will show when you hover over the item version of this block type)
     */
    String getName();

    /**
     * Returns the numeric ID of this block type. This should preferably not be used, rather, use {@link BlockType#getTechnicalName()} instead.
     */
    int getNumericId();

    /**
     * Returns the technical name of this block (i.e. what's after the colon in minecraft:grass, minecraft:coal_ore, etc.)
     */
    String getTechnicalName();

    /**
     * Creates an {@link org.granitemc.granite.api.item.ItemStack} of this block type
     *
     * @param amount The amount of items in this {@link org.granitemc.granite.api.item.ItemStack}
     */
    ItemStack create(int amount);

    /**
     * Returns the default state of this BlockType, ignoring all metadata
     */
    BlockType getDefaultState();

    /**
     * Converts a meta int / item damage value into a BlockType
     * @param meta The metadata in int form
     * @return The BlockType containing the needed state
     */
    BlockType getStateFromMeta(int meta);

    /**
     * Converts this BlockType, including metadata, to a meta int (for use as an ItemStack data value)
     * @return The integer value of this BlockType's metadata
     */
    int getMetaFromState();
}
