package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockUpdateEvent;

import java.util.Collection;

public class GraniteBlockUpdateEvent extends GraniteBlockEvent implements BlockUpdateEvent {

    private final Collection<BlockLoc> affectedBlocks;

    public GraniteBlockUpdateEvent(BlockLoc blockLoc, Collection<BlockLoc> affectedBlocks) {
        super(blockLoc);
        this.affectedBlocks = affectedBlocks;
    }

    @Override
    public Collection<BlockLoc> getAffectedBlocks() {
        return this.affectedBlocks;
    }
}
