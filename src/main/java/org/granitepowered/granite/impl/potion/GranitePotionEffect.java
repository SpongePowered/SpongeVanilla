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

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCPotionEffect;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.service.persistence.DataSource;
import org.spongepowered.api.service.persistence.data.DataContainer;

public class GranitePotionEffect extends Composite<MCPotionEffect> implements PotionEffect {

    public GranitePotionEffect(Object obj) {
        super(obj);
    }

    @Override
    public PotionEffectType getType() {
        // TODO: Wait for Potion API to be finished
        throw new NotImplementedException("");
    }

    @Override
    public void apply(Living living) {
        // TODO: Wait for Potion API to be finished
        throw new NotImplementedException("");
    }

    @Override
    public int getDuration() {
        return obj.fieldGet$duration();
    }

    @Override
    public void setDuration(int duration) {
        obj.fieldSet$duration(duration);
    }

    @Override
    public int getAmplifier() {
        return obj.fieldGet$amplifier();
    }

    @Override
    public void setAmplifier(int amplifier) {
        obj.fieldSet$amplifier(amplifier);
    }

    @Override
    public boolean isAmbient() {
        return obj.fieldGet$isAmbient();
    }

    @Override
    public void setAmbient(boolean ambient) {
        obj.fieldSet$isAmbient(ambient);
    }

    @Override
    public boolean getShowParticles() {
        return obj.fieldGet$showParticles();
    }

    @Override
    public void setShowParticles(boolean particles) {
        obj.fieldSet$showParticles(particles);
    }

    @Override
    public DataContainer toContainer() {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public void serialize(DataSource source) {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }
}
