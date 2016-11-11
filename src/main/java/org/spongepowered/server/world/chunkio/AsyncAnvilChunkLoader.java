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
package org.spongepowered.server.world.chunkio;

import static net.minecraft.world.chunk.storage.AnvilChunkLoader.readChunkEntity;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;

import java.io.DataInputStream;
import java.io.IOException;

public final class AsyncAnvilChunkLoader {

    private AsyncAnvilChunkLoader() {
    }

    /**
     * Returns the NBT data for the chunk. Returns the cached data if already
     * loaded, otherwise it will check the stored files for the chunk.
     *
     * <p>This is save to call asynchronously from a separate thread.</p>
     *
     * @param loader The loader to use for loading the data
     * @param x The x coordinate of the chunk
     * @param z The y coordinate of the chunk
     * @return The NBT data, or null if no data is present for the chunk
     * @throws IOException If an error occurs while reading the chunk
     */
    public static NBTTagCompound read(AnvilChunkLoader loader, int x, int z) throws IOException {
        // Copied from AnvilChunkLoader.loadChunk, keep this up-to-date!

        ChunkPos chunkpos = new ChunkPos(x, z);
        NBTTagCompound nbttagcompound = loader.chunksToRemove.get(chunkpos);

        if (nbttagcompound == null) {
            DataInputStream datainputstream = RegionFileCache.getChunkInputStream(loader.chunkSaveLocation, x, z);

            if (datainputstream == null) {
                return null;
            }

            nbttagcompound = loader.dataFixer.process(FixTypes.CHUNK, CompressedStreamTools.read(datainputstream));
        }

        // Sponge: Return NBT data and load chunk later
        return nbttagcompound;
    }

    /**
     * Finishes the loading of a chunk and adds stored entities and tile
     * entities.
     *
     * <p>This part of the chunk loading should be <b>always</b> called
     * on the main thread.</p>
     *
     * @param worldIn The world to load the chunk in
     * @param chunk The chunk to add the entities to
     * @param compound The loaded data of the chunk
     */
    public static void loadEntities(World worldIn, Chunk chunk, NBTTagCompound compound) {
        compound = compound.getCompoundTag("Level"); // Sponge: Method below expects Level nbt compound

        // Copied from AnvilChunkLoader.readChunkFromNBT, keep this up-to-date!
        NBTTagList nbttaglist1 = compound.getTagList("Entities", 10);

        //if (nbttaglist1 != null) { // Sponge: Condition is always true
            for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1) {
                NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j1);
                readChunkEntity(nbttagcompound1, worldIn, chunk);
                chunk.setHasEntities(true);
            }
        //}

        NBTTagList nbttaglist2 = compound.getTagList("TileEntities", 10);

        //if (nbttaglist2 != null) { // Sponge: Condition is always true
            for (int k1 = 0; k1 < nbttaglist2.tagCount(); ++k1) {
                NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(k1);
                TileEntity tileentity = TileEntity.create(worldIn, nbttagcompound2);

                if (tileentity != null) {
                    chunk.addTileEntity(tileentity);
                }
            }
        //}

        if (compound.hasKey("TileTicks", 9)) {
            NBTTagList nbttaglist3 = compound.getTagList("TileTicks", 10);

            //if (nbttaglist3 != null) {  // Sponge: Condition is always true
                for (int l1 = 0; l1 < nbttaglist3.tagCount(); ++l1) {
                    NBTTagCompound nbttagcompound3 = nbttaglist3.getCompoundTagAt(l1);
                    Block block;

                    if (nbttagcompound3.hasKey("i", 8)) {
                        block = Block.getBlockFromName(nbttagcompound3.getString("i"));
                    } else {
                        block = Block.getBlockById(nbttagcompound3.getInteger("i"));
                    }

                    worldIn.mth_000649_b(
                            new BlockPos(nbttagcompound3.getInteger("x"), nbttagcompound3.getInteger("y"), nbttagcompound3.getInteger("z")), block,
                            nbttagcompound3.getInteger("t"), nbttagcompound3.getInteger("p"));
                }
            //}
        }
    }

}
