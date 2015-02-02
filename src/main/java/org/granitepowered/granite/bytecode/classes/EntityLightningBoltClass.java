package org.granitepowered.granite.bytecode.classes;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.mappings.Mappings;

public class EntityLightningBoltClass extends BytecodeClass {

    public EntityLightningBoltClass() {
        super("EntityLightningBolt");

        instrumentMethod("onUpdate", new ExprEditor() {
            @Override
            public void edit(NewExpr newExpr) throws CannotCompileException {
                if (newExpr.getClassName().equals(Mappings.getClass("BlockPos").getName()) || newExpr.getClassName().equals(Mappings.getClass("AxisAlignedBB").getName())) {
                    newExpr.replace("{if (((org.granitepowered.granite.impl.entity.weather.GraniteEntityLightningBolt) this).isEffect()) { return; }; $_ = $proceed($$);}");
                }
            }
        });
    }
}
