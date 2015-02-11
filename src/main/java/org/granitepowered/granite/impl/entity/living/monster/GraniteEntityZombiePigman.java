package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntityPigZombie;
import org.spongepowered.api.entity.living.monster.ZombiePigman;

public class GraniteEntityZombiePigman extends GraniteEntityZombie<MCEntityPigZombie> implements ZombiePigman {

    public GraniteEntityZombiePigman(MCEntityPigZombie obj) {
        super(obj);
    }

    @Override
    public int getAngerLevel() {
        return obj.fieldGet$angerLevel();
    }

    @Override
    public void setAngerLevel(int angerLevel) {
        obj.fieldSet$angerLevel(angerLevel);
    }
}
