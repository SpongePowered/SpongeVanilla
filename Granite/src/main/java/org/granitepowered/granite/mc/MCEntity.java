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

import java.util.UUID;

@Implement(name = "Entity")
public class MCEntity implements MC {

    public boolean fieldGet$onGround() {
        return Boolean.parseBoolean(null);
    }

    public double fieldGet$posX() {
        return Double.parseDouble(null);
    }

    public double fieldGet$posY() {
        return Double.parseDouble(null);
    }

    public double fieldGet$posZ() {
        return Double.parseDouble(null);
    }

    public MCEntity fieldGet$riddenByEntity() {
        return null;
    }

    public MCEntity fieldGet$ridingEntity() {
        return null;
    }

    public void fieldSet$riddenByEntity(MCEntity entity) {
    }

    public void fieldSet$ridingEntity(MCEntity entity) {
    }

    public float fieldGet$width() {
        return Float.parseFloat(null);
    }

    public float fieldGet$height() {
        return Float.parseFloat(null);
    }

    public boolean fieldGet$isDead() {
        return Boolean.parseBoolean(null);
    }

    public int fieldGet$fire() {
        return Integer.parseInt(null);
    }

    public void fieldSet$fire(int ticks) {
    }

    public UUID fieldGet$entityUniqueID() {
        return null;
    }

    public int fieldGet$fireResistance() {
        return Integer.parseInt(null);
    }

    public MCWorld fieldGet$worldObj() {
        return null;
    }

    public int fieldGet$hurtResistantTime() {
        return Integer.parseInt(null);
    }

    public void fieldSet$hurtResistantTime(int ticks) {
    }

    public float fieldGet$rotationYaw() {
        return Float.parseFloat(null);
    }

    public void fieldSet$rotationYaw(float yaw) {
    }

    public float fieldGet$rotationPitch() {
        return Float.parseFloat(null);
    }

    public void fieldSet$rotationPitch(float pitch) {
    }

    public MCDataWatcher fieldGet$dataWatcher() {
        return null;
    }

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

    public boolean fieldGet$isInWater() {
        return Boolean.parseBoolean(null);
    }

    public double fieldGet$motionX() {
        return Double.parseDouble(null);
    }

    public void fieldSet$motionX(double motionX) {
    }

    public double fieldGet$motionY() {
        return Double.parseDouble(null);
    }

    public void fieldSet$motionY(double motionY) {
    }

    public double fieldGet$motionZ() {
        return Double.parseDouble(null);
    }

    public void fieldSet$motionZ(double motionZ) {
    }
}
