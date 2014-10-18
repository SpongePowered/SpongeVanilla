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

package org.granitemc.granite.block;

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.world.World;

public class GraniteBlock implements Block {
    private World world;
    private int x;
    private int y;
    private int z;
    private BlockType type;

    public GraniteBlock(Location location, GraniteBlockType type) {
        this.world = location.getWorld();
        this.x = (int) location.getX();
        this.y = (int) location.getY();
        this.z = (int) location.getZ();
        this.type = type;
    }

    public GraniteBlock(World world, int x, int y, int z, GraniteBlockType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockType getBlockType() {
        return type;
    }

    public BlockType getBlockTypeAtLocation() {
        return world.getBlockTypeAtPosition(x, y, z);
    }

    public void setBlockTypeAtLocation(BlockType type) {
        world.setBlockTypeAtPosition(x, y, z, type);
    }

	public Location getLocation() {
		return new Location(world, x, y, z);
	}
}
