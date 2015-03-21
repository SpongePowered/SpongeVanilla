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

@Implement(name = "Block")
public class MCBlock implements MC {

    public MCBlockState defaultBlockState;

    public boolean needsRandomTick;

    public int lightValue;

    public float blockHardness;

    public boolean enableStats;
    public MCMaterial blockMaterial;

    public boolean isSolidFullCube() {
        return Boolean.parseBoolean(null);
    }

    public MCBlockState getStateFromMeta(int meta) {
        return null;
    }

    public MCBlockState getStateFromId(int id) {
        return null;
    }

    public int getStateId(MCBlockState blockState) {
        return Integer.parseInt(null);
    }

    public int getMetaFromState(MCBlockState state) {
        return Integer.parseInt(null);
    }

    public String getUnlocalizedName() {
        return null;
    }

    public boolean isReplaceable(MCWorld world, MCBlockPos pos) {
        return Boolean.parseBoolean(null);
    }
}
