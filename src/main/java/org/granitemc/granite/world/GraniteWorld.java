package org.granitemc.granite.world;

/*****************************************************************************************
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************/

import com.google.common.collect.ImmutableMap;
import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.BlockTypes;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.block.*;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.reflect.InvocationTargetException;

public class GraniteWorld extends Composite implements World {
    public GraniteWorld(Object parent) {
        super(parent);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        GraniteBlockType type = new GraniteBlockType(invoke("net.minecraft.world.World", "getBlock(n.m.util.ChunkCoordinates)", MinecraftUtils.createChunkCoordinates(x, y, z)));
        return new GraniteBlock(x, y, z, type, this);
    }

    public Block getBlock(Object chunkCoordinates) {
        int x = (int) Mappings.invoke(chunkCoordinates, "n.m.util.ChunkCoordinates", "getX");
        int y = (int) Mappings.invoke(chunkCoordinates, "n.m.util.ChunkCoordinates", "getY");
        int z = (int) Mappings.invoke(chunkCoordinates, "n.m.util.ChunkCoordinates", "getZ");
        return getBlock(x, y, z);
    }

    public BlockType getBlockTypeAtPosition(int x, int y, int z) {

        Object blockType = invoke("net.minecraft.world.World", "getBlock(n.m.util.ChunkCoordinates)", MinecraftUtils.createChunkCoordinates(x, y, z));
        //GraniteBlockType.setValue(blockType, "facing", "west");

        return new GraniteBlockType(blockType);
    }

    public void setBlockTypeAtPosition(int x, int y, int z, BlockType type) {

        invoke("n.m.world.World", "setBlock(n.m.util.ChunkCoordinates;n.m.block.IBlockWithMetadata)", MinecraftUtils.createChunkCoordinates(x, y, z), ((GraniteBlockType) type).parent);
    }
}
