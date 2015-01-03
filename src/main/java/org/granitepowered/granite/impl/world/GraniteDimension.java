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

package org.granitepowered.granite.impl.world;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCWorldProvider;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

public class GraniteDimension extends Composite<MCWorldProvider> implements Dimension {
    boolean allowPlayerRespawns;

    public GraniteDimension(Object obj) {
        super(obj);
    }

    @Override
    public int getDimensionId() {
        return obj.fieldGet$dimensionId();
    }

    @Override
    public String getName() {
        return obj.getDimensionName();
    }

    @Override
    public boolean allowsPlayerRespawns() {
        return allowPlayerRespawns;
    }

    @Override
    public void setAllowsPlayerRespawns(boolean b) {
        throw new NotImplementedException("");
    }

    @Override
    public int getMinimumSpawnHeight() {
        return obj.getAverageGroundLevel();
    }

    @Override
    public boolean doesWaterEvaporate() {
        return obj.fieldGet$isHellWorld();
    }

    @Override
    public void setWaterEvaporates(boolean isHellWorld) {
        obj.fieldSet$isHellWorld(isHellWorld);
    }

    @Override
    public boolean hasSky() {
        return !obj.fieldGet$hasNoSky();
    }

    @Override
    public DimensionType getType() {
        return new GraniteDimensionType(this);
    }
}
