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
package org.spongepowered.vanilla.mixin.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.text.SpongeTexts;

import java.util.Iterator;

@Mixin(ServerConfigurationManager.class)
public abstract class MixinServerConfigurationManager {

    @Shadow private MinecraftServer mcServer;

    @Redirect(method = "initializeConnectionToPlayer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void initializeConnectionToPlayerRedirectSendChatMsg(ServerConfigurationManager this$0, IChatComponent component) {
        // Nullroute the call, we're sending the message below
    }

    @Inject(method = "initializeConnectionToPlayer", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void initializeConnectionToPlayerInject(NetworkManager netManager, EntityPlayerMP player, CallbackInfo ci, GameProfile gameprofile,
            PlayerProfileCache playerprofilecache, GameProfile gameprofile1, String s, NBTTagCompound nbttagcompound, String s1,
            WorldServer worldserver, WorldInfo worldinfo, BlockPos blockpos, NetHandlerPlayServer nethandlerplayserver,
            ChatComponentTranslation chatcomponenttranslation, Iterator iterator) {

        PlayerJoinEvent event = SpongeEventFactory.createPlayerJoin(Sponge.getGame(), (Player) player, SpongeTexts.toText(chatcomponenttranslation));
        Sponge.getGame().getEventManager().post(event);

        // Send (possibly changed) join message
        ((Server) this.mcServer).broadcastMessage(event.getJoinMessage());
    }

}
