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
package org.spongepowered.server.mixin.chunkio;

import co.aikar.timings.Timing;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.chunkio.ChunkIOExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.interfaces.world.IMixinWorldServer;
import org.spongepowered.server.interfaces.world.chunkio.IMixinChunkProviderServer;

import java.util.function.Consumer;

import javax.annotation.Nullable;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer_ChunkIO implements IChunkProvider, IMixinChunkProviderServer {

    @Shadow @Final private IChunkLoader chunkLoader;
    @Shadow @Final public WorldServer worldObj;

    /**
     * @author Minecrell - May 28th, 2016
     * @reason Load chunk through asynchronous executor
     */
    @Nullable
    @Overwrite
    public Chunk loadChunk(int x, int z) {
        return loadChunk(x, z, null);
    }

    @Nullable
    @Override
    public Chunk loadChunk(int x, int z, @Nullable Consumer<Chunk> callback) {
        Chunk chunk = getLoadedChunk(x, z);

        if (chunk == null) {
            if (callback != null) {
                ChunkIOExecutor.queueChunkLoad(this.worldObj, (AnvilChunkLoader) this.chunkLoader, (ChunkProviderServer) (Object) this, x, z,
                        callback);
                return null;
            } else {
                Timing timing = ((IMixinWorldServer) this.worldObj).getTimingsHandler().syncChunkLoadDataTimer;
                timing.startTiming();
                chunk = ChunkIOExecutor.syncChunkLoad(this.worldObj, (AnvilChunkLoader) this.chunkLoader, (ChunkProviderServer) (Object) this, x, z);
                timing.stopTiming();
            }
        }

        return chunk;
    }

}
