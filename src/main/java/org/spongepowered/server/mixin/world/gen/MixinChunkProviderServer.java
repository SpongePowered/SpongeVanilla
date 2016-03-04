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

import com.google.common.collect.Lists;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Set;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer {

    @Shadow @Final public WorldServer worldObj;
    @Shadow @Final private Set<Long> droppedChunksSet;
    @Shadow public IChunkGenerator chunkGenerator;
    @Shadow @Final private IChunkLoader chunkLoader;
    @Shadow @Final private LongHashMap<Chunk> id2ChunkMap;
    public final List<Chunk> loadedChunks = Lists.<Chunk>newArrayList();
    @Shadow abstract void saveChunkData(Chunk chunkIn);
    @Shadow abstract void saveChunkExtraData(Chunk chunkIn);

    /**
     * Author: Zidane
     * Purpose:
     *  - Check droppedChunksSet first before iterating 100 times "just because"
     */
    @Overwrite
    public boolean unloadQueuedChunks() {
        // Sponge start -
        if (!this.worldObj.disableLevelSaving && !this.droppedChunksSet.isEmpty()) {
            for (int i = 0; i < 100; ++i) {
                Long olong = (Long) this.droppedChunksSet.iterator().next();
                Chunk chunk = (Chunk) this.id2ChunkMap.getValueByKey(olong.longValue());

                if (chunk != null) {
                    chunk.onChunkUnload();
                    this.saveChunkData(chunk);
                    this.saveChunkExtraData(chunk);
                    this.id2ChunkMap.remove(olong.longValue());
                    this.loadedChunks.remove(chunk);
                }

                this.droppedChunksSet.remove(olong);
            }
        }

        this.chunkLoader.chunkTick();

        return false;
    }
}
