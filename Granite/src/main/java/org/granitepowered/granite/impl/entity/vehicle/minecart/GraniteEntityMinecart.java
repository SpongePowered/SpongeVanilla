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

package org.granitepowered.granite.impl.entity.vehicle.minecart;

import com.flowpowered.math.GenericMath;
import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import mc.MCBlockRailBase;
import mc.MCBlockState;
import mc.MCEntityMinecart;
import org.granitepowered.granite.util.Instantiator;
import org.spongepowered.api.entity.vehicle.minecart.Minecart;

public class GraniteEntityMinecart<T extends MCEntityMinecart> extends GraniteEntity<T> implements Minecart {

    private double maxSpeed = 0.4D;
    private boolean slowWhenEmpty = true;
    private Vector3d airborneMod = new Vector3d(0.5D, 0.5D, 0.5D);
    private Vector3d derailedMod = new Vector3d(0.5D, 0.5D, 0.5D);

    public GraniteEntityMinecart(T obj) {
        super(obj);
    }

    @Override
    public boolean isOnRail() {
        return obj.onGround;
    }

    @Override
    public double getSwiftness() {
        return this.maxSpeed;
    }

    @Override
    public void setSwiftness(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public double getPotentialMaxSpeed() {
        int x = GenericMath.floor(obj.posX);
        int y = GenericMath.floor(obj.posY);
        int z = GenericMath.floor(obj.posZ);

        MCBlockRailBase railBase = Instantiator.get().newBlockRailBase(false);

        if (railBase.isOnRailWorld(obj.worldObj, Instantiator.get().newBlockPos(x, y - 1, z))) {
            --y;
        }
        MCBlockState state = obj.worldObj.getBlockState(Instantiator.get().newBlockPos(x, y, z));
        if (!railBase.isOnRail(state)) {
            return obj.getMaxSpeed();
        }

        double railMaxSpeed = 0.4D;
        return Math.min(railMaxSpeed, 1.2D);
    }

    @Override
    public boolean doesSlowWhenEmpty() {
        return this.slowWhenEmpty;
    }

    @Override
    public Vector3d getAirborneVelocityMod() {
        return this.airborneMod;
    }

    @Override
    public void setAirborneVelocityMod(Vector3d airborneMod) {
        this.airborneMod = airborneMod;
    }

    @Override
    public Vector3d getDerailedVelocityMod() {
        return this.derailedMod;
    }

    @Override
    public void setDerailedVelocityMod(Vector3d derailedMod) {
        this.derailedMod = derailedMod;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isSlowWhenEmpty() {
        return slowWhenEmpty;
    }

    @Override
    public void setSlowWhenEmpty(boolean slowWhenEmpty) {
        this.slowWhenEmpty = slowWhenEmpty;
    }

    public Vector3d getAirborneMod() {
        return airborneMod;
    }

    public void setAirborneMod(Vector3d airborneMod) {
        this.airborneMod = airborneMod;
    }

    public Vector3d getDerailedMod() {
        return derailedMod;
    }

    public void setDerailedMod(Vector3d derailedMod) {
        this.derailedMod = derailedMod;
    }
}
