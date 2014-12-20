/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

package org.granitepowered.granite.impl.world;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCWorldBorder;
import org.spongepowered.api.world.WorldBorder;

public class GraniteWorldBorder extends Composite<MCWorldBorder> implements WorldBorder {
    public GraniteWorldBorder(Object obj) {
        super(obj);
    }

    @Override
    public double getNewRadius() {
        return obj.getDiameter() / 2;
    }

    @Override
    public double getRadius() {
        return obj.fieldGet$startDiameter() / 2;
    }

    @Override
    public void setRadius(double diameter) {
        obj.setDiameter(diameter);
    }

    @Override
    public void setRadius(double endDiameter, long time) {
        // TODO: Temp fix until Marvin looks at mappings
        obj.setTargetAndTime((obj.fieldGet$startDiameter()), endDiameter, time);
    }

    @Override
    public long getTimeRemaining() {
        return obj.getTimeRemaining();
    }

    @Override
    public void setCenter(double x, double z) {
        obj.setCenter(x, z);
    }

    @Override
    public Vector3d getCenter() {
        return new Vector3d(new Vector2d(obj.fieldGet$centerX(), obj.fieldGet$centerZ()));
    }

    @Override
    public int getWarningTime() {
        return obj.fieldGet$warningTime();
    }

    @Override
    public void setWarningTime(int ticks) {
        obj.setWarningTime(ticks);
    }

    @Override
    public int getWarningDistance() {
        return obj.fieldGet$warningDistance();
    }

    @Override
    public void setWarningDistance(int blocks) {
        obj.setWarningDistance(blocks);
    }

    @Override
    public int getBlockBuffer() {
        return (int) obj.fieldGet$blockBuffer();
    }

    @Override
    public void setBlockBuffer(int buffer) {
        obj.setBlockBuffer(buffer);
    }

    @Override
    public int getDamageAmount() {
        return (int) obj.fieldGet$damageAmount();
    }

    @Override
    public void setDamageAmount(int damage) {
        obj.setDamageAmount(damage);
    }
}
