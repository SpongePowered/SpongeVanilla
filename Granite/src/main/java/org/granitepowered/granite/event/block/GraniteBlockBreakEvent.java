package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.BlockBreakEvent;

public class GraniteBlockBreakEvent extends GraniteBlockChangeEvent implements BlockBreakEvent {

    private int droppedXp;

    public GraniteBlockBreakEvent(BlockLoc blockLoc, BlockSnapshot blockSnapshot, int droppedXp) {
        super(blockLoc, blockSnapshot);
        this.droppedXp = droppedXp;
    }

    @Override
    public int getExp() {
        return this.droppedXp;
    }

    @Override
    public void setExp(int droppedXp) {
        this.droppedXp = droppedXp;
    }
}
