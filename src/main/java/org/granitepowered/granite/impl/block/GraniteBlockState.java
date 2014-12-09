/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.block.BlockProperty;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

import java.util.*;

public class GraniteBlockState extends Composite implements BlockState {
    public GraniteBlockState(Object parent) {
        super(parent);
    }

    @Override
    public BlockType getType() {
        return (BlockType) MinecraftUtils.wrapComposite(fieldGet("block"));
    }

    @Override
    public ImmutableMap<BlockProperty<?>, ? extends Comparable<?>> getProperties() {
        Map<BlockProperty<?>, Comparable<?>> ret = new HashMap<>();

        for (Map.Entry<Object, Object> entry : getMCProperties().entrySet()) {
            ret.put((BlockProperty) MinecraftUtils.wrapComposite(entry.getKey()), (Comparable<?>) entry.getValue());
        }

        return new ImmutableMap.Builder<BlockProperty<?>, Comparable<?>>().putAll(ret).build();
    }

    @Override
    public Collection<String> getPropertyNames() {
        List<String> names = new ArrayList<>();
        for (Object property : getMCProperties().keySet()) {
            names.add((String) fieldGet(property, "name"));
        }
        return names;
    }

    @Override
    public Optional<BlockProperty<?>> getPropertyByName(String s) {
        for (BlockProperty<?> property : getProperties().keySet()) {
            if (property.getName().equals(s)) {
                return (Optional<BlockProperty<?>>) Optional.of(property);
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
        return (BlockState) MinecraftUtils.wrapComposite(invoke("withProperty", ((GraniteBlockProperty) blockProperty).parent, comparable));
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
        return (byte) Mappings.invoke(fieldGet("block"), "getMetaFromState", parent);
    }

    public Map<Object, Object> getMCProperties() {
        return (Map<Object, Object>) fieldGet("properties");
    }
}
