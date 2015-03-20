package org.granitepowered.granite.bytecode.classes;

import org.granitepowered.granite.bytecode.*;
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
        if (info.getCaller().fieldGet$riddenByEntity() != null || !minecart.isSlowWhenEmpty()) {
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionX() * 1.0D);
            info.getCaller().fieldSet$motionY(info.getCaller().fieldGet$motionY() * 0.0D);
            info.getCaller().fieldSet$motionZ(info.getCaller().fieldGet$motionZ() * 1.0D);
        } else {
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionX() * 0.96D);
            info.getCaller().fieldSet$motionY(info.getCaller().fieldGet$motionY() * 0.0D);
            info.getCaller().fieldSet$motionZ(info.getCaller().fieldGet$motionZ() * 0.96D);
        }
    }

    @Insert(methodName = "motionUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.FIELD, value = "onGround", index = 1))
    public void implementCustomAirborneDeceleration(CallbackInfo<MCEntityMinecart> info) {
        GraniteEntityMinecart minecart = MinecraftUtils.wrap(info.getCaller());

        double drag = 0.94999998807907104D;
        if (info.getCaller().fieldGet$onGround()) {
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionX() / drag);
            info.getCaller().fieldSet$motionY(info.getCaller().fieldGet$motionY() / drag);
            info.getCaller().fieldSet$motionZ(info.getCaller().fieldGet$motionZ() / drag);
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionX() * minecart.getAirborneMod().getX());
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionY() * minecart.getAirborneMod().getY());
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionZ() * minecart.getAirborneMod().getZ());
        }
    }

    @Insert(methodName = "motionUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "Entity#moveEntity"))
    public void implementCustomDerailedDeceleration(CallbackInfo<MCEntityMinecart> info) {
        GraniteEntityMinecart minecart = MinecraftUtils.wrap(info.getCaller());

        if (info.getCaller().fieldGet$onGround()) {
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionX() / 0.5D);
            info.getCaller().fieldSet$motionY(info.getCaller().fieldGet$motionY() / 0.5D);
            info.getCaller().fieldSet$motionZ(info.getCaller().fieldGet$motionZ() / 0.5D);
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionX() * minecart.getDerailedMod().getX());
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionY() * minecart.getDerailedMod().getY());
            info.getCaller().fieldSet$motionX(info.getCaller().fieldGet$motionZ() * minecart.getDerailedMod().getZ());
        }
    }
}
