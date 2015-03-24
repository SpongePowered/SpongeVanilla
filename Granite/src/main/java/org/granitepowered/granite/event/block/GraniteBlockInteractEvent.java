package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockInteractEvent;

public class GraniteBlockInteractEvent extends GraniteBlockEvent implements BlockInteractEvent {

    public GraniteBlockInteractEvent(BlockLoc blockLoc) {
        super(blockLoc);
    }
}
