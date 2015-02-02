package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.mc.MCEntityChicken;
import org.spongepowered.api.entity.living.animal.Chicken;

public class GraniteEntityChicken extends GraniteEntityAnimal<MCEntityChicken> implements Chicken {

    public GraniteEntityChicken(MCEntityChicken obj) {
        super(obj);
    }
}
