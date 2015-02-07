package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityAmbientCreature;

public class GraniteEntityAmbientCreature<T extends MCEntityAmbientCreature> extends GraniteEntityLiving<T> {

    public GraniteEntityAmbientCreature(T obj) {
        super(obj);
    }
}
