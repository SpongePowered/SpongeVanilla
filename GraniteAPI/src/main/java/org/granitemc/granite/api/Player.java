package org.granitemc.granite.api;

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

import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.inventory.Inventory;
import org.granitemc.granite.api.inventory.PlayerInventory;
import org.granitemc.granite.api.item.ItemStack;

public interface Player extends CommandSender, Entity {
    /**
     * Returns whether this player is currently using an item
     */
    boolean isUsingItem();

    /**
     * Stops using an item
     */
    void stopUsingItem();

    /**
     * Clears the item in use
     */
    void clearItemInUse();

    /**
     * Returns the equipment in a slot
     * @param slot The slot
     */
    ItemStack getEquipmentInSlot(int slot);

    /**
     * Returns the held item
     */
    ItemStack getHeldItem();

    /**
     * Sets an inventory or armor slot
     * @param slot The slot to set
     * @param item The {@link org.granitemc.granite.api.item.ItemStack} to set to
     */
    void setCurrentItemOrArmor(int slot, ItemStack item);

    /**
     * Returns this player's inventory
     */
    PlayerInventory getInventory();
}
