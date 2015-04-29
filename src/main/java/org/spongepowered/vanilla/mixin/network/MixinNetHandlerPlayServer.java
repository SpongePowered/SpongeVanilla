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
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.event.entity.player.PlayerQuitEvent;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.VecHelper;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements INetHandlerPlayServer {

    @Shadow private EntityPlayerMP playerEntity;
    @Shadow private MinecraftServer serverController;

    @Redirect(method = "processChatMessage",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsgImpl(Lnet/minecraft/util/IChatComponent;Z)V"))
    public void onProcessChatMessage(ServerConfigurationManager this$0, IChatComponent component, boolean isChat) {
        final PlayerChatEvent event = SpongeEventFactory.createPlayerChat(Sponge.getGame(), (Player) this.playerEntity, (CommandSource)
                this.playerEntity, SpongeTexts.toText(component));
        if (!Sponge.getGame().getEventManager().post(event)) {
            ((Server) this.serverController).broadcastMessage(event.getMessage());
        }
    }

    @Redirect(method = "onDisconnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void onDisconnectHandler(ServerConfigurationManager this$0, IChatComponent component) {
        PlayerQuitEvent event = SpongeEventFactory.createPlayerQuit(Sponge.getGame(), (Player) this.playerEntity, SpongeTexts.toText(component));
        Sponge.getGame().getEventManager().post(event);
        ((Server) this.serverController).broadcastMessage(event.getQuitMessage());
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "net/minecraft/network/play/client/C07PacketPlayerDigging$Action.START_DESTROY_BLOCK:Lnet/minecraft/network/play/client/C07PacketPlayerDigging$Action;", shift = At.Shift.AFTER, by = 2), cancellable = true)
    public void injectProcessPlayerDigging(C07PacketPlayerDigging packetIn, CallbackInfo ci) {
        final WorldServer worldserver = this.serverController.worldServerForDimension(this.playerEntity.dimension);
        //TODO Quote gabizou: Calculate the clicked location on the server. Hmmm, sounds fun...come back and do later -_-.
        boolean cancelled = Sponge.getGame().getEventManager().post(SpongeEventFactory.createPlayerInteractBlock(Sponge.getGame(), new Cause(null, playerEntity, null), (Player) playerEntity, new Location((World) worldserver, VecHelper.toVector(packetIn.getPosition())), EntityInteractionTypes.ATTACK, null));
        if (cancelled) {
            ci.cancel();
            this.playerEntity.playerNetServerHandler.sendPacket(new S23PacketBlockChange(worldserver, packetIn.getPosition()));
            TileEntity tileentity = worldserver.getTileEntity(packetIn.getPosition());
            if (tileentity != null) {
                this.playerEntity.playerNetServerHandler.sendPacket(tileentity.getDescriptionPacket());
            }
        }
    }

}
