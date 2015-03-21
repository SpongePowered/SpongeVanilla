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

package org.granitepowered.granite.impl.entity.vehicle;

import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCEntityBoat;
import org.spongepowered.api.entity.vehicle.Boat;

public class GraniteEntityBoat extends GraniteEntity<MCEntityBoat> implements Boat {

    private double speedMultiplier = obj.speedMultiplier;
    private boolean moveOnLand = false;
    private double maxSpeed = 0.35D;
    private double occupiedDeceleration = 0D;
    private double unoccupiedDeceleration = 0.8D;
    private double initialDisplacement;
    private double tempMotionX;
    private double tempMotionZ;
    private double tempSpeedMultiplier;

    public GraniteEntityBoat(MCEntityBoat obj) {
        super(obj);
    }

    @Override
    public boolean isInWater() {
        return obj.isInWater;
    }

    @Override
    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    @Override
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public boolean canMoveOnLand() {
        return this.moveOnLand;
    }

    @Override
    public double getOccupiedDeceleration() {
        return this.occupiedDeceleration;
    }

    @Override
    public void setOccupiedDeceleration(double occupiedDeceleration) {
        this.occupiedDeceleration = occupiedDeceleration;
    }

    @Override
    public double getUnoccupiedDeceleration() {
        return this.unoccupiedDeceleration;
    }

    @Override
    public void setUnoccupiedDeceleration(double unoccupiedDeceleration) {
        this.unoccupiedDeceleration = unoccupiedDeceleration;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public boolean isMoveOnLand() {
        return moveOnLand;
    }

    @Override
    public void setMoveOnLand(boolean moveOnLand) {
        this.moveOnLand = moveOnLand;
    }

    public double getInitialDisplacement() {
        return initialDisplacement;
    }

    public void setInitialDisplacement(double initialDisplacement) {
        this.initialDisplacement = initialDisplacement;
    }

    public double getTempMotionX() {
        return tempMotionX;
    }

    public void setTempMotionX(double tempMotionX) {
        this.tempMotionX = tempMotionX;
    }

    public double getTempMotionZ() {
        return tempMotionZ;
    }

    public void setTempMotionZ(double tempMotionZ) {
        this.tempMotionZ = tempMotionZ;
    }

    public double getTempSpeedMultiplier() {
        return tempSpeedMultiplier;
    }

    public void setTempSpeedMultiplier(double tempSpeedMultiplier) {
        this.tempSpeedMultiplier = tempSpeedMultiplier;
    }
}
