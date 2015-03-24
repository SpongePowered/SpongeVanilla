package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockRedstoneUpdateEvent;

import java.util.Collection;

public class GraniteBlockRedstoneUpdateEvent extends GraniteBlockUpdateEvent implements BlockRedstoneUpdateEvent {

    private final int oldSignalStrength;
    private int newSignalStrength;

    public GraniteBlockRedstoneUpdateEvent(BlockLoc blockLoc, Collection<BlockLoc> affectedBlocks, int oldSignalStrength, int newSignalStrength) {
        super(blockLoc, affectedBlocks);
        this.oldSignalStrength = oldSignalStrength;
        this.newSignalStrength = newSignalStrength;
    }

    @Override
    public int getOldSignalStrength() {
        return this.oldSignalStrength;
    }

    @Override
    public int getNewSignalStrength() {
        return this.newSignalStrength;
    }

    @Override
    public void setNewSignalStrength(int newSignalStrength) {
        this.newSignalStrength = newSignalStrength;
    }
}
