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

package org.granitepowered.granite.impl.block;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCProperty;
import org.spongepowered.api.block.BlockProperty;

import java.util.Collection;

public class GraniteBlockProperty<T extends Comparable<T>> extends Composite<MCProperty> implements BlockProperty<T> {

    public GraniteBlockProperty(MCProperty obj) {
        super(obj);
    }

    @Override
    public String getName() {
        return obj.fieldGet$name();
    }

    @Override
    public Collection<T> getValidValues() {
        ImmutableList.Builder<T> builder = ImmutableList.builder();

        for (Comparable<?> value : obj.getAllowedValues()) {
            builder.add((T) value);
        }

        return builder.build();
    }

    @Override
    public String getNameForValue(T value) {
        return value.toString();
    }

    @Override
    public Optional<T> getValueForName(String name) {
        for (T value : getValidValues()) {
            if (getNameForValue(value).equals(name)) {
                return Optional.of(value);
            }
        }
        return Optional.absent();
    }
}
