package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.BlockChangeEvent;

public class GraniteBlockChangeEvent extends GraniteBlockEvent implements BlockChangeEvent {

    private final BlockSnapshot blockSnapshot;

    public GraniteBlockChangeEvent(BlockLoc blockLoc, BlockSnapshot blockSnapshot) {
        super(blockLoc);
        this.blockSnapshot = blockSnapshot;
    }

    @Override
    public BlockSnapshot getReplacementBlock() {
        return this.blockSnapshot;
    }
}
