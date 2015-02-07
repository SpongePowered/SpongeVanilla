package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.impl.entity.living.GraniteEntityAgeable;
import org.granitepowered.granite.mc.MCEntityCow;
import org.spongepowered.api.entity.living.animal.Cow;

public class GraniteEntityCow<T extends MCEntityCow> extends GraniteEntityAgeable<T> implements Cow {

    public GraniteEntityCow(T obj) {
        super(obj);
    }
}
