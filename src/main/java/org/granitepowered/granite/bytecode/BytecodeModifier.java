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

package org.granitepowered.granite.bytecode;

import com.google.common.base.Throwables;
import com.google.common.reflect.ClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.Main;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.Implement;
import org.granitepowered.granite.util.Instantiator;
import org.granitepowered.granite.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BytecodeModifier {

    List<BytecodeClass> bcs;
    CtClass ctInstantiator;

    public BytecodeModifier() {
        bcs = new ArrayList<>();
    }

    public void add(BytecodeClass clazz) {
        bcs.add(clazz);
    }

    public void modify() {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getTopLevelClassesRecursive("org.granitepowered.granite.mc")) {
                CtClass ctClass = ClassPool.getDefault().get(classInfo.getName());

                if (ctClass.hasAnnotation(Implement.class)) {
                    CtClass mcClass = Mappings.getCtClass(((Implement) ctClass.getAnnotation(Implement.class)).name());

                    if (mcClass == null) {
                        throw new RuntimeException(((Implement) ctClass.getAnnotation(Implement.class)).name() + " not in mappings");
                    }

                    if (Main.debugLog) {
                        Granite.getInstance().getLogger().info("Injecting " + ctClass.getSimpleName() + " into " + mcClass.getName());
                    }

                    BytecodeClass bc = new BytecodeClass(((Implement) ctClass.getAnnotation(Implement.class)).name());
                    bcs.add(bc);

                    bc.implement(BytecodeClass.getFromCt(ctClass));
                }
            }

            // Instantiator doesn't sound like a word any more...
            ctInstantiator = ClassPool.getDefault().makeClass("InstantiatorImpl");
            BytecodeClass instantiator = new BytecodeClass(ctInstantiator);
            instantiator.instantiator(Instantiator.InstantiatorInterface.class);
            ctInstantiator.addInterface(ClassPool.getDefault().get(Instantiator.InstantiatorInterface.class.getName()));
            // Deliberately not adding it here, as there's no need to write it, it can be loaded from memory after post

            for (BytecodeClass bc : bcs) {
                bc.writeClass();
            }
        } catch (IOException | NotFoundException | ClassNotFoundException e) {
            Throwables.propagate(e);
        }
    }

    public void post() {
        try {
            Field instantiatorField = Instantiator.class.getDeclaredField("instance");

            ReflectionUtils.forceAccessible(instantiatorField);
            instantiatorField.set(null, ctInstantiator.toClass().newInstance());
        } catch (NoSuchFieldException | InstantiationException | IllegalAccessException | CannotCompileException e) {
            e.printStackTrace();
        }

        for (BytecodeClass bc : bcs) {
            bc.post();
        }
    }
}
