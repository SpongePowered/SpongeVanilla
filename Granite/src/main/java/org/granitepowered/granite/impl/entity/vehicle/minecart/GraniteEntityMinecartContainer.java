package org.granitepowered.granite.impl.entity.vehicle.minecart;

import org.granitepowered.granite.mc.MCEntityMinecartContainer;
import org.spongepowered.api.entity.vehicle.minecart.MinecartContainer;

public class GraniteEntityMinecartContainer<T extends MCEntityMinecartContainer> extends GraniteEntityMinecart<T> implements MinecartContainer {

    public GraniteEntityMinecartContainer(T obj) {
        super(obj);
    }
}
