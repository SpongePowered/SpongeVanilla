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

import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleType implements ParticleType {

    private int id;
    private String name;
    private boolean hasMotion;

    public GraniteParticleType(String name, boolean hasMotion) {
        this.name = name;
        this.hasMotion = hasMotion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasMotion() {
        return hasMotion;
    }

    public static class GraniteColorable extends GraniteParticleType implements ParticleType.Colorable {

        private Color color;

        public GraniteColorable(String name, Color color) {
            super(name, false);
            this.color = color;
        }

        @Override
        public Color getDefaultColor() {
            return color;
        }
    }

    public static class GraniteResizable extends GraniteParticleType implements ParticleType.Resizable {

        private float size;

        public GraniteResizable(String name, float size) {
            super(name, false);
            this.size = size;
        }


        @Override
        public float getDefaultSize() {
            return size;
        }
    }

    public static class GraniteNote extends GraniteParticleType implements ParticleType.Note {

        private float note;

        public GraniteNote(String name, float note) {
            super(name, false);
            this.note = note;
        }


        @Override
        public float getDefaultNote() {
            return note;
        }
    }

    public static class GraniteMaterial extends GraniteParticleType implements ParticleType.Material {

        private ItemStack material;

        public GraniteMaterial(String name, boolean hasMotion, ItemStack material) {
            super(name, hasMotion);
            this.material = material;
        }


        @Override
        public ItemStack getDefaultItem() {
            return material;
        }
    }
}
