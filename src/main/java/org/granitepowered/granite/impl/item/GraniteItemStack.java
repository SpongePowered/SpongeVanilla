/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

package org.granitepowered.granite.impl.item;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteItemStack extends Composite implements ItemStack {
    public GraniteItemStack(Object parent) {
        super(parent);
    }

    @Override
    public ItemType getItem() {
        throw new NotImplementedException("");
    }

    @Override
    public short getDamage() {
        return (short) fieldGet("itemDamage");
    }

    @Override
    public void setDamage(short damage) {
        fieldSet("itemDamage", damage);
    }

    @Override
    public int getQuantity() {
        return (int) fieldGet("stackSize");
    }

    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity > getMaxStackQuantity()) {
            throw new IllegalArgumentException("Quantity exceeds maximum quantity");
        }
        fieldSet("stackSize", quantity);
    }

    @Override
    public int getMaxStackQuantity() {
        return (int) fieldGet(getMCItem(), "maxStackSize");
    }

    @Override
    public void setMaxStackQuantity(int quantity) {
        // TODO: Check if you can set max stack size on an ItemStack
        throw new UnsupportedOperationException("Cannot set max stack size on a single ItemStack");
    }

    @Override
    public int compareTo(ItemStack o) {
        // TODO: Check what this actually compares
        return 0;
    }

    public Object getMCItem() {
        return fieldGet("item");
    }
}
