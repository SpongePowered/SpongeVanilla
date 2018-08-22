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
package org.spongepowered.server.mixin.core.server.management;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.event.SpongeCommonEventFactory;
import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayerMP;
import org.spongepowered.common.util.VecHelper;

import com.flowpowered.math.vector.Vector3d;

@Mixin(PlayerInteractionManager.class)
public abstract class MixinPlayerInteractionManager {

    @Shadow public EntityPlayerMP player;

    @Inject(method = "onBlockClicked", at = @At("HEAD"), cancellable = true)
    public void onBlockClicked(BlockPos pos, EnumFacing side, CallbackInfo ci) {
        final BlockSnapshot blockSnapshot = new Location<>((World) this.player.world, VecHelper.toVector3d(pos)).createSnapshot();
        final RayTraceResult result = SpongeImplHooks.rayTraceEyes(this.player, SpongeImplHooks.getBlockReachDistance(this.player));
        final Vector3d vec = result == null ? null : VecHelper.toVector3d(result.hitVec);
        final ItemStack stack = this.player.getHeldItemMainhand();

        if (SpongeCommonEventFactory.callInteractItemEventPrimary(this.player, stack, EnumHand.MAIN_HAND, vec, blockSnapshot).isCancelled() ||
                SpongeCommonEventFactory.callInteractBlockEventPrimary(this.player, blockSnapshot, EnumHand.MAIN_HAND, side, vec).isCancelled()) {
            ((IMixinEntityPlayerMP) this.player).sendBlockChange(pos, this.player.world.getBlockState(pos));
            this.player.world.notifyBlockUpdate(pos, player.world.getBlockState(pos), player.world.getBlockState(pos), 3);
            ci.cancel();
        }
    }

    @Inject(method = "processRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 0), cancellable = true)
    public void onProcessRightClick(EntityPlayer player, net.minecraft.world.World worldIn, ItemStack stack, EnumHand hand,
            CallbackInfoReturnable<EnumActionResult> cir) {
        if (!player.world.isRemote) {
            SpongeCommonEventFactory.lastSecondaryPacketTick = SpongeImpl.getServer().getTickCounter();
            final long packetDiff = System.currentTimeMillis() - SpongeCommonEventFactory.lastTryBlockPacketTimeStamp;
            // If the time between packets is small enough, ignore event.
            if (packetDiff < 100) {
                // Note: We do not cancel here as we still want to process right-click
                return;
            }
        }

        final RayTraceResult result = SpongeImplHooks.rayTraceEyes(player, SpongeImplHooks.getBlockReachDistance((EntityPlayerMP) player));
        final BlockPos pos = result == null ? new BlockPos(player) : result.getBlockPos();
        final Vector3d vec = result == null ? VecHelper.toVector3d(pos) : VecHelper.toVector3d(result.hitVec);
        final BlockSnapshot blockSnapshot = new Location<>((World) player.world, VecHelper.toVector3d(pos)).createSnapshot();
        Object hitTarget = null;
        if (result != null && result.entityHit != null) {
            hitTarget = result.entityHit;
        } else if (result != null && pos != null) {
            hitTarget = blockSnapshot;
        }

        boolean isCancelled = false;
        if (SpongeCommonEventFactory.callInteractItemEventSecondary(player, stack, EnumHand.MAIN_HAND, vec, hitTarget).isCancelled()) {
            isCancelled = true;
        } else {
            final InteractBlockEvent.Secondary event =
                    SpongeCommonEventFactory.createInteractBlockEventSecondary(player, stack, vec, blockSnapshot, Direction.NONE, hand);
            SpongeImpl.postEvent(event);
            if (event.isCancelled() && event.getUseItemResult() != Tristate.TRUE) {
                isCancelled = true;
            }
        }
        if (isCancelled) {
            // Multiple slots may have been changed on the client. Right
            // clicking armor is one example - the client changes it
            // without the server telling it to.
            this.player.sendAllContents(player.openContainer, player.openContainer.getInventory());
            cir.setReturnValue(EnumActionResult.PASS);
        }
    }
}
