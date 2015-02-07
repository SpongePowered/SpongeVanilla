package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityWaterMob;

public class GraniteEntityWaterMob<T extends MCEntityWaterMob> extends GraniteEntityLiving<T> {

    public GraniteEntityWaterMob(T obj) {
        super(obj);
    }
}
