/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.google.common.base.Optional;
import mc.MCBlock;
import mc.MCItem;
import mc.MCItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.spongepowered.api.attribute.AttributeModifier;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemDataTransactionResult;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.data.ItemData;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.properties.ItemProperty;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.service.persistence.data.DataContainer;

import java.util.Collection;

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
        super(ItemStack.class, new Class[]{MCItem.class, int.class, int.class}, item, amount, damage);
    }

    public GraniteItemStack(MCBlock block, int amount) {
        this(block, amount, 0);
    }

    public GraniteItemStack(MCBlock block, int amount, int damage) {
        super(ItemStack.class, new Class[]{MCBlock.class, int.class, int.class}, block, amount, damage);
    }

    @Override
    public ItemType getItem() {
        return (ItemType) wrap(getMCItem());
    }

    @Override
    public int getQuantity() {
        return obj.stackSize;
    }

    @Override
    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity > getMaxStackQuantity()) {
            quantity = getMaxStackQuantity();
        }
        obj.stackSize = quantity;
    }

    @Override
    public int getMaxStackQuantity() {
        return getMCItem().maxStackSize;
    }

    @Override
    public void setMaxStackQuantity(int quantity) {
        // TODO: Decision lies with Sponge on this as is impossible to change the max size of a stack
        throw new UnsupportedOperationException("This is soon to be removed from the API");
    }

    @Override
    public <T extends ItemData<T>> ItemDataTransactionResult setItemData(T t) {
        throw new NotImplementedException("");
    }

    @Override
    public <T extends ItemData<T>> Optional<T> getOrCreateItemData(Class<T> aClass) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<ItemProperty<?, ?>> getProperties() {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<ItemData<?>> getItemData() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean vaildateData(DataContainer dataContainer) {
        throw new NotImplementedException("");
    }

    @Override
    public void setRawData(DataContainer dataContainer) throws InvalidDataException {
        throw new NotImplementedException("");
    }

    public MCItem getMCItem() {
        return obj.item;
    }

    @Override
    public DataContainer toContainer() {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers() {
        throw new NotImplementedException("");
    }

    @Override
    public <T> Optional<T> getData(Class<T> aClass) {
        throw new NotImplementedException("");
    }
}
