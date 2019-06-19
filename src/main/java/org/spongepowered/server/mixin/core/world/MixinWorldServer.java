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
package org.spongepowered.server.mixin.core.world;

import net.minecraft.network.Packet;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.interfaces.world.IMixinWorldInfo;
import org.spongepowered.common.interfaces.world.IMixinWorldServer;
import org.spongepowered.common.world.WorldManager;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World implements IMixinWorldServer {

    private MixinWorldServer(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/DimensionType;getById(I)Lnet/minecraft/world/DimensionType;"))
    private static DimensionType getDimensionType(int dimensionId) {
        return WorldManager.getDimensionType(dimensionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dimension id: " + dimensionId));
    }

    @Redirect(method = "updateWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/DimensionType;getId()I"))
    private int onGetDimensionIdForWeather(DimensionType type) {
        return getDimensionId();
    }

    // Prevent wrong weather changes getting sent to players in other (unaffected) dimensions
    // This causes "phantom rain" on the client, sunny and rainy weather at the same time
    @Redirect(method = "updateWeather", require = 4, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/management/PlayerList;sendPacketToAllPlayers(Lnet/minecraft/network/Packet;)V"))
    private void onSendWeatherPacket(PlayerList manager, Packet<?> packet) {
        manager.sendPacketToAllPlayersInDimension(packet, getDimensionId());
    }

    @Override
    public int getDimensionId() {
        return ((IMixinWorldInfo) this.worldInfo).getDimensionId();
    }
}
