package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityFlying;
import org.spongepowered.api.entity.living.Aerial;

public class GraniteEntityAerial<T extends MCEntityFlying> extends GraniteEntityLiving<T> implements Aerial {

    public GraniteEntityAerial(T obj) {
        super(obj);
    }
}
