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
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.api.world.World;
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
    public Block getBlock(Location location) {
        return getBlock((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        //GraniteBlockType type = (GraniteBlockType) MinecraftUtils.wrap(invoke("getBlock", MinecraftUtils.createBlockPos(x, y, z)));
        return new GraniteBlock(x, y, z, this);
    }

    public Block getBlock(Object chunkCoordinates) {
        int x = (int) Mappings.invoke(chunkCoordinates, "getX");
        int y = (int) Mappings.invoke(chunkCoordinates, "getY");
        int z = (int) Mappings.invoke(chunkCoordinates, "getZ");
        return getBlock(x, y, z);
    }

    @Override
    public BlockType getBlockTypeAtPosition(int x, int y, int z) {
        Object blockType = invoke("getBlock", MinecraftUtils.toMinecraftLocation(new Location(this, x, y, z)));

        return (BlockType) MinecraftUtils.wrap(blockType);
    }

    @Override
    public void setBlockTypeAtPosition(int x, int y, int z, BlockType type) {
        invoke("setBlock", MinecraftUtils.toMinecraftLocation(new Location(this, x, y, z)), ((GraniteBlockType) type).parent);
    }

    // TODO: No direct method to get dimension in MC1.8
    public int getDimension() {
        return (int) Mappings.invoke(getWorldInfo(), "getDimension");
    }

    public Object getWorldInfo() {
        return invoke("getWorldInfo");
    }

    @Override
    public long getSeed() {
        return (long) Mappings.invoke(getWorldInfo(), "getSeed");
    }

    public int getSpawnX() {
        return (int) Mappings.invoke(getWorldInfo(), "getSpawnX");
    }

    public int getSpawnY() {
        return (int) Mappings.invoke(getWorldInfo(), "getSpawnY");
    }

    public int getSpawnZ() {
        return (int) Mappings.invoke(getWorldInfo(), "getSpawnZ");
    }

    @Override
    public Block getSpawnBlock() {
        return getBlock((int) getSpawnX(), (int) getSpawnY(), (int) getSpawnZ());
    }

    @Override
    public long getTime() {
        return (long) Mappings.invoke(getWorldInfo(), "getTime");
    }

    @Override
    public long getDayTime() {
        return (long) Mappings.invoke(getWorldInfo(), "getDayTime");
    }

    //TODO:  setSpawn: a(dt)

    @Override
    public void setTime(long t) {
        Mappings.invoke(getWorldInfo(), "setTime", t);
    }

    @Override
    public void setDayTime(long t) {
        Mappings.invoke(getWorldInfo(), "setDayTime", t);
    }

    @Override
    public String getLevelName() {
        return (String) Mappings.invoke(getWorldInfo(), "getLevelName");
    }

    @Override
    public void setLevelName(String s) {
        Mappings.invoke(getWorldInfo(), "setLevelName", s);
    }

/*    @Override
    public WorldBorder getWorldBorder() {
        return new GraniteWorldBorder(this);
    }*/

    public int getVersion() {
        return (int) Mappings.invoke(getWorldInfo(), "getVersion");
    }

    public void setVersion(int i) {
        Mappings.invoke(getWorldInfo(), "setVersion", i);
    }

    @Override
    public boolean isThundering() {
        return (boolean) Mappings.invoke(getWorldInfo(), "isThundering");
    }

    @Override
    public void setThundering(boolean b) {
        Mappings.invoke(getWorldInfo(), "setThundering", b);
    }

    @Override
    public int getThunderDuration() {
        return (int) Mappings.invoke(getWorldInfo(), "getThunderDuration");
    }

    @Override
    public void setThunderDuration(int i) {
        Mappings.invoke(getWorldInfo(), "setThunderDuration", i);
    }

    @Override
    public boolean isRaining() {
        return (boolean) Mappings.invoke(getWorldInfo(), "isRaining");
    }

    @Override
    public void setRaining(boolean b) {
        Mappings.invoke(getWorldInfo(), "setRaining", b);
    }

    @Override
    public int getRainDuration() {
        return (int) Mappings.invoke(getWorldInfo(), "getRainDuration");
    }

    @Override
    public void setRainDuration(int i) {
        Mappings.invoke(getWorldInfo(), "setRainDuration", i);
    }

    //TODO: getGameType: r

    @Override
    public boolean getUseMapFeatures() {
        return (boolean) Mappings.invoke(getWorldInfo(), "getUseMapFeatures");
    }

    @Override
    public void setUseMapFeatures(boolean b) {
        Mappings.invoke(getWorldInfo(), "setUseMapFeatures", b);
    }

    //TODO: setGameType: a(arc)

    @Override
    public boolean isHardcore() {
        return (boolean) Mappings.invoke(getWorldInfo(), "isHardcore");
    }

    @Override
    public void setHardcore(boolean b) {
        Mappings.invoke(getWorldInfo(), "setHardcore", b);
    }

    @Override
    public RayTraceResult rayTrace(Vector from, Vector to, boolean stopOnLiquid) {
        Object ret = invoke("rayTraceBlocks",
                MinecraftUtils.toMinecraftVector(from), MinecraftUtils.toMinecraftVector(to), stopOnLiquid);

        try {
            if (ret != null && ((Enum) Mappings.getField("MovingObjectPosition", "typeOfHit").get(ret)).ordinal() != 0) {
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
        return (String) Mappings.invoke(getWorldInfo(), "getGeneratorOptions");
    }

    public boolean getAllowCommands() {
        return (boolean) Mappings.invoke(getWorldInfo(), "getAllowCommands");
    }

    public void setAllowCommands(boolean b) {
        Mappings.invoke(getWorldInfo(), "setAllowCommands", b);
    }

    public boolean isInitialized() {
        return (boolean) Mappings.invoke(getWorldInfo(), "isInitialized");
    }

    public void setInitialized(boolean b) {
        Mappings.invoke(getWorldInfo(), "setInitialized", b);
    }

    //TODO: getGameRules: x

    public double getBorderCenterX() {
        return (double) Mappings.invoke(getWorldInfo(), "getBorderCenterX");
    }

    public double getBorderCenterZ() {
        return (double) Mappings.invoke(getWorldInfo(), "getBorderCenterZ");
    }

    public double getBorderSize() {
        return (double) Mappings.invoke(getWorldInfo(), "getBorderSize");
    }

    public void setBorderSize(double d) {
        Mappings.invoke(getWorldInfo(), "setBorderSize", d);
    }

    public long getBorderSizeLerpTime() {
        return (long) Mappings.invoke(getWorldInfo(), "getBorderSizeLerpTime");
    }

    public void setBorderSizeLerpTime(long l) {
        Mappings.invoke(getWorldInfo(), "setBorderSizeLerpTime", l);
    }

    public double getBorderSizeLerpTarget() {
        return (double) Mappings.invoke(getWorldInfo(), "getBorderSizeLerpTarget");
    }

    public void setBorderSizeLerpTarget(double d) {
        Mappings.invoke(getWorldInfo(), "setBorderSizeLerpTarget", d);
    }

    public void setBorderCenterZ(double d) {
        Mappings.invoke(getWorldInfo(), "setBorderCenterZ", d);
    }

    public void setBorderCenterX(double d) {
        Mappings.invoke(getWorldInfo(), "setBorderCenterX", d);
    }

    public double getBorderSafeZone() {
        return (double) Mappings.invoke(getWorldInfo(), "getBorderSafeZone");
    }

    public void setBorderSafeZone(double d) {
        Mappings.invoke(getWorldInfo(), "setBorderSafeZone", d);
    }

    public double getBorderDamagePerBlock() {
        return (double) Mappings.invoke(getWorldInfo(), "getBorderDamagePerBlock");
    }

    public void setBorderDamagePerBlock(double d) {
        Mappings.invoke(getWorldInfo(), "setBorderDamagePerBlock", d);
    }

    public int getBorderWarningBlocks() {
        return (int) Mappings.invoke(getWorldInfo(), "getBorderWarningBlocks");
    }

    public int getBorderWarningTime() {
        return (int) Mappings.invoke(getWorldInfo(), "getBorderWarningTime");
    }

    public void setBorderWarningBlocks(int i) {
        Mappings.invoke(getWorldInfo(), "setBorderWarningBlocks", i);
    }

    public void setBorderWarningTime(int i) {
        Mappings.invoke(getWorldInfo(), "setBorderWarningTime", i);
    }

    //TODO: getDifficulty: y

    //TODO: setDifficulty: a(vt)

    public boolean getDifficultyLocked() {
        return (boolean) Mappings.invoke(getWorldInfo(), "getDifficultyLocked");
    }

    public void setDifficultyLocked(boolean b) {
        Mappings.invoke(getWorldInfo(), "setDifficultyLocked", b);
    }
}
