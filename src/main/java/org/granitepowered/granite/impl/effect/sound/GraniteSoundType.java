package org.granitepowered.granite.impl.effect.sound;

import org.spongepowered.api.effect.sound.SoundType;

public class GraniteSoundType implements SoundType {

    String name;

    public GraniteSoundType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
