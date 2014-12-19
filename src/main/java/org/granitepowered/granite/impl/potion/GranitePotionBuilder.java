package org.granitepowered.granite.impl.potion;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;

public class GranitePotionBuilder implements PotionEffectBuilder {

    PotionEffectType potionEffectType;
    int duration;
    int amplifier;
    boolean ambience;
    boolean particles;

    public GranitePotionBuilder() {
        this.potionEffectType = PotionEffectTypes.HASTE;
        this.duration = 1;
        this.amplifier = 1;
        this.ambience = false;
        this.particles = true;
    }

    @Override
    public PotionEffectBuilder potionType(PotionEffectType potionEffectType) {
        this.potionEffectType = potionEffectType;
        return this;
    }

    @Override
    public PotionEffectBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public PotionEffectBuilder amplifier(int amplifier) throws IllegalArgumentException {
        this.amplifier = amplifier;
        return this;
    }

    @Override
    public PotionEffectBuilder ambience(boolean ambience) {
        this.ambience = ambience;
        return this;
    }

    @Override
    public PotionEffectBuilder particles(boolean particles) {
        this.particles = particles;
        return this;
    }

    @Override
    public PotionEffectBuilder reset() {
        this.potionEffectType = PotionEffectTypes.HASTE;
        this.duration = 1;
        this.amplifier = 1;
        this.ambience = false;
        this.particles = true;
        return this;
    }

    @Override
    public PotionEffect build() throws IllegalStateException {
        throw new NotImplementedException("");
    }
}
