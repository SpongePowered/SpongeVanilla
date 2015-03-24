package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.BlockBurnEvent;

public class GraniteBlockBurnEvent extends GraniteBlockChangeEvent implements BlockBurnEvent {

    public GraniteBlockBurnEvent(BlockLoc blockLoc, BlockSnapshot blockSnapshot) {
        super(blockLoc, blockSnapshot);
    }
}
