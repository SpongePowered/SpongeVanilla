package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.mc.MCEntityPig;
import org.spongepowered.api.entity.living.animal.Pig;

public class GraniteEntityPig extends GraniteEntityAnimal<MCEntityPig> implements Pig {

    public GraniteEntityPig(MCEntityPig obj) {
        super(obj);
    }

    @Override
    public boolean isSaddled() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 1) != 0;
    }

    @Override
    public void setSaddled(boolean saddled) {
        obj.fieldGet$dataWatcher().updateObject(16, saddled ? (byte) 1 : (byte) 0);
    }
}
