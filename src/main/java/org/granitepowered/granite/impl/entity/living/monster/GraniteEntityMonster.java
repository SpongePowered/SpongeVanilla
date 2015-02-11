package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.impl.entity.living.GraniteEntityCreature;
import org.granitepowered.granite.mc.MCEntityMob;
import org.spongepowered.api.entity.living.monster.Monster;

public class GraniteEntityMonster<T extends MCEntityMob> extends GraniteEntityCreature<T> implements Monster {

    public GraniteEntityMonster(T obj) {
        super(obj);
    }
}
