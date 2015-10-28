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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.command.MessageSinkEvent;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.registry.SpongeGameRegistry;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.VecHelper;

import java.util.Optional;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements INetHandlerPlayServer {

    @Shadow private EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer serverController;

    @Redirect(method = "processChatMessage",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsgImpl(Lnet/minecraft/util/IChatComponent;Z)V"))
    public void onProcessChatMessage(ServerConfigurationManager this$0, IChatComponent component, boolean isChat) {
        final Player spongePlayer = ((Player) playerEntity);
        final Text message = SpongeTexts.toText(component);
        final MessageSink sink = spongePlayer.getMessageSink();
        final MessageSinkEvent event = SpongeEventFactory.createMessageSinkEvent(Sponge.getGame(), Cause.of(spongePlayer), message, message, sink,
                sink);
        if (!Sponge.getGame().getEventManager().post(event)) {
            event.getSink().sendMessage(event.getMessage());
        }
    }

    @Redirect(method = "onDisconnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void onDisconnectHandler(ServerConfigurationManager this$0, IChatComponent component) {
        final Player spongePlayer = ((Player) playerEntity);
        final Text message = SpongeTexts.toText(component);
        final MessageSink sink = spongePlayer.getMessageSink();
        final ClientConnectionEvent.Disconnect event = SpongeEventFactory.createClientConnectionEventDisconnect(Sponge.getGame(), Cause.of
                (spongePlayer), message, message, sink, sink, spongePlayer);
        Sponge.getGame().getEventManager().post(event);
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isBlockProtected"
            + "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;)Z"), cancellable = true)
    public void injectProcessPlayerDigging(C07PacketPlayerDigging packetIn, CallbackInfo ci) {
        // We'll handle all the logic here
        ci.cancel();
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        final Location<World> location = new Location<World>((World) worldserver, VecHelper.toVector(packetIn.getPosition()));
//        final BlockRay<World> blockRay = BlockRay.from((Entity) playerEntity).filter(BlockRay
//                .<World>onlyAirFilter()).build();
//        final Optional<Vector3d> optInteractionPoint = Optional.of(blockRay.end().get().getNormal());
//        final InteractBlockEvent.Primary event = SpongeEventFactory.createInteractBlockEventPrimary(Sponge.getGame(), Cause.of(playerEntity),
//                optInteractionPoint, location.createSnapshot(), SpongeGameRegistry.directionMap.inverse().get(packetIn.getFacing()));
        final InteractBlockEvent.Primary event = SpongeEventFactory.createInteractBlockEventPrimary(Sponge.getGame(), Cause.of(playerEntity),
                Optional.<Vector3d>empty(), location.createSnapshot(), SpongeGameRegistry.directionMap.inverse().get(packetIn.getFacing()));
        boolean cancelled = Sponge.getGame().getEventManager().post(event);

        if (!cancelled) {
            if (!this.serverController.isBlockProtected(worldserver, packetIn.getPosition(), this.playerEntity) && worldserver.getWorldBorder()
                    .contains(packetIn.getPosition())) {
                this.playerEntity.theItemInWorldManager.onBlockClicked(packetIn.getPosition(), packetIn.getFacing());
            } else {
                cancelled = true;
            }
        }

        if (cancelled) {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, packetIn.getPosition()));
        }
    }

    /**
     * Invoke after {@code itemstack == null} (line 578 in source) to stop the logic for using an item if {@link UseItemStackEvent} was cancelled.
     * @param packetIn Injected packet param
     * @param ci Info to provide mixin on how to handle the callback
     */
    @Inject(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ItemInWorldManager;tryUseItem(Lnet/minecraft/entity/player/EntityPlayer;"
                    + "Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void injectAfterItemStackCheck(C08PacketPlayerBlockPlacement packetIn, CallbackInfo ci, WorldServer worldserver, ItemStack itemstack,
            @Coerce boolean flag, BlockPos blockpos, EnumFacing enumfacing) {
        // Handle interact logic
        ci.cancel();

        ItemStackSnapshot original = ((org.spongepowered.api.item.inventory.ItemStack) itemstack).createSnapshot();
        final Transaction<ItemStackSnapshot> itemStackTransaction = new Transaction<ItemStackSnapshot>(original, original.copy());
        final UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(Sponge.getGame(), Cause.of(playerEntity),
                0, 0, itemStackTransaction);
        boolean cancelled = Sponge.getGame().getEventManager().post(event);

        if (!cancelled) {
            // Play out item use
            this.playerEntity.theItemInWorldManager
                    .tryUseItem(this.playerEntity, this.serverController.worldServerForDimension(this.playerEntity.dimension),
                            this.playerEntity.inventory.getCurrentItem());

            // Fire interact block on air block we swung our itemstack at
            final Optional<Vector3d> optInteractionPoint = Optional.of(new Vector3d(packetIn.getPlacedBlockOffsetX(), packetIn
                    .getPlacedBlockOffsetY(), packetIn.getPlacedBlockOffsetZ()));

            final Optional<BlockRayHit<World>> optBlockHit = BlockRay.from((Entity) playerEntity).filter(BlockRay.<World>onlyAirFilter()).end();
            final BlockSnapshot current = ((World) worldserver).createSnapshot(optBlockHit.get().getBlockPosition());
            final InteractBlockEvent.Secondary otherEvent =
                    SpongeEventFactory.createInteractBlockEventSecondary(Sponge.getGame(), Cause.of(playerEntity), optInteractionPoint, current,
                            SpongeGameRegistry.directionMap.inverse().get(enumfacing.getOpposite()));
            Sponge.getGame().getEventManager().post(otherEvent);

        }
    }
}
