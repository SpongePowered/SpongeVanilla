package org.granitepowered.granite.impl.item;

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
    public ItemStackBuilder withItemType(ItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    @Override
    public ItemStackBuilder withDamage(int damage) {
        if (damage < 0) damage = 0;
        this.damage = damage;
        return this;
    }

    @Override
    public ItemStackBuilder withQuantity(int quantity) throws IllegalArgumentException {
        if (quantity < 0) quantity = 0;
        if (quantity > 64) quantity = 64;
        this.quantity = quantity;
        return this;
    }

    @Override
    public ItemStackBuilder withMaxQuantity(int maxQuantity) {
        // TODO: Decision lies with Sponge on this as is impossible to change the max size of a stack
        throw new NotImplementedException("Decision lies with Sponge on this as is impossible to change the max size of a stack");
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
