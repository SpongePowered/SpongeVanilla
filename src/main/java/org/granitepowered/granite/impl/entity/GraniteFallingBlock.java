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

import static org.granitepowered.granite.utils.MinecraftUtils.unwrap;
import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

import org.granitepowered.granite.mc.MCBlockState;
import org.granitepowered.granite.mc.MCEntityFallingBlock;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.FallingBlock;

public class GraniteFallingBlock extends GraniteEntity<MCEntityFallingBlock> implements FallingBlock {

    public GraniteFallingBlock(MCEntityFallingBlock obj) {
        super(obj);
    }

    @Override
    public double getFallDamagePerBlock() {
        return obj.fieldGet$fallHurtAmount();
    }

    @Override
    public void setFallDamagePerBlock(double v) {
        obj.fieldSet$fallHurtAmount((float) v);
    }

    @Override
    public double getMaxFallDamage() {
        return obj.fieldGet$fallHurtMax();
    }

    @Override
    public void setMaxFallDamage(double v) {
        obj.fieldSet$fallHurtMax((int) v);
    }

    @Override
    public BlockState getBlockState() {
        return wrap(obj.fieldGet$field_175132_d());
    }

    @Override
    public void setBlockState(BlockState blockState) {
        obj.fieldSet$field_175132_d((MCBlockState) unwrap(blockState));
    }

    @Override
    public boolean getCanPlaceAsBlock() {
        return obj.fieldGet$canPlaceAsBlock();
    }

    @Override
    public void setCanPlaceAsBlock(boolean b) {
        obj.fieldSet$canPlaceAsBlock(b);
    }

    @Override
    public boolean getCanDropAsItem() {
        return obj.fieldGet$shouldDropItem();
    }

    @Override
    public void setCanDropAsItem(boolean b) {
        obj.fieldSet$shouldDropItem(b);
    }
}
