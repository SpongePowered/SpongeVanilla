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

package org.granitemc.granite.api.block;

import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.world.World;

public interface Block {
    /**
     * Returns the {@link org.granitemc.granite.api.world.World} this block is in
     */
    World getWorld();

    /**
     * Returns the X position of this block
     */
    int getX();

    /**
     * Returns the Y position of this block
     */
    int getY();

    /**
     * Returns the Z position of this block
     */
    int getZ();

    /**
     * Returns the {@link org.granitemc.granite.api.block.BlockType} of this block at the location specified
     */
    BlockType getType();

    /**
     * Sets the {@link org.granitemc.granite.api.block.BlockType} of this block at the location specified - this will edit the block
     *
     * @param type The type to set
     */
    void setType(BlockType type);
    
    /**
     * Returns the {@link org.granitemc.granite.api.utils.Location} of this block
     */
	Location getLocation();
}
