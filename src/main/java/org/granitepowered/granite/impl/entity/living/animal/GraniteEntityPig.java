package org.granitepowered.granite.impl.entity.living.animal;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityPig;
import org.spongepowered.api.entity.living.animal.Pig;

public class GraniteEntityPig extends GraniteEntityAnimal<MCEntityPig> implements Pig {

    public GraniteEntityPig(MCEntityPig obj) {
        super(obj);
    }

    @Override
    public boolean isSaddled() {
        throw new NotImplementedException("");
    }

    @Override
    public void setSaddled(boolean saddled) {
        throw new NotImplementedException("");
    }
}
