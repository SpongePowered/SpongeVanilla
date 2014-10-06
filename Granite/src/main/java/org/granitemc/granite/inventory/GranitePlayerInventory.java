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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.inventory;

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.inventory.PlayerInventory;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.MinecraftUtils;

public class GranitePlayerInventory extends Composite implements PlayerInventory {
    public GranitePlayerInventory(Object parent) {
        super(parent);
    }

    public ItemStack getItemStack(int slot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("n.m.inventory.IInventory", "getStackInSlot(int)", slot));
    }

    public int getFirstEmptySlot() {
        return (int) invoke("n.m.inventory.IInventory", "getFirstEmptyStack");
    }

    public Player getPlayer() {
        return (Player) MinecraftUtils.wrap(fieldGet("player"));
    }

    public ItemStack getSelectedHotbarItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("n.m.entity.player.InventoryPlayer", "getCurrentItem"));
    }

    public int getSelectedHotbarIndex() {
        return (int) fieldGet("currentItem");
    }

    public void setSelectedHotbarItem(ItemStack itemStack) {
        setItemStack(getSelectedHotbarIndex(), itemStack);
    }

    // TODO: is this even possible to do?
    //Override
    /*public void moveHotbarCursor(int index) {

    }*/

    public void addItemStack(ItemStack itemStack) {
        invoke("n.m.entity.player.InventoryPlayer", "addItemStackToInventory(n.m.item.ItemStack)", itemStack);
    }

    public ItemStack getHelmetItem() {
        return getItemStack(39);
    }

    public ItemStack getChestplateItem() {
        return getItemStack(38);
    }

    public ItemStack getLeggingsItem() {
        return getItemStack(37);
    }

    public ItemStack getBootsItem() {
        return getItemStack(36);
    }

    public void setHelmetItem(ItemStack itemStack) {
        setItemStack(39, itemStack);
    }

    public void setChestplateItem(ItemStack itemStack) {
        setItemStack(38, itemStack);
    }

    public void setLeggingsItem(ItemStack itemStack) {
        setItemStack(37, itemStack);
    }

    public void setBootsItem(ItemStack itemStack) {
        setItemStack(36, itemStack);
    }

    public void setItemStack(int slot, ItemStack itemStack) {
        invoke("n.m.inventory.IInventory", "setInventorySlotContents(int;n.m.item.ItemStack)", slot, ((GraniteItemStack) itemStack).parent);
    }

    public int getSize() {
        return (int) invoke("n.m.inventory.IInventory", "getSizeInventory");
    }
}
