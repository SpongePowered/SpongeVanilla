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
package org.spongepowered.server.mixin.network;

import com.flowpowered.math.vector.Vector3d;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDoublePlant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.command.MessageSinkEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.registry.provider.DirectionFacingProvider;
import org.spongepowered.common.text.SpongeTexts;

import java.util.Optional;

import javax.annotation.Nullable;

@Mixin(value = NetHandlerPlayServer.class, priority = 1001)
public abstract class MixinNetHandlerPlayServer implements INetHandlerPlayServer {

    @Shadow private EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer serverController;

    private boolean forceUpdateInventorySlot;

    @Redirect(method = "processChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsgImpl(Lnet/minecraft/util/IChatComponent;Z)V"))
    public void onProcessChatMessage(ServerConfigurationManager this$0, IChatComponent component, boolean isChat) {
        final Text message = SpongeTexts.toText(component);
        final MessageSink sink = ((Player) playerEntity).getMessageSink();
        final MessageSinkEvent event = SpongeEventFactory.createMessageSinkEventChat(SpongeImpl.getGame(), Cause.of(NamedCause.source(playerEntity)),
                message, message, sink, sink, message);
        if (!SpongeImpl.postEvent(event)) {
            event.getSink().sendMessage(event.getMessage());
        }
    }

    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void onDisconnectHandler(ServerConfigurationManager this$0, IChatComponent component) {
        final Player spongePlayer = ((Player) playerEntity);
        final Text message = SpongeTexts.toText(component);
        final MessageSink sink = spongePlayer.getMessageSink();
        final ClientConnectionEvent.Disconnect event = SpongeEventFactory.createClientConnectionEventDisconnect(SpongeImpl.getGame(),
                Cause.of(NamedCause.source(spongePlayer)), message, message, sink, sink, spongePlayer);
        SpongeImpl.postEvent(event);
    }

    @Redirect(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ItemInWorldManager;tryUseItem(Lnet/minecraft/entity/player/EntityPlayer;"
                    + "Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"))
    public boolean tryUseItem(ItemInWorldManager itemInWorldManager, EntityPlayer player, World world, ItemStack stack) {
        // TODO: Forge passes (0,0,0) as block when interacting with the air
        //BlockRayHit<World> blockHit = BlockRay.from((Entity) player).filter(BlockRay.<World>onlyAirFilter()).end().get();
        BlockSnapshot block = ((org.spongepowered.api.world.World) world).createSnapshot(0, 0, 0);

        InteractBlockEvent.Secondary event = SpongeEventFactory.createInteractBlockEventSecondary(SpongeImpl.getGame(),
                Cause.of(NamedCause.source(player)), Optional.<Vector3d>empty(), block, Direction.NONE); // TODO: Pass direction? (Forge doesn't)
        return !SpongeImpl.postEvent(event) && itemInWorldManager.tryUseItem(player, world, stack);
    }

    @Redirect(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ItemInWorldManager;activateBlockOrUseItem(Lnet/minecraft/entity/player/EntityPlayer;"
                    + "Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;FFF)Z"))
    public boolean onActivateBlockOrUseItem(ItemInWorldManager itemInWorldManager, EntityPlayer player, World world,
            @Nullable ItemStack stack, BlockPos pos, EnumFacing side, float offsetX, float offsetY, float offsetZ) {
        BlockSnapshot currentSnapshot = ((org.spongepowered.api.world.World) world).createSnapshot(pos.getX(), pos.getY(), pos.getZ());
        InteractBlockEvent.Secondary event = SpongeEventFactory.createInteractBlockEventSecondary(SpongeImpl.getGame(),
                Cause.of(NamedCause.source(player)), Optional.of(new Vector3d(offsetX, offsetY, offsetZ)), currentSnapshot,
                DirectionFacingProvider.getInstance().getKey(side).get());
        if (SpongeImpl.postEvent(event)) {
            final IBlockState state = world.getBlockState(pos);

            if (state.getBlock() == Blocks.command_block) {
                // CommandBlock GUI opens solely on the client, we need to force it close on cancellation
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(0));

            } else if (state.getProperties().containsKey(BlockDoor.HALF)) {
                // Stopping a door from opening while interacting the top part will allow the door to open, we need to update the
                // client to resolve this
                if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos.up()));
                } else {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos.down()));
                }

            } else if (stack != null) {
                // Stopping the placement of a door or double plant causes artifacts (ghosts) on the top-side of the block. We need to remove it
                if (stack.getItem() instanceof ItemDoor || stack.getItem() instanceof ItemDoublePlant) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos.up(2)));
                }
            }

            // Force update inventory slot (client assumes block placement doesn't fail)
            this.forceUpdateInventorySlot = true;
            return false;
        }

        this.forceUpdateInventorySlot = false;

        // Try placing the block or using the item on the block
        // If that is not successful, try to use the item without a block instead
        // This is necessary because we filter the second packet sent when interacting with a block
        return itemInWorldManager.activateBlockOrUseItem(player, world, stack, pos, side, offsetX, offsetY, offsetZ)
                || itemInWorldManager.tryUseItem(player, world, stack);
    }

    @Redirect(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemStacksEqual"
            + "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    public boolean onAreItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        // Force client to update the itemstack if event was cancelled
        boolean result = !this.forceUpdateInventorySlot && ItemStack.areItemStacksEqual(stackA, stackB);
        System.out.println(result);
        return result;
    }

}
