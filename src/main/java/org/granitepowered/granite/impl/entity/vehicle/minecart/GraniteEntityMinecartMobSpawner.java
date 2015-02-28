package org.granitepowered.granite.impl.entity.vehicle.minecart;

import org.granitepowered.granite.mc.MCEntityMinecartMobSpawner;
import org.spongepowered.api.entity.vehicle.minecart.MinecartMobSpawner;

public class GraniteEntityMinecartMobSpawner<T extends MCEntityMinecartMobSpawner> extends GraniteEntityMinecart<T> implements MinecartMobSpawner {

    public GraniteEntityMinecartMobSpawner(T obj) {
        super(obj);
    }
}
