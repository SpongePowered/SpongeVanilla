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

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTransaction;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.world.ExplosionEvent;
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
public abstract class MixinExplosion {
    @Shadow private World worldObj;
    @Shadow private Entity exploder;
    @Shadow private List<? super Object> affectedBlockPositions;
    @Shadow abstract EntityLivingBase getExplosivePlacedBy();

    @Inject(method = "doExplosionA", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;"
            + "getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void callWorldOnExplosionEvent(CallbackInfo ci, HashSet<?> hashset, boolean flag, int j, int k, float f3, int j1, int l, int k1, int i1,
            List<?> list) {
        final org.spongepowered.api.world.explosion.Explosion spongeExplosion = (org.spongepowered.api.world.explosion.Explosion) this;

        final ImmutableList.Builder<BlockTransaction> blockTransactionBuilder = ImmutableList.builder();

        for (Object obj : affectedBlockPositions) {
            final BlockPos blockPos = (BlockPos) obj;
            final BlockSnapshot currentSnapshot = ((org.spongepowered.api.world.World) worldObj).createSnapshot(blockPos.getX(), blockPos.getY(),
                    blockPos.getZ());
            // TODO Is this the correct state? Would replacement state depend on blocktype?
            blockTransactionBuilder.add(new BlockTransaction(currentSnapshot, currentSnapshot.withState(BlockTypes.AIR.getDefaultState())));
        }

        final ImmutableList<BlockTransaction> blockTransactions = blockTransactionBuilder.build();

        Cause cause;
        final EntityLivingBase igniter = getExplosivePlacedBy();

        if (exploder != null && igniter != null) {
            cause = Cause.of(igniter, exploder, worldObj);
        } else if (exploder == null && igniter != null) {
            cause = Cause.of(igniter, worldObj);
        } else if (exploder != null && igniter == null) {
            cause = Cause.of(exploder, worldObj);
        } else {
            cause = Cause.of(worldObj);
        }

        final ImmutableList.Builder<EntitySnapshot> entitySnapshotBuilder = ImmutableList.builder();
        for (Object obj : list) {
            org.spongepowered.api.entity.Entity spongeEntity = (org.spongepowered.api.entity.Entity) obj;
            entitySnapshotBuilder.add(spongeEntity.createSnapshot());
        }
        final ImmutableList<EntitySnapshot> entitySnapshots = entitySnapshotBuilder.build();

        @SuppressWarnings("unchecked")
        final ExplosionEvent.Detonate event = SpongeEventFactory.createExplosionEventDetonate(Sponge.getGame(), cause,
                (List<org.spongepowered.api.entity.Entity>) list, entitySnapshots, spongeExplosion, (org.spongepowered.api.world.World) worldObj,
                blockTransactions);
        boolean cancelled = Sponge.getGame().getEventManager().post(event);

        affectedBlockPositions.clear();

        // TODO Rolling back an explosion...this will be difficult
        if (!cancelled) {
            if (spongeExplosion.shouldBreakBlocks()) {
                for (BlockTransaction transaction : event.getTransactions()) {
                    if (transaction.isValid()) {
                        affectedBlockPositions.add(VecHelper.toBlockPos(transaction.getFinalReplacement().getPosition()));
                    }
                }
            }
        }
    }
}
