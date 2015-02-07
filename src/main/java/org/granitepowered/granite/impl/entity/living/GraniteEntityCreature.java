package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityCreature;

public class GraniteEntityCreature<T extends MCEntityCreature> extends GraniteEntityLiving<T> {

    public GraniteEntityCreature(T obj) {
        super(obj);
    }
}
