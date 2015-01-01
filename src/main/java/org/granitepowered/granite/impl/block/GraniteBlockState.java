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
import com.google.common.collect.ImmutableMap;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCBlockState;
import org.granitepowered.granite.mc.MCProperty;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.block.BlockProperty;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

import java.util.*;

import static org.granitepowered.granite.utils.MinecraftUtils.unwrap;
import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class GraniteBlockState extends Composite<MCBlockState> implements BlockState {
    public GraniteBlockState(MCBlockState obj) {
        super(obj);
    }

    @Override
    public BlockType getType() {
        return MinecraftUtils.<GraniteBlockType>wrap(obj.fieldGet$block());
    }

    @Override
    public ImmutableMap<BlockProperty<?>, ? extends Comparable<?>> getProperties() {
        Map<BlockProperty<?>, Comparable<?>> ret = new HashMap<>();

        for (Map.Entry<MCProperty, Comparable> entry : obj.fieldGet$properties().entrySet()) {
            ret.put((BlockProperty) wrap(entry.getKey()), (Comparable<?>) entry.getValue());
        }

        return new ImmutableMap.Builder<BlockProperty<?>, Comparable<?>>().putAll(ret).build();
    }

    @Override
    public Collection<String> getPropertyNames() {
        List<String> names = new ArrayList<>();
        for (MCProperty property : obj.fieldGet$properties().keySet()) {
            names.add(property.fieldGet$name());
        }
        return names;
    }

    @Override
    public Optional<BlockProperty<?>> getPropertyByName(String s) {
        for (BlockProperty<?> property : getProperties().keySet()) {
            if (property.getName().equals(s)) {
                return Optional.<BlockProperty<?>>of(property);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<? extends Comparable<?>> getPropertyValue(String s) {
        return Optional.fromNullable(getProperties().get(getPropertyByName(s).orNull()));
    }

    @Override
    public BlockState withProperty(BlockProperty<?> blockProperty, Comparable<?> comparable) {
        return wrap(obj.withProperty((MCProperty) unwrap(blockProperty), comparable));
    }

    @Override
    public BlockState cycleProperty(BlockProperty<?> blockProperty) {
        List<? extends Comparable> sortedValues = new ArrayList<>(blockProperty.getValidValues());
        Collections.sort(sortedValues);

        int idx = sortedValues.indexOf(getProperties().get(blockProperty));
        idx = (idx + 1) % sortedValues.size();

        return withProperty(blockProperty, sortedValues.get(idx));
    }

    @Override
    @SuppressWarnings("deprecated")
    public byte getDataValue() {
        return (byte) obj.fieldGet$block().getMetaFromState(obj);
    }
}
