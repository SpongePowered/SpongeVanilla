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

package org.spongepowered.granite.world;

import net.minecraft.world.WorldProvider;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;

public class GraniteDimensionType implements DimensionType {

    private final String name;
    private final boolean keepSpawnLoaded;
    private final Class<? extends WorldProvider> dimensionClass;

    public GraniteDimensionType(String name, boolean keepSpawnLoaded, Class<? extends WorldProvider> dimensionClass) {
        this.name = name;
        this.keepSpawnLoaded = keepSpawnLoaded;
        this.dimensionClass = dimensionClass;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean doesKeepSpawnLoaded() {
        return this.keepSpawnLoaded;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Dimension> getDimensionClass() {
        return (Class<? extends Dimension>) this.dimensionClass;
    }
}
