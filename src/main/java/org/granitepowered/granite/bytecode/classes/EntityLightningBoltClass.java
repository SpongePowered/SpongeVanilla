package org.granitepowered.granite.bytecode.classes;

import org.granitepowered.granite.bytecode.*;
import org.granitepowered.granite.impl.entity.weather.GraniteEntityLightningBolt;
import org.granitepowered.granite.mc.MCEntityLightningBolt;
import org.granitepowered.granite.util.MinecraftUtils;

public class EntityLightningBoltClass extends BytecodeClass {

    public EntityLightningBoltClass() {
        super("EntityLightningBolt");
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.NEW, value = "BlockPos"))
    public void onUpdateBP(CallbackInfo<MCEntityLightningBolt> info) {
        if (((GraniteEntityLightningBolt) MinecraftUtils.wrap(info.getCaller())).isEffect()) info.cancel();
    }

    @Insert(methodName = "onUpdate", mode = CodeInsertionMode.BEFORE, position = @Position(mode = Position.PositionMode.NEW, value = "AxisAlignedBB"))
    public void onUpdateAABB(CallbackInfo<MCEntityLightningBolt> info) {
        if (((GraniteEntityLightningBolt) MinecraftUtils.wrap(info.getCaller())).isEffect()) info.cancel();
    }
}
