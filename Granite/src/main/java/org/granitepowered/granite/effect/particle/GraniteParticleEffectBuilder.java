package org.granitepowered.granite.effect.particle;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Preconditions;
import mc.Item;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleEffectBuilder implements ParticleEffectBuilder {

    protected final GraniteParticleType type;

    protected Vector3f motion = Vector3f.ZERO;
    protected Vector3f offset = Vector3f.ZERO;

    protected int count = 1;

    public GraniteParticleEffectBuilder(GraniteParticleType type) {
        this.type = type;
    }

    @Override
    public GraniteParticleEffectBuilder motion(Vector3f motion) {
        Preconditions.checkNotNull(motion, "The motion vector cannot be null! Use Vector3f.ZERO instead!");
        this.motion = motion;
        return this;
    }

    @Override
    public GraniteParticleEffectBuilder offset(Vector3f offset) {
        Preconditions.checkNotNull(offset, "The offset vector cannot be null! Use Vector3f.ZERO instead!");
        this.offset = offset;
        return this;
    }

    @Override
    public GraniteParticleEffectBuilder count(int count) throws IllegalArgumentException {
        Preconditions.checkArgument(count > 0, "The count has to be greater then zero!");
        this.count = count;
        return this;
    }

    @Override
    public GraniteParticleEffect build() throws IllegalStateException {
        return new GraniteParticleEffect(this.type, this.motion, this.offset, this.count);
    }

    public static class BuilderColorable extends GraniteParticleEffectBuilder implements ParticleEffectBuilder.Colorable {

        private Color color;

        public BuilderColorable(GraniteParticleType.Colorable type) {
            super(type);
            this.color = type.getDefaultColor();
        }

        @Override
        public BuilderColorable color(Color color) {
            Preconditions.checkNotNull(color, "The color cannot be null!");
            this.color = color;
            return this;
        }

        @Override
        public BuilderColorable motion(Vector3f motion) {
            return (BuilderColorable) super.motion(motion);
        }

        @Override
        public BuilderColorable offset(Vector3f motion) {
            return (BuilderColorable) super.offset(motion);
        }

        @Override
        public BuilderColorable count(int count) {
            return (BuilderColorable) super.count(count);
        }

        @Override
        public GraniteParticleEffect.Colored build() {
            return new GraniteParticleEffect.Colored(this.type, this.motion, this.offset, this.color, this.count);
        }

    }

    public static class BuilderResizable extends GraniteParticleEffectBuilder implements ParticleEffectBuilder.Resizable {

        private float size;

        public BuilderResizable(GraniteParticleType.Resizable type) {
            super(type);
            this.size = type.getDefaultSize();
        }

        @Override
        public BuilderResizable size(float size) {
            Preconditions.checkArgument(size >= 0f, "The size has to be greater or equal to zero!");
            this.size = size;
            return this;
        }

        @Override
        public BuilderResizable motion(Vector3f motion) {
            return (BuilderResizable) super.motion(motion);
        }

        @Override
        public BuilderResizable offset(Vector3f offset) {
            return (BuilderResizable) super.offset(offset);
        }

        @Override
        public BuilderResizable count(int count) {
            return (BuilderResizable) super.count(count);
        }

        @Override
        public GraniteParticleEffect.Resized build() {
            return new GraniteParticleEffect.Resized(this.type, this.motion, this.offset, this.size, this.count);
        }

    }

    public static class BuilderNote extends GraniteParticleEffectBuilder implements ParticleEffectBuilder.Note {

        private float note;

        public BuilderNote(GraniteParticleType.Note type) {
            super(type);
            this.note = type.getDefaultNote();
        }

        @Override
        public BuilderNote note(float note) {
            Preconditions.checkArgument(note >= 0f && note <= 24f, "The note has to scale between 0 and 24!");
            this.note = note;
            return this;
        }

        @Override
        public BuilderNote motion(Vector3f motion) {
            return (BuilderNote) super.motion(motion);
        }

        @Override
        public BuilderNote offset(Vector3f offset) {
            return (BuilderNote) super.offset(offset);
        }

        @Override
        public BuilderNote count(int count) {
            return (BuilderNote) super.count(count);
        }

        @Override
        public GraniteParticleEffect.Note build() {
            return new GraniteParticleEffect.Note(this.type, this.motion, this.offset, this.note, this.count);
        }

    }

    public static class BuilderMaterial extends GraniteParticleEffectBuilder implements ParticleEffectBuilder.Material {

        private ItemStack item;

        public BuilderMaterial(GraniteParticleType.Material type) {
            super(type);
            this.item = type.getDefaultItem();
        }

        @Override
        public BuilderMaterial item(ItemStack item) {
            Preconditions.checkNotNull(item, "The item stack cannot be null!");
            this.item = item;
            return this;
        }

        @Override
        public Material itemType(ItemType item) {
            Preconditions.checkNotNull(item, "The item type cannot be null!");
            this.item = ItemStack.class.cast(new mc.ItemStack((Item) item));
            return null;
        }

        @Override
        public BuilderMaterial motion(Vector3f motion) {
            return (BuilderMaterial) super.motion(motion);
        }

        @Override
        public BuilderMaterial offset(Vector3f offset) {
            return (BuilderMaterial) super.offset(offset);
        }

        @Override
        public BuilderMaterial count(int count) {
            return (BuilderMaterial) super.count(count);
        }

        @Override
        public GraniteParticleEffect.Materialized build() {
            return new GraniteParticleEffect.Materialized(this.type, this.motion, this.offset, this.item, this.count);
        }
    }
}
