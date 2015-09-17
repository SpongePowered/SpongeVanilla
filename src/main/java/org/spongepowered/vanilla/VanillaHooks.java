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
package org.spongepowered.vanilla;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTransaction;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.block.BreakBlockEvent;
import org.spongepowered.api.event.block.PlaceBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.block.SpongeBlockSnapshot;
import org.spongepowered.common.util.VecHelper;
import org.spongepowered.vanilla.interfaces.IMixinWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class VanillaHooks {

    private VanillaHooks() {
    }

    /**
     * Hook that prepares server logic for the firing of a {@link BreakBlockEvent}.
     *
     * @param world The world
     * @param gameType The gametype
     * @param entityPlayer The player
     * @param pos The position
     * @param blockFacing The face of the block
     * @return The called event
     */
    public static BreakBlockEvent prepareBreakBlockEventAsPlayer(World world, WorldSettings.GameType gameType, EntityPlayerMP
            entityPlayer, BlockPos pos, EnumFacing blockFacing) {
        boolean preCancelEvent = false;
        if (gameType.isCreative() && entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof ItemSword) {
            preCancelEvent = true;
        }
        if (gameType.isAdventure()) {
            if (gameType == WorldSettings.GameType.SPECTATOR) {
                preCancelEvent = true;
            }

            if (!entityPlayer.isAllowEdit()) {
                ItemStack itemstack = entityPlayer.getCurrentEquippedItem();
                if (itemstack == null || !itemstack.canDestroy(world.getBlockState(pos).getBlock())) {
                    preCancelEvent = true;
                }
            }
        }

        // Tell client the block is gone immediately then process events
        if (world.getTileEntity(pos) == null) {
            S23PacketBlockChange packet = new S23PacketBlockChange(world, pos);
            packet.blockState = Blocks.air.getDefaultState();
            entityPlayer.playerNetServerHandler.sendPacket(packet);
        }

        final BlockSnapshot currentSnapshot = ((org.spongepowered.api.world.World) world).createSnapshot(pos.getX(), pos.getY(), pos.getZ());
        final BlockSnapshot defaultSnapshot = currentSnapshot.withState(BlockTypes.AIR.getDefaultState());
        final ImmutableList<BlockTransaction> transactions = ImmutableList.<BlockTransaction>builder().add(new BlockTransaction(currentSnapshot,
                defaultSnapshot)).build();
        final BreakBlockEvent event = SpongeEventFactory.createBreakBlockEvent(Sponge.getGame(), Cause.of(entityPlayer),
                (org.spongepowered.api.world.World) world, transactions);
        // Post the block break event
        event.setCancelled(preCancelEvent);
        Sponge.getGame().getEventManager().post(event);
        if (event.isCancelled()) {
            // Let the client know the block still exists
            entityPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));

            // Update any tile entity data for this block
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity != null) {
                Packet packet = tileentity.getDescriptionPacket();
                if (packet != null) {
                    entityPlayer.playerNetServerHandler.sendPacket(packet);
                }
            }
        } else {
            for (BlockTransaction transaction : event.getTransactions()) {
                if (transaction.getCustomReplacement().isPresent() && transaction.isValid()) {
                    transaction.getFinalReplacement().restore(true, false);
                }
            }
        }
        return event;
    }

    /**
     * Calls a PlaceBlockEvent as a Player.
     * @param stack The stack
     * @param player The player
     * @param world The world
     * @param pos The position
     * @param side The side of the block being placed against
     * @param hitX The interaction point x value
     * @param hitY The interaction point y value
     * @param hitZ The interaction point z value
     * @return True if place suceeded, false otherwise
     */
    @SuppressWarnings("unchecked")
    public static boolean callPlaceBlockEventAsPlayer(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
            float hitY, float hitZ) {
        // Store current itemstack state
        final org.spongepowered.api.item.inventory.ItemStack originalStack = (org.spongepowered.api.item.inventory.ItemStack) stack.copy();
        ((IMixinWorld) world).captureBlockSnapshots(true);
        // Perform item use
        boolean success = stack.getItem().onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
        ((IMixinWorld) world).captureBlockSnapshots(false);

        // If item use is successful, process player block placement
        if (success) {
            // Store the stack after ItemUse
            final org.spongepowered.api.item.inventory.ItemStack currentItemStack = (org.spongepowered.api.item.inventory.ItemStack) stack.copy();
            // Set the stack back to the pre item use for event
            copyStack((ItemStack) originalStack, stack);
            final List<SpongeBlockSnapshot> copiedSnapshots = (ArrayList<SpongeBlockSnapshot>) ((IMixinWorld) world).getCapturedSnapshots().clone();
            ((IMixinWorld) world).getCapturedSnapshots().clear();

            // Build the transactions
            final ImmutableList.Builder<BlockTransaction> blockTransactionBuilder = ImmutableList.builder();
            for (SpongeBlockSnapshot original : copiedSnapshots) {
                final BlockPos opposite = pos.offset(side);
                final SpongeBlockSnapshot now = (SpongeBlockSnapshot) ((org.spongepowered.api.world.World) world).createSnapshot(opposite.getX
                        (), opposite.getY(), opposite.getZ());
                blockTransactionBuilder.add(new BlockTransaction(original, now));
            }
            final PlaceBlockEvent event = SpongeEventFactory.createPlaceBlockEvent(Sponge.getGame(), Cause.of(player), (org.spongepowered.api
                    .world.World) world, blockTransactionBuilder.build());
            // Rollback if cancelled
            if (Sponge.getGame().getEventManager().post(event)) {
                success = false;
            } else {
                // Set itemstack to new content
                copyStack((ItemStack) currentItemStack, stack);
            }
            for (BlockTransaction transaction : event.getTransactions()) {
                if (!transaction.isValid() || event.isCancelled()) {
                    ((IMixinWorld) world).restoreBlockSnapshots(true);
                    transaction.getOriginal().restore(true, false);
                    ((IMixinWorld) world).restoreBlockSnapshots(false);
                } else {
                    transaction.getFinalReplacement().restore(true, true);
                    final IBlockState original = (IBlockState) transaction.getOriginal().getState();
                    final IBlockState now = (IBlockState) transaction.getFinalReplacement().getState();

                    // TODO Restore in SpongeCommon is bugged I think, this line is needed else the above applying fails
                    ((IMixinWorld) world).markAndNotifyBlock(VecHelper.toBlockPos(transaction.getOriginal().getPosition()), null, original,
                            now, 3);
                }
            }
        }

        ((IMixinWorld) world).getCapturedSnapshots().clear();
        return success;
    }

    // This method may be un-needed now
    private static void copyStack(ItemStack source, ItemStack target) {
        target.stackSize = source.stackSize;
        // TODO May need to clear manipulators from source stack sans durability
        final Collection<DataManipulator<?, ?>> manipulators = ((org.spongepowered.api.item.inventory.ItemStack) source).getContainers();
        for (DataManipulator<?, ?> manipulator : manipulators) {
            ((org.spongepowered.api.item.inventory.ItemStack) target).offer((DataManipulator) manipulator);
        }
    }
}
