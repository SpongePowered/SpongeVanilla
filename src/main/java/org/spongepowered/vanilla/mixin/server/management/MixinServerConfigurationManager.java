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
package org.spongepowered.vanilla.mixin.server.management;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.entity.player.PlayerRespawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.text.SpongeTexts;

import javax.annotation.Nullable;

@Mixin(ServerConfigurationManager.class)
public abstract class MixinServerConfigurationManager {

    @Shadow private MinecraftServer mcServer;
    @Nullable private IChatComponent joinMessage;

    @Redirect(method = "initializeConnectionToPlayer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void initializeConnectionToPlayerRedirectSendChatMsg(ServerConfigurationManager this$0, IChatComponent component) {
        this.joinMessage = component;
    }

    @Inject(method = "initializeConnectionToPlayer", at = @At("RETURN"))
    public void initializeConnectionToPlayerInject(NetworkManager netManager, EntityPlayerMP player, CallbackInfo ci) {
        PlayerJoinEvent event = SpongeEventFactory.createPlayerJoin(Sponge.getGame(), (Player) player, SpongeTexts.toText(this.joinMessage));
        this.joinMessage = null;
        Sponge.getGame().getEventManager().post(event);

        // Send (possibly changed) join message
        ((Server) this.mcServer).broadcastMessage(event.getJoinMessage());
    }

    /**
     * Invoke before <code>return entityplayermp1;</code> (line 513 of source) to fire {@link org.spongepowered.api.event.entity.player.PlayerRespawnEvent}.
     * @param playerIn Injected player param
     * @param dimension Injected dimension param
     * @param conqueredEnd Injected conquered end flag
     * @param ci Info to provide mixin on how to handle the callback
     */
    @Inject(method = "recreatePlayerEntity", at = @At("RETURN"))
    public void onRecreatePlayerEntityEnd(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd, CallbackInfoReturnable<EntityPlayerMP> ci) {
        final PlayerRespawnEvent event = SpongeEventFactory.createPlayerRespawn(Sponge.getGame(), (Player) playerIn, playerIn.getBedLocation() != null, ((Player) playerIn).getLocation());
        Sponge.getGame().getEventManager().post(event);
        ((Player) playerIn).setLocation(event.getRespawnLocation());
    }

}
