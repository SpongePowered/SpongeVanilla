package org.granitemc.granite.api.item;

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

import org.granitemc.granite.api.nbt.NBTCompound;
import org.granitemc.granite.api.item.meta.ItemMeta;

public interface ItemStack {

    /**
     * Returns the damage of this ItemStack
     */
    int getItemDamage();

    /**
     * Sets the damage of this ItemStack
     *
     * @param damage The damage to set
     */
    void setItemDamage(int damage);

    /**
     * Returns the maximum amount of damage this ItemStack can have
     */
    int getMaxDamage();

    /**
     * Returns the type of this ItemStack
     */
    ItemType getType();

    /**
     * Gets the current size of the ItemStack
     */
    int getStackSize();

    /**
     * Sets the size of the ItemStack
     *
     * @param amount
     */
    void setStackSize(int amount);

    /**
     * Gets the NBTCompound for the ItemStack
     *
     * @return
     * @throws IllegalAccessException
     */
    NBTCompound getNBTCompound() throws IllegalAccessException, InstantiationException;

    /**
     * Sets the NBTCompound for the ItemStack
     *
     * @param NBTCompound
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    void setNBTCompound(NBTCompound NBTCompound) throws InstantiationException, IllegalAccessException;

    ItemMeta getMetadata();

}