package org.granitepowered.granite.bytecode.classes;

import org.granitepowered.granite.bytecode.BytecodeClass;

public class EntityLivingBaseClass extends BytecodeClass {

    public EntityLivingBaseClass() {
        super("EntityLivingBase");

        // TODO: Throwing null
        /*insert("onEntityUpdate",
               "this." + Mappings.getMethod(Mappings.getClass("EntityLivingBase"), "setAir").toString() + "(((" + GraniteLivingBase.class.getName()
               + ") " + MinecraftUtils.class.getName() + ".wrap((" + MCEntityLivingBase.class.getName() + ") this).getMaxAir();",
               CodeInsertionMode.REPLACE, new CodePosition.MethodCallPosition("EntityLivingBase", "setAir"));*/
    }
}
