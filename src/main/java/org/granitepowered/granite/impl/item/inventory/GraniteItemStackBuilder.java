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

package org.granitepowered.granite.impl.item.inventory;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackBuilder;

public class GraniteItemStackBuilder implements ItemStackBuilder {

    ItemType itemType;
    int damage;
    int quantity;

    public GraniteItemStackBuilder() {
        this.itemType = ItemTypes.STONE;
        this.damage = 0;
        this.quantity = 0;
    }

    @Override
    public ItemStackBuilder itemType(ItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    @Override
    public ItemStackBuilder damage(int damage) {
        if (damage < 0) damage = 0;
        this.damage = damage;
        return this;
    }

    @Override
    public ItemStackBuilder quantity(int quantity) throws IllegalArgumentException {
        if (quantity < 0) quantity = 0;
        if (quantity > 64) quantity = 64;
        this.quantity = quantity;
        return this;
    }

    @Override
    public ItemStackBuilder maxQuantity(int i) {
        throw new NotImplementedException("I DON'T LIKE YOU SPONGE!!!");
    }

    @Override
    public ItemStackBuilder fromItemStack(ItemStack itemStack) {
        this.itemType = itemStack.getItem();
        this.damage = itemStack.getDamage();
        this.quantity = itemStack.getQuantity();
        return this;
    }

    @Override
    public ItemStackBuilder reset() {
        this.itemType = ItemTypes.STONE;
        this.damage = 0;
        this.quantity = 0;
        return this;
    }

    @Override
    public ItemStack build() throws IllegalStateException {
        return new GraniteItemStack(this.itemType, this.quantity, this.damage);
    }
}
