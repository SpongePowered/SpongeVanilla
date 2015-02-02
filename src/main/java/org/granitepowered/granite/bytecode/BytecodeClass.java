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

import com.google.common.base.Defaults;
import com.google.common.base.Throwables;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.Bytecode;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.bytecode.analysis.Type;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.Implement;
import org.granitepowered.granite.mc.MCInterface;
import org.granitepowered.granite.util.ReflectionUtils;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BytecodeClass {

    static Map<CtClass, Class<?>> classMap;
    protected CtClass clazz;
    protected List<PostCallback> callbacks;
    Set<String> classesToLoad;

    ClassPool pool;

    public BytecodeClass(String str) {
        this(Mappings.getCtClass(str));
    }

    public BytecodeClass(CtClass clazz) {
        this.clazz = clazz;

        classMap = new HashMap<>();

        callbacks = new ArrayList<>();

        classesToLoad = new HashSet<>();

        pool = Granite.getInstance().getClassPool();
    }

    public static Class getFromCt(CtClass clazz) {
        if (clazz.isPrimitive()) {
            switch (clazz.getName()) {
                case "float":
                    return float.class;
                case "double":
                    return double.class;
                case "byte":
                    return byte.class;
                case "int":
                    return int.class;
                case "boolean":
                    return boolean.class;
                case "char":
                    return char.class;
                case "short":
                    return short.class;
                case "long":
                    return long.class;
                case "void":
                    return void.class;
            }
        }

        try {
            return Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
            Throwables.propagate(e);
        }
        return null;
    }

    public void proxy(final String methodName, ProxyHandler handler) {
        try {
            final CtMethod method = Mappings.getCtMethod(clazz, methodName);

            final String oldName = method.getName();
            method.setName(oldName + "$cb");

            injectField(method.getDeclaringClass(), pool.get(Method.class.getName()), methodName + "$method", new PostCallback() {
                @Override
                public void callback() {
                    try {
                        Field f = getFromCt(method.getDeclaringClass()).getDeclaredField(methodName + "$method");

                        Class<?>[] paramTypes = new Class[method.getParameterTypes().length];
                        for (int i = 0; i < paramTypes.length; i++) {
                            paramTypes[i] = getFromCt(method.getParameterTypes()[i]);
                        }

                        @SuppressWarnings("unchecked")
                        Method m = getFromCt(method.getDeclaringClass()).getDeclaredMethod(method.getName(), paramTypes);

                        f.setAccessible(true);
                        f.set(null, m);
                    } catch (NoSuchFieldException | NoSuchMethodException | NotFoundException | IllegalAccessException e) {
                        Throwables.propagate(e);
                    }
                }
            });

            injectField(method.getDeclaringClass(), pool.get(ProxyHandler.class.getName()), methodName + "$handler", handler);

            CtMethod newMethod = new CtMethod(method.getReturnType(), oldName, method.getParameterTypes(), method.getDeclaringClass());
            newMethod.setBody("return ($r) " + methodName + "$handler.preHandle(this, $args, " + methodName + "$method);");

            method.getDeclaringClass().addMethod(newMethod);

            classesToLoad.add(method.getDeclaringClass().getName());
        } catch (NotFoundException | CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void injectField(CtClass type, String name, Object value) {
        injectField(this.clazz, type, name, value);
    }

    public void injectField(CtClass type, String name, PostCallback callback) {
        injectField(this.clazz, type, name, callback);
    }

    public void injectField(CtClass clazz, CtClass type, String name, PostCallback callback) {
        try {
            CtField f = new CtField(type, name, clazz);
            f.setModifiers(0x0008); // static
            clazz.addField(f);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }

        callbacks.add(callback);
    }

    public void injectField(final CtClass clazz, CtClass type, final String name, final Object value) {
        injectField(clazz, type, name, new PostCallback() {
            @Override
            public void callback() {
                try {
                    Field f = getFromCt(clazz).getDeclaredField(name);
                    f.setAccessible(true);
                    f.set(null, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Throwables.propagate(e);
                }
            }
        });
    }

    public void implement(Class<? extends MCInterface> mcInterface) {
        try {
            CtClass ctMcInterface = pool.get(mcInterface.getName());

            if (!Arrays.asList(clazz.getInterfaces()).contains(ctMcInterface)) {
                for (CtMethod interfaceMethod : ctMcInterface.getDeclaredMethods()) {
                    if (interfaceMethod.getName().startsWith("fieldGet$")) {
                        CtField mcField = Mappings.getCtField(clazz, interfaceMethod.getName().substring("fieldGet$".length()));
                        mcField.setModifiers(AccessFlag.setPublic(mcField.getModifiers()));

                        classesToLoad.add(mcField.getDeclaringClass().getName());

                        CtMethod newMethod = new CtMethod(interfaceMethod.getReturnType(), interfaceMethod.getName(), new CtClass[]{}, clazz);
                        newMethod.setModifiers(Modifier.PUBLIC);

                        MethodInfo mi = newMethod.getMethodInfo();

                        Bytecode bytecode = new Bytecode(mi.getConstPool(), 2, 1);
                        bytecode.addLoad(0, clazz);
                        bytecode.addGetfield(mcField.getDeclaringClass(), mcField.getName(), mcField.getFieldInfo().getDescriptor());

                        if (!interfaceMethod.getReturnType().isPrimitive()) {
                            bytecode.addLdc(bytecode.getConstPool().addClassInfo(interfaceMethod.getReturnType()));
                            bytecode.addInvokestatic(ReflectionUtils.class.getName(), "cast",
                                                     "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
                            bytecode.addCheckcast(interfaceMethod.getReturnType());
                        }

                        bytecode.addReturn(mcField.getType());

                        mi.setCodeAttribute(bytecode.toCodeAttribute());

                        clazz.addMethod(newMethod);
                    } else if (interfaceMethod.getName().startsWith("fieldSet$")) {
                        CtField mcField = Mappings.getCtField(clazz, interfaceMethod.getName().substring("fieldSet$".length()));
                        mcField.setModifiers(Modifier.setPublic(mcField.getModifiers()));

                        classesToLoad.add(mcField.getDeclaringClass().getName());

                        CtMethod
                                newMethod =
                                new CtMethod(interfaceMethod.getReturnType(), interfaceMethod.getName(), interfaceMethod.getParameterTypes(), clazz);
                        newMethod.setModifiers(Modifier.PUBLIC);

                        MethodInfo mi = newMethod.getMethodInfo();
                        Bytecode bytecode = new Bytecode(mi.getConstPool(), 2, 2);
                        bytecode.addAload(0);
                        bytecode.addLoad(1, mcField.getType());

                        if (!mcField.getType().isPrimitive()) {
                            bytecode.addLdc(bytecode.getConstPool().addClassInfo(interfaceMethod.getParameterTypes()[0]));
                            bytecode.addInvokestatic(ReflectionUtils.class.getName(), "cast",
                                                     "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
                            bytecode.addCheckcast(mcField.getType());
                        }

                        bytecode.incMaxLocals(1);
                        bytecode.addPutfield(mcField.getDeclaringClass(), mcField.getName(), mcField.getFieldInfo().getDescriptor());
                        bytecode.addReturn(CtClass.voidType);

                        mi.setCodeAttribute(bytecode.toCodeAttribute());

                        clazz.addMethod(newMethod);
                    } else {
                        CtMethod mcMethod = Mappings.getCtMethod(clazz, interfaceMethod.getName());

                        if (mcMethod == null) {
                            throw new RuntimeException(interfaceMethod.getName() + " not found in mappings");
                        }

                        CtMethod
                                newMethod =
                                new CtMethod(interfaceMethod.getReturnType(), interfaceMethod.getName(), interfaceMethod.getParameterTypes(), clazz);

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

                classesToLoad.add(clazz.getName());
            }
        } catch (NotFoundException | CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void instantiator(Class<?> instantiatorInterface) {
        try {
            CtClass ctMcInterface = pool.get(instantiatorInterface.getName());

            for (CtMethod interfaceMethod : ctMcInterface.getDeclaredMethods()) {
                CtClass mcInterfaceRet = interfaceMethod.getReturnType();

                CtClass mcActualRet = Mappings.getCtClass(((Implement) mcInterfaceRet.getAnnotation(Implement.class)).name());

                CtMethod newMethod = new CtMethod(mcInterfaceRet, interfaceMethod.getName(), interfaceMethod.getParameterTypes(), clazz);

                newMethod.setModifiers(Modifier.PUBLIC);

                MethodInfo mi = newMethod.getMethodInfo();
                Bytecode
                        bytecode =
                        new Bytecode(mi.getConstPool(), 1, 1);
                bytecode.addNew(mcActualRet);
                bytecode.addOpcode(Opcode.DUP);

                CtClass[] parameterTypes = newMethod.getParameterTypes();

                CtConstructor constructor = null;
                for (CtConstructor loopConstructor : mcActualRet.getDeclaredConstructors()) {
                    if (loopConstructor.getParameterTypes().length == parameterTypes.length) {
                        boolean works = true;
                        for (int i = 0; i < loopConstructor.getParameterTypes().length; i++) {
                            CtClass constructorParameterType = loopConstructor.getParameterTypes()[i];
                            CtClass instantiatorParameterType = parameterTypes[i];

                            if (instantiatorParameterType.subtypeOf(ClassPool.getDefault().get(MCInterface.class.getName()))) {
                                instantiatorParameterType =
                                        Mappings.getCtClass(((Implement) instantiatorParameterType.getAnnotation(Implement.class)).name());
                            }

                            if (!(Type.get(constructorParameterType).isAssignableFrom(Type.get(instantiatorParameterType)) || Type
                                    .get(instantiatorParameterType).isAssignableFrom(Type.get(constructorParameterType)))) {
                                works = false;
                            }
                        }
                        if (!works) {
                            continue;
                        }
                        constructor = loopConstructor;
                        break;
                    }
                }

                if (constructor == null) {
                    throw Throwables.propagate(new NotFoundException("no such constructor"));
                }

                int localCounter = 1;

                for (int i = 0; i < constructor.getParameterTypes().length; i++) {
                    CtClass parameterType = constructor.getParameterTypes()[i];

                    // Make sure maxLocals starts out being 1, otherwise this code breaks
                    bytecode.addLoad(bytecode.getMaxLocals(), parameterType);
                    if (!parameterType.isPrimitive()) {
                        bytecode.addCheckcast(parameterType);
                    }
                    bytecode.growStack(1);

                    int localDiff = (parameterType == CtClass.doubleType || parameterType == CtClass.longType) ? 2 : 1;

                    bytecode.incMaxLocals(localDiff);
                    localCounter += localDiff;
                }

                bytecode.addInvokespecial(mcActualRet, "<init>", CtClass.voidType, constructor.getParameterTypes());
                bytecode.addReturn(mcInterfaceRet);

                mi.setCodeAttribute(bytecode.toCodeAttribute());

                clazz.addMethod(newMethod);

                classesToLoad.add(clazz.getName());
            }
        } catch (NotFoundException | ClassNotFoundException | CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void replaceMethod(String methodName, String code) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.setBody(code);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void beforeMethod(String methodName, String code) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.insertBefore(code);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void afterMethod(String methodName, String code) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.insertAfter(code);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void addArgumentsVariable(String methodName) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);

        try {
            method.addLocalVariable("$mArgs", ClassPool.getDefault().get("java.lang.Object[]"));
            method.insertBefore("$mArgs = $args;");
        } catch (CannotCompileException | NotFoundException e) {
            Throwables.propagate(e);
        }
    }

    public void instrumentMethod(String methodName, ExprEditor editor) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.instrument(editor);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void instrument(ExprEditor editor) {
        try {
            clazz.instrument(editor);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void convertMethod(String methodName, CodeConverter converter) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.instrument(converter);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void convert(CodeConverter converter) {
        try {
            clazz.instrument(converter);
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public int addParameter(String methodName, CtClass type) {
        CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            CtMethod other = new CtMethod(method.getReturnType(), method.getName(), method.getParameterTypes(), method.getDeclaringClass());

            method.addParameter(type);

            String val = "null";
            if (type.isPrimitive()) {
                val = Defaults.defaultValue(getFromCt(type)).toString();
            }

            other.setBody("return " + method.getName() + "($$, (" + type.getName() + ") " + val + ");");

            method.getDeclaringClass().addMethod(other);

            return method.getParameterTypes().length - 1;
        } catch (CannotCompileException | NotFoundException e) {
            Throwables.propagate(e);
        }
        return 0;
    }

    public void replaceMethodCallParameter(String methodName, final CtMethod methodCall, final int parameterToReplace, final String replaceWith) {
        final CtMethod method = Mappings.getCtMethod(clazz, methodName);
        try {
            method.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    try {
                        if (m.getMethod().equals(methodCall)) {
                            String code = "$_ = $proceed(";

                            for (int i = 0; i < methodCall.getParameterTypes().length; i++) {
                                if (i == parameterToReplace) {
                                    CtClass parameterType = methodCall.getParameterTypes()[i];

                                    if (parameterType.isPrimitive()) {
                                        code +=
                                                "((" + ClassUtils.primitiveToWrapper(getFromCt(parameterType)).getName() + ") " + replaceWith + ")."
                                                + getFromCt(parameterType).getName() + "Value()";
                                    } else {
                                        code += "(" + parameterType.getName() + ") " + replaceWith;
                                    }
                                } else {
                                    code += "$" + (i + 1);
                                }

                                if (i < methodCall.getParameterTypes().length - 1) {
                                    code += ", ";
                                }
                            }

                            code += ");";
                            m.replace(code);
                        }
                    } catch (NotFoundException e) {
                        Throwables.propagate(e);
                    }
                }
            });
        } catch (CannotCompileException e) {
            Throwables.propagate(e);
        }
    }

    public void insert(String methodName, String codeToAdd, CodeInsertionMode insertionMode, CodePosition position) {
        final CtMethod method = Mappings.getCtMethod(clazz, methodName);

        if (position instanceof CodePosition.NewPosition) {
            String code = "";

            final CtClass clazz = ((CodePosition.NewPosition) position).getClazz();
            switch (insertionMode) {
                case BEFORE:
                    code += "{";
                    code += codeToAdd;
                    code += "$_ = $proceed($$);";
                    code += "}";
                    break;
                case REPLACE:
                    code += "{";
                    code += codeToAdd;
                    code += "}";
                    break;
                case AFTER:
                    code += "{";
                    code += "$_ = $proceed($$);";
                    code += codeToAdd;
                    code += "}";
                    break;
            }

            try {
                final String finalCode = code;
                method.instrument(new ExprEditor() {
                    @Override
                    public void edit(NewExpr e) throws CannotCompileException {
                        if (Objects.equals(e.getClassName(), clazz.getName())) {
                            e.replace(finalCode);
                        }
                    }
                });
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

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

    public void writeClass() {
        for (String className : classesToLoad) {
            try {
                CtClass ctClass = pool.get(className);
                ctClass.writeFile(Granite.getInstance().getClassesDir().getAbsolutePath());
            } catch (IOException | NotFoundException | CannotCompileException e) {
                Throwables.propagate(e);
            }
        }
    }

    public static interface ProxyHandlerCallback {

        Object invokeParent(Object... args) throws Throwable;
    }

    public static interface PostCallback {

        void callback();
    }

    public static abstract class ProxyHandler {

        public Object preHandle(final Object caller, Object[] args, final Method method) throws Throwable {
            ProxyHandlerCallback callback = new ProxyHandlerCallback() {
                @Override
                public Object invokeParent(Object... args) throws Throwable {
                    method.setAccessible(true);
                    MethodHandle handle = MethodHandles.lookup().unreflect(method);
                    return handle.invokeWithArguments(ArrayUtils.add(args, 0, caller));
                }
            };

            try {
                return handle(caller, args, callback);
            } catch (Throwable t) {
                if (!Mappings.getClass("ThreadQuickExitException").isInstance(t)) {
                    Granite.error(t);
                }
                throw t;
            }
        }

        protected abstract Object handle(Object caller, Object[] args, ProxyHandlerCallback callback) throws Throwable;
    }
}
