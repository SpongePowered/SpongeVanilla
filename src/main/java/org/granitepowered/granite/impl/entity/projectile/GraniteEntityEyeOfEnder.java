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

package org.granitepowered.granite.impl.entity.projectile;

import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.mc.MCEntityEnderEye;
import org.spongepowered.api.entity.projectile.EyeOfEnder;

public class GraniteEntityEyeOfEnder extends GraniteEntityThrowable<MCEntityEnderEye> implements EyeOfEnder {

    public GraniteEntityEyeOfEnder(MCEntityEnderEye obj) {
        super(obj);
    }

    @Override
    public Vector3d getTargetedLocation() {
        return new Vector3d(obj.fieldGet$targetX(), obj.fieldGet$targetY(), obj.fieldGet$targetZ());
    }

    @Override
    public void setTargetedLocation(Vector3d vector3d) {
        obj.fieldSet$targetX(vector3d.getX());
        obj.fieldSet$targetY(vector3d.getY());
        obj.fieldSet$targetZ(vector3d.getZ());
    }

    @Override
    public boolean doesShatterOnDrop() {
        return obj.fieldGet$shatterOrDrop();
    }

    @Override
    public void setShatterOnDrop(boolean shatter) {
        obj.fieldSet$shatterOrDrop(shatter);
    }
}
