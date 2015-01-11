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

package org.granitepowered.granite.impl.entity.hanging;

import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCEntityHanging;
import org.granitepowered.granite.mc.MCEnumFacing;
import org.spongepowered.api.entity.hanging.Hanging;
import org.spongepowered.api.util.Direction;

public class GraniteHanging<T extends MCEntityHanging> extends GraniteEntity<T> implements Hanging {

    public GraniteHanging(T obj) {
        super(obj);
    }

    @Override
    public Direction getHangingDirection() {
        MCEnumFacing enumFacing = obj.fieldGet$facing();
        Vector3d
                vector3d =
                new Vector3d(enumFacing.fieldGet$directionVec().fieldGet$x(), enumFacing.fieldGet$directionVec().fieldGet$y(),
                             enumFacing.fieldGet$directionVec().fieldGet$z());
        return Direction.getClosest(vector3d);
    }

    // TODO: this boolean is to force? but it wont do anything in Minecraft
    @Override
    public void setHangingDirection(Direction direction, boolean b) {
        Vector3d directionVector = direction.toVector3d();
        MCEnumFacing enumFacing = obj.fieldGet$facing();
        enumFacing.fieldGet$directionVec().fieldSet$x((int) directionVector.getX());
        enumFacing.fieldGet$directionVec().fieldSet$y((int) directionVector.getY());
        enumFacing.fieldGet$directionVec().fieldSet$z((int) directionVector.getZ());
        obj.setFacing(enumFacing);
    }
}
