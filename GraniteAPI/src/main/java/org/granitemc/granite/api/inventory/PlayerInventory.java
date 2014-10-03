package org.granitemc.granite.api.inventory;

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

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;

public interface PlayerInventory extends Inventory {
    /**
     * Returns the player that owns this inventory
     */
    Player getPlayer();

    /**
     * Returns the currently selected item in this player's hotbar
     */
    ItemStack getSelectedHotbarItem();

    /**
     * Returns the index of the currently selected item in this player's hotbar (0-8)
     */
    int getSelectedHotbarIndex();

    /**
     * Sets the item in the currently selected slot in this player's hotbar
     * @param itemStack The ItemStack to set
     */
    void setSelectedHotbarItem(ItemStack itemStack);

    /**
     * Moves the hotbar cursor to this index (0-8)
     * @param index The index to move to
     */
    //void moveHotbarCursor(int index);

    /**
     * Adds an ItemStack to this inventory
     * @param itemStack The ItemStack to add
     */
    void addItemStack(ItemStack itemStack);

    /**
     * Returns this player's helmet item
     */
    ItemStack getHelmetItem();

    /**
     * Returns this player's chestplate item
     */
    ItemStack getChestplateItem();

    /**
     * Returns this player's leggings item
     */
    ItemStack getLeggingsItem();

    /**
     * Returns this player's boots item
     */
    ItemStack getBootsItem();

    /**
     * Sets this player's helmet item
     * @param itemStack The ItemStack to set
     */
    void setHelmetItem(ItemStack itemStack);

    /**
     * Sets this player's chestplate item
     * @param itemStack The ItemStack to set
     */
    void setChestplateItem(ItemStack itemStack);

    /**
     * Sets this player's leggings item
     * @param itemStack The ItemStack to set
     */
    void setLeggingsItem(ItemStack itemStack);

    /**
     * Sets this player's boots item
     * @param itemStack The ItemStack to set
     */
    void setBootsItem(ItemStack itemStack);
}
