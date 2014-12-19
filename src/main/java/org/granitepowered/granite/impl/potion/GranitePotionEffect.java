package org.granitepowered.granite.impl.potion;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCPotionEffect;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;

public class GranitePotionEffect extends Composite<MCPotionEffect> implements PotionEffect {
    public GranitePotionEffect(Object obj) {
        super(obj);
    }

    @Override
    public PotionEffectType getType() {
        // TODO: Wait for Potion API to be finished
        throw new NotImplementedException("");
    }

    @Override
    public void apply(Living living) {
        // TODO: Wait for Potion API to be finished
        throw new NotImplementedException("");
    }

    @Override
    public int getDuration() {
        return obj.fieldGet$duration();
    }

    @Override
    public void setDuration(int duration) {
        obj.fieldSet$duration(duration);
    }

    @Override
    public int getAmplifier() {
        return obj.fieldGet$amplifier();
    }

    @Override
    public void setAmplifier(int amplifier) {
        obj.fieldSet$amplifier(amplifier);
    }

    @Override
    public boolean isAmbient() {
        return obj.fieldGet$isAmbient();
    }

    @Override
    public void setAmbient(boolean ambient) {
        obj.fieldSet$isAmbient(ambient);
    }

    @Override
    public boolean getShowParticles() {
        return obj.fieldGet$showParticles();
    }

    @Override
    public void setShowParticles(boolean particles) {
        obj.fieldSet$showParticles(particles);
    }
}
