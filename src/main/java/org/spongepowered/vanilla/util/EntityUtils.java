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

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class EntityUtils {
    public static MovingObjectPosition rayTraceFromEntity(Entity entity, double traceDistance, float partialTicks) {
        Vec3 var4 = EntityUtils.getPositionEyes(entity, partialTicks);
        Vec3 var5 = entity.getLook(partialTicks);
        Vec3 var6 = var4.addVector(var5.xCoord * traceDistance, var5.yCoord * traceDistance, var5.zCoord * traceDistance);
        return entity.worldObj.rayTraceBlocks(var4, var6, false, false, true);
    }

    public static Vec3 getPositionEyes(Entity entity, float partialTicks) {
        if (partialTicks == 1.0F) {
            return new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        }

        double interpX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double interpY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + entity.getEyeHeight();
        double interpZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        return new Vec3(interpX, interpY, interpZ);
    }
}
