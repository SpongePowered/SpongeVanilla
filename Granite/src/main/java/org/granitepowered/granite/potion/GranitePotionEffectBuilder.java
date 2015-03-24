package org.granitepowered.granite.potion;

import com.google.common.base.Preconditions;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;

public class GranitePotionEffectBuilder implements PotionEffectBuilder {

    private PotionEffectType potionType;
    private int duration;
    private int amplifier;
    private boolean isAmbient;
    private boolean showParticles;

    public GranitePotionEffectBuilder() {
        reset();
    }

    @Override
    public PotionEffectBuilder potionType(PotionEffectType potionEffectType) {
        Preconditions.checkNotNull(potionEffectType, "Potion effect type cannot be null");
        this.potionType = potionEffectType;
        return this;
    }

    @Override
    public PotionEffectBuilder duration(int duration) {
        Preconditions.checkArgument(duration > 0, "Duration must be greater than 0");
        this.duration = duration;
        return this;
    }

    @Override
    public PotionEffectBuilder amplifier(int amplifier) throws IllegalArgumentException {
        Preconditions.checkArgument(amplifier >= 0, "Amplifier must not be negative");
        this.amplifier = amplifier;
        return this;
    }

    @Override
    public PotionEffectBuilder ambience(boolean ambience) {
        this.isAmbient = ambience;
        return this;
    }

    @Override
    public PotionEffectBuilder particles(boolean showsParticles) {
        this.showParticles = showsParticles;
        return this;
    }

    @Override
    public PotionEffectBuilder reset() {
        this.potionType = null;
        this.duration = 0;
        this.amplifier = 0;
        this.isAmbient = false;
        this.showParticles = true;
        return this;
    }

    @Override
    public PotionEffect build() throws IllegalStateException {
        Preconditions.checkState(this.potionType != null, "Potion type has not been set");
        Preconditions.checkState(this.duration > 0, "Duration has not been set");
        return (PotionEffect) new mc.PotionEffect(((mc.Potion) this.potionType).id, this.duration, this.amplifier, this.isAmbient,
                this.showParticles);
    }
}
