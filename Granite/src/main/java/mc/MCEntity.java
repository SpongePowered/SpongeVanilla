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

import java.util.UUID;

public class MCEntity implements MC {

    public boolean onGround;

    public double posX;

    public double posY;

    public double posZ;

    public MCEntity riddenByEntity;

    public MCEntity ridingEntity;

    public float width;

    public float height;

    public boolean isDead;

    public int fire;

    public UUID entityUniqueID;

    public int fireResistance;

    public MCWorldServer worldObj;

    public int hurtResistantTime;

    public float rotationYaw;

    public float rotationPitch;

    public MCDataWatcher dataWatcher;
    public boolean isInWater;
    public double motionX;
    public double motionY;
    public double motionZ;

    public void mountEntity(MCEntity other) {
    }

    public float getEyeHeight() {
        return Float.parseFloat(null);
    }

    public int getAir() {
        return Integer.parseInt(null);
    }

    public void setAir(int air) {
    }

    public String getCustomNameTag() {
        return null;
    }

    public void setCustomNameTag(String tag) {
    }

    public boolean getAlwaysRenderNameTag() {
        return Boolean.parseBoolean(null);
    }

    public void setAlwaysRenderNameTag(boolean value) {
    }

    public String getName() {
        return null;
    }

    public void setPositionAndUpdate(double x, double y, double z) {
    }

    public void travelToDimension(int dimensionId) {
    }
}
