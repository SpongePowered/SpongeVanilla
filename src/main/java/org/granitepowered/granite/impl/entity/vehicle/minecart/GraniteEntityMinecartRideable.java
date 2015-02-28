package org.granitepowered.granite.impl.entity.vehicle.minecart;

import org.granitepowered.granite.mc.MCEntityMinecartRideable;
import org.spongepowered.api.entity.vehicle.minecart.MinecartRideable;

public class GraniteEntityMinecartRideable<T extends MCEntityMinecartRideable> extends GraniteEntityMinecart<T> implements MinecartRideable {

    public GraniteEntityMinecartRideable(T obj) {
        super(obj);
    }
}
