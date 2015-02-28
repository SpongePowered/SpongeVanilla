package org.granitepowered.granite.impl.entity.vehicle.minecart;

import org.granitepowered.granite.mc.MCEntityMinecartTNT;
import org.spongepowered.api.entity.vehicle.minecart.MinecartTNT;

public class GraniteEntityMinecartTNT extends GraniteEntityMinecart<MCEntityMinecartTNT> implements MinecartTNT {

    public GraniteEntityMinecartTNT(MCEntityMinecartTNT obj) {
        super(obj);
    }

    @Override
    public void ignite() {
        setFuseDuration(80);
    }

    @Override
    public void ignite(int ignite) {
        setFuseDuration(ignite);
    }

    @Override
    public int getFuseDuration() {
        return obj.fieldGet$fuse();
    }

    @Override
    public void setFuseDuration(int fuseDuration) {
        obj.fieldSet$fuse(fuseDuration);
    }

    @Override
    public void detonate() {
        setFuseDuration(0);
    }
}
