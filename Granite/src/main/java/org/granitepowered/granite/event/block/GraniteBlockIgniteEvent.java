package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockIgniteEvent;

public class GraniteBlockIgniteEvent extends GraniteBlockEvent implements BlockIgniteEvent {

    public GraniteBlockIgniteEvent(BlockLoc blockLoc) {
        super(blockLoc);
    }
}
