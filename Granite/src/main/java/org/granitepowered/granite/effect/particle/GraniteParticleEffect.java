package org.granitepowered.granite.effect.particle;

import com.flowpowered.math.vector.Vector3f;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleEffect implements ParticleEffect {

    private final ParticleType particleType;
    private final Vector3f motion;
    private final Vector3f offset;
    private final int count;

    public GraniteParticleEffect(ParticleType particleType, Vector3f motion, Vector3f offset, int count) {
        this.particleType = particleType;
        this.motion = motion;
        this.offset = offset;
        this.count = count;
    }

    @Override
    public ParticleType getType() {
        return this.particleType;
    }

    @Override
    public Vector3f getMotion() {
        return this.motion;
    }

    @Override
    public Vector3f getOffset() {
        return this.offset;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    public static class Colored extends GraniteParticleEffect implements ParticleEffect.Colorable {

        private final Color color;

        public Colored(GraniteParticleType type, Vector3f motion, Vector3f offset, Color color, int count) {
            super(type, motion, offset, count);
            this.color = color;
        }

        @Override
        public Color getColor() {
            return this.color;
        }
    }

    public static class Materialized extends GraniteParticleEffect implements ParticleEffect.Material {

        private final ItemStack item;

        public Materialized(GraniteParticleType type, Vector3f motion, Vector3f offset, ItemStack item, int count) {
            super(type, motion, offset, count);
            this.item = item;
        }

        @Override
        public ItemStack getItem() {
            return this.item;
        }
    }

    public static class Note extends GraniteParticleEffect implements ParticleEffect.Note {

        private final float note;

        public Note(GraniteParticleType type, Vector3f motion, Vector3f offset, float note, int count) {
            super(type, motion, offset, count);
            this.note = note;
        }

        @Override
        public float getNote() {
            return this.note;
        }
    }

    public static class Resized extends GraniteParticleEffect implements ParticleEffect.Resizable {

        private final float size;

        public Resized(GraniteParticleType type, Vector3f motion, Vector3f offset, float size, int count) {
            super(type, motion, offset, count);
            this.size = size;
        }

        @Override
        public float getSize() {
            return this.size;
        }
    }
}
