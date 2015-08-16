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
package org.spongepowered.vanilla.mixin.world.gen;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.vanilla.world.VanillaDimensionManager;

import java.util.List;
import java.util.Set;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer {
    @Shadow public WorldServer worldObj;
    @Shadow private Set<? super Object> droppedChunksSet;
    @Shadow public List<?> loadedChunks;
    @Shadow public IChunkProvider serverChunkGenerator;

    @SuppressWarnings("unchecked")
    @Overwrite
    public void dropChunk(int p_73241_1_, int p_73241_2_) {
        if (this.worldObj.provider.canRespawnHere() && VanillaDimensionManager.shouldLoadSpawn(this.worldObj.provider.getDimensionId())) {
            if (!this.worldObj.isSpawnChunk(p_73241_1_, p_73241_2_)) {
                this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
            }
        }
        else {
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
        }
    }

    @Inject(method = "unloadQueuedChunks", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target =
            "Ljava/util/List;remove(Ljava/lang/Object;)Z"), cancellable = true)
    public void onUnloadQueuedChunks(CallbackInfoReturnable<Boolean> cir) {
        if (this.loadedChunks.size() == 0 && !VanillaDimensionManager.shouldLoadSpawn(this.worldObj.provider.getDimensionId())){
            VanillaDimensionManager.unloadWorld(this.worldObj.provider.getDimensionId());
            cir.setReturnValue(this.serverChunkGenerator.unloadQueuedChunks());
        }
    }
}
