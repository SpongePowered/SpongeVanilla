/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

package org.granitepowered.granite.bytecode;

import com.google.common.reflect.ClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.Implement;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class BytecodeModifier {
    Set<BytecodeClass> runPostOn;

    public void modify() {
        runPostOn = new LinkedHashSet<>();

        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClassesRecursive("org.granitepowered.granite.mc")) {
                CtClass ctClass = ClassPool.getDefault().get(classInfo.getName());

                if (ctClass.hasAnnotation(Implement.class)) {
                    CtClass mcClass = Mappings.getCtClass(((Implement) ctClass.getAnnotation(Implement.class)).name());

                    if (mcClass == null) {
                        throw new RuntimeException(((Implement) ctClass.getAnnotation(Implement.class)).name() + " not in mappings");
                    }

                    Granite.getInstance().getLogger().info("Injecting " + ctClass.getSimpleName() + " into " + mcClass.getName());

                    BytecodeClass bc = new BytecodeClass(mcClass);
                    bc.implement(ctClass.toClass());

                    runPostOn.add(bc);
                }
            }

            BytecodeClass minecraftServer = new BytecodeClass(Mappings.getCtClass("MinecraftServer"));
            minecraftServer.replaceMethod("getServerModName", "return \"Granite\";");

            runPostOn.add(minecraftServer);

            for (BytecodeClass bc : runPostOn) {
                bc.post();
            }
        } catch (IOException | NotFoundException | ClassNotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
