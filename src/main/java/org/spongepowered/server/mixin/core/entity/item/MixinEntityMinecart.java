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
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.interfaces.IMixinMinecart;

@Mixin(EntityMinecart.class)
public abstract class MixinEntityMinecart extends Entity implements IMixinMinecart {

    private static final String MINECART_MOTION_X_FIELD = "Lnet/minecraft/entity/item/EntityMinecart;motionX:D";
    private static final String MINECART_MOTION_Y_FIELD = "Lnet/minecraft/entity/item/EntityMinecart;motionY:D";
    private static final String MINECART_MOTION_Z_FIELD = "Lnet/minecraft/entity/item/EntityMinecart;motionZ:D";

    protected Vector3d derailedMod;
    protected Vector3d airborneMod;

    private double maxSpeed;

    protected MixinEntityMinecart(World worldIn) {
        super(worldIn);
    }

    @Override
    public double getMaximumMinecartSpeed() {
        return this.maxSpeed;
    }


    @Redirect(method = "moveDerailedMinecart", at = @At(value = "FIELD", target = MINECART_MOTION_Y_FIELD, opcode = Opcodes.PUTFIELD, ordinal = 0))
    private void onDecelerateY(EntityMinecart self, double modifier) {
        self.motionY *= this.derailedMod.getY();
    }

    /**
     * @author gabizou - February 3rd, 2016
     *
     * These are still ordinal 1 since the previous redirects reduce the opcodes by 1.
     * Logically, these should be assigning when the motions are being applied during
     * air drag.
     */
    @Redirect(method = "moveDerailedMinecart", at = @At(value = "FIELD", target = MINECART_MOTION_X_FIELD, opcode = Opcodes.PUTFIELD, ordinal = 2))
    private void onGetDragAirX(EntityMinecart self, double modifier) {
        self.motionX *= this.airborneMod.getX();
    }

    @Redirect(method = "moveDerailedMinecart", at = @At(value = "FIELD", target = MINECART_MOTION_Y_FIELD, opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void onGetDragAirY(EntityMinecart self, double modifier) {
        self.motionY *= this.airborneMod.getY();
    }

    @Redirect(method = "moveDerailedMinecart", at = @At(value = "FIELD", target = MINECART_MOTION_Z_FIELD, opcode = Opcodes.PUTFIELD, ordinal = 2))
    private void onGetDragAirZ(EntityMinecart self, double modifier) {
        self.motionZ *= this.airborneMod.getZ();
    }


}
