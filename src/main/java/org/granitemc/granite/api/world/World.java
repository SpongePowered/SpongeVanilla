package org.granitemc.granite.api.world;

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

public interface World {
    /**
     * Returns the {@link org.granitemc.granite.api.block.Block} at the specified location
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     */
    Block getBlock(int x, int y, int z);

    /**
     * Returns the {@link org.granitemc.granite.api.block.BlockType} at the specified location
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     */
    public BlockType getBlockTypeAtPosition(int x, int y, int z);

    /**
     * Sets the {@link org.granitemc.granite.api.block.BlockType} at the specified location
     * @param x The X position
     * @param y The Y position
     * @param z The Z position
     * @param type The type to set
     */
    public void setBlockTypeAtPosition(int x, int y, int z, BlockType type);

    /**
     * Returns the seed of this world
     */
    public long getSeed();

    /**
     * Returns the spawn block of this world
     */
    public Block getSpawnBlock();

    /**
     * Returns the current time of this world
     */
    public long getTime();

    /**
     * Returns the current day time of this world (time mod 24000)
     */
    public long getDayTime();

    /**
     * Sets the time of this world
     * @param t The time
     */
    public void setTime(long t);

    /**
     * Sets the day time of this world
     * @param t The time
     */
    public void setDayTime(long t);

    /**
     * Returns the level name of this world
     */
    public String getLevelName();

    /**
     * Sets the level name of this world
     * @param s The level name
     */
    public void setLevelName(String s);

    /**
     * Returns the world border of this world
     */
    public WorldBorder getWorldBorder();

    // TODO: is this needed?
    //public int getVersion();
    
    //public void setVersion(int i);

    /**
     * Returns whether it's currently thundering in this world
     */
    public boolean isThundering();

    /**
     * Sets whether it's currently thundering in this world
     * @param b Whether it's thundering
     */
    public void setThundering(boolean b);

    /**
     * Returns for how long thundering will continue
     */
    public int getThunderDuration();

    /**
     * Sets for how long thundering will continue
     * @param i How long thundering will continue
     */
    public void setThunderDuration(int i);

    /**
     * Returns whether it's currently raining in this world
     */
    public boolean isRaining();

    /**
     * Sets whether it's currently raining in this world
     * @param b Whether it's raining
     */
    public void setRaining(boolean b);

    /**
     * Returns for how long raining will continue
     */
    public int getRainDuration();

    /**
     * Sets for how long raining will continue
     * @param i How long raining will continue
     */
    public void setRainDuration(int i);

    /**
     * Returns whether this world uses map features (e.g. strongholds)
     */
    public boolean getUseMapFeatures();

    /**
     * Sets whether this world uses map features (e.g. strongholds)
     * @param b Whether this world uses map features
     */
    public void setUseMapFeatures(boolean b);

    /**
     * Returns whether this world is hardcore
     */
    public boolean isHardcore();

    /**
     * Sets whether this world is hardcore
     * @param b Whether this world is hardcore
     */
    public void setHardcore(boolean b);

    // TODO: are these needed?
    //public String getGeneratorOptions();
        
    //public boolean getAllowCommands();
    
    //public void setAllowCommands(boolean b);
    
    //public boolean getDifficultyLocked();
    
    //public void setDifficultyLocked(boolean b);
}
