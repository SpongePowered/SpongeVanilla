package org.granitepowered.granite.bytecode.classes;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.CodeInsertionMode;
import org.granitepowered.granite.bytecode.CodePosition;
import org.granitepowered.granite.impl.entity.weather.GraniteEntityLightningBolt;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCEntityLightningBolt;
import org.granitepowered.granite.util.MinecraftUtils;

public class EntityLightningBoltClass extends BytecodeClass {

    public EntityLightningBoltClass() {
        super("EntityLightningBolt");


        insert("onUpdate", "if (((" + GraniteEntityLightningBolt.class.getName() + ") " + MinecraftUtils.class.getName() + ".wrap((" + MCEntityLightningBolt.class.getName() + ") this)).isEffect()) return;",
                CodeInsertionMode.BEFORE, new CodePosition.NewPosition("BlockPos"));
        /*instrumentMethod("onUpdate", new ExprEditor() {
            @Override
            public void edit(NewExpr newExpr) throws CannotCompileException {
                if (newExpr.getClassName().equals(Mappings.getClass("BlockPos").getName()) || newExpr.getClassName().equals(Mappings.getClass("AxisAlignedBB").getName())) {
                    newExpr.replace("{if (((org.granitepowered.granite.impl.entity.weather.GraniteEntityLightningBolt) this).isEffect()) { return; }; $_ = $proceed($$);}");
                }
            }
        });*/
    }
}
