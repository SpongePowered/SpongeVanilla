package org.granitepowered.granite.event.block;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.event.GraniteGameEvent;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.BlockEvent;
import org.spongepowered.api.event.block.BulkBlockEvent;
import org.spongepowered.api.event.cause.Cause;

import java.util.List;

public class GraniteBulkBlockEvent extends GraniteGameEvent implements BulkBlockEvent {

    public final List<BlockLoc> blockLocs;

    public GraniteBulkBlockEvent(List<BlockLoc> blockLocs) {
        this.blockLocs = blockLocs;
    }

    @Override
    public List<BlockLoc> getBlocks() {
        return this.blockLocs;
    }

    @Override
    public void filter(Predicate<BlockLoc> predicate) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Cause> getCause() {
        throw new NotImplementedException("");
    }
}
