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

import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.event.state.ServerStoppedEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.vanilla.SpongeVanilla;

import java.util.Collection;
import java.util.Collections;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements Server, CommandSource, ICommandSender {

    @Shadow
    private static Logger logger;

    @Shadow
    private ServerConfigurationManager serverConfigManager;

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;addFaviconToStatusResponse"
            + "(Lnet/minecraft/network/ServerStatusResponse;)V", shift = At.Shift.AFTER))
    public void onServerStarted(CallbackInfo ci) {
        SpongeVanilla.INSTANCE.postState(ServerStartedEvent.class);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;finalTick(Lnet/minecraft/crash/CrashReport;)V",
            ordinal = 0, shift = At.Shift.BY, by = -9))
    public void onServerStopping(CallbackInfo ci) {
        SpongeVanilla.INSTANCE.postState(ServerStoppingEvent.class);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;systemExitNow()V"))
    public void onServerStopped(CallbackInfo ci) {
        SpongeVanilla.INSTANCE.postState(ServerStoppedEvent.class);
    }

    @Overwrite
    public String getServerModName() {
        return SpongeVanilla.INSTANCE.getName();
    }

    @Override
    @Overwrite
    public void addChatMessage(IChatComponent component) {
        logger.info(SpongeTexts.toLegacy(component));
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        return (Collection<Player>) Collections.unmodifiableCollection(serverConfigManager.playerEntityList);
    }
}
