package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntitySpider;
import org.spongepowered.api.entity.living.monster.Spider;

public class GraniteEntitySpider<T extends MCEntitySpider> extends GraniteEntityMonster<T> implements Spider {

    public GraniteEntitySpider(T obj) {
        super(obj);
    }

    @Override
    public boolean isClimbing() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 1) != 0;
    }
}
