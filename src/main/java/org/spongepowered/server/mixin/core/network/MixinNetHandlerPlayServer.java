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
package org.spongepowered.server.mixin.core.network;

import static org.spongepowered.server.network.VanillaChannelRegistrar.CHANNEL_SEPARATOR;
import static org.spongepowered.server.network.VanillaChannelRegistrar.INTERNAL_PREFIX;
import static org.spongepowered.server.network.VanillaChannelRegistrar.REGISTER_CHANNEL;
import static org.spongepowered.server.network.VanillaChannelRegistrar.UNREGISTER_CHANNEL;

import com.google.common.base.Splitter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.server.chat.ChatFormatter;
import org.spongepowered.server.interfaces.IMixinNetHandlerPlayServer;
import org.spongepowered.server.network.VanillaChannelRegistrar;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer implements RemoteConnection, IMixinNetHandlerPlayServer {

    @Shadow @Final private MinecraftServer serverController;
    @Shadow public EntityPlayerMP playerEntity;

    @Shadow public abstract void sendPacket(final Packet<?> packetIn);

    private static final Splitter CHANNEL_SPLITTER = Splitter.on(CHANNEL_SEPARATOR);

    private final Set<String> registeredChannels = new HashSet<>();

    @Override
    public boolean supportsChannel(String name) {
        return this.registeredChannels.contains(name);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void registerChannels(CallbackInfo ci) {
        ((VanillaChannelRegistrar) Sponge.getChannelRegistrar()).registerChannels((NetHandlerPlayServer) (Object) this);
    }

    @Inject(method = "processChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/PlayerList;sendMessage(Lnet/minecraft/util/text/ITextComponent;Z)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onProcessChatMessage(CPacketChatMessage packet, CallbackInfo ci, String s, ITextComponent component) {
        ChatFormatter.formatChatComponent((TextComponentTranslation) component);
        final Text[] message = SpongeTexts.splitChatMessage((TextComponentTranslation) component); // safe cast
        final MessageChannel originalChannel = ((Player) this.playerEntity).getMessageChannel();
        final MessageChannelEvent.Chat event = SpongeEventFactory.createMessageChannelEventChat(
                Cause.of(NamedCause.source(this.playerEntity)), originalChannel, Optional.of(originalChannel),
                new MessageEvent.MessageFormatter(message[0], message[1]), Text.of(s), false
        );
        if (!SpongeImpl.postEvent(event) && !event.isMessageCancelled()) {
            event.getChannel().ifPresent(channel -> channel.send(this.playerEntity, event.getMessage(), ChatTypes.CHAT));
        } else {
            ci.cancel();
        }
    }

    @Redirect(method = "processChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/PlayerList;sendMessage(Lnet/minecraft/util/text/ITextComponent;Z)V"))
    private void cancelSendChatMsgImpl(PlayerList manager, ITextComponent component, boolean chat) {
        // Do nothing
    }

    @Inject(method = "processCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onProcessPluginMessage(CPacketCustomPayload packet, CallbackInfo ci) {
        final String name = packet.getChannelName();
        if (name.startsWith(INTERNAL_PREFIX)) {
            return;
        }

        ci.cancel();
        switch (name) {
            case REGISTER_CHANNEL: {
                final String channels = packet.getBufferData().toString(StandardCharsets.UTF_8);
                for (String channel : CHANNEL_SPLITTER.split(channels)) {
                    if (this.registeredChannels.add(channel)) {
                        SpongeImpl.postEvent(SpongeEventFactory.createChannelRegistrationEventRegister(Cause.of(NamedCause.source(this.playerEntity),
                                NamedCause.of("connection", this)), channel));
                    }
                }
                break;
            }
            case UNREGISTER_CHANNEL: {
                final String channels = packet.getBufferData().toString(StandardCharsets.UTF_8);
                for (String channel : CHANNEL_SPLITTER.split(channels)) {
                    if (this.registeredChannels.remove(channel)) {
                        SpongeImpl
                                .postEvent(SpongeEventFactory.createChannelRegistrationEventUnregister(Cause.of(NamedCause.source(this.playerEntity),
                                        NamedCause.of("connection", this)), channel));
                    }
                }
                break;
            }
            default:
                // Custom channel
                ((VanillaChannelRegistrar) Sponge.getChannelRegistrar()).post(this, packet);
                break;
        }
    }
}
