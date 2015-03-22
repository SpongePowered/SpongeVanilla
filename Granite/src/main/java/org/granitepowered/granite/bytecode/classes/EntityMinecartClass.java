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
import org.granitepowered.granite.bytecode.Proxy;
import org.granitepowered.granite.bytecode.ProxyCallbackInfo;
import org.granitepowered.granite.impl.entity.vehicle.minecart.GraniteEntityMinecart;
import org.granitepowered.granite.mc.MCEntityMinecart;
import org.granitepowered.granite.util.MinecraftUtils;

public class EntityMinecartClass extends BytecodeClass {

    public EntityMinecartClass() {
        super("EntityMinecart");
    }

    @Proxy(methodName = "getMaxSpeed")
    public double maxSpeed(ProxyCallbackInfo<MCEntityMinecart> info) {
        return ((GraniteEntityMinecart) MinecraftUtils.wrap(info.getCaller())).getMaxSpeed();
    }

    @Proxy(methodName = "applyDrag")
    public void applyDrag(ProxyCallbackInfo<MCEntityMinecart> info) {
        GraniteEntityMinecart minecart = MinecraftUtils.wrap(info.getCaller());
        if (info.getCaller().riddenByEntity != null || !minecart.isSlowWhenEmpty()) {
            info.getCaller().motionX = (info.getCaller().motionX * 1.0D);
            info.getCaller().motionY = (info.getCaller().motionY * 0.0D);
            info.getCaller().motionZ = (info.getCaller().motionZ * 1.0D);
        } else {
            info.getCaller().motionX = (info.getCaller().motionX * 0.96D);
            info.getCaller().motionY = (info.getCaller().motionY * 0.0D);
            info.getCaller().motionZ = (info.getCaller().motionZ * 0.96D);
        }
    }

    @Insert(methodName = "motionUpdate", mode = CodeInsertionMode.BEFORE,
            position = @Position(mode = Position.PositionMode.FIELD, value = "onGround", index = 1))
    public void implementCustomAirborneDeceleration(CallbackInfo<MCEntityMinecart> info) {
        GraniteEntityMinecart minecart = MinecraftUtils.wrap(info.getCaller());

        double drag = 0.94999998807907104D;
        if (info.getCaller().onGround) {
            info.getCaller().motionX = (info.getCaller().motionX / drag);
            info.getCaller().motionY = (info.getCaller().motionY / drag);
            info.getCaller().motionZ = (info.getCaller().motionZ / drag);
            info.getCaller().motionX = (info.getCaller().motionX * minecart.getAirborneMod().getX());
            info.getCaller().motionX = (info.getCaller().motionY * minecart.getAirborneMod().getY());
            info.getCaller().motionX = (info.getCaller().motionZ * minecart.getAirborneMod().getZ());
        }
    }

    @Insert(methodName = "motionUpdate", mode = CodeInsertionMode.BEFORE,
            position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "Entity#moveEntity"))
    public void implementCustomDerailedDeceleration(CallbackInfo<MCEntityMinecart> info) {
        GraniteEntityMinecart minecart = MinecraftUtils.wrap(info.getCaller());

        if (info.getCaller().onGround) {
            info.getCaller().motionX = (info.getCaller().motionX / 0.5D);
            info.getCaller().motionY = (info.getCaller().motionY / 0.5D);
            info.getCaller().motionZ = (info.getCaller().motionZ / 0.5D);
            info.getCaller().motionX = (info.getCaller().motionX * minecart.getDerailedMod().getX());
            info.getCaller().motionX = (info.getCaller().motionY * minecart.getDerailedMod().getY());
            info.getCaller().motionX = (info.getCaller().motionZ * minecart.getDerailedMod().getZ());
        }
    }
}
