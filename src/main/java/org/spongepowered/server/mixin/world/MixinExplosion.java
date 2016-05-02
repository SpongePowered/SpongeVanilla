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
package org.spongepowered.server.mixin.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.util.VecHelper;
import org.spongepowered.server.interfaces.IMixinExplosion;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

@Mixin(Explosion.class)
public abstract class MixinExplosion implements org.spongepowered.api.world.explosion.Explosion, IMixinExplosion {

    @Shadow private World worldObj;
    @Shadow @Nullable private Entity exploder;
    @Shadow @Final private List<BlockPos> affectedBlockPositions;
    @Shadow @Nullable abstract EntityLivingBase getExplosivePlacedBy();

    @Override
    public Cause createCause() {
        Object source;
        Object projectileSource = null;
        Object igniter = null;
        if (this.exploder == null) {
            source = getWorld().getBlock(getLocation().getPosition().toInt());
        } else {
            source = this.exploder;
            if (source instanceof Projectile) {
                projectileSource = ((Projectile) this.exploder).getShooter();
            }

            // Don't use the exploder itself as igniter
            igniter = getExplosivePlacedBy();
            if (this.exploder == igniter) {
                igniter = null;
            }
        }

        if (projectileSource != null) {
            if (igniter != null) {
                return Cause.of(NamedCause.source(source), NamedCause.of("ProjectileSource", projectileSource), NamedCause.of("Igniter", igniter));
            } else {
                return Cause.of(NamedCause.source(source), NamedCause.of("ProjectileSource", projectileSource));
            }
        } else if (igniter != null) {
            return Cause.of(NamedCause.source(source), NamedCause.of("Igniter", igniter));
        } else {
            return Cause.of(NamedCause.source(source));
        }
    }

    @Redirect(method = "doExplosionA", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;"
            + "getEntitiesWithinAABBExcludingEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;"))
    private List<Entity> callWorldOnExplosionEvent(World world, Entity entity, AxisAlignedBB aabb) {
        final List<Entity> affectedEntities = this.shouldDamageEntities() ? world.getEntitiesWithinAABBExcludingEntity(entity, aabb) : Collections.emptyList();
        final org.spongepowered.api.world.World spongeWorld = (org.spongepowered.api.world.World) this.worldObj;

        final ImmutableList.Builder<Transaction<BlockSnapshot>> blockTransactionBuilder = ImmutableList.builder();
        for (BlockPos pos : this.affectedBlockPositions) {
            if (world.isBlockLoaded(pos, false)) {
                final BlockSnapshot currentSnapshot = spongeWorld.createSnapshot(pos.getX(), pos.getY(), pos.getZ());
                // TODO Is this the correct state? Would replacement state depend on blocktype?
                blockTransactionBuilder.add(new Transaction<>(currentSnapshot, currentSnapshot.withState(BlockTypes.AIR.getDefaultState())));
            }
        }
        final ImmutableList<Transaction<BlockSnapshot>> blockTransactions = blockTransactionBuilder.build();

        this.affectedBlockPositions.clear();

        @SuppressWarnings("unchecked")
        final ExplosionEvent.Detonate event = SpongeEventFactory.createExplosionEventDetonate(createCause(),
                (List) affectedEntities, this, (org.spongepowered.api.world.World) worldObj,
                blockTransactions);

        // TODO Rolling back an explosion...this will be difficult
        if (!SpongeImpl.postEvent(event)) {
            if (shouldBreakBlocks()) {
                for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                    if (transaction.isValid()) {
                        affectedBlockPositions.add(VecHelper.toBlockPos(transaction.getFinal().getPosition()));
                    }
                }
            }
        } else {
            // Don't hurt entities if event is cancelled
            affectedEntities.clear();
        }

        return affectedEntities;
    }

}
