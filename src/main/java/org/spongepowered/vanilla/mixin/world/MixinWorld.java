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

import com.flowpowered.math.vector.Vector3i;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.util.VecHelper;
import org.spongepowered.vanilla.block.VanillaBlockSnapshot;

@Mixin(World.class)
public abstract class MixinWorld implements org.spongepowered.api.world.World {

    @Shadow
    public abstract IBlockState getBlockState(BlockPos blockPos);

    @Inject(method = "spawnEntityInWorld", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getChunkFromChunkCoords(II)Lnet/minecraft/world/chunk/Chunk;"))
    public void onSpawnEntityInWorld(Entity entity, CallbackInfoReturnable<Boolean> cir, int i, int j, @Coerce boolean flag) {
        org.spongepowered.api.entity.Entity spongeEntity = (org.spongepowered.api.entity.Entity) entity;
        if (Sponge.getGame().getEventManager().post(SpongeEventFactory.createEntitySpawn(Sponge.getGame(), spongeEntity, spongeEntity.getLocation()))
                && !flag) {
            cir.setReturnValue(false);
        }
    }

    @Surrogate
    public void onSpawnEntityInWorld(Entity entity, CallbackInfoReturnable<Boolean> cir, int i, int j) {
        boolean flag = entity.forceSpawn || entity instanceof EntityPlayer;
        this.onSpawnEntityInWorld(entity, cir, i, j, flag);
    }

    @Override
    public BlockSnapshot getBlockSnapshot(Vector3i position) {
        final BlockPos blockPos = VecHelper.toBlockPos(position);
        return new VanillaBlockSnapshot((World) (Object) this, blockPos, getBlockState(blockPos));
    }

    @Override
    public BlockSnapshot getBlockSnapshot(int x, int y, int z) {
        final BlockPos blockPos = new BlockPos(x, y, z);
        return new VanillaBlockSnapshot((World) (Object) this, blockPos , getBlockState(blockPos));
    }

}
