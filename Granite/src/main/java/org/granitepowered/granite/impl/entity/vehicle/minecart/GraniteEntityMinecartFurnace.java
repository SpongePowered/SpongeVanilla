package org.granitepowered.granite.impl.entity.vehicle.minecart;

import org.granitepowered.granite.mc.MCEntityMinecartFurnace;
import org.spongepowered.api.entity.vehicle.minecart.MinecartFurnace;

public class GraniteEntityMinecartFurnace extends GraniteEntityMinecartContainer<MCEntityMinecartFurnace> implements MinecartFurnace {

    public GraniteEntityMinecartFurnace(MCEntityMinecartFurnace obj) {
        super(obj);
    }

    @Override
    public int getFuel() {
        return obj.fuel;
    }

    @Override
    public void setFuel(int fuel) {
        obj.fuel = fuel;
    }
}
