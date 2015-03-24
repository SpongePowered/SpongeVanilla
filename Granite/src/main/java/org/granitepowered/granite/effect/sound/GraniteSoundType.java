package org.granitepowered.granite.effect.sound;

import org.spongepowered.api.effect.sound.SoundType;

public class GraniteSoundType implements SoundType {

    private final String name;

    public GraniteSoundType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
