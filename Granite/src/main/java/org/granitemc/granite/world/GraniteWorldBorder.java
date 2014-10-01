package org.granitemc.granite.world;

import org.granitemc.granite.api.world.Location;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.api.world.WorldBorder;

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

public class GraniteWorldBorder implements WorldBorder {
    private GraniteWorld world;

    public GraniteWorldBorder(GraniteWorld world) {
        this.world = world;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Location getCenter() {
        return new Location(world, world.getBorderCenterX(), 0, world.getBorderCenterZ());
    }

    @Override
    public double getBorderSize() {
        return world.getBorderSize();
    }

    @Override
    public void setBorderSize(double d) {
        world.setBorderSize(d);
    }

    @Override
    public void setCenter(Location l) {
        world.setBorderCenterX(l.getX());
        world.setBorderCenterZ(l.getZ());
    }

    @Override
    public double getSafeZone() {
        return world.getBorderSafeZone();
    }

    @Override
    public void setSafeZone(double d) {
        world.setBorderSafeZone(d);
    }

    @Override
    public double getDamagePerBlock() {
        return world.getBorderDamagePerBlock();
    }

    @Override
    public void setDamagePerBlock(double d) {
        world.setBorderDamagePerBlock(d);
    }

    @Override
    public int getWarningBlocks() {
        return world.getBorderWarningBlocks();
    }

    @Override
    public int getWarningTime() {
        return world.getBorderWarningTime();
    }

    @Override
    public void setWarningBlocks(int i) {
        world.setBorderWarningBlocks(i);
    }

    @Override
    public void setWarningTime(int i) {
        world.setBorderWarningTime(i);
    }
}
