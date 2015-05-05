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
package org.spongepowered.vanilla.mixin.management;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDoublePlant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.util.VecHelper;
import org.spongepowered.vanilla.VanillaHooks;

@Mixin(ItemInWorldManager.class)
public abstract class MixinItemInWorldManager {

    @Shadow public World theWorld;

    @Shadow private WorldSettings.GameType gameType;

    @Shadow public EntityPlayerMP thisPlayerMP;

    @Inject(method = "tryHarvestBlock", at = @At("HEAD"), cancellable = true)
    public void onTryHarvestBlock(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        if (VanillaHooks.prepareBlockBreakEvent(this.theWorld, this.gameType, this.thisPlayerMP, pos).isCancelled()) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "activateBlockOrUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isSneaking()Z"), cancellable = true)
    public void onActivateBlockOrUseItem(EntityPlayer player, World worldIn, ItemStack stack, BlockPos pos, EnumFacing side, float hitx, float hity, float hitz, CallbackInfoReturnable<Boolean> ci) {
        // TODO no side in API for side for this factory?
        // TODO Clean this up
        boolean cancelled = Sponge.getGame().getEventManager().post(SpongeEventFactory.createPlayerInteractBlock(Sponge.getGame(), new Cause(null, player, null), (Player) player, new Location(((Player) player).getWorld(), VecHelper.toVector(pos)), EntityInteractionTypes.USE, new Vector3d(hitx, hity, hitz)));
        if (cancelled) {
            final IBlockState state = worldIn.getBlockState(pos);
            boolean isBottom = false;
            if (state.getProperties().containsKey(BlockDoor.HALF)) {
                if (state.getValue(BlockDoor.HALF) != BlockDoor.EnumDoorHalf.UPPER) {
                            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos));
                    isBottom = true;
                }
            } else {
                if (stack.getItem() instanceof ItemDoor || stack.getItem() instanceof ItemDoublePlant) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos.add(0, 2, 0)));
                }
            }
            if (isBottom) {
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos.add(0, 1, 0)));
            } else {
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(theWorld, pos.add(0, -1, 0)));
            }

            ci.setReturnValue(false);
        }
    }
}
