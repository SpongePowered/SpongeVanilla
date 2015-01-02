package org.granitepowered.granite.impl.world;

import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;

public class GraniteDimensionType implements DimensionType {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean doesKeepSpawnLoaded() {
        return false;
    }

    @Override
    public Class<? extends Dimension> getDimensionClass() {
        return null;
    }
}
