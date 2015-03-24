package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.BlockPlaceEvent;

public class GraniteBlockPlaceEvent extends GraniteBlockChangeEvent implements BlockPlaceEvent {

    public GraniteBlockPlaceEvent(BlockLoc blockLoc, BlockSnapshot blockSnapshot) {
        super(blockLoc, blockSnapshot);
    }
}
