package org.granitepowered.granite.event.block;

import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.event.block.FluidSpreadEvent;

import java.util.List;

public class GraniteFluidSpreadEvent extends GraniteBulkBlockEvent implements FluidSpreadEvent {

    public GraniteFluidSpreadEvent(List<BlockLoc> blockLocs) {
        super(blockLocs);
    }
}
