package org.granitemc.granite.reflect;

/*****************************************************************************************
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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************/

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.utils.Mappings;

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
            .build();

    /**
     * Will force access to a field. This even works with private static final fields!
     * <p/>
     * Internally, this uses some reflection-on-reflection trickery I found on StackOverflow :)
     *
     * @param f The field to force access to
     */
    public static void forceStaticAccessible(Field f) {
        try {
            f.setAccessible(true);

            // Some reflection-ception trickery
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean areTypesCompatible(Class<?> actual, Class<?> expected) {
        if (expected.isAssignableFrom(actual)) {
            return true;
        } else {
            // If actual is primitive and expected isn't, or vice versa (xor)
            if (actual.isPrimitive() ^ expected.isPrimitive()) {
                if (primitives.containsKey(actual) && primitives.get(actual).equals(expected)) {
                    return true;
                } else if (primitives.inverse().containsKey(actual) && primitives.inverse().get(actual).equals(expected)) {
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
        if (m.getParameterTypes().length != args.length) return false;
        for (int i = 0; i < m.getParameterTypes().length; i++) {
            if (!areTypesCompatible(args[i], m.getParameterTypes()[i])) return false;
        }
        return true;
    }

    /**
     * Invoke a method, casting every type to the appropriate type, primitives included
     *
     * @param instance The instance to invoke on
     * @param m        The method to invoke
     * @param args     The arguments to feed the method
     * @return The object returned by the method
     * @throws InvocationTargetException
     */
    public static Object invoke(Object instance, Method m, Object... args) throws InvocationTargetException {
        try {
            Class<?>[] paramTypes = m.getParameterTypes();

            for (int i = 0; i < args.length; i++) {
                Object obj = args[i];
                Class<?> actual = obj.getClass();
                Class<?> expected = paramTypes[i].getClass();

                if (areTypesCompatible(actual, expected)) {
                    args[i] = expected.cast(obj);
                }
            }
            return m.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a proxy that will direct every method called into the specified {@link javassist.util.proxy.MethodHandler}.
     * This will create a new instance of the source object.
     *
     * @param source          The source object
     * @param handler         The handler to proxy every method call to
     * @param createIdentical If true, will copy every field from the source object into the new proxy
     * @param paramTypes      The type of the constructor parameters - must match the types of the actual constructor parameters exactly
     * @param args            The objects to pass to the constructor as arguments
     * @return A new instance of the source object, with a proxy on top
     */
    public static Object createProxy(Object source, MethodHandler handler, boolean createIdentical, Class<?>[] paramTypes, Object... args) {
        ProxyFactory pf = new ProxyFactory();
        pf.setSuperclass(ReflectionUtils.extractClass(source));

        try {
            Object proxy = pf.create(paramTypes, args, handler);

            if (createIdentical) {
                for (Field f : proxy.getClass().getSuperclass().getDeclaredFields()) {
                    Granite.getLogger().debug(f);
                }
            }

            return proxy;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> extractClass(Object obj) {
        Class<?> clazz;

        /*if (obj instanceof Mappings.MCClass) {
            clazz = ((Mappings.MCClass) obj).getJavaClass();
        } else */
        if (obj instanceof Class<?>) {
            clazz = (Class<?>) obj;
        } else {
            clazz = obj.getClass();
        }

        return clazz;
    }

    public static Class<?> getClassByName(String name) {

        try {
            return Mappings.getClass(name);
        } catch (Mappings.MappingNotFoundException ignored) {
        }

        for (Class<?> primitive : primitives.keySet()) {
            if (Objects.equals(primitive.getName(), name)) {
                return primitive;
            }
        }

        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
        }

        try {
            return Class.forName("java.lang." + name);
        } catch (ClassNotFoundException ignored) {
        }
        return null;
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
            ret[i] = objects.getClass();
        }

        return ret;
    }
}
