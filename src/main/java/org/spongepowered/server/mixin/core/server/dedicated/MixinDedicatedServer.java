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
package org.spongepowered.server.mixin.core.server.dedicated;

import static org.spongepowered.common.SpongeImpl.MINECRAFT_VERSION;

import net.minecraft.network.ServerStatusResponse;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.scheduler.SpongeScheduler;
import org.spongepowered.server.SpongeVanilla;

@Mixin(DedicatedServer.class)
public abstract class MixinDedicatedServer extends MinecraftServer {

    @Shadow public abstract String getMotd();

    private static final String CONSTRUCT_CONFIG_MANAGER
            = "Lnet/minecraft/server/dedicated/DedicatedPlayerList;<init>(Lnet/minecraft/server/dedicated/DedicatedServer;)V";
    private static final String SET_PROPERTY = "Lnet/minecraft/server/dedicated/PropertyManager;setProperty(Ljava/lang/String;Ljava/lang/Object;)V";
    private static final String LOAD_ALL_WORLDS = "Lnet/minecraft/server/dedicated/DedicatedServer;loadAllWorlds"
            + "(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/world/WorldType;Ljava/lang/String;)V";

    public MixinDedicatedServer() {
        super(null, null, null, null, null, null, null);
    }

    @Shadow private PropertyManager settings;

    @Inject(method = "startServer()Z", at = @At(value = "INVOKE_STRING", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V",
            args = "ldc=Loading properties", remap = false))
    private void onServerLoad(CallbackInfoReturnable<Boolean> ci) throws Exception {
        SpongeVanilla.INSTANCE.preInitialize();
    }

    @Inject(method = "startServer()Z", at = @At(value = "INVOKE", target = CONSTRUCT_CONFIG_MANAGER, shift = At.Shift.BEFORE))
    private void onServerInitialize(CallbackInfoReturnable<Boolean> ci) {
        if (this.getFolderName() == null) {
            this.mth_000170_j(this.settings.getStringProperty("level-name", "world"));
        }

        SpongeVanilla.INSTANCE.initialize();
        ServerStatusResponse statusResponse = getServerStatusResponse();
        statusResponse.setServerDescription(new TextComponentString(this.getMotd()));
        statusResponse.setVersion(
                new ServerStatusResponse.Version(MINECRAFT_VERSION.getName(), MINECRAFT_VERSION.getProtocol()));
        this.applyServerIconToResponse(statusResponse);
    }

    @Inject(method = "startServer()Z", at = @At(value = "INVOKE", target = SET_PROPERTY, ordinal = 2, shift = At.Shift.AFTER))
    private void callServerAboutToStart(CallbackInfoReturnable<Boolean> ci) {
        SpongeVanilla.INSTANCE.onServerAboutToStart();
    }

    @Inject(method = "startServer()Z", at = @At(value = "INVOKE", target = LOAD_ALL_WORLDS, shift = At.Shift.AFTER))
    private void callServerStarting(CallbackInfoReturnable<Boolean> ci) {
        SpongeVanilla.INSTANCE.onServerStarting();
    }

    @Inject(method = "updateTimeLightAndEntities", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        SpongeScheduler.getInstance().tickSyncScheduler();
    }

    @Inject(method = "systemExitNow", at = @At(value = "HEAD"))
    private void callServerStopped(CallbackInfo ci) throws Exception {
        SpongeVanilla.INSTANCE.onServerStopped();
    }

}
