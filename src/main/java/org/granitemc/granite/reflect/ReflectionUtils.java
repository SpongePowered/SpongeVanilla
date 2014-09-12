package org.granitemc.granite.reflect;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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

    public static Object invoke(Object instance, Method m, Object... args) throws InvocationTargetException {
        try {
            Class<?>[] paramTypes = m.getParameterTypes();

            for (int i = 0; i < args.length; i++) {
                Object obj = args[i];
                Class<?> actual = obj.getClass();
                Class<?> expected = paramTypes[i].getClass();

                boolean canCast = false;
                if (expected.isAssignableFrom(actual)) {
                    canCast = true;
                } else {
                    // If actual is primitive and expected isn't, or vice versa (xor)
                    if (actual.isPrimitive() ^ !expected.isPrimitive()) {
                        if (primitives.get(actual).equals(expected)) {
                            canCast = true;
                        } else if (primitives.inverse().get(actual).equals(expected)) {
                            canCast = true;
                        }
                    }
                }

                if (canCast) {
                    args[i] = expected.cast(obj);
                }
            }
            return m.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
