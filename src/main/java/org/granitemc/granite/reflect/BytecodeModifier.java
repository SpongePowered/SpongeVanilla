package org.granitemc.granite.reflect;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

import java.util.Objects;

/**
 * **************************************************************************************
 * License (MIT)
 * <p/>
 * Copyright (c) 2014. Granite Team
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * **************************************************************************************
 */

public class BytecodeModifier {
    // This stuff has to be updated by hand, and doesn't involve mappings

    public static void modify() {
        try {
            ClassPool pool = new ClassPool(true);

            modifyServer(pool);
            modifyScm(pool);
        } catch (CannotCompileException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void modifyServer(ClassPool pool) throws NotFoundException, CannotCompileException {
        CtClass cc = pool.get("po");
        CtMethod m = cc.getDeclaredMethod("i");

        m.instrument(new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException {
                if (Objects.equals(e.getClassName(), "pn")) {
                    // replace pn constructor with null - we'll set it to something in a proxy later
                    e.replace("$_ = null;");
                }
                super.edit(e);
            }
        });

        // Save everything
        cc.toClass();
    }

    public static void modifyScm(ClassPool pool) {
        // TODO
    }
}
