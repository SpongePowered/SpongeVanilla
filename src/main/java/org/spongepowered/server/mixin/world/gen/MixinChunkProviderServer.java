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
package org.spongepowered.server.mixin.world.gen;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.server.world.VanillaDimensionManager;

import java.util.List;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer {

    @Shadow private IChunkProvider serverChunkGenerator;
    @Shadow private List<Chunk> loadedChunks;
    @Shadow private WorldServer worldObj;

    // Optionally unload spawn chunks if not specified in the world configuration
    @Redirect(method = "dropChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;canRespawnHere()Z"))
    private boolean onCanRespawnHere(WorldProvider provider) {
        return provider.canRespawnHere() && VanillaDimensionManager.shouldLoadSpawn(provider.getDimensionType().getId());
    }

    @Inject(method = "unloadQueuedChunks", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", remap = false),
            cancellable = true)
    private void onUnloadQueuedChunks(CallbackInfoReturnable<Boolean> cir) {
        if (this.loadedChunks.isEmpty() && !VanillaDimensionManager.shouldLoadSpawn(this.worldObj.provider.getDimensionType().getId())) {
            VanillaDimensionManager.unloadWorld(this.worldObj.provider.getDimensionType().getId());
            cir.setReturnValue(this.serverChunkGenerator.unloadQueuedChunks());
        }
    }

}
