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

import static org.spongepowered.server.network.VanillaChannelRegistrar.CHANNEL_SEPARATOR;
import static org.spongepowered.server.network.VanillaChannelRegistrar.INTERNAL_PREFIX;
import static org.spongepowered.server.network.VanillaChannelRegistrar.REGISTER_CHANNEL;
import static org.spongepowered.server.network.VanillaChannelRegistrar.UNREGISTER_CHANNEL;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Splitter;
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
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.registry.provider.DirectionFacingProvider;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.server.interfaces.IMixinNetHandlerPlayServer;
import org.spongepowered.server.network.VanillaChannelRegistrar;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(value = NetHandlerPlayServer.class, priority = 1001)
public abstract class MixinNetHandlerPlayServer implements RemoteConnection, INetHandlerPlayServer, IMixinNetHandlerPlayServer {

    private static final String ACTIVATE_BLOCK_OR_USE_ITEM =
            "Lnet/minecraft/server/management/ItemInWorldManager;activateBlockOrUseItem(Lnet/minecraft/entity/player/EntityPlayer;"
            + "Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;FFF)Z";
    private static final String
            PROCESS_BLOCK_PLACEMENT =
            "processPlayerBlockPlacement(Lnet/minecraft/network/play/client/C08PacketPlayerBlockPlacement;)V";
    @Shadow private EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer serverController;

    private static final Splitter CHANNEL_SPLITTER = Splitter.on(CHANNEL_SEPARATOR);

    private final Set<String> registeredChannels = new HashSet<>();
    private boolean forceUpdateInventorySlot;

    @Override
    public boolean supportsChannel(String name) {
        return registeredChannels.contains(name);
    }

    @Inject(method = "processChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsgImpl(Lnet/minecraft/util/IChatComponent;Z)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void onProcessChatMessage(C01PacketChatMessage packet, CallbackInfo ci, String s, IChatComponent component) {
        final Optional<Text> message = Optional.ofNullable(SpongeTexts.toText(component));
        final MessageChannel originalChannel = ((Player) this.playerEntity).getMessageChannel();
        final MessageChannelEvent.Chat event = SpongeEventFactory.createMessageChannelEventChat(Cause.of(NamedCause.source(this.playerEntity)),
                originalChannel, Optional.of(originalChannel), message,  message, Text.of(s));
        if (!SpongeImpl.postEvent(event)) {
            event.getMessage().ifPresent(text -> event.getChannel().ifPresent(channel -> channel.send(text)));
        } else {
            ci.cancel();
        }
    }

    @Redirect(method = "processChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsgImpl(Lnet/minecraft/util/IChatComponent;Z)V"))
    public void cancelSendChatMsgImpl(ServerConfigurationManager manager, IChatComponent component, boolean chat) {
        // do nothing
    }

    @Redirect(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ItemInWorldManager;tryUseItem(Lnet/minecraft/entity/player/EntityPlayer;"
                    + "Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean tryUseItem(ItemInWorldManager itemInWorldManager, EntityPlayer player, World world, ItemStack stack) {
        // TODO: Forge passes (0,0,0) as block when interacting with the air
        //BlockRayHit<World> blockHit = BlockRay.from((Entity) player).filter(BlockRay.<World>onlyAirFilter()).end().get();
        BlockSnapshot block = ((org.spongepowered.api.world.World) world).createSnapshot(0, 0, 0).withState(BlockTypes.AIR.getDefaultState());

        InteractBlockEvent.Secondary event = SpongeEventFactory.createInteractBlockEventSecondary(Cause.of(NamedCause.source(player)),
                Optional.<Vector3d>empty(), block, Direction.NONE); // TODO: Pass direction? (Forge doesn't)
        return !SpongeImpl.postEvent(event) && itemInWorldManager.tryUseItem(player, world, stack);
    }

    @Redirect(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE", target = ACTIVATE_BLOCK_OR_USE_ITEM))
    public boolean onActivateBlockOrUseItem(ItemInWorldManager itemManager, EntityPlayer player, net.minecraft.world.World worldIn, ItemStack stack,
            BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        BlockSnapshot currentSnapshot = ((org.spongepowered.api.world.World) worldIn).createSnapshot(pos.getX(), pos.getY(), pos.getZ());
        InteractBlockEvent.Secondary event = SpongeEventFactory.createInteractBlockEventSecondary(Cause.of(NamedCause.source(player)),
                Optional.of(new Vector3d(hitX, hitY, hitZ)), currentSnapshot, DirectionFacingProvider.getInstance().getKey(side).get());
        if (SpongeImpl.postEvent(event)) {
            final IBlockState state = worldIn.getBlockState(pos);

            if (state.getBlock() == Blocks.command_block) {
                // CommandBlock GUI opens solely on the client, we need to force it close on cancellation
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(0));

            } else if (state.getProperties().containsKey(BlockDoor.HALF)) {
                // Stopping a door from opening while interacting the top part will allow the door to open, we need to update the
                // client to resolve this
                if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldIn, pos.up()));
                } else {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldIn, pos.down()));
                }

            } else if (stack != null) {
                // Stopping the placement of a door or double plant causes artifacts (ghosts) on the top-side of the block. We need to remove it
                if (stack.getItem() instanceof ItemDoor || stack.getItem() instanceof ItemDoublePlant) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldIn, pos.up(2)));
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
        return itemManager.activateBlockOrUseItem(player, worldIn, stack, pos, side, hitX, hitY, hitZ)
                || itemManager.tryUseItem(player, worldIn, stack);
    }

    @Redirect(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemStacksEqual"
            + "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean onAreItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        // Force client to update the itemstack if event was cancelled
        return !this.forceUpdateInventorySlot && ItemStack.areItemStacksEqual(stackA, stackB);
    }

    @Inject(method = "processVanilla250Packet", at = @At("HEAD"), cancellable = true)
    private void onProcessPluginMessage(C17PacketCustomPayload packet, CallbackInfo ci) {
        final String name = packet.getChannelName();
        if (name.startsWith(INTERNAL_PREFIX)) {
            return;
        }

        ci.cancel();
        if (name.equals(REGISTER_CHANNEL)) {
            final String channels = packet.getBufferData().toString(StandardCharsets.UTF_8);
            for (String channel : CHANNEL_SPLITTER.split(channels)) {
                registeredChannels.add(channel);
            }
        } else if (name.equals(UNREGISTER_CHANNEL)) {
            final String channels = packet.getBufferData().toString(StandardCharsets.UTF_8);
            for (String channel : CHANNEL_SPLITTER.split(channels)) {
                registeredChannels.remove(channel);
            }
        } else {
            // Custom channel
            ((VanillaChannelRegistrar) Sponge.getChannelRegistrar()).post(this, packet);
        }
    }

}
