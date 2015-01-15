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

package org.granitepowered.granite.mc;

import java.util.List;

@Implement(name = "WorldServer")
public interface MCWorld extends MCInterface {

    MCWorldInfo fieldGet$worldInfo();

    MCWorldBorder fieldGet$worldBorder();

    List<MCEntity> fieldGet$loadedEntityList();

    MCChunkProvider fieldGet$chunkProvider();

    MCWorldProvider fieldGet$provider();

    boolean spawnEntityInWorld(MCEntity entity);

    MCBlockState getBlockState(MCBlockPos blockPos);

    boolean setBlockState(MCBlockPos blockPos, MCBlockState blockState);

    int getLightFor(Enum source, MCBlockPos blockPos);

    boolean isPowered(MCBlockPos blockPos);

    boolean isFacePowered(MCBlockPos blockPos, MCEnumFacing enumFacing);

    int isIndirectlyPowered(MCBlockPos blockPos);

    MCBiomeGenBase getBiomeGenForCoords(MCBlockPos blockPos);

    void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch);
}
