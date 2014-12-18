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

package org.granitepowered.granite.mc;

import java.util.UUID;

@Implement(name = "Entity")
public interface MCEntity extends MCInterface {
    boolean fieldGet$onGround();

    double fieldGet$posX();

    double fieldGet$posY();

    double fieldGet$posZ();

    MCEntity fieldGet$riddenByEntity();

    MCEntity fieldGet$ridingEntity();

    void fieldSet$riddenByEntity(MCEntity entity);

    void fieldSet$ridingEntity(MCEntity entity);


    float fieldGet$width();

    float fieldGet$height();

    boolean fieldGet$isDead();

    int fieldGet$fire();

    void fieldSet$fire(int ticks);

    UUID fieldGet$entityUniqueID();

    int fieldGet$fireResistance();

    MCWorld fieldGet$worldObj();

    int fieldGet$hurtResistantTime();

    void fieldSet$hurtResistantTime(int ticks);

    void mountEntity(MCEntity other);

    float getEyeHeight();

    int getAir();

    void setAir(int air);

    String getCustomNameTag();

    void setCustomNameTag(String tag);

    boolean getAlwaysRenderNameTag();

    void setAlwaysRenderNameTag(boolean value);

    String getName();

    void setPosition(double x, double y, double z);
}
