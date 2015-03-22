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
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleEffect implements ParticleEffect {

    ParticleType type;
    Vector3f motion;
    Vector3f offset;
    int count;

    public GraniteParticleEffect(ParticleType type, Vector3f motion, Vector3f offset, int count) {
        this.type = type;
        this.motion = motion;
        this.offset = offset;
        this.count = count;
    }

    @Override
    public ParticleType getType() {
        return type;
    }

    @Override
    public Vector3f getMotion() {
        return motion;
    }

    @Override
    public Vector3f getOffset() {
        return offset;
    }

    @Override
    public int getCount() {
        return count;
    }

    public static class GraniteColorable extends GraniteParticleEffect implements Colorable {

        private Color color;

        public GraniteColorable(ParticleType type, Vector3f motion, Vector3f offset, int count, Color color) {
            super(type, motion, offset, count);
            this.color = color;
        }

        @Override
        public Color getColor() {
            return color;
        }
    }

    public static class GraniteResizable extends GraniteParticleEffect implements Resizable {

        private float size;

        public GraniteResizable(ParticleType type, Vector3f motion, Vector3f offset, int count, float size) {
            super(type, motion, offset, count);
            this.size = size;
        }

        @Override
        public float getSize() {
            return size;
        }
    }

    public static class GraniteNote extends GraniteParticleEffect implements Note {

        private float note;

        public GraniteNote(ParticleType type, Vector3f motion, Vector3f offset, int count, float note) {
            super(type, motion, offset, count);
            this.note = note;
        }

        @Override
        public float getNote() {
            return note;
        }
    }

    public static class GraniteMaterial extends GraniteParticleEffect implements Material {

        private ItemStack item;

        public GraniteMaterial(ParticleType type, Vector3f motion, Vector3f offset, int count, ItemStack item) {
            super(type, motion, offset, count);
            this.item = item;
        }

        @Override
        public ItemStack getItem() {
            return item;
        }
    }
}
