package org.granitepowered.granite.impl.world;

import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;

public class GraniteDimensionType implements DimensionType {

    Dimension dimension;

    public GraniteDimensionType(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public String getName() {
        return dimension.getName();
    }

    @Override
    public boolean doesKeepSpawnLoaded() {
        return dimension.getName().equals("Overworld");
    }

    @Override
    public Class<? extends Dimension> getDimensionClass() {
        return dimension.getClass();
    }
}
