package org.granitepowered.granite.effect.particle;

import mc.EnumParticleType;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleType implements ParticleType {

    private final EnumParticleType particleType;
    private final boolean motion;

    public GraniteParticleType(EnumParticleType particleType, boolean motion) {
        this.particleType = particleType;
        this.motion = motion;
    }

    @Override
    public String getName() {
        return this.particleType.getParticleName();
    }

    @Override
    public boolean hasMotion() {
        return this.motion;
    }

    public static class Colorable extends GraniteParticleType implements ParticleType.Colorable {

        private final Color color;

        public Colorable(EnumParticleType particleType, Color color) {
            super(particleType, false);
            this.color = color;
        }

        @Override
        public Color getDefaultColor() {
            return this.color;
        }
    }

    public static class Material extends GraniteParticleType implements ParticleType.Material{

        private final mc.ItemStack itemStack;

        public Material(EnumParticleType particleType, boolean motion, mc.ItemStack itemStack) {
            super(particleType, motion);
            this.itemStack = itemStack;
        }

        @Override
        public ItemStack getDefaultItem() {
            return ItemStack.class.cast(this.itemStack.copy());
        }
    }

    public static class Note extends GraniteParticleType implements ParticleType.Note {

        private final float defaultNote;

        public Note(EnumParticleType particleType, float defaultNote) {
            super(particleType, false);
            this.defaultNote = defaultNote;
        }

        @Override
        public float getDefaultNote() {
            return this.defaultNote;
        }
    }

    public static class Resizable extends GraniteParticleType implements ParticleType.Resizable {

        private final float defaultSize;

        public Resizable(EnumParticleType particleType, float defaultSize) {
            super(particleType, false);
            this.defaultSize = defaultSize;
        }

        @Override
        public float getDefaultSize() {
            return this.defaultSize;
        }
    }
}
