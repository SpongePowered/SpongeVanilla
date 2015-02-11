package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityFlying;
import org.spongepowered.api.entity.living.Aerial;

public class GraniteEntityAriel<T extends MCEntityFlying> extends GraniteEntityLiving<T> implements Aerial {

    public GraniteEntityAriel(T obj) {
        super(obj);
    }
}
