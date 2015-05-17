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
package org.spongepowered.vanilla.mixin.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldServer;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.event.entity.player.PlayerQuitEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.registry.SpongeGameRegistry;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.VecHelper;
import org.spongepowered.vanilla.util.EntityUtils;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements INetHandlerPlayServer {

    @Shadow private EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer serverController;
    boolean isRightClickAirCancelled;

    @Redirect(method = "processChatMessage",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsgImpl(Lnet/minecraft/util/IChatComponent;Z)V"))
    public void onProcessChatMessage(ServerConfigurationManager this$0, IChatComponent component, boolean isChat) {
        final PlayerChatEvent event = SpongeEventFactory.createPlayerChat(Sponge.getGame(), (Player) this.playerEntity, SpongeTexts.toText(component));
        if (!Sponge.getGame().getEventManager().post(event)) {
            ((Server) this.serverController).broadcastMessage(event.getNewMessage());
        }
    }

    @Redirect(method = "onDisconnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void onDisconnectHandler(ServerConfigurationManager this$0, IChatComponent component) {
        final PlayerQuitEvent event = SpongeEventFactory.createPlayerQuit(Sponge.getGame(), (Player) this.playerEntity, SpongeTexts.toText
                (component));
        Sponge.getGame().getEventManager().post(event);
        ((Server) this.serverController).broadcastMessage(event.getNewMessage());
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isBlockProtected"
            + "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;)Z"), cancellable = true)
    public void injectProcessPlayerDigging(C07PacketPlayerDigging packetIn, CallbackInfo ci) {
        // We'll handle all the logic here
        ci.cancel();
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        //TODO Quote gabizou: Calculate the clicked location on the server. Hmmm, sounds fun...come back and do later -_-.
        boolean cancelled = Sponge.getGame().getEventManager().post(SpongeEventFactory.createPlayerInteractBlock(Sponge.getGame(),
                new Cause(null, this.playerEntity, null), (Player) this.playerEntity,
                new Location((World) worldserver, VecHelper.toVector(packetIn.getPosition())),
                SpongeGameRegistry.directionMap.inverse().get(packetIn.getFacing()), EntityInteractionTypes.ATTACK, null));
        boolean revert = cancelled;

        if (!cancelled) {
            if (!this.serverController.isBlockProtected(worldserver, packetIn.getPosition(), this.playerEntity) && worldserver.getWorldBorder()
                    .contains(packetIn.getPosition())) {
                this.playerEntity.theItemInWorldManager.onBlockClicked(packetIn.getPosition(), packetIn.getFacing());
            } else {
                revert = true;
            }
        }

        if (revert) {
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, packetIn.getPosition()));
        }
    }

    /**
     * Invoke before {@code packetIn.getPlacedBlockDirection() == 255} (line 576 in source) to reset "isRightClickAirCancelled" flag.
     * @param packetIn Injected packet param
     * @param ci Info to provide mixin on how to handle the callback
     */
    @Inject(method = "processPlayerBlockPlacement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/client/C08PacketPlayerBlockPlacement;getPlacedBlockDirection()I",
                    ordinal = 1))
    public void injectBeforeBlockDirectionCheck(C08PacketPlayerBlockPlacement packetIn, CallbackInfo ci) {
        this.isRightClickAirCancelled = false;
    }

    /**
     * Invoke after {@code packetIn.getPlacedBlockDirection() == 255} (line 576 in source) to fire {@link org.spongepowered.api.event.entity
     * .player.PlayerInteractEvent} on right click with air with item in-hand.
     * @param packetIn Injected packet param
     * @param ci Info to provide mixin on how to handle the callback
     */
    @Inject(method = "processPlayerBlockPlacement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/client/C08PacketPlayerBlockPlacement;getPlacedBlockDirection()I",
                    ordinal = 1, shift = At.Shift.BY, by = 3))
    public void injectBeforeItemStackCheck(C08PacketPlayerBlockPlacement packetIn, CallbackInfo ci) {
        final MovingObjectPosition objectPosition = EntityUtils.rayTraceFromEntity(this.playerEntity, 4, 1);
        if (objectPosition != null && objectPosition.entityHit == null) {
            this.isRightClickAirCancelled = Sponge.getGame().getEventManager().post(SpongeEventFactory.createPlayerInteract(Sponge.getGame(),
                    (Player) this.playerEntity, EntityInteractionTypes.USE, null));
        }
    }

    /**
     * Invoke before {@code itemstack == null} (line 578 in source) to stop the logic for using an item if {@link org.spongepowered.api.event
     * .entity.player.PlayerInteractEvent} was cancelled.
     * @param packetIn Injected packet param
     * @param ci Info to provide mixin on how to handle the callback
     */
    @Inject(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ItemInWorldManager;tryUseItem(Lnet/minecraft/entity/player/EntityPlayer;"
                    + "Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z",
            ordinal = 0), cancellable = true)
    public void injectAfterItemStackCheck(C08PacketPlayerBlockPlacement packetIn, CallbackInfo ci) {
        // Handle interact logic
        ci.cancel();
        if (!this.isRightClickAirCancelled) {
            this.playerEntity.theItemInWorldManager
                    .tryUseItem(this.playerEntity, this.serverController.worldServerForDimension(this.playerEntity.dimension),
                            this.playerEntity.inventory.getCurrentItem());
        }
    }
}
