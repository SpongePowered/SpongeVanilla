package org.granitepowered.granite.impl.potion;

import org.spongepowered.api.potion.PotionEffectType;

public class GranitePotionEffectType implements PotionEffectType {
    private boolean isInstant;

    public GranitePotionEffectType(boolean isInstant){
        this.isInstant = isInstant;
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
