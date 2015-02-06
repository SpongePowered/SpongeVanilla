package org.granitepowered.granite.impl.entity.living.animal;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityWolf;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.animal.DyeColor;
import org.spongepowered.api.entity.living.animal.Wolf;

public class GraniteEntityWolf extends GraniteEntityAnimal<MCEntityWolf> implements Wolf {

    public GraniteEntityWolf(MCEntityWolf obj) {
        super(obj);
    }

    @Override
    public DyeColor getColor() {
        throw new NotImplementedException("");
    }

    @Override
    public void setColor(DyeColor dyeColor) {
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
