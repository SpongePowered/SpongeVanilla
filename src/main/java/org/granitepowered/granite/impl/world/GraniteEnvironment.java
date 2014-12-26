package org.granitepowered.granite.impl.world;

import org.spongepowered.api.world.Environment;

public class GraniteEnvironment implements Environment {

    int dimensionId;
    String name;

    public GraniteEnvironment(int dimensionId, String name) {
        this.dimensionId = dimensionId;
        this.name = name;
    }

    @Override
    public int getDimensionId() {
        return dimensionId;
    }

    @Override
    public String getName() {
        return name;
    }
}
