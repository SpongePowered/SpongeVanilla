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
package org.spongepowered.server.mixin.server.management;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.registry.provider.DirectionFacingProvider;
import org.spongepowered.server.interfaces.IMixinNetHandlerPlayServer;

import javax.annotation.Nullable;

@Mixin(PlayerInteractionManager.class)
public abstract class MixinPlayerInteractionManager {

    // TODO 1.9 - Have Aaron re-write InteractBlockPrimary hook (needs to go into common)
//    @Shadow public net.minecraft.world.World theWorld;
//    @Shadow public EntityPlayerMP thisPlayerMP;
//
//    @Inject(method = "onBlockClicked", at = @At("HEAD"), cancellable = true)
//    private void onOnBlockClicked(BlockPos pos, EnumFacing side, CallbackInfo ci) {
//        final Location<World> location = new Location<>((World) this.theWorld, VecHelper.toVector3i(pos));
//        final InteractBlockEvent.Primary event;
//        if (= SpongeEventFactory.createInteractBlockEventPrimary(Cause.of(NamedCause.source(this.thisPlayerMP)),
//                Vector3d.ZERO, location.createSnapshot(), DirectionFacingProvider.getInstance().getKey(side).get());
//        if (SpongeImpl.postEvent(event)) {
//            this.thisPlayerMP.playerNetServerHandler.sendPacket(new SPacketBlockChange(this.theWorld, pos));
//            ci.cancel();
//        }
//    }

    @Inject(method = "processRightClickBlock", at = @At(value = "HEAD"), cancellable = true)
    private void onProcessRightClickBlock(EntityPlayer player, net.minecraft.world.World worldIn,
            @Nullable ItemStack stack, EnumHand hand, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> cir) {
        final BlockSnapshot currentSnapshot = ((Extent) worldIn).createSnapshot(pos.getX(), pos.getY(), pos.getZ());
        final InteractBlockEvent.Secondary event;
        if (hand == EnumHand.MAIN_HAND) {
            event = SpongeEventFactory.createInteractBlockEventSecondaryMainHand(Cause.of(NamedCause.source(player)),
                    new Vector3d(hitX, hitY, hitZ), currentSnapshot, DirectionFacingProvider.getInstance().getKey(side).get());
        } else {
            event = SpongeEventFactory.createInteractBlockEventSecondaryOffHand(Cause.of(NamedCause.source(player)),
                    new Vector3d(hitX, hitY, hitZ), currentSnapshot, DirectionFacingProvider.getInstance().getKey(side).get());
        }

        if (SpongeImpl.postEvent(event)) {
            final IBlockState state = worldIn.getBlockState(pos);

            if (state.getBlock() == Blocks.command_block) {
                // CommandBlock GUI opens solely on the client, we need to force it close on cancellation
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new SPacketCloseWindow(0));

            } else if (state.getProperties().containsKey(BlockDoor.HALF)) {
                // Stopping a door from opening while interacting the top part will allow the door to open, we need to update the
                // client to resolve this
                if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new SPacketBlockChange(worldIn, pos.up()));
                } else {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new SPacketBlockChange(worldIn, pos.down()));
                }

            } else if (stack != null) {
                // Stopping the placement of a door or double plant causes artifacts (ghosts) on the top-side of the block. We need to remove it
                if (stack.getItem() instanceof ItemDoor || Item.getItemFromBlock(Blocks.double_plant).equals(stack.getItem())) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new SPacketBlockChange(worldIn, pos.up(2)));
                }
            }

            // Force update inventory slot (client assumes block placement doesn't fail)
            ((IMixinNetHandlerPlayServer) ((EntityPlayerMP) player).playerNetServerHandler).forceUpdateInventorySlot(true);
            cir.setReturnValue(EnumActionResult.FAIL);
        }

        ((IMixinNetHandlerPlayServer) ((EntityPlayerMP) player).playerNetServerHandler).forceUpdateInventorySlot(false);
    }
}
