package org.granitepowered.granite.mc;

@Implement(name = "BlockRailBase")
public interface MCBlockRailBase extends MCBlock {

    boolean isOnRailWorld(MCWorld world, MCBlockPos blockPos);

    boolean isOnRail(MCBlockState blockState);
}
