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

@Implement(name = "WorldBorder")
public interface MCWorldBorder extends MCInterface {

    double getDiameter();

    void setDiameter(double size);

    double fieldGet$startDiameter();

    void setTargetAndTime(double newSize, double newEndSize, long time);

    long getTimeRemaining();

    double fieldGet$centerX();

    double fieldGet$centerZ();

    void setCenter(double x, double z);

    int fieldGet$warningTime();

    void setWarningTime(int ticks);

    int fieldGet$warningDistance();

    void setWarningDistance(int blocks);

    double fieldGet$blockBuffer();

    void setBlockBuffer(int buffer);

    double fieldGet$damageAmount();

    void setDamageAmount(int damage);

}
