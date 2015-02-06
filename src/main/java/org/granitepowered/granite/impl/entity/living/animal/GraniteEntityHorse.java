package org.granitepowered.granite.impl.entity.living.animal;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityHorse;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.animal.Horse;
import org.spongepowered.api.entity.living.animal.HorseColor;
import org.spongepowered.api.entity.living.animal.HorseStyle;
import org.spongepowered.api.entity.living.animal.HorseVariant;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEntityHorse extends GraniteEntityAnimal<MCEntityHorse> implements Horse {

    public GraniteEntityHorse(MCEntityHorse obj) {
        super(obj);
    }

    @Override
    public HorseStyle getStyle() {
        throw new NotImplementedException("");
    }

    @Override
    public void setStyle(HorseStyle horseStyle) {
        throw new NotImplementedException("");
    }

    @Override
    public HorseColor getColor() {
        throw new NotImplementedException("");
    }

    @Override
    public void setColor(HorseColor horseColor) {
        throw new NotImplementedException("");
    }

    @Override
    public HorseVariant getVariant() {
        throw new NotImplementedException("");
    }

    @Override
    public void setVariant(HorseVariant horseVariant) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<ItemStack> getSaddle() {
        throw new NotImplementedException("");
    }

    @Override
    public void setSaddle(ItemStack itemStack) {
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
