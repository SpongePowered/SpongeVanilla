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

package org.granitepowered.granite.impl.world.biome;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCBiomeGenBase;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.Populator;

public class GraniteBiomeType extends Composite<MCBiomeGenBase> implements BiomeType {

    public GraniteBiomeType(Object obj) {
        super(obj);
    }

    @Override
    public String getName() {
        throw new NotImplementedException("");
    }

    @Override
    public double getTemperature() {
        return obj.temperature;
    }

    @Override
    public double getHumidity() {
        return obj.rainFall;
    }

    @Override
    public float getMinHeight() {
        return obj.minHeight;
    }

    @Override
    public float getMaxHeight() {
        return obj.maxHeight;
    }

    @Override
    public Iterable<Populator> getPopulators() {
        throw new NotImplementedException("");
    }

    @Override
    public void insertPopulator(Populator populator, int i) {
        throw new NotImplementedException("");
    }
}
