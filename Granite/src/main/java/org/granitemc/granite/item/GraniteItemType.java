package org.granitemc.granite.item;

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

import org.granitemc.granite.api.block.ItemType;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.reflect.composite.Composite;

public class GraniteItemType extends Composite implements ItemType {

    public GraniteItemType(Object parent) {
        super(parent);
    }

    @Override
    public int getMaxStackSize() {
        return (int) fieldGet("n.m.item.Item", "maxStackSize");
    }

    @Override
    public int getMaxDamage() {
        return (int) fieldGet("n.m.item.Item", "maxDamage");
    }

    @Override
    public String getName() {
        return new GraniteItemStack(this, 1).getDisplayName();
    }

    @Override
    public int getNumericId() {
        return (int) invoke("n.m.item.Item", "getIdFromItem");
    }

    @Override
    public String getTechnicalName() {
        return null;
    }

    @Override
    public IItemStack createItemStack(int amount) {
        return new GraniteItemStack(this, amount);
    }
}
