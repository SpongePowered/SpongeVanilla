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

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.block.ItemType;

public class ItemStack implements IItemStack {
    IItemStack parent;

    public ItemStack(ItemType type) {
        parent = Granite.createItemStack(type, type.getMaxStackSize());
    }

    public ItemStack(ItemType type, int amount) {
        parent = Granite.createItemStack(type, amount);
    }

    @Override
    public int getItemDamage() {
        return parent.getItemDamage();
    }

    @Override
    public void setItemDamage(int damage) {
        parent.setItemDamage(damage);
    }

    @Override
    public String[] getItemLore() {
        return parent.getItemLore();
    }

    @Override
    public void setItemLore(String... lines) {
        parent.setItemLore(lines);
    }

    @Override
    public int getMaxDamage() {
        return parent.getMaxDamage();
    }

    @Override
    public String getDisplayName() {
        return parent.getDisplayName();
    }

    @Override
    public void setDisplayName(String name) {
        parent.setDisplayName(name);
    }

    @Override
    public boolean hasDisplayName() {
        return parent.hasDisplayName();
    }

    @Override
    public ItemType getType() {
        return parent.getType();
    }

    @Override
    public void clearCustomName() {
        parent.clearCustomName();
    }
}
