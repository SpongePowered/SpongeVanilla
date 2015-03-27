package org.spongepowered.granite.effect.particle;

import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleType implements ParticleType {

    private EnumParticleTypes type;
    private boolean motion;

    public GraniteParticleType(EnumParticleTypes type, boolean motion) {
        this.motion = motion;
        this.type = type;
    }

    public EnumParticleTypes getInternalType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.type.getParticleName();
    }

    @Override
    public boolean hasMotion() {
        return this.motion;
    }

    public static class Colorable extends GraniteParticleType implements ParticleType.Colorable {

        private Color color;

        public Colorable(EnumParticleTypes type, Color color) {
            super(type, false);
            this.color = color;
        }

        @Override
        public Color getDefaultColor() {
            return this.color;
        }

    }

    public static class Resizable extends GraniteParticleType implements ParticleType.Resizable {

        private float size;

        public Resizable(EnumParticleTypes type, float size) {
            super(type, false);
            this.size = size;
        }

        @Override
        public float getDefaultSize() {
            return this.size;
        }

    }

    public static class Note extends GraniteParticleType implements ParticleType.Note {

        private float note;

        public Note(EnumParticleTypes type, float note) {
            super(type, false);
            this.note = note;
        }

        @Override
        public float getDefaultNote() {
            return this.note;
        }

    }

    public static class Material extends GraniteParticleType implements ParticleType.Material {

        // TODO: This should change to the sponge item stack type if a clone method available is
        private net.minecraft.item.ItemStack item;

        public Material(EnumParticleTypes type, net.minecraft.item.ItemStack item, boolean motion) {
            super(type, motion);
            this.item = item;
        }

        @Override
        public ItemStack getDefaultItem() {
            return ItemStack.class.cast(this.item.copy());
        }

    }

}