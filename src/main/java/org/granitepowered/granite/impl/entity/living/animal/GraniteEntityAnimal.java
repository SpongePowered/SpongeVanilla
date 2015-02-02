package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.impl.entity.living.GraniteAgeable;
import org.granitepowered.granite.mc.MCEntityAnimal;
import org.spongepowered.api.entity.living.animal.Animal;

public class GraniteEntityAnimal<T extends MCEntityAnimal> extends GraniteAgeable<T> implements Animal {

    public GraniteEntityAnimal(T obj) {
        super(obj);
    }
}
