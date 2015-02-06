package org.granitepowered.granite.impl.entity.living.animal;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntitySheep;
import org.spongepowered.api.entity.living.animal.DyeColor;
import org.spongepowered.api.entity.living.animal.Sheep;

public class GraniteEntitySheep extends GraniteEntityAnimal<MCEntitySheep> implements Sheep {

    public GraniteEntitySheep(MCEntitySheep obj) {
        super(obj);
    }

    @Override
    public boolean isSheared() {
        throw new NotImplementedException("");
    }

    @Override
    public void setSheared(boolean sheared) {
        throw new NotImplementedException("");
    }

    @Override
    public DyeColor getColor() {
        throw new NotImplementedException("");
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        throw new NotImplementedException("");
    }
}
