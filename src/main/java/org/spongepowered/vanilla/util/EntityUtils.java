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
package org.spongepowered.vanilla.util;

import com.flowpowered.math.GenericMath;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class EntityUtils {

    private EntityUtils() {
    }

    public static MovingObjectPosition rayTraceFromEntity(Entity entity, double traceDistance, float partialTicks) {
        final Vec3 vecPositionEyes = EntityUtils.getPositionEyes(entity, partialTicks);
        final Vec3 vecFacing = entity.getLook(partialTicks);
        final Vec3 vecInFront = vecPositionEyes.addVector(vecFacing.xCoord * traceDistance, vecFacing.yCoord * traceDistance,
                vecFacing.zCoord * traceDistance);
        return entity.worldObj.rayTraceBlocks(vecPositionEyes, vecInFront, false, false, true);
    }

    public static Vec3 getPositionEyes(Entity entity, float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        }

        double interpX = GenericMath.lerp(entity.prevPosX, entity.posX, partialTicks);
        double interpY = GenericMath.lerp(entity.prevPosY, entity.posY, partialTicks);
        double interpZ = GenericMath.lerp(entity.prevPosZ, entity.posZ, partialTicks);
        return new Vec3(interpX, interpY, interpZ);
    }
}
