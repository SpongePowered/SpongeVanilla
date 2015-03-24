package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.block.BlockHarvestEvent;

import java.util.Collection;
import java.util.Set;

public class GraniteBlockHarvestEvent extends GraniteBlockEvent implements BlockHarvestEvent {

    private Collection<Item> droppedItems;
    private float dropChance;

    public GraniteBlockHarvestEvent(BlockLoc blockLoc, Set<Item> droppedItems, float dropChance) {
        super(blockLoc);
        this.droppedItems = droppedItems;
        this.dropChance = dropChance;
    }

    @Override
    public Collection<Item> getDroppedItems() {
        return this.droppedItems;
    }

    @Override
    public void setDroppedItems(Collection<Item> droppedItems) {
        this.droppedItems = droppedItems;
    }

    @Override
    public float getDropChance() {
        return this.dropChance;
    }

    @Override
    public void setDropChance(float dropChance) {
        this.dropChance = dropChance;
    }
}
