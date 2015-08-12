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
package org.spongepowered.vanilla.mixin.world;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.world.WorldOnExplosionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.util.VecHelper;

import java.util.HashSet;
import java.util.List;

@Mixin(value = Explosion.class, priority = 1001)
public abstract class MixinExplosion implements org.spongepowered.api.world.explosion.Explosion {
    @Shadow private World worldObj;
    @Shadow private Entity exploder;
    @Shadow private List affectedBlockPositions;
    @Shadow abstract EntityLivingBase getExplosivePlacedBy();

    @Inject(method = "doExplosionA", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;"
            + "getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void callWorldOnExplosionEvent(CallbackInfo ci, HashSet hashset, boolean flag, int j, int k, float f3, int j1, int l, int k1, int i1,
            List list) {
        // Minecraft -> Sponge
        final List<Location> locations = Lists.newArrayList();
        for (Object obj : affectedBlockPositions) {
            final BlockPos blockPos = (BlockPos) obj;
            locations.add(new Location((org.spongepowered.api.world.World) worldObj, VecHelper.toVector(blockPos)));
        }

        // Cause -> Something placed it -> Something exploded -> boom
        Cause cause;
        final EntityLivingBase igniter = getExplosivePlacedBy();

        if (exploder != null && igniter != null) {
            cause = new Cause(new Cause(null, igniter, null), exploder, null);
        } else if (exploder == null && igniter != null) {
            cause = new Cause(null, igniter, null);
        } else if (exploder != null && igniter == null) {
            // TODO If the exploder isn't null but igniter is, is that the root cause as well? Or is world simply the cause? Plugin?
            cause = new Cause(null, exploder, null);
        } else {
            // TODO At this point both exploder and igniter are null, is world simply the cause? Plugin?
            cause = null;
        }

        final WorldOnExplosionEvent event = SpongeEventFactory.createWorldOnExplosion(Sponge.getGame(), cause, this, locations,
                (List<org.spongepowered.api.entity.Entity>) list);
        Sponge.getGame().getEventManager().post(event);

        System.out.println("Explosion exploder: " + exploder);
        System.out.println("Explosion igniter: " + igniter);

        // Sponge -> Minecraft
        affectedBlockPositions.clear();

        if (((org.spongepowered.api.world.explosion.Explosion) (Object) this).shouldBreakBlocks()) {
            List<Location> postLocations = event.getLocations();
            for (Location location : postLocations) {
                affectedBlockPositions.add(VecHelper.toBlockPos(location.getBlockPosition()));
            }
        }
    }
}
