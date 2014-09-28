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
    Block getBlock(int x, int y, int z);

    public BlockType getBlockTypeAtPosition(int x, int y, int z);

    public void setBlockTypeAtPosition(int x, int y, int z, BlockType type);

    public long getSeed();
    
    public long getSpawnX();

    public long getSpawnY();

    public long getSpawnZ();

    public long getTime();

    public long getDayTime();

    public void setTime(long t);

    public void setDayTime(long t);
    
    public String getLevelName();
    
    public void setLevelName(String s);

    public int getVersion();
    
    public void setVersion(int i);
    
    public boolean isThundering();
    
    public void setThundering(boolean b);
    
    public int getThunderDuration();

    public void setThunderDuration(int i);
    
    public boolean isRaining();
    
    public void setRaining(boolean b);
    
    public int getRainDuration();

    public void setRainDuration(int i);

    public boolean getUseMapFeatures();
    
    public void setUseMapFeatures(boolean b);
    
    public boolean isHardcore();
    
    public void setHardcore(boolean b);
    
    public String getGeneratorOptions();
        
    public boolean getAllowCommands();
    
    public void setAllowCommands(boolean b);
    
    public double getBorderCenterX();
    
    public double getBorderCenterZ();
    
    public double getBorderSize();
        
    public void setBorderSize(double d);

    // TODO: check if necessary
    // TODO: extract to WorldBorder class
    /*public long getBorderSizeLerpTime();
        
    public void setBorderSizeLerpTime(long l);
    
    public double getBorderSizeLerpTarget();
        
    public void setBorderSizeLerpTarget(double d);*/
    
    public void setBorderCenterZ(double d);
    
    public void setBorderCenterX(double d);
    
    public double getBorderSafeZone();
        
    public void setBorderSafeZone(double d);
    
    public double getBorderDamagePerBlock();
        
    public void setBorderDamagePerBlock(double d);
    
    public int getBorderWarningBlocks();
    
    public int getBorderWarningTime();
    
    public void setBorderWarningBlocks(int i);
    
    public void setBorderWarningTime(int i);
    
    public boolean getDifficultyLocked();
    
    public void setDifficultyLocked(boolean b);
}
