package org.granitemc.granite.api.item;

import org.granitemc.granite.api.block.ItemType;

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

public interface IItemStack {
    /**
     * Returns the damage of this ItemStack
     */
    int getItemDamage();

    /**
     * Sets the damage of this ItemStack
     * @param damage The damage to set
     */
    void setItemDamage(int damage);

    /**
     * Returns the lore of this ItemStack - the lore is the extended text in the item hover description, below the display name
     */
    String[] getItemLore();

    /**
     * Sets the lore of this ItemStack - the lore is the extended text in the item hover description, below the display name
     * @param lines A vararg array of strings, one per line
     */
    void setItemLore(String... lines);

    /**
     * Returns the maximum amount of damage this ItemStack can have
     */
    int getMaxDamage();

    /**
     * Returns the display name of this ItemStack
     */
    String getDisplayName();

    /**
     * Sets the display name of this ItemStack
     * @param name The display name to set
     */
    void setDisplayName(String name);

    /**
     * Returns whether this ItemStack has a display name
     */
    boolean hasDisplayName();

    /**
     * Returns the type of this ItemStack
     */
    ItemType getType();

    /**
     * Clears any custom display name set on this ItemStack, and returns it to its original name
     */
    void clearCustomName();
}