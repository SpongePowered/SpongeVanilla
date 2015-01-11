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

package org.granitepowered.granite.impl.potion;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.mc.MCPotion;
import org.granitepowered.granite.util.Instantiator;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;

public class GranitePotionBuilder implements PotionEffectBuilder {

    PotionEffectType potionEffectType;
    int duration;
    int amplifier;
    boolean ambience;
    boolean particles;

    public GranitePotionBuilder() {
        reset();
    }

    @Override
    public PotionEffectBuilder potionType(PotionEffectType potionEffectType) {
        this.potionEffectType = potionEffectType;
        return this;
    }

    @Override
    public PotionEffectBuilder duration(int duration) {
        if (duration <= 0) Granite.instance.getLogger().error("Duration must be greater than 0");
        this.duration = duration;
        return this;
    }

    @Override
    public PotionEffectBuilder amplifier(int amplifier) throws IllegalArgumentException {
        this.amplifier = amplifier;
        return this;
    }

    @Override
    public PotionEffectBuilder ambience(boolean ambience) {
        this.ambience = ambience;
        return this;
    }

    @Override
    public PotionEffectBuilder particles(boolean particles) {
        this.particles = particles;
        return this;
    }

    @Override
    public PotionEffectBuilder reset() {
        this.potionEffectType = null;
        this.duration = 0;
        this.amplifier = 0;
        this.ambience = false;
        this.particles = true;
        return this;
    }

    @Override
    public PotionEffect build() throws IllegalStateException {
        return new GranitePotionEffect(Instantiator.get().newPotionEffect(
                ((MCPotion) MinecraftUtils.unwrap(potionEffectType)).fieldGet$id(),
                duration, amplifier, ambience, particles
        ));
    }
}
