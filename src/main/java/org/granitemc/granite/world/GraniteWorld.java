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

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.api.world.WorldBorder;
import org.granitemc.granite.block.GraniteBlock;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

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

    @Override
    public BlockType getBlockTypeAtPosition(int x, int y, int z) {
        Object blockType = invoke("net.minecraft.world.World", "getBlock(n.m.util.ChunkCoordinates)", MinecraftUtils.createChunkCoordinates(x, y, z));

        return new GraniteBlockType(blockType);
    }

    @Override
    public void setBlockTypeAtPosition(int x, int y, int z, BlockType type) {
        invoke("n.m.world.World", "setBlock(n.m.util.ChunkCoordinates;n.m.block.IBlockWithMetadata)", MinecraftUtils.createChunkCoordinates(x, y, z), ((GraniteBlockType) type).parent);
    }

    // TODO: No direct method to get dimension in MC1.8
    public int getDimension() {
        return (int) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getDimension");
    }

    public Object getWorldInfo() {
        return invoke("n.m.world.World", "getWorldInfo()");
    }
    
    @Override
    public long getSeed() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getSeed");
    }
    
    @Override
    public long getSpawnX() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getSpawnX");
    }

    @Override
    public long getSpawnY() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getSpawnY");
    }

    @Override
    public long getSpawnZ() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getSpawnZ");
    }

    @Override
    public long getTime() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getTime");
    }

    @Override
    public long getDayTime() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getDayTime");
    }

    //TODO:  setSpawn: a(dt)
    
    @Override
    public void setTime(long t) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setTime(long)", t);
    }

    @Override
    public void setDayTime(long t) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setDayTime(long)", t);
    }
    
    @Override
    public String getLevelName() {
        return (String) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getLevelName");
    }
    
    @Override
    public void setLevelName(String s) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setLevelName(String)", s);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new GraniteWorldBorder(this);
    }

    @Override
    public int getVersion() {
        return (int) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getVersion");
    }
    
    @Override
    public void setVersion(int i) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setVersion(int)", i);
    }
    
    @Override
    public boolean isThundering() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "isThundering");
    }
    
    @Override
    public void setThundering(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setThundering(boolean)", b);
    }
    
    @Override
    public int getThunderDuration() {
        return (int) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getThunderDuration");
    }

    @Override
    public void setThunderDuration(int i) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setThunderDuration(int)", i);
    }
    
    @Override
    public boolean isRaining() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "isRaining");
    }
    
    @Override
    public void setRaining(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setRaining(boolean)", b);
    }
    
    @Override
    public int getRainDuration() {
        return (int) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getRainDuration");
    }

    @Override
    public void setRainDuration(int i) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setRainDuration(int)", i);
    }
    
    //TODO: getGameType: r
    
    @Override
    public boolean getUseMapFeatures() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getUseMapFeatures");
    }
    
    @Override
    public void setUseMapFeatures(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setUseMapFeatures(boolean)", b);
    }
    
    //TODO: setGameType: a(arc)
    
    @Override
    public boolean isHardcore() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "isHardcore");
    }
    
    @Override
    public void setHardcore(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setHardcore(boolean)", b);
    }
    
    //TODO: getWorldGenerator: u
    
    //TODO: setWorldGenerator: a(are)
    
    @Override
    public String getGeneratorOptions() {
        return (String) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getGeneratorOptions");
    }
        
    @Override
    public boolean getAllowCommands() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getAllowCommands");
    }
    
    @Override
    public void setAllowCommands(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setAllowCommands(boolean)", b);
    }
    
    public boolean isInitialized() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "isInitialized");
    }
    
    public void setInitialized(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setInitialized(boolean)", b);
    }
    
    //TODO: getGameRules: x
    
    public double getBorderCenterX() {
        return (double) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderCenterX");
    }
    
    public double getBorderCenterZ() {
        return (double) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderCenterZ");
    }
    
    public double getBorderSize() {
        return (double) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderSize");
    }
        
    public void setBorderSize(double d) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderSize(int)", d);
    }
    
    public long getBorderSizeLerpTime() {
        return (long) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderSizeLerpTime");
    }
        
    public void setBorderSizeLerpTime(long l) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderSizeLerpTime(int)", l);
    }
    
    public double getBorderSizeLerpTarget() {
        return (double) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderSizeLerpTarget");
    }
        
    public void setBorderSizeLerpTarget(double d) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderSizeLerpTarget(int)", d);
    }
    
    public void setBorderCenterZ(double d) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderCenterZ(int)", d);
    }
    
    public void setBorderCenterX(double d) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderCenterX(int)", d);
    }
    
    public double getBorderSafeZone() {
        return (double) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderSafeZone");
    }
        
    public void setBorderSafeZone(double d) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderSafeZone(int)", d);
    }
    
    public double getBorderDamagePerBlock() {
        return (double) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderDamagePerBlock");
    }
        
    public void setBorderDamagePerBlock(double d) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderDamagePerBlock(int)", d);
    }

    public int getBorderWarningBlocks() {
        return (int) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderWarningBlocks");
    }
    
    public int getBorderWarningTime() {
        return (int) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getBorderWarningTime");
    }
    
    public void setBorderWarningBlocks(int i) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderWarningBlocks(int)", i);
    }
    
    public void setBorderWarningTime(int i) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setBorderWarningTime(int)", i);
    }
    
    //TODO: getDifficulty: y

    //TODO: setDifficulty: a(vt)
    
    @Override
    public boolean getDifficultyLocked() {
        return (boolean) Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "getDifficultyLocked");
    }
    
    @Override
    public void setDifficultyLocked(boolean b) {
        Mappings.invoke(getWorldInfo(), "n.m.world.storage.WorldInfo", "setDifficultyLocked(boolean)", b);
    }
}
