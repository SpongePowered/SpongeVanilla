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

import javassist.*;
import javassist.bytecode.*;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;
import org.apache.commons.lang3.ArrayUtils;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCInterface;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BytecodeClass {
    CtClass clazz;

    List<PostCallback> callbacks;

    static Map<CtClass, Class<?>> classMap;

    public BytecodeClass(CtClass clazz) {
        this.clazz = clazz;

        classMap = new HashMap<>();

        callbacks = new ArrayList<>();
    }

    public void proxy(String methodName, ProxyHandler handler) {
        try {
            final CtMethod method = Mappings.getCtMethod(clazz, methodName);

            final String oldName = method.getName();
            method.setName(oldName + "$cb");

            injectField(ClassPool.getDefault().get(Method.class.getName()), oldName + "$method", new PostCallback() {
                @Override
                public void callback() {
                    try {
                        Field f = getFromCt(clazz).getDeclaredField(oldName + "$method");

                        Class<?>[] paramTypes = new Class[method.getParameterTypes().length];
                        for (int i = 0; i < paramTypes.length; i++) {
                            paramTypes[i] = getFromCt(method.getParameterTypes()[i]);
                        }

                        @SuppressWarnings("unchecked")
                        Method m = getFromCt(method.getDeclaringClass()).getDeclaredMethod(method.getName(), paramTypes);

                        f.setAccessible(true);
                        f.set(null, m);
                    } catch (NoSuchFieldException | NoSuchMethodException | NotFoundException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });

            injectField(ClassPool.getDefault().get(ProxyHandler.class.getName()), oldName + "$handler", handler);

            CtMethod newMethod = new CtMethod(method.getReturnType(), oldName, method.getParameterTypes(), method.getDeclaringClass());
            newMethod.setBody("return " + oldName + "$handler.preHandle(this, $args, " + oldName + "$method);");

            clazz.addMethod(newMethod);
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void injectField(String name, Object value) {
        injectField(clazz, name, value);
    }

    public void injectField(CtClass type, String name, PostCallback callback) {
        try {
            CtField f = new CtField(type, name, clazz);
            f.setModifiers(0x0008); // static
            clazz.addField(f);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        callbacks.add(callback);
    }

    public void injectField(CtClass type, final String name, final Object value) {
        injectField(type, name, new PostCallback() {
            @Override
            public void callback() {
                try {
                    Field f = getFromCt(clazz).getDeclaredField(name);
                    f.setAccessible(true);
                    f.set(null, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void implement(Class<? extends MCInterface> mcInterface) {
        try {
            CtClass ctMcInterface = ClassPool.getDefault().get(mcInterface.getName());

            if (!Arrays.asList(clazz.getInterfaces()).contains(ctMcInterface)) {
                for (CtMethod interfaceMethod : ctMcInterface.getDeclaredMethods()) {
                    if (interfaceMethod.getName().startsWith("fieldGet$")) {
                        CtField mcField = Mappings.getCtField(clazz, interfaceMethod.getName().substring("fieldSet$".length()));
                        mcField.setModifiers(AccessFlag.setPublic(mcField.getModifiers()));

                        CtMethod newMethod = new CtMethod(interfaceMethod.getReturnType(), interfaceMethod.getName(), new CtClass[]{}, clazz);
                        newMethod.setModifiers(Modifier.PUBLIC);

                        MethodInfo mi = newMethod.getMethodInfo();

                        Bytecode bytecode = new Bytecode(mi.getConstPool(), 2, 1);
                        bytecode.addLoad(0, clazz);
                        bytecode.addGetfield(mcField.getDeclaringClass(), mcField.getName(), mcField.getFieldInfo().getDescriptor());

                        if (!interfaceMethod.getReturnType().isPrimitive()) {
                            bytecode.addLdc(bytecode.getConstPool().addClassInfo(interfaceMethod.getReturnType()));
                            bytecode.addInvokestatic("org.granitepowered.granite.utils.ReflectionUtils", "cast", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
                            bytecode.addCheckcast(interfaceMethod.getReturnType());
                        }

                        bytecode.addReturn(mcField.getType());

                        mi.setCodeAttribute(bytecode.toCodeAttribute());

                        clazz.addMethod(newMethod);
                    } else if (interfaceMethod.getName().startsWith("fieldSet$")) {
                        CtField mcField = Mappings.getCtField(clazz, interfaceMethod.getName().substring("fieldSet$".length()));

                        CtMethod newMethod = new CtMethod(interfaceMethod.getReturnType(), interfaceMethod.getName(), interfaceMethod.getParameterTypes(), clazz);
                        newMethod.setModifiers(Modifier.PUBLIC);

                        MethodInfo mi = newMethod.getMethodInfo();
                        Bytecode bytecode = new Bytecode(mi.getConstPool(), 2, 2);
                        bytecode.addAload(0);
                        bytecode.addLoad(1, mcField.getType());

                        if (!mcField.getType().isPrimitive()) {
                            bytecode.addLdc(bytecode.getConstPool().addClassInfo(interfaceMethod.getReturnType()));
                            bytecode.addInvokestatic("org.granitepowered.granite.utils.ReflectionUtils", "cast", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
                            bytecode.addCheckcast(mcField.getType());
                        }

                        bytecode.addPutfield(mcField.getDeclaringClass(), mcField.getName(), mcField.getFieldInfo().getDescriptor());
                        bytecode.addReturn(CtClass.voidType);

                        mi.setCodeAttribute(bytecode.toCodeAttribute());

                        clazz.addMethod(newMethod);
                    } else {
                        CtMethod mcMethod = Mappings.getCtMethod(clazz, interfaceMethod.getName());

                        if (mcMethod == null) {
                            throw new RuntimeException(interfaceMethod.getName() + " not found in mappings");
                        }

                        CtMethod newMethod = new CtMethod(interfaceMethod.getReturnType(), interfaceMethod.getName(), interfaceMethod.getParameterTypes(), clazz);

                        String methodSig;
                        if (newMethod.getParameterTypes().length > 0) {
                            methodSig = "(";

                            for (int i = 0; i < newMethod.getParameterTypes().length; i++) {
                                CtClass actualType = mcMethod.getParameterTypes()[i];

                                methodSig += "(" + actualType.getName() + ") $" + (i + 1) + ",";
                            }

                            methodSig = methodSig.substring(0, methodSig.length() - 1) + ")";
                        } else {
                            methodSig = "()";
                        }

                        newMethod.setBody("return ($r) " + mcMethod.getName() + methodSig + ";");

                        clazz.addMethod(newMethod);
                    }
                }

                clazz.addInterface(ctMcInterface);
            }
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void replaceMethod(String methodName, String code) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.setBody(code);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void beforeMethod(String methodName, String code) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.insertBefore(code);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void afterMethod(String methodName, String code) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.insertAfter(code);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void instrumentMethod(String methodName, ExprEditor editor) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.instrument(editor);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void instrument(ExprEditor editor) {
        try {
            clazz.instrument(editor);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void convertMethod(String methodName, CodeConverter converter) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.instrument(converter);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void convert(CodeConverter converter) {
        try {
            clazz.instrument(converter);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BytecodeClass that = (BytecodeClass) o;

        return clazz.getName().equals(that.clazz.getName());

    }

    @Override
    public int hashCode() {
        return clazz.getName().hashCode();
    }

    public void post() {
        for (PostCallback callback : callbacks) {
            callback.callback();
        }
    }

    private static Class<?> getFromCt(CtClass clazz) {
        if (!classMap.containsKey(clazz)) {
            try {
                classMap.put(clazz, clazz.toClass());
            } catch (CannotCompileException | LinkageError e) {
                e.printStackTrace();
                return null;
            }
        }
        return classMap.get(clazz);
    }

    public void loadClass() {
        getFromCt(clazz);
    }

    public static interface ProxyHandlerCallback {
        Object invokeParent(Object... args) throws Throwable;
    }

    public static abstract class ProxyHandler {
        public Object preHandle(final Object caller, Object[] args, final Method method) throws Throwable {
            ProxyHandlerCallback callback = new ProxyHandlerCallback() {
                @Override
                public Object invokeParent(Object... args) throws Throwable {
                    MethodHandle handle = MethodHandles.lookup().unreflect(method);
                    return handle.invokeWithArguments(ArrayUtils.add(args, 0, caller));
                }
            };

            return handle(caller, args, callback);
        }

        protected abstract Object handle(Object caller, Object[] args, ProxyHandlerCallback callback) throws Throwable;
    }

    private static interface PostCallback {
        void callback();
    }
}
