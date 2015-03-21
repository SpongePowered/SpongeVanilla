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

import org.granitepowered.granite.bytecode.*;
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
        if (boat.obj.onGround && boat.canMoveOnLand()) {
            boat.obj.motionX = (boat.obj.motionX / 0.5);
            boat.obj.motionY = (boat.obj.motionY / 0.5);
            boat.obj.motionZ = (boat.obj.motionZ / 0.5);
        }
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "java.lang.Math#sqrt", index = 0))
    public void beforeMotionModify(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        boat.setInitialDisplacement(Math.sqrt(boat.obj.motionX * boat.obj.motionX + boat.obj.motionZ * boat.obj.motionX));
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "java.lang.Math#sqrt", index = 1))
    public void beforeLimitSpeed(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        boat.setTempMotionX(boat.obj.motionX);
        boat.setTempMotionZ(boat.obj.motionZ);
        boat.setTempSpeedMultiplier(boat.obj.speedMultiplier);
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.FIELD, value = "onGround", index = 1))
    public void afterLimitSpeed(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        boat.obj.motionX = boat.getTempMotionX();
        boat.obj.motionZ = boat.getTempMotionZ();
        boat.obj.speedMultiplier = boat.getTempSpeedMultiplier();
        double displacement = Math.sqrt(boat.obj.motionX * boat.obj.motionX + boat.obj.motionZ * boat.obj.motionZ);

        if (displacement > boat.obj.speedMultiplier) {
            double ratio = boat.getMaxSpeed() / displacement;
            boat.obj.motionX = (boat.obj.motionX * ratio);
            boat.obj.motionZ = (boat.obj.motionZ * ratio);
            displacement = boat.getMaxSpeed();
        }

        if ((displacement > boat.getInitialDisplacement()) && (boat.obj.speedMultiplier < boat.getMaxSpeed())) {
            boat.obj.speedMultiplier = (boat.obj.speedMultiplier + ((boat.getMaxSpeed() - boat.obj.speedMultiplier) / boat.getMaxSpeed() * 100));
            boat.obj.speedMultiplier = (Math.min(boat.obj.speedMultiplier, boat.getMaxSpeed()));
        } else {
            boat.obj.speedMultiplier = (boat.obj.speedMultiplier - ((boat.obj.speedMultiplier - 0.07) / boat.getMaxSpeed() * 100));
            boat.obj.speedMultiplier = (Math.max(boat.obj.speedMultiplier, 0.07));
        }
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.FIELD, value = "riddenByEntity", index = 0))
    public void customDeceleration(CallbackInfo<MCEntityBoat> info) {
        GraniteEntityBoat boat = MinecraftUtils.wrap(info.getCaller());
        if (!(boat.obj.riddenByEntity instanceof MCEntityLivingBase)) {
            double deceleration = boat.obj.riddenByEntity == null ? boat.getUnoccupiedDeceleration() : boat.getOccupiedDeceleration();
            boat.obj.motionX = (boat.obj.motionX * deceleration);
            boat.obj.motionZ = (boat.obj.motionZ * deceleration);

            if (boat.obj.motionX < 0.00005D) {
                boat.obj.motionX = 0.0D;
            }

            if (boat.obj.motionZ < 0.00005) {
                boat.obj.motionZ = 0.0D;
            }
        }
    }
}
