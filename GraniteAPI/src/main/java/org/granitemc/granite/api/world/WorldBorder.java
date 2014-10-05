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

package org.granitemc.granite.api.world;

import org.granitemc.granite.api.utils.Location;

public interface WorldBorder {
    /**
     * Returns the world of this border
     */
    public World getWorld();

    /**
     * Returns the center location of this border
     */
    public Location getCenter();

    /**
     * Returns the size of this border
     */
    public double getBorderSize();

    /**
     * Sets the size of this border
     *
     * @param d The size to set
     */
    public void setBorderSize(double d);

    // TODO: check if necessary
    /*public long getBorderSizeLerpTime();

    public void setBorderSizeLerpTime(long l);

    public double getBorderSizeLerpTarget();

    public void setBorderSizeLerpTarget(double d);*/

    /**
     * Sets the center location of this border
     *
     * @param l The location to set
     */
    public void setCenter(Location l);

    /**
     * Returns the safe zone of this border
     */
    public double getSafeZone();

    /**
     * Sets the safe zone size of this border
     *
     * @param d The safe zone size to set
     */
    public void setSafeZone(double d);

    /**
     * Returns how many points of damage per block you take
     */
    public double getDamagePerBlock();

    /**
     * Sets how many points of damage per block you take
     *
     * @param d How many points of damage per block you take
     */
    public void setDamagePerBlock(double d);

    /**
     * Returns the warning blocks of this border
     */
    public int getWarningBlocks();

    /**
     * Returns the warning time of this border
     */
    public int getWarningTime();

    /**
     * Sets the warning blocks of this border
     *
     * @param i The warning blocks to set
     */
    public void setWarningBlocks(int i);

    /**
     * Sets the warning time of this border
     *
     * @param i The warning time to set
     */
    public void setWarningTime(int i);
}
