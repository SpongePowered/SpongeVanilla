package org.granitepowered.granite.impl.entity.living.animal;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityOcelot;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.animal.Ocelot;
import org.spongepowered.api.entity.living.animal.OcelotType;

public class GraniteEntityOcelot extends GraniteEntityAnimal<MCEntityOcelot> implements Ocelot {

    public GraniteEntityOcelot(MCEntityOcelot obj) {
        super(obj);
    }

    @Override
    public OcelotType getOcelotType() {
        throw new NotImplementedException("");
    }

    @Override
    public void setOcelotType(OcelotType ocelotType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isSitting() {
        throw new NotImplementedException("");
    }

    @Override
    public void setSitting(boolean sitting) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isTamed() {
        throw new NotImplementedException("");
    }

    @Override
    public void setTamed(boolean tamed) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Tamer> getOwner() {
        throw new NotImplementedException("");
    }

    @Override
    public void setOwner(Tamer tamer) {
        throw new NotImplementedException("");
    }
}
