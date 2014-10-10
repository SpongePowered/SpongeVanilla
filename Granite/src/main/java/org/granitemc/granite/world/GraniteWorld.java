/*
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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.world;

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.utils.Facing;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.utils.Vector;
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
        GraniteBlockType type = (GraniteBlockType) MinecraftUtils.wrap(invoke("getBlock", MinecraftUtils.createBlockPos(x, y, z)));
        return new GraniteBlock(x, y, z, type, this);
    }

    public Block getBlock(Object chunkCoordinates) {
        int x = (int) Mappings.invoke(chunkCoordinates, "getX");
        int y = (int) Mappings.invoke(chunkCoordinates, "getY");
        int z = (int) Mappings.invoke(chunkCoordinates, "getZ");
        return getBlock(x, y, z);
    }

    @Override
    public BlockType getBlockTypeAtPosition(int x, int y, int z) {
        Object blockType = invoke("getBlock", MinecraftUtils.createBlockPos(x, y, z));

        return (BlockType) MinecraftUtils.wrap(blockType);
    }

    @Override
    public void setBlockTypeAtPosition(int x, int y, int z, BlockType type) {
        invoke("World", "setBlock", MinecraftUtils.createBlockPos(x, y, z), ((GraniteBlockType) type).parent);
    }

    // TODO: No direct method to get dimension in MC1.8
    public int getDimension() {
        return (int) Mappings.invoke(getWorldInfo(), "WorldInfo", "getDimension");
    }

    public Object getWorldInfo() {
        return invoke("World", "getWorldInfo");
    }

    @Override
    public long getSeed() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getSeed");
    }

    public long getSpawnX() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getSpawnX");
    }

    public long getSpawnY() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getSpawnY");
    }

    public long getSpawnZ() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getSpawnZ");
    }

    @Override
    public Block getSpawnBlock() {
        return getBlock((int) getSpawnX(), (int) getSpawnY(), (int) getSpawnZ());
    }

    @Override
    public long getTime() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getTime");
    }

    @Override
    public long getDayTime() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getDayTime");
    }

    //TODO:  setSpawn: a(dt)

    @Override
    public void setTime(long t) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setTime", t);
    }

    @Override
    public void setDayTime(long t) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setDayTime", t);
    }

    @Override
    public String getLevelName() {
        return (String) Mappings.invoke(getWorldInfo(), "WorldInfo", "getLevelName");
    }

    @Override
    public void setLevelName(String s) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setLevelName", s);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new GraniteWorldBorder(this);
    }

    public int getVersion() {
        return (int) Mappings.invoke(getWorldInfo(), "WorldInfo", "getVersion");
    }

    public void setVersion(int i) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setVersion", i);
    }

    @Override
    public boolean isThundering() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "isThundering");
    }

    @Override
    public void setThundering(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setThundering", b);
    }

    @Override
    public int getThunderDuration() {
        return (int) Mappings.invoke(getWorldInfo(), "WorldInfo", "getThunderDuration");
    }

    @Override
    public void setThunderDuration(int i) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setThunderDuration", i);
    }

    @Override
    public boolean isRaining() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "isRaining");
    }

    @Override
    public void setRaining(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setRaining", b);
    }

    @Override
    public int getRainDuration() {
        return (int) Mappings.invoke(getWorldInfo(), "WorldInfo", "getRainDuration");
    }

    @Override
    public void setRainDuration(int i) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setRainDuration", i);
    }

    //TODO: getGameType: r

    @Override
    public boolean getUseMapFeatures() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "getUseMapFeatures");
    }

    @Override
    public void setUseMapFeatures(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setUseMapFeatures", b);
    }

    //TODO: setGameType: a(arc)

    @Override
    public boolean isHardcore() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "isHardcore");
    }

    @Override
    public void setHardcore(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setHardcore", b);
    }

    @Override
    public RayTraceResult rayTrace(Vector from, Vector to, boolean stopOnLiquid) {
        Object ret = invoke("World", "rayTraceBlocks",
                MinecraftUtils.toMinecraftVector(from), MinecraftUtils.toMinecraftVector(to), stopOnLiquid);

        try {
            if (((Enum) Mappings.getField("MovingObjectPosition", "typeOfHit").get(ret)).ordinal() != 0) {
                Block b = getBlock(Mappings.getField("MovingObjectPosition", "blockPos").get(ret));

                Facing f = Facing.values()[((Enum) Mappings.getField("MovingObjectPosition", "field_178784_b").get(ret)).ordinal()];
                return new RayTraceResult(b, f);
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RayTraceResult rayTrace(Vector from, Vector direction, double distance, boolean stopOnLiquid) {
        return rayTrace(from, from.add(direction.normalize().scale(distance)), stopOnLiquid);
    }

    //TODO: getWorldGenerator: u

    //TODO: setWorldGenerator: a(are)

    public String getGeneratorOptions() {
        return (String) Mappings.invoke(getWorldInfo(), "WorldInfo", "getGeneratorOptions");
    }

    public boolean getAllowCommands() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "getAllowCommands");
    }

    public void setAllowCommands(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setAllowCommands", b);
    }

    public boolean isInitialized() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "isInitialized");
    }

    public void setInitialized(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setInitialized", b);
    }

    //TODO: getGameRules: x

    public double getBorderCenterX() {
        return (double) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderCenterX");
    }

    public double getBorderCenterZ() {
        return (double) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderCenterZ");
    }

    public double getBorderSize() {
        return (double) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderSize");
    }

    public void setBorderSize(double d) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderSize", d);
    }

    public long getBorderSizeLerpTime() {
        return (long) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderSizeLerpTime");
    }

    public void setBorderSizeLerpTime(long l) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderSizeLerpTime", l);
    }

    public double getBorderSizeLerpTarget() {
        return (double) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderSizeLerpTarget");
    }

    public void setBorderSizeLerpTarget(double d) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderSizeLerpTarget", d);
    }

    public void setBorderCenterZ(double d) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderCenterZ", d);
    }

    public void setBorderCenterX(double d) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderCenterX", d);
    }

    public double getBorderSafeZone() {
        return (double) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderSafeZone");
    }

    public void setBorderSafeZone(double d) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderSafeZone", d);
    }

    public double getBorderDamagePerBlock() {
        return (double) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderDamagePerBlock");
    }

    public void setBorderDamagePerBlock(double d) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderDamagePerBlock", d);
    }

    public int getBorderWarningBlocks() {
        return (int) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderWarningBlocks");
    }

    public int getBorderWarningTime() {
        return (int) Mappings.invoke(getWorldInfo(), "WorldInfo", "getBorderWarningTime");
    }

    public void setBorderWarningBlocks(int i) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderWarningBlocks", i);
    }

    public void setBorderWarningTime(int i) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setBorderWarningTime", i);
    }

    //TODO: getDifficulty: y

    //TODO: setDifficulty: a(vt)

    public boolean getDifficultyLocked() {
        return (boolean) Mappings.invoke(getWorldInfo(), "WorldInfo", "getDifficultyLocked");
    }

    public void setDifficultyLocked(boolean b) {
        Mappings.invoke(getWorldInfo(), "WorldInfo", "setDifficultyLocked", b);
    }
}
