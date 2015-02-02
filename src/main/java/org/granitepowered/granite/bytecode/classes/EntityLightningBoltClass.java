package org.granitepowered.granite.bytecode.classes;

import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.CodeInsertionMode;
import org.granitepowered.granite.bytecode.CodePosition;
import org.granitepowered.granite.impl.entity.weather.GraniteEntityLightningBolt;
import org.granitepowered.granite.mc.MCEntityLightningBolt;
import org.granitepowered.granite.util.MinecraftUtils;

public class EntityLightningBoltClass extends BytecodeClass {

    public EntityLightningBoltClass() {
        super("EntityLightningBolt");

        insert("onUpdate",
               "if (((" + GraniteEntityLightningBolt.class.getName() + ") " + MinecraftUtils.class.getName() + ".wrap((" + MCEntityLightningBolt.class
                       .getName() + ") this)).isEffect()) return;",
               CodeInsertionMode.BEFORE, new CodePosition.NewPosition("BlockPos"));

        insert("onUpdate",
               "if (((" + GraniteEntityLightningBolt.class.getName() + ") " + MinecraftUtils.class.getName() + ".wrap((" + MCEntityLightningBolt.class
                       .getName() + ") this)).isEffect()) return;",
               CodeInsertionMode.BEFORE, new CodePosition.NewPosition("AxisAlignedBB"));
    }
}
