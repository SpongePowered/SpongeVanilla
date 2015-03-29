/*
 * This file is part of Granite, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.spongepowered.granite.effect.particle;

import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.Color;

public class GraniteParticleType implements ParticleType {

    private final String name;
    private final boolean hasMotion;
    private final EnumParticleTypes particleType;

    public GraniteParticleType(EnumParticleTypes particleType, boolean hasMotion) {
        this.name = particleType.getParticleName();
        this.hasMotion = hasMotion;
        this.particleType = particleType;
    }

    public EnumParticleTypes getInternalType() {
        return this.particleType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasMotion() {
        return this.hasMotion;
    }

    public static class Colorable extends GraniteParticleType implements ParticleType.Colorable {

        private final Color color;

        public Colorable(EnumParticleTypes particleType, Color color) {
            super(particleType, false);
            this.color = color;
        }

        @Override
        public Color getDefaultColor() {
            return this.color;
        }
    }

    public static class Resizable extends GraniteParticleType implements ParticleType.Resizable {

        private final float size;

        public Resizable(EnumParticleTypes particleType, float size) {
            super(particleType, false);
            this.size = size;
        }

        @Override
        public float getDefaultSize() {
            return this.size;
        }
    }

    public static class Note extends GraniteParticleType implements ParticleType.Note {

        private final float note;

        public Note(EnumParticleTypes particleType, float size) {
            super(particleType, false);
            this.note = size;
        }

        @Override
        public float getDefaultNote() {
            return this.note;
        }
    }

    public static class Material extends GraniteParticleType implements ParticleType.Material {

        private final ItemStack itemStack;

        public Material(EnumParticleTypes particleType, net.minecraft.item.ItemStack itemStack, boolean hasMotion) {
            super(particleType, hasMotion);
            this.itemStack = ItemStack.class.cast(itemStack);
        }

        @Override
        public ItemStack getDefaultItem() {
            return this.itemStack;
        }
    }
}

