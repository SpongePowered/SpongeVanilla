package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.LeafDecayEvent;

public class GraniteLeafDecayEvent extends GraniteBlockChangeEvent implements LeafDecayEvent {

    public GraniteLeafDecayEvent(BlockLoc blockLoc, BlockSnapshot blockSnapshot) {
        super(blockLoc, blockSnapshot);
    }
}
