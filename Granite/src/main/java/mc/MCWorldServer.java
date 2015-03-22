/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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
 */

package mc;

import java.util.List;

@Implement(name = "WorldServer")
public class MCWorldServer implements MC {

    public MCWorldInfo worldInfo;

    public MCWorldBorder worldBorder;

    public List<MCEntity> loadedEntityList;

    public MCChunkProvider chunkProvider;

    public MCWorldProvider provider;

    public boolean spawnEntityInWorld(MCEntity entity) {
        return Boolean.parseBoolean(null);
    }

    public MCBlockState getBlockState(MCBlockPos blockPos) {
        return null;
    }

    public boolean setBlockState(MCBlockPos blockPos, MCBlockState blockState) {
        return Boolean.parseBoolean(null);
    }

    public int getLightFor(Enum source, MCBlockPos blockPos) {
        return Integer.parseInt(null);
    }

    public boolean isPowered(MCBlockPos blockPos) {
        return Boolean.parseBoolean(null);
    }

    public boolean isFacePowered(MCBlockPos blockPos, MCEnumFacing enumFacing) {
        return Boolean.parseBoolean(null);
    }

    public int isIndirectlyPowered(MCBlockPos blockPos) {
        return Integer.parseInt(null);
    }

    public MCBiomeGenBase getBiomeGenForCoords(MCBlockPos blockPos) {
        return null;
    }

    public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
    }

    public MCTileEntity getTileEntity(MCBlockPos pos) {
        return null;
    }
}
