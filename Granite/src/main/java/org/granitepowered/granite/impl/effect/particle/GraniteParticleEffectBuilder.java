/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitepowered.granite.impl.effect.particle;

import com.flowpowered.math.vector.Vector3f;
import org.granitepowered.granite.Granite;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.*;

public class GraniteParticleEffectBuilder implements ParticleEffectBuilder {

    protected GraniteParticleType type;
    protected Vector3f motion;
    protected Vector3f offset;
    protected int count;

    public GraniteParticleEffectBuilder(GraniteParticleType type) {
        this.type = type;
        motion = Vector3f.ZERO;
        offset = Vector3f.ZERO;
        count = 1;
    }

    @Override
    public ParticleEffectBuilder motion(Vector3f motion) {
        this.motion = motion;
        return this;
    }

    @Override
    public ParticleEffectBuilder offset(Vector3f offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public ParticleEffectBuilder count(int count) throws IllegalArgumentException {
        this.count = count;
        return this;
    }

    @Override
    public ParticleEffect build() {
        return new GraniteParticleEffect(type, motion, offset, count);
    }

    public static class GraniteColorable extends GraniteParticleEffectBuilder implements Colorable {

        private Color color;

        public GraniteColorable(GraniteParticleType.GraniteColorable type) {
            super(type);
            color = type.getDefaultColor();
        }

        @Override
        public Colorable color(Color color) {
            this.color = color;
            return this;
        }

        @Override
        public GraniteColorable motion(Vector3f motion) {
            return (GraniteColorable) super.motion(motion);
        }

        @Override
        public GraniteColorable offset(Vector3f motion) {
            return (GraniteColorable) super.offset(motion);
        }

        @Override
        public GraniteColorable count(int count) {
            return (GraniteColorable) super.count(count);
        }

        @Override
        public GraniteParticleEffect.GraniteColorable build() {
            return new GraniteParticleEffect.GraniteColorable(type, motion, offset, count, color);
        }
    }

    public static class GraniteResizable extends GraniteParticleEffectBuilder implements Resizable {

        private float size;

        public GraniteResizable(GraniteParticleType.GraniteResizable type) {
            super(type);
            size = type.getDefaultSize();
        }

        @Override
        public Resizable size(float size) {
            this.size = size;
            return this;
        }

        @Override
        public GraniteResizable motion(Vector3f motion) {
            return (GraniteResizable) super.motion(motion);
        }

        @Override
        public GraniteResizable offset(Vector3f motion) {
            return (GraniteResizable) super.offset(motion);
        }

        @Override
        public GraniteResizable count(int count) {
            return (GraniteResizable) super.count(count);
        }

        @Override
        public GraniteParticleEffect.GraniteResizable build() {
            return new GraniteParticleEffect.GraniteResizable(type, motion, offset, count, size);
        }
    }

    public static class GraniteNote extends GraniteParticleEffectBuilder implements Note {

        private float note;

        public GraniteNote(GraniteParticleType.GraniteNote type) {
            super(type);
            note = type.getDefaultNote();
        }

        @Override
        public Note note(float note) {
            this.note = note;
            return this;
        }

        @Override
        public GraniteNote motion(Vector3f motion) {
            return (GraniteNote) super.motion(motion);
        }

        @Override
        public GraniteNote offset(Vector3f motion) {
            return (GraniteNote) super.offset(motion);
        }

        @Override
        public GraniteNote count(int count) {
            return (GraniteNote) super.count(count);
        }

        @Override
        public GraniteParticleEffect.GraniteNote build() {
            return new GraniteParticleEffect.GraniteNote(type, motion, offset, count, note);
        }
    }

    public static class GraniteMaterial extends GraniteParticleEffectBuilder implements Material {

        private ItemStack item;

        public GraniteMaterial(GraniteParticleType.GraniteMaterial type) {
            super(type);
            item = type.getDefaultItem();
        }

        @Override
        public Material item(ItemStack item) {
            this.item = item;
            return this;
        }

        @Override
        public Material itemType(ItemType item) {
            this.item = Granite.getInstance().getRegistry().getItemBuilder().itemType(item).build();
            return this;
        }

        @Override
        public GraniteMaterial motion(Vector3f motion) {
            return (GraniteMaterial) super.motion(motion);
        }

        @Override
        public GraniteMaterial offset(Vector3f motion) {
            return (GraniteMaterial) super.offset(motion);
        }

        @Override
        public GraniteMaterial count(int count) {
            return (GraniteMaterial) super.count(count);
        }

        @Override
        public GraniteParticleEffect.GraniteMaterial build() {
            return new GraniteParticleEffect.GraniteMaterial(type, motion, offset, count, item);
        }
    }
}
