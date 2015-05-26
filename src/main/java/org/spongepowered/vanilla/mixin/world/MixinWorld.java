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
package org.spongepowered.vanilla.mixin.world;

import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.configuration.SpongeConfig;
import org.spongepowered.common.interfaces.IMixinWorld;
import org.spongepowered.common.world.border.PlayerBorderListener;

import java.io.File;

@Mixin(World.class)
public abstract class MixinWorld implements org.spongepowered.api.world.World, IMixinWorld {

    public SpongeConfig<SpongeConfig.WorldConfig> worldConfig;

    @Shadow private net.minecraft.world.border.WorldBorder worldBorder;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstructed(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client,
            CallbackInfo ci) {
        String providerName = providerIn.getDimensionName().toLowerCase().replace(" ", "_").replace("[^A-Za-z0-9_]", "");
        this.worldConfig =
                new SpongeConfig<SpongeConfig.WorldConfig>(SpongeConfig.Type.WORLD, new File(Sponge.getConfigDirectory()
                        + File.separator + providerName
                        + File.separator + "dim0", "world.conf"),
                        "sponge");

        this.worldBorder.addListener(new PlayerBorderListener());
    }

    @Inject(method = "spawnEntityInWorld", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getChunkFromChunkCoords(II)Lnet/minecraft/world/chunk/Chunk;"))
    public void onSpawnEntityInWorld(Entity entity, CallbackInfoReturnable<Boolean> cir, int i, int j, @Coerce boolean flag) {
        org.spongepowered.api.entity.Entity spongeEntity = (org.spongepowered.api.entity.Entity) entity;
        if (Sponge.getGame().getEventManager().post(SpongeEventFactory.createEntitySpawn(Sponge.getGame(), spongeEntity, spongeEntity.getLocation()))
                && !flag) {
            cir.setReturnValue(false);
        }
    }

    @Surrogate
    public void onSpawnEntityInWorld(Entity entity, CallbackInfoReturnable<Boolean> cir, int i, int j) {
        boolean flag = entity.forceSpawn || entity instanceof EntityPlayer;
        this.onSpawnEntityInWorld(entity, cir, i, j, flag);
    }

}
