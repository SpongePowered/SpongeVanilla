package org.granitemc.granite.utils;

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
import com.google.common.collect.HashBiMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.granitemc.granite.reflect.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Mappings {
    // method signature = executeCommand(n.m.command.ICommandSender, String)
    // full method signature = n.m.command.ICommandManager.executeCommand(n.m.command.ICommandSender, String)

    // human -> clazz
    private static BiMap<String, Class<?>> classes;

    // humanClass -> <humanMethodSig, method>
    private static Map<String, BiMap<String, Method>> methods;

    // humanClass -> <humanField, method>
    private static Map<String, BiMap<String, Field>> fields;

    @SuppressWarnings("unchecked")
    public static void load() {
        Config mappings = ConfigFactory.parseURL(Mappings.class.getResource("/mappings.conf"));

        classes = HashBiMap.create();
        methods = new HashMap<>();
        fields = new HashMap<>();

        // Step one - load all the classes and names
        for (Map.Entry<String, Object> entry : mappings.getObject("classes").unwrapped().entrySet()) {
            Map<String, Object> classMap = (Map<String, Object>) entry.getValue();

            Class<?> clazz = null;
            try {
                clazz = Class.forName(expandShortcuts((String) classMap.get("name")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            classes.put(entry.getKey(), clazz);
        }

        // Step two - load the methods and fields
        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            Map<String, Object> classMap = mappings.getObject("classes." + "\"" + entry.getKey() + "\"").unwrapped();

            // Create map
            methods.put(entry.getKey(), HashBiMap.<String, Method>create());

            if (classMap.containsKey("methods")) {
                Map<String, Object> methodsMap = (Map<String, Object>) classMap.get("methods");
                for (Map.Entry<String, Object> methodEntry : methodsMap.entrySet()) {
                    if (methodEntry.getValue() instanceof List) {
                        for (String obfuscatedMethodSignature : (List<String>) methodEntry.getValue()) {
                            loadMethod(entry.getKey(), methodEntry.getKey(), obfuscatedMethodSignature);
                        }
                    } else {
                        loadMethod(entry.getKey(), methodEntry.getKey(), (String) methodEntry.getValue());
                    }
                }
            }

            // Create map
            fields.put(entry.getKey(), HashBiMap.<String, Field>create());
            if (classMap.containsKey("fields")) {
                Map<String, Object> fieldsMap = (Map<String, Object>) classMap.get("fields");
                for (Map.Entry<String, Object> fieldEntry : fieldsMap.entrySet()) {
                    Field f = null;
                    try {
                        f = entry.getValue().getDeclaredField((String) fieldEntry.getValue());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    fields.get(entry.getKey()).put(fieldEntry.getKey(), f);
                }
            }
        }
    }

    private static String[] splitLast(String str, String delim) {
        int i = str.lastIndexOf(delim);
        if (i < 0) {
            return new String[]{str};
        }
        return new String[]{str.substring(0, i), str.substring(i + 1)};
    }

    private static void loadMethod(String className, String methodName, String obfuscatedMethodSignature) {
        // Parse the obfuscated method signature
        Method m = getMethodByObfuscatedSignature(className + "." + obfuscatedMethodSignature);

        // Build the human signature
        String sig = "";
        for (Class<?> clazz : m.getParameterTypes()) {
            String humanName = getClassName(clazz, false);
            if (humanName != null) {
                sig = sig + humanName;
            } else if (StringUtils.countMatches(clazz.getName(), ".") == 2 && clazz.getName().startsWith("java.lang.")) {
                sig = sig + clazz.getName().substring(10);
            } else {
                sig = sig + clazz.getName();
            }
            sig = sig + ";";
        }

        if (sig.length() > 0) {
            sig = sig.substring(0, sig.length() - 1);
        }

        String methodSignature = methodName + "(" + sig + ")";

        // Smack it in dat map
        methods.get(className).put(methodSignature, m);
    }

    public static String expandShortcuts(String str) {
        return str.replaceAll("n.m", "net.minecraft");
    }

    public static Class<?> getClass(String className) {
        Class<?> res = classes.get(expandShortcuts(className));
        if (res == null) {
            throw new MappingNotFoundException(expandShortcuts(className));
        }
        return res;
    }

    private static String getClassName(Class<?> clazz, boolean throwException) {
        String res = classes.inverse().get(clazz);
        if (res == null && throwException) {
            throw new MappingNotFoundException(clazz.getName());
        }
        return res;
    }

    public static String getClassName(Class<?> clazz) {
        return getClassName(clazz, true);
    }

    public static String getClassName(String obfuscatedClassName) {
        try {
            String res = classes.inverse().get(Class.forName(obfuscatedClassName));
            if (res == null) {
                throw new MappingNotFoundException(obfuscatedClassName);
            }
            return res;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(String className, String methodSignature) {
        if (!methodSignature.contains("(")) {
            methodSignature = methodSignature + "()";
        }

        Method res = null;
        Class<?> searchClass = getClass(className);
        while (res == null && !searchClass.equals(Object.class)) {
            if (methods.containsKey(getClassName(searchClass)) && methods.get(getClassName(searchClass)).containsKey(methodSignature)) {
                res = methods.get(getClassName(searchClass)).get(methodSignature);
                break;
            } else {
                searchClass = searchClass.getSuperclass();
            }
        }

        if (res == null) {
            throw new MappingNotFoundException(expandShortcuts(className) + "/" + methodSignature);
        }

        return res;
    }

    public static Method getMethod(Class<?> clazz, String methodSignature) {
        return getMethod(getClassName(clazz), methodSignature);
    }

    public static Method getMethod(String fullSignature) {
        String[] split = fullSignature.split(".", -1);
        return getMethod(split[0], split[1]);
    }

    public static String getMethodSignature(String className, Method method) {
        String res = methods.get(expandShortcuts(className)).inverse().get(method);
        if (res == null) {
            throw new MappingNotFoundException(className + "/" + method.getName());
        }

        return res;
    }

    public static String getMethodSignature(Class<?> clazz, Method method) {
        return getMethodSignature(getClassName(clazz), method);
    }

    public static String getMethodSignature(Method method) {
        return getMethodSignature(getClassName(method.getDeclaringClass()), method);
    }

    public static Field getField(String className, String fieldName) {
        Field res = fields.get(expandShortcuts(className)).get(fieldName);

        if (res == null) {
            throw new MappingNotFoundException(className + "/" + fieldName);
        }

        return res;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return getField(getClassName(clazz), fieldName);
    }

    public static Field getField(String fullFieldName) {
        String[] split = splitLast(fullFieldName, ".");
        return getField(split[0], split[1]);
    }

    public static Object invoke(Object instance, Method m, Object... args) {
        try {
            return m.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.getCause().printStackTrace();
        }
        return null;
    }

    public static Object invoke(Object instance, Class<?> clazz, String methodSignature, Object... args) {
        return invoke(instance, getMethod(clazz, methodSignature), args);
    }

    public static Object invoke(Object instance, String className, String methodSignature, Object... args) {
        return invoke(instance, getMethod(className, methodSignature), args);
    }

    public static Object invoke(Object instance, String fullMethodSignature, Object... args) {
        return invoke(instance, getMethod(fullMethodSignature), args);
    }

    public static Method getMethodByObfuscatedSignature(String obfuscatedFullSignature) {
        Class<?>[] types;

        String methodSignature = splitLast(obfuscatedFullSignature, ".")[1];
        String methodName = methodSignature.split("\\(")[0];
        if (methodName.contains(")")) methodName = methodName.substring(0, methodName.length() - 1);

        String unobfClassName = splitLast(obfuscatedFullSignature.split("\\(")[0], ".")[0];
        if (obfuscatedFullSignature.contains("(")) {
            methodSignature = obfuscatedFullSignature.substring(obfuscatedFullSignature.split("\\(")[0].lastIndexOf(".") + 1);
            types = new Class<?>[StringUtils.countMatches(methodSignature, ";") + 1];
            String params = methodSignature.split("\\(")[1].split("\\)")[0];

            int i = 0;
            for (String param : params.split(";")) {
                Class<?> type = ReflectionUtils.getClassByName(expandShortcuts(param.trim()));
                types[i] = type;
                ++i;
            }


            methodName = methodSignature.split("\\(")[0];
        } else {
            types = new Class<?>[0];
        }

        if (classes.containsKey(unobfClassName)) {
            Class<?> clazz = classes.get(unobfClassName);

            // insertion-ordered set
            Set<Method> joinedMethods = new LinkedHashSet<>();
            joinedMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            joinedMethods.addAll(Arrays.asList(clazz.getMethods()));

            for (Method m : joinedMethods) {
                if (m.getName().equals(methodName) && ReflectionUtils.isMethodCompatible(m, types)) {
                    return m;
                }
            }
        }
        throw new MappingNotFoundException(unobfClassName + "/" + splitLast(obfuscatedFullSignature, ".")[1]);
    }

    public static Method getMethodByObfuscatedSignature(Class<?> clazz, String methodSignature) {
        return getMethodByObfuscatedSignature(getClassName(clazz) + "." + methodSignature);
    }

    public static Method getMethodByObfuscatedSignature(String className, String methodSignature) {
        return getMethodByObfuscatedSignature(className + "." + methodSignature);
    }

    public static class MappingNotFoundException extends RuntimeException {

        private String triedToAccess;

        public MappingNotFoundException(String triedToAccess) {
            super("Tried to access " + triedToAccess + " - but couldn't find it.");
            this.triedToAccess = triedToAccess;
        }

        public String getTriedToAccess() {
            return triedToAccess;
        }
    }
}
