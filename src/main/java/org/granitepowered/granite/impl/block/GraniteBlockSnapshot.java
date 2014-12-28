package org.granitepowered.granite.impl.block;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;

public class GraniteBlockSnapshot implements BlockSnapshot {
    GraniteBlockState state;

    public GraniteBlockSnapshot(GraniteBlockState state) {
        this.state = state;
    }

    @Override
    public BlockState getState() {
        return state;
    }
}
