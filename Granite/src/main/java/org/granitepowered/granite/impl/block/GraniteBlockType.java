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

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.text.translation.GraniteTranslation;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBlock;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemBlock;
import org.spongepowered.api.text.translation.Translation;

public class GraniteBlockType extends Composite<MCBlock> implements BlockType {

    public GraniteBlockType(MCBlock obj) {
        super(obj);
    }

    @Override
    public String getId() {
        try {
            Object registry = Mappings.getField("Block", "blockRegistry").get(null);

            Object resourceLocation = Mappings.invoke(registry, "getNameForObject", obj);
            return (String) Mappings.getField(resourceLocation.getClass(), "resourcePath").get(resourceLocation);
        } catch (IllegalAccessException e) {
            Granite.error(e);
        }

        return "error";
    }

    @Override
    public BlockState getDefaultState() {
        return wrap(obj.defaultBlockState);
    }

    @Override
    @SuppressWarnings("deprecated")
    public BlockState getStateFromDataValue(byte dataValue) {
        return wrap(obj.getStateFromMeta(dataValue));
    }

    @Override
    public boolean getTickRandomly() {
        return obj.needsRandomTick;
    }

    @Override
    public void setTickRandomly(boolean tickRandomly) {
        obj.needsRandomTick = tickRandomly;
    }

    @Override
    public boolean isLiquid() {
        return Mappings.getClass("BlockLiquid").isAssignableFrom(this.getClass());
    }

    @Override
    public boolean isSolidCube() {
        return obj.isSolidFullCube();
    }

    @Override
    public boolean isAffectedByGravity() {
        return Mappings.getClass("BlockFalling").isAssignableFrom(this.getClass());
    }

    @Override
    public boolean areStatisticsEnabled() {
        return obj.enableStats;
    }

    @Override
    public float getEmittedLight() {
        return obj.lightValue;
    }

    @Override
    public Optional<ItemBlock> getHeldItem() {
        throw new NotImplementedException("");
    }

    @Override
    public Translation getTranslation() {
        return new GraniteTranslation(obj.getUnlocalizedName());
    }
}
