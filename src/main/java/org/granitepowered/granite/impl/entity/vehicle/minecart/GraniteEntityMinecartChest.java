package org.granitepowered.granite.impl.entity.vehicle.minecart;

import org.granitepowered.granite.mc.MCEntityMinecartChest;
import org.spongepowered.api.entity.vehicle.minecart.MinecartChest;

public class GraniteEntityMinecartChest extends GraniteEntityMinecartContainer<MCEntityMinecartChest> implements MinecartChest {

    public GraniteEntityMinecartChest(MCEntityMinecartChest obj) {
        super(obj);
    }
}
