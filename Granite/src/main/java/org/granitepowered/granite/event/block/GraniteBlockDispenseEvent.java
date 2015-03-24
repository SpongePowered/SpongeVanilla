package org.granitepowered.granite.event.block;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockDispenseEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteBlockDispenseEvent extends GraniteBlockEvent implements BlockDispenseEvent {

    private mc.ItemStack despensedItem;
    private Vector3d velocity;

    public GraniteBlockDispenseEvent(BlockLoc blockLoc, mc.ItemStack despensedItem, Vector3d velocity) {
        super(blockLoc);
        this.despensedItem = despensedItem;
        this.velocity = velocity;
    }

    @Override
    public ItemStack getDispensedItem() {
        return ItemStack.class.cast(this.despensedItem);
    }

    @Override
    public void setDispensedItem(ItemStack despensedItem) {
        this.despensedItem = (mc.ItemStack) despensedItem;
    }

    @Override
    public Vector3d getVelocity() {
        return this.velocity;
    }

    @Override
    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }
}
