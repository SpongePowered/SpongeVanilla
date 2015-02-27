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

package org.granitepowered.granite.bytecode.classes;

import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.CallbackInfo;
import org.granitepowered.granite.bytecode.CodeInsertionMode;
import org.granitepowered.granite.bytecode.Insert;
import org.granitepowered.granite.bytecode.Position;
import org.granitepowered.granite.impl.entity.vehicle.GraniteEntityBoat;
import org.granitepowered.granite.mc.MCEntityBoat;
import org.granitepowered.granite.mc.MCEntityLivingBase;
import org.granitepowered.granite.util.MinecraftUtils;

public class EntityBoatClass extends BytecodeClass {

    public EntityBoatClass() {
        super("EntityBoat");
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "Entity#moveEntity"))
    public void moveOnLand(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        if (boat.obj.fieldGet$onGround() && boat.canMoveOnLand()) {
            double motionX = boat.obj.fieldGet$motionX();
            double motionY = boat.obj.fieldGet$motionY();
            double motionZ = boat.obj.fieldGet$motionZ();
            boat.obj.fieldSet$motionX(motionX /= 0.5);
            boat.obj.fieldSet$motionY(motionY /= 0.5);
            boat.obj.fieldSet$motionZ(motionZ /= 0.5);
        }
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "java.lang.Math#sqrt", index = 0))
    public void beforeMotionModify(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        boat.setInitialDisplacement(Math.sqrt(boat.obj.fieldGet$motionX() * boat.obj.fieldGet$motionX() + boat.obj.fieldGet$motionZ() * boat.obj.fieldGet$motionX()));
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "java.lang.Math#sqrt", index = 1))
    public void beforeLimitSpeed(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        boat.setTempMotionX(boat.obj.fieldGet$motionX());
        boat.setTempMotionZ(boat.obj.fieldGet$motionZ());
        boat.setTempSpeedMultiplier(boat.obj.fieldGet$speedMultiplier());
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.FIELD, value = "onGround", index = 1))
    public void afterLimitSpeed(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        boat.obj.fieldSet$motionX(boat.getTempMotionX());
        boat.obj.fieldSet$motionZ(boat.getTempMotionZ());
        boat.obj.fieldSet$speedMultiplier(boat.getTempSpeedMultiplier());
        double displacement = Math.sqrt(boat.obj.fieldGet$motionX() * boat.obj.fieldGet$motionX() + boat.obj.fieldGet$motionZ() * boat.obj.fieldGet$motionZ());

        if (displacement > boat.obj.fieldGet$speedMultiplier()) {
            double ratio = boat.getMaxSpeed() / displacement;
            boat.obj.fieldSet$motionX(boat.obj.fieldGet$motionX() * ratio);
            boat.obj.fieldSet$motionZ(boat.obj.fieldGet$motionZ() * ratio);
            displacement = boat.getMaxSpeed();
        }

        if ((displacement> boat.getInitialDisplacement()) && (boat.obj.fieldGet$speedMultiplier() < boat.getMaxSpeed())) {
            boat.obj.fieldSet$speedMultiplier(boat.obj.fieldGet$speedMultiplier() + ((boat.getMaxSpeed() - boat.obj.fieldGet$speedMultiplier()) / boat.getMaxSpeed() * 100));
            boat.obj.fieldSet$speedMultiplier(Math.min(boat.obj.fieldGet$speedMultiplier(), boat.getMaxSpeed()));
        } else {
            boat.obj.fieldSet$speedMultiplier(boat.obj.fieldGet$speedMultiplier() - ((boat.obj.fieldGet$speedMultiplier() - 0.07) / boat.getMaxSpeed() * 100));
            boat.obj.fieldSet$speedMultiplier(Math.max(boat.obj.fieldGet$speedMultiplier(), 0.07));
        }
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.FIELD, value = "riddenByEntity", index = 0))
    public void customDeceleration(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        if (!(boat.obj.fieldGet$riddenByEntity() instanceof MCEntityLivingBase)) {
            double deceleration = boat.obj.fieldGet$riddenByEntity() == null ? boat.getUnoccupiedDeceleration() : boat.getOccupiedDeceleration();
            boat.obj.fieldSet$motionX(boat.obj.fieldGet$motionX() * deceleration);
            boat.obj.fieldSet$motionZ(boat.obj.fieldGet$motionZ() * deceleration);

            if (boat.obj.fieldGet$motionX() < 0.00005) {
                boat.obj.fieldSet$motionX(0.0);
            }

            if (boat.obj.fieldGet$motionZ() < 0.00005) {
                boat.obj.fieldSet$motionZ(0.0);
            }
        }
    }
}
