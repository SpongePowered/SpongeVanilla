package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockRandomTickEvent;

public class GraniteBlockRandomTickEvent extends GraniteBlockEvent implements BlockRandomTickEvent {

    public GraniteBlockRandomTickEvent(BlockLoc blockLoc) {
        super(blockLoc);
    }
}
