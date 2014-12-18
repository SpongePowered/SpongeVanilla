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

import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBlock;
import org.granitepowered.granite.mc.MCItem;
import org.granitepowered.granite.mc.MCItemStack;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import static org.granitepowered.granite.utils.MinecraftUtils.unwrap;
import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class GraniteItemStack extends Composite<MCItemStack> implements ItemStack {
    public GraniteItemStack(MCItemStack obj) {
        super(obj);
    }

    public GraniteItemStack(ItemType type, int amount) {
        this((MCItem) unwrap(type), amount);
    }
    public GraniteItemStack(ItemType type, int amount, int damage) {
        this((MCItem) unwrap(type), amount, damage);
    }

    public GraniteItemStack(BlockType type, int amount) {
        this((MCBlock) unwrap(type), amount);
    }

    public GraniteItemStack(BlockType type, int amount, int damage) {
        this((MCBlock) unwrap(type), amount, damage);
    }

    public GraniteItemStack(MCItem item, int amount) {
        this(item, amount, 0);
    }

    public GraniteItemStack(MCItem item, int amount, int damage) {
        super(Mappings.getClass("ItemStack"), new Class[]{Mappings.getClass("Item"), int.class, int.class}, item, amount, damage);
    }

    public GraniteItemStack(MCBlock block, int amount) {
        this(block, amount, 0);
    }

    public GraniteItemStack(MCBlock block, int amount, int damage) {
        super(Mappings.getClass("ItemStack"), new Class[]{Mappings.getClass("Block"), int.class, int.class}, block, amount, damage);
    }

    @Override
    public ItemType getItem() {
        return (ItemType) wrap(getMCItem());
    }

    @Override
    public short getDamage() {
        return (short) obj.fieldGet$itemDamage();
    }

    @Override
    public void setDamage(short damage) {
        obj.fieldSet$itemDamage(damage);
    }

    @Override
    public int getQuantity() {
        return obj.fieldGet$stackSize();
    }

    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity > getMaxStackQuantity()) quantity = getMaxStackQuantity();
        obj.fieldSet$stackSize(quantity);
    }

    @Override
    public int getMaxStackQuantity() {
        return getMCItem().fieldGet$maxStackSize();
    }

    @Override
    public void setMaxStackQuantity(int quantity) {
        // TODO: Decision lies with Sponge on this as is impossible to change the max size of a stack
        throw new UnsupportedOperationException("Decision lies with Sponge on this as is impossible to change the max size of a stack");
    }

    @Override
    public int compareTo(ItemStack itemStack) {
        throw new UnsupportedOperationException("This is going to be removed in a later release of Sponge");
    }

    public MCItem getMCItem() {
        return obj.fieldGet$item();
    }
}
