package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntityMagmaCube;
import org.granitepowered.granite.mc.MCEntitySlime;
import org.spongepowered.api.entity.living.monster.MagmaCube;
import org.spongepowered.api.entity.living.monster.Slime;

public class GraniteEntityMagmaCube extends GraniteEntitySlime<MCEntityMagmaCube> implements MagmaCube {

    public GraniteEntityMagmaCube(MCEntityMagmaCube obj) {
        super(obj);
    }
}
