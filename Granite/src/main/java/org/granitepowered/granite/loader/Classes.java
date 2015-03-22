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

package org.granitepowered.granite.loader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Classes {

    public static ClassPool pool;

    private static Map<String, Class> classCache = new HashMap<>();
    private static Map<String, Method> methodCache = new HashMap<>();
    private static Map<String, Field> fieldCache = new HashMap<>();


    public static CtClass getCtClass(String name) {
        return pool.getOrNull(name);
    }

    public static CtMethod getCtMethod(String fullMethodSignature) {
        return getCtMethod(fullMethodSignature.split("#")[0], fullMethodSignature.split("#")[1]);
    }

    public static CtMethod getCtMethod(String clazz, String methodSignature) {
        return getCtMethod(getCtClass(clazz), methodSignature);
    }

    public static CtMethod getCtMethod(CtClass clazz, String methodSignature) {
        // If this is a full signature (ie. includes params)
        if (methodSignature.contains("(")) {
            // Then find the full match
            String methodName = methodSignature.split("\\(")[0];
            String methodParams = "(" + methodSignature.split("\\(")[1];

            for (CtMethod method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    if (Descriptor.toString(method.getSignature()).equals(methodParams)) {
                        // Match!

                        return method;
                    }
                }
            }
        } else {
            // Else get just by name (usually first match)
            try {
                return clazz.getDeclaredMethod(methodSignature);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static CtField getCtField(String fullFieldName) {
        return getCtField(fullFieldName.split("#")[0], fullFieldName.split("#")[1]);
    }

    public static CtField getCtField(String clazz, String fieldName) {
        return getCtField(getCtClass(clazz), fieldName);
    }

    public static CtField getCtField(CtClass clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getClass(String className) {
        if (!className.contains(".")) {
            className = "mc." + className;
        }
        if (classCache.containsKey(className)) {
            return classCache.get(className);
        }
        try {
            classCache.put(className, Class.forName(className));
            return classCache.get(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(String fullMethodSignature) {
        return getMethod(fullMethodSignature.split("#")[0], fullMethodSignature.split("#")[1]);
    }

    public static Method getMethod(String clazz, String methodSignature) {
        return getMethod(getClass(clazz), methodSignature);
    }

    public static Method getMethod(Class<?> clazz, String methodSignature) {
        String key = clazz.getName() + "#" + methodSignature;
        if (methodCache.containsKey(key)) {
            return methodCache.get(key);
        } else {
            // If this is a full signature (ie. includes params)
            if (methodSignature.contains("(")) {
                // Then find the full match
                String methodName = methodSignature.split("\\(")[0];
                String methodParamStr = methodSignature.split("\\(")[1].split("\\(")[0];

                String[] methodParams = methodParamStr.split(",");

                // Look through every method
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(methodName)) {
                        // If the length is different, there's absolutely no way it's the right method
                        if (method.getParameterTypes().length == methodParams.length) {
                            boolean match = true;

                            // Loop through all the parameters, if any of them are wrong, stop
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            for (int i = 0; i < parameterTypes.length; i++) {
                                Class<?> actualMethodParameter = parameterTypes[i];

                                if (!actualMethodParameter.getName().equals(methodParams[i])) {
                                    match = false;
                                }
                            }

                            if (match) {
                                methodCache.put(key, method);
                                return method;
                            }
                        }
                    }
                }
            } else {
                // Else get just by name (first match)
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equals(methodSignature)) {
                        // Add it under the plain name here, this will cause another lookup if the method is
                        // accessed with its full signature, but that doesn't matter.
                        methodCache.put(key, method);
                        return method;
                    }
                }
            }
        }
        return null;
    }

    public static Object invokeStatic(String className, String methodSignature, Object... parameters) {
        try {
            return getMethod(className, methodSignature).invoke(null, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(String fullFieldName) {
        return getField(fullFieldName.split("#")[0], fullFieldName.split("#")[1]);
    }

    public static Field getField(String clazz, String fieldName) {
        return getField(getClass(clazz), fieldName);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        String key = clazz.getName() + "#" + fieldName;
        if (fieldCache.containsKey(key)) {
            return fieldCache.get(key);
        } else {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                fieldCache.put(key, field);
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
