package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.FloraGrowEvent;

public class GraniteFloraGrowEvent extends GraniteBlockChangeEvent implements FloraGrowEvent {

    public GraniteFloraGrowEvent(BlockLoc blockLoc, BlockSnapshot blockSnapshot) {
        super(blockLoc, blockSnapshot);
    }
}
