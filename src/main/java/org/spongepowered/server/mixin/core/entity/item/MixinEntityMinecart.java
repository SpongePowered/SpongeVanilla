/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
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
package org.spongepowered.server.mixin.core.entity.item;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityMinecart.class)
public abstract class MixinEntityMinecart extends Entity {

    private static final double DEFAULT_AIRBORNE_MOD = 0.949999988079071D;

    // Added in SpongeCommon
    private Vector3d airborneMod;

    protected MixinEntityMinecart(World worldIn) {
        super(worldIn);
    }

    @ModifyConstant(method = "moveDerailedMinecart", constant = @Constant(doubleValue = DEFAULT_AIRBORNE_MOD, ordinal = 0))
    private double onAirX(double defaultValue) {
        return this.airborneMod.getX();
    }

    @ModifyConstant(method = "moveDerailedMinecart", constant = @Constant(doubleValue = DEFAULT_AIRBORNE_MOD, ordinal = 1))
    private double onAirY(double defaultValue) {
        return this.airborneMod.getY();
    }

    @ModifyConstant(method = "moveDerailedMinecart", constant = @Constant(doubleValue = DEFAULT_AIRBORNE_MOD, ordinal = 2))
    private double onAirZ(double defaultValue) {
        return this.airborneMod.getZ();
    }

}
