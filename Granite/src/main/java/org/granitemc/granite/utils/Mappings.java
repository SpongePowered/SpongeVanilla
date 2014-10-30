/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import javassist.*;
import org.apache.commons.lang3.ArrayUtils;
import org.granitemc.granite.reflect.ReflectionUtils;

import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Mappings {
    static Config file;

    // Human -> Obfuscated Class
    static BiMap<String, Class<?>> classes;
    static BiMap<String, CtClass> ctClasses;

    // Class -> <Signature -> Obfuscated Signature>
    //static Map<Class<?>, BiMap<SignatureParser.MethodSignature, String>> methodNames;

    // Class -> <Name -> Method>
    static Map<CtClass, BiMap<String, MethodHandle>> methods;
    static Map<CtClass, BiMap<String, CtMethod>> ctMethods;
    // TODO: this BiMap may cause an issue with method overloads

    // Class -> <Human Name -> Method>
    static Map<CtClass, BiMap<String, Field>> fields;
    static Map<CtClass, BiMap<String, CtField>> ctFields;

    static ClassPool pool;

    public static void load() {
        file = ConfigFactory.parseReader(new InputStreamReader(Mappings.class.getResourceAsStream(String.valueOf(org.granitemc.granite.utils.Config.mappings))));

        classes = HashBiMap.create();
        ctClasses = HashBiMap.create();

        methods = new HashMap<>();
        ctMethods = new HashMap<>();

        fields = new HashMap<>();
        ctFields = new HashMap<>();

        pool = new ClassPool(true);

        try {
            for (Map.Entry<String, ConfigValue> classObject : file.getObject("classes").entrySet()) {
                String className = (String) ((ConfigObject) classObject.getValue()).get("name").unwrapped();
                ctClasses.put(classObject.getKey(), pool.get(className));
                ctClasses.put(classObject.getKey() + "[]", pool.get(className + "[]"));
            }

            for (Map.Entry<String, CtClass> entry : ctClasses.entrySet()) {
                CtClass ctClass = entry.getValue();
                if (!entry.getKey().endsWith("[]")) {
                    ConfigObject classObject = file.getObject("classes." + entry.getKey().replaceAll("\\$", "\"\\$\""));

                    methods.put(ctClass, HashBiMap.<String, MethodHandle>create());
                    ctMethods.put(ctClass, HashBiMap.<String, CtMethod>create());

                    if (classObject.containsKey("methods")) {
                        for (Map.Entry<String, ConfigValue> methodEntry : ((ConfigObject) classObject.get("methods")).entrySet()) {
                            String methodSignature = methodEntry.getKey();
                            String methodName = (String) methodEntry.getValue().unwrapped();

                            SignatureParser.MethodSignature obfSig = SignatureParser.parseJvm(methodSignature);

                            /*MethodHandle mh = null;
                            try {
                                mh = MethodHandles.lookup().findVirtual(clazz.getValue(), methodSignature.split("\\(")[0], MethodType.methodType(obfSig.getReturnType(), obfSig.getParamTypes()));
                            } catch (NoSuchMethodException | IllegalAccessException e) {
                                if (e.getMessage().startsWith("no such method")) {
                                    try {
                                        Method m = clazz.getValue().getDeclaredMethod(methodSignature.split("\\(")[0], obfSig.getParamTypes());
                                        m.setAccessible(true);
                                        mh = MethodHandles.lookup().unreflect(m);
                                    } catch (NoSuchMethodException | IllegalAccessException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }

                            if (mh == null) {
                                mh = mh;
                            }*/


                            CtMethod method = ctClass.getMethod(methodSignature.split("\\(")[0], "(" + methodSignature.split("\\(")[1]);
                            ctMethods.get(method.getDeclaringClass()).put(methodName, method);
                        }
                    }

                    fields.put(ctClass, HashBiMap.<String, Field>create());
                    ctFields.put(ctClass, HashBiMap.<String, CtField>create());

                    if (classObject.containsKey("fields")) {
                        for (Map.Entry<String, ConfigValue> fieldEntry : ((ConfigObject) classObject.get("fields")).entrySet()) {
                            String obfuscatedFieldName = fieldEntry.getKey();
                            String fieldName = (String) fieldEntry.getValue().unwrapped();

                            ctFields.get(ctClass).put(fieldName, ctClass.getDeclaredField(obfuscatedFieldName));
                        }
                    }
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static CtClass getCtClass(String humanClassName) {
        return ctClasses.get(humanClassName);
    }

    public static CtClass getCtClass(Class<?> clazz) {
        try {
            return pool.get((clazz.getName().contains("$$") ? clazz.getSuperclass() : clazz).getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getClassName(CtClass ctClass) {
        return ctClasses.inverse().get(ctClass);
    }

    public static String getClassName(Class<?> clazz) {
        if (classes.inverse().containsKey(clazz)) {
            return classes.inverse().get(clazz);
        } else {
            try {
                return getClassName(pool.get(clazz.getName()));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Class<?> getClass(String humanClassName) {
        if (classes.containsKey(humanClassName)) {
            return classes.get(humanClassName);
        } else {
            try {
                Class<?> clazz = Class.forName(ctClasses.get(humanClassName).getName());
                classes.put(humanClassName, clazz);
                return clazz;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Field getField(CtClass clazz, String humanFieldName) {
        if (fields.containsKey(clazz)) {
            if (fields.get(clazz).containsKey(humanFieldName)) {
                return fields.get(clazz).get(humanFieldName);
            } else {
                try {
                    CtField ctField = ctFields.get(clazz).get(humanFieldName);
                    if (ctField == null && !clazz.getName().equals("java.lang.Object")) {
                        return getField(clazz.getSuperclass(), humanFieldName);
                    }
                    Field field = getClass(getClassName(ctField.getDeclaringClass())).getDeclaredField(ctField.getName());
                    fields.get(clazz).put(humanFieldName, field);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException | NotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (!clazz.getName().equals("java.lang.Object")) {
            try {
                return getField(clazz.getSuperclass(), humanFieldName);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Field getField(String clazz, String humanFieldName) {
        return getField(getCtClass(clazz), humanFieldName);
    }

    public static Field getField(Class<?> clazz, String humanFieldName) {
        return getField(getCtClass(clazz), humanFieldName);
    }

    public static MethodHandle getMethod(CtClass clazz, String methodName) {
        if (methods.containsKey(clazz)) {
            if (methods.get(clazz).containsKey(methodName)) {
                return methods.get(clazz).get(methodName);
            } else {
                CtMethod ctMethod = ctMethods.get(clazz).get(methodName);

                if (ctMethod != null) {
                    try {
                        Class<?>[] paramTypes = new Class<?>[ctMethod.getParameterTypes().length];
                        for (int i = 0; i < paramTypes.length; i++) {
                            paramTypes[i] = ReflectionUtils.getClassByName(ctMethod.getParameterTypes()[i].getName());
                        }
                        Method m = Class.forName(ctMethod.getDeclaringClass().getName()).getDeclaredMethod(ctMethod.getName(), paramTypes);
                        m.setAccessible(true);
                        MethodHandle handle = MethodHandles.lookup().unreflect(m);

                        methods.get(clazz).put(methodName, handle);
                        return handle;
                    } catch (NotFoundException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            CtClass superClass = clazz.getSuperclass();
            if (superClass == null || superClass.getName().equals("java.lang.Object")) {
                for (CtClass interfac : clazz.getInterfaces()) {
                    MethodHandle m = getMethod(interfac, methodName);
                    return m;
                }
            } else {
                return getMethod(superClass, methodName);
            }
        } catch (NotFoundException ignored) {
        }
        return null;
    }

    public static MethodHandle getMethod(String clazz, String methodName) {
        return getMethod(getCtClass(clazz), methodName);
    }

    public static MethodHandle getMethod(Class<?> clazz, String methodName) {
        return getMethod(getCtClass(clazz), methodName);
    }

    /*public static String getMethodName(Class<?> clazz, MethodHandle methodHandle) {
        return methods.get(getCtClass(clazz)).inverse().get(methodHandle);
    }

    public static String getMethodName(Class<?> clazz, Method method) {
        try {
            return getMethodName(clazz, MethodHandles.lookup().unreflect(method));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static Object invoke(Object object, String methodName, Object... args) {
        return invoke(object, getMethod(getCtClass(object.getClass()), methodName), args);
    }

    public static Object invoke(Object object, MethodHandle handle, Object... args) {
        try {
            return handle.invokeWithArguments(ArrayUtils.add(args, 0, object));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static Object invokeStatic(Class<?> clazz, String methodName, Object... args) {
        try {
            return getMethod(getCtClass(clazz), methodName).invokeWithArguments(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static Object invokeStatic(String className, String methodName, Object... args) {
        return invokeStatic(getClass(className), methodName, args);
    }
}
