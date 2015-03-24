package org.granitepowered.granite.event.block;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.event.GraniteGameEvent;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockEvent;
import org.spongepowered.api.event.cause.Cause;

public class GraniteBlockEvent extends GraniteGameEvent implements BlockEvent {

    private final BlockLoc blockLoc;

    public GraniteBlockEvent(BlockLoc blockLoc) {
        this.blockLoc = blockLoc;
    }

    @Override
    public BlockLoc getBlock() {
        return this.blockLoc;
    }

    @Override
    public Optional<Cause> getCause() {
        throw new NotImplementedException("");
    }
}
