package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockMoveEvent;

import java.util.List;

public class GraniteBlockMoveEvent extends GraniteBulkBlockEvent implements BlockMoveEvent {

    public GraniteBlockMoveEvent(List<BlockLoc> blockLocs) {
        super(blockLocs);
    }
}
