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

package org.granitepowered.granite.util;

import com.google.common.base.Throwables;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.mappings.Mappings;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class ReflectionUtils {

    private static BiMap<Class<?>, Class<?>> primitives = new ImmutableBiMap.Builder<Class<?>, Class<?>>()
            .put(byte.class, Byte.class)
            .put(short.class, Short.class)
            .put(int.class, Integer.class)
            .put(long.class, Long.class)
            .put(float.class, Float.class)
            .put(double.class, Double.class)
            .put(boolean.class, Boolean.class)
            .put(char.class, Character.class)
            .put(void.class, Void.class)
            .build();

    private static BiMap<Class<?>, CtClass> ctPrimitives = new ImmutableBiMap.Builder<Class<?>, CtClass>()
            .put(byte.class, CtClass.byteType)
            .put(short.class, CtClass.shortType)
            .put(int.class, CtClass.intType)
            .put(long.class, CtClass.longType)
            .put(float.class, CtClass.floatType)
            .put(double.class, CtClass.doubleType)
            .put(boolean.class, CtClass.booleanType)
            .put(char.class, CtClass.charType)
            .put(void.class, CtClass.voidType)
            .build();

    /**
     * Will force access to a field. This even works with private static final fields!
     * <p/>
     * Internally, this uses some reflection-on-reflection trickery I found on StackOverflow :)
     *
     * @param f The field to force access to
     */
    public static void forceAccessible(Field f) {
        try {
            f.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Throwables.propagate(e);
        }
    }

    public static boolean areTypesCompatible(Class<?> actual, Class<?> expected) {
        if (actual == null || expected == null) {
            return false;
        }
        if (expected.isAssignableFrom(actual)) {
            return true;
        } else {
            if (actual.isPrimitive() ^ expected.isPrimitive()) {
                if (ctPrimitives.containsKey(actual) && ctPrimitives.get(actual).equals(expected)) {
                    return true;
                } else if (ctPrimitives.inverse().containsKey(actual) && ctPrimitives.inverse().get(actual).equals(expected)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMethodCompatible(Method m, Class<?> returnType, Class<?>... args) {
        return areTypesCompatible(returnType, m.getReturnType()) && isMethodCompatible(m, args);
    }

    public static boolean isMethodCompatible(Method m, Class<?>... args) {
        if (m.getParameterTypes().length != args.length) {
            return false;
        }
        for (int i = 0; i < m.getParameterTypes().length; i++) {
            if (!areTypesCompatible(args[i], m.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }

    public static Class<?> extractClass(Object obj) {
        Class<?> clazz;

        if (obj instanceof Class<?>) {
            clazz = (Class<?>) obj;
        } else {
            clazz = obj.getClass();
        }

        return clazz;
    }

    public static Class<?> getClassByName(String name) {
        boolean array = name.endsWith("[]");
        name = name.replaceAll("\\[\\]", "");

        Class<?> clazz = null;

        for (Class<?> primitive : ctPrimitives.keySet()) {
            if (Objects.equals(primitive.getName(), name)) {
                clazz = primitive;
            }
        }

        if (name.split("\\.").length == 1 && !name.toLowerCase().equals(name)) {
            clazz = Mappings.getClass(name);
        }

        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException ignored) {
        }

        try {
            clazz = Class.forName("java.lang." + name);
        } catch (ClassNotFoundException ignored) {
        }

        if (array) {
            clazz = Array.newInstance(clazz, 0).getClass();
        }
        return clazz;
    }

    public static CtClass getCtClassByName(String name) {
        boolean array = name.endsWith("[]");
        name = name.replaceAll("\\[\\]", "");

        CtClass clazz = null;

        for (CtClass primitive : ctPrimitives.values()) {
            if (Objects.equals(primitive.getName(), name)) {
                clazz = primitive;
            }
        }

        if (name.split("\\.").length == 1 && !name.toLowerCase().equals(name)) {
            clazz = Mappings.getCtClass(name);
        }

        try {
            clazz = Granite.getInstance().getClassPool().get(name);
        } catch (NotFoundException ignored) {
        }

        try {
            clazz = Granite.getInstance().getClassPool().get("java.lang." + name);
        } catch (NotFoundException ignored) {
        }

        if (array) {
            try {
                clazz = Granite.getInstance().getClassPool().get(name + "[]");
            } catch (NotFoundException ignored) {
            }
        }
        return clazz;
    }

    public static String getMethodSignature(Method m) {
        String sig = "";
        for (Class<?> type : m.getParameterTypes()) {
            sig = sig + type.getName() + ";";
        }
        return m.getName() + "(" + sig.substring(0, sig.length() - 1) + ")";
    }

    public static Class<?>[] getTypes(Object... objects) {
        Class<?>[] ret = new Class<?>[objects.length];

        for (int i = 0; i < objects.length; i++) {
            ret[i] = objects[i].getClass();
        }

        return ret;
    }

    // Used from bytecode, don't remove
    public static Object cast(Object input, Class clazz) {
        if (clazz.isArray()) {
            try {
                Class<?> actualType = Class.forName(clazz.getName().substring(2, clazz.getName().length() - 1));

                Object[] src = (Object[]) input;

                Object[] out = (Object[]) Array.newInstance(actualType, src.length);

                if (!out.getClass().isAssignableFrom(input.getClass())) {
                    for (int i = 0; i < src.length; i++) {
                        out[i] = actualType.cast(src[i]);
                    }

                    return out;
                } else {
                    return input;
                }
            } catch (ClassNotFoundException e) {
                Throwables.propagate(e);
            }
        }
        return input;
    }

    public static CtMethod getCtMethod(String clazz, String method) {
        try {
            return Mappings.getCtMethod(clazz, method);
        } catch (Mappings.MappingNotFoundException e) {
            CtClass clazzz = getCtClassByName(clazz);

            if (method.contains("(")) {
                for (CtMethod methodd : clazzz.getDeclaredMethods()) {
                    if (method.split("\\(")[0].equals(methodd.getName())) {
                        if (methodd.getLongName().split("\\(")[1].equals(method.split("\\(")[1])) {
                            return methodd;
                        }
                    }
                }
                throw new RuntimeException(clazz + "#" + method + " not found");
            }
            try {
                return clazzz.getDeclaredMethod(method);
            } catch (NotFoundException e1) {
                throw Throwables.propagate(e1);
            }
        }
    }
}
