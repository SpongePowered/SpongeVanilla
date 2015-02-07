package org.granitepowered.granite.impl.entity.player;

import org.granitepowered.granite.impl.entity.living.GraniteEntityLivingBase;
import org.granitepowered.granite.mc.MCEntityPlayer;

public class GraniteEntityPlayer<T extends MCEntityPlayer> extends GraniteEntityLivingBase<T> {

    public GraniteEntityPlayer(T obj) {
        super(obj);
    }
}
