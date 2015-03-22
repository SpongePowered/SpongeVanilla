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

import mc.MCPotionEffect;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.spongepowered.api.attribute.AttributeModifier;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.service.persistence.data.DataContainer;

import java.util.Collection;

public class GranitePotionEffect extends Composite<MCPotionEffect> implements PotionEffect {

    public GranitePotionEffect(Object obj) {
        super(obj);
    }

    @Override
    public PotionEffectType getType() {
        /*Class potionClass = MCPotion.class;
        Field potionTypes = Mappings.getField(potionClass, "potionTypes");
        MCPotion[] mcPotions = null;
        try {
            mcPotions = (MCPotion[]) potionTypes.get(potionClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
        return new GranitePotionEffectType(obj.potionId);
    }

    @Override
    public int getDuration() {
        return obj.duration;
    }

    @Override
    public int getAmplifier() {
        return obj.amplifier;
    }

    @Override
    public boolean isAmbient() {
        return obj.isAmbient;
    }

    @Override
    public boolean getShowParticles() {
        return obj.showParticles;
    }

    @Override
    public DataContainer toContainer() {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers() {
        throw new NotImplementedException("");
    }
}
