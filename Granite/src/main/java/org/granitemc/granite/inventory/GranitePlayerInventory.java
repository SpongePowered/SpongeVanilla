package org.granitemc.granite.inventory;

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
import org.granitemc.granite.api.inventory.PlayerInventory;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.MinecraftUtils;

public class GranitePlayerInventory extends Composite implements PlayerInventory {
    public GranitePlayerInventory(Object parent) {
        super(parent);
    }

    @Override
    public IItemStack getItemStack(int slot) {
        return (IItemStack) MinecraftUtils.wrap(invoke("n.m.inventory.IInventory", "getStackInSlot(int)", slot));
    }

    @Override
    public int getFirstEmptySlot() {
        return (int) invoke("n.m.inventory.IInventory", "getFirstEmptyStack");
    }

    @Override
    public Player getPlayer() {
        return (Player) MinecraftUtils.wrap(fieldGet("player"));
    }

    @Override
    public IItemStack getSelectedHotbarItem() {
        return (IItemStack) MinecraftUtils.wrap(invoke("n.m.entity.player.InventoryPlayer", "getCurrentItem"));
    }

    @Override
    public int getSelectedHotbarIndex() {
        return (int) fieldGet("currentItem");
    }

    @Override
    public void setSelectedHotbarItem(IItemStack itemStack) {
        setItemStack(getSelectedHotbarIndex(), itemStack);
    }

    // TODO: is this even possible to do?
    //Override
    /*public void moveHotbarCursor(int index) {

    }*/

    @Override
    public void addItemStack(IItemStack itemStack) {
        invoke("n.m.entity.player.InventoryPlayer", "addItemStackToInventory(n.m.item.ItemStack)", itemStack);
    }

    @Override
    public IItemStack getHelmetItem() {
        return getItemStack(39);
    }

    @Override
    public IItemStack getChestplateItem() {
        return getItemStack(38);
    }

    @Override
    public IItemStack getLeggingsItem() {
        return getItemStack(37);
    }

    @Override
    public IItemStack getBootsItem() {
        return getItemStack(36);
    }

    @Override
    public void setHelmetItem(IItemStack itemStack) {
        setItemStack(39, itemStack);
    }

    @Override
    public void setChestplateItem(IItemStack itemStack) {
        setItemStack(38, itemStack);

    }

    @Override
    public void setLeggingsItem(IItemStack itemStack) {
        setItemStack(37, itemStack);

    }

    @Override
    public void setBootsItem(IItemStack itemStack) {
        setItemStack(36, itemStack);

    }

    @Override
    public void setItemStack(int slot, IItemStack itemStack) {
        invoke("n.m.inventory.IInventory", "setInventorySlotContents(int;n.m.item.ItemStack)", slot, ((GraniteItemStack) itemStack).parent);
    }

    @Override
    public int getSize() {
        return (int) invoke("n.m.inventory.IInventory", "getSizeInventory");
    }
}
