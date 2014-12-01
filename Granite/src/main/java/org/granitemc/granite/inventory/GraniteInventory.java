package org.granitemc.granite.inventory;

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

import org.granitemc.granite.api.block.BlockTypes;
import org.granitemc.granite.api.inventory.Inventory;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.util.ArrayList;
import java.util.List;

public class GraniteInventory extends Composite implements Inventory {

    public GraniteInventory(String name, int size) {
        super(Mappings.getClass("InventoryBasic"), new Class[]{String.class, boolean.class, int.class}, name, true, size);
    }

    public GraniteInventory(Object parent) {
        super(parent);
    }

    public ItemStack getItemStack(int slot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("getStackInSlot", slot));
    }

    //TODO: Create this and get inventoryContents field
    /*public ItemStack[] getContent() {
    }*/

    public List<Integer> getEmptySlot(boolean returnAll) {
        List<Integer> emptySlots = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            if (getItemStack(i) == null) {
                if (returnAll) {
                    emptySlots.add(i);
                } else {
                    emptySlots.add(i);
                    return emptySlots;
                }
            }
        }
        return emptySlots;
    }

    public void addItemStack(ItemStack itemStack) {
        List<Integer> slots = getEmptySlot(false);
        if (slots.get(0) > 0) {
            setItemStack(slots.get(0), itemStack);
        }
    }

    public void setItemStack(int slot, ItemStack itemStack) {
        invoke("setInventorySlotContents", slot, ((GraniteItemStack) itemStack).parent);
    }

    public void removeItemStack(int slot) {
        setItemStack(slot, BlockTypes.air.create(1));
    }

    public int getSize() {
        return (int) invoke("getSizeInventory");
    }

}
