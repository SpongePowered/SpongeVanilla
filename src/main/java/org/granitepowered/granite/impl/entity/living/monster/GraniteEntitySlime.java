package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntitySlime;
import org.spongepowered.api.entity.living.monster.Slime;

public class GraniteEntitySlime<T extends MCEntitySlime> extends GraniteEntityMonster<T> implements Slime {

    public GraniteEntitySlime(T obj) {
        super(obj);
    }

    @Override
    public int getSize() {
        return (int) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
    }

    @Override
    public void setSize(int size) {
        obj.setSlimeSize(size);
    }
}
