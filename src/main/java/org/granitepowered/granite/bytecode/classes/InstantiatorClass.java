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

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.util.Instantiator;
import org.granitepowered.granite.util.ReflectionUtils;

import java.lang.reflect.Field;

public class InstantiatorClass extends BytecodeClass {
    public InstantiatorClass() {
        super(ClassPool.getDefault().makeClass("InstantiatorImpl"));

        instantiator(Instantiator.InstantiatorInterface.class);
        try {
            clazz.addInterface(ClassPool.getDefault().get(Instantiator.InstantiatorInterface.class.getName()));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        callbacks.add(new PostCallback() {
            @Override
            public void callback() {
                try {
                    Field instantiatorField = Instantiator.class.getDeclaredField("instance");

                    ReflectionUtils.forceAccessible(instantiatorField);
                    instantiatorField.set(null, clazz.toClass().newInstance());
                } catch (NoSuchFieldException | InstantiationException | IllegalAccessException | CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
