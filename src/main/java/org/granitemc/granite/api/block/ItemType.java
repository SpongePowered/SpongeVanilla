package org.granitemc.granite.api.block;

import org.granitemc.granite.api.item.ItemStack;

/*****************************************************************************************
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
 ****************************************************************************************/

public interface ItemType {
    /**
     * Returns the maximum amount of items in a stack of this item
     */
    int getMaxStackSize();

    /**
     * Returns the maximum damage this item can take (damage can also be a crude form of item metadata)
     */
    int getMaxDamage();

    /**
     * Gets the human name of this block (i.e. what you will see when you hover over this item in an inventory)
     */
    String getName();

    /**
     * Returns the numeric ID of this block type. This should preferably not be used, rather, use {@link ItemType#getTechnicalName()} instead.
     */
    int getNumericId();

    /**
     * Returns the technical name of this block (i.e. what's after the colon in minecraft:diamond, minecraft:wooden_door, etc.)
     */
    String getTechnicalName();

    /**
     * Creates an {@link org.granitemc.granite.api.item.ItemStack} of this item type
     * @param amount The amount of items in this {@link org.granitemc.granite.api.item.ItemStack}
     */
    ItemStack createItemStack(int amount);
}
