package org.granitepowered.granite.impl.entity.living.animal;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityRabbit;
import org.spongepowered.api.entity.living.animal.Rabbit;
import org.spongepowered.api.entity.living.animal.RabbitType;

public class GraniteEntityRabbit extends GraniteEntityAnimal<MCEntityRabbit> implements Rabbit {

    public GraniteEntityRabbit(MCEntityRabbit obj) {
        super(obj);
    }

    @Override
    public RabbitType getRabbitType() {
        throw new NotImplementedException("");
    }

    @Override
    public void setRabbitType(RabbitType rabbitType) {
        throw new NotImplementedException("");
    }
}
