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

package org.granitepowered.granite.impl.entity;

import org.granitepowered.granite.mc.MCEntityFallingBlock;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.FallingBlock;

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

public class GraniteEntityFallingBlock extends GraniteEntity<MCEntityFallingBlock> implements FallingBlock {

    public GraniteEntityFallingBlock(MCEntityFallingBlock obj) {
        super(obj);
    }

    @Override
    public double getFallDamagePerBlock() {
        return obj.fallHurtAmount;
    }

    @Override
    public void setFallDamagePerBlock(double fallDamagePerBlock) {
        obj.fallHurtAmount = (float) fallDamagePerBlock;
    }

    @Override
    public double getMaxFallDamage() {
        return obj.fallHurtMax;
    }

    @Override
    public void setMaxFallDamage(double maxFallDamage) {
        obj.fallHurtMax = (int) maxFallDamage;
    }

    @Override
    public BlockState getBlockState() {
        return wrap(obj.block);
    }

    @Override
    public void setBlockState(BlockState blockState) {
        obj.block = unwrap(blockState);
    }

    @Override
    public boolean getCanPlaceAsBlock() {
        return obj.canPlaceAsBlock;
    }

    @Override
    public void setCanPlaceAsBlock(boolean placeAsBlock) {
        obj.canPlaceAsBlock = (placeAsBlock);
    }

    @Override
    public boolean getCanDropAsItem() {
        return obj.shouldDropItem;
    }

    @Override
    public void setCanDropAsItem(boolean dropAsItem) {
        obj.shouldDropItem = dropAsItem;
    }
}
