package org.spongepowered.granite.effect.particle;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleEffect implements ParticleEffect {

    private GraniteParticleType type;

    private Vector3d motion;
    private Vector3d offset;

    private int count;

    public GraniteParticleEffect(GraniteParticleType type, Vector3d motion, Vector3d offset, int count) {
        this.motion = motion;
        this.offset = offset;
        this.count = count;
        this.type = type;
    }

    @Override
    public GraniteParticleType getType() {
        return this.type;
    }

    @Override
    public Vector3d getMotion() {
        return this.motion;
    }

    @Override
    public Vector3d getOffset() {
        return this.offset;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    public static class Colored extends GraniteParticleEffect implements ParticleEffect.Colorable {

        private Color color;

        public Colored(GraniteParticleType type, Vector3d motion, Vector3d offset, Color color, int count) {
            super(type, motion, offset, count);
            this.color = color;
        }

        @Override
        public Color getColor() {
            return this.color;
        }

    }

    public static class Resized extends GraniteParticleEffect implements ParticleEffect.Resizable {

        private float size;

        public Resized(GraniteParticleType type, Vector3d motion, Vector3d offset, float size, int count) {
            super(type, motion, offset, count);
            this.size = size;
        }

        @Override
        public float getSize() {
            return this.size;
        }

    }

    public static class Note extends GraniteParticleEffect implements ParticleEffect.Note {

        private float note;

        public Note(GraniteParticleType type, Vector3d motion, Vector3d offset, float note, int count) {
            super(type, motion, offset, count);
            this.note = note;
        }

        @Override
        public float getNote() {
            return this.note;
        }

    }

    public static class Materialized extends GraniteParticleEffect implements ParticleEffect.Material {

        private ItemStack item;

        public Materialized(GraniteParticleType type, Vector3d motion, Vector3d offset, ItemStack item, int count) {
            super(type, motion, offset, count);
            this.item = item;
        }

        @Override
        public ItemStack getItem() {
            return this.item;
        }

    }

}