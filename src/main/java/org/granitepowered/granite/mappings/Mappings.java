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

package org.granitepowered.granite.mappings;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Throwables;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigValue;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.apache.commons.lang3.ArrayUtils;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.util.ReflectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Mappings {

    static Config file;

    // Human -> Obfuscated Class
    static BiMap<String, Class<?>> classes;
    static BiMap<String, CtClass> ctClasses;

    // Class -> <Name -> Method>
    static Map<CtClass, BiMap<String, MethodHandle>> methods;
    static Map<CtClass, BiMap<String, CtMethod>> ctMethods;

    // Class -> <Human Name -> Field>
    static Map<CtClass, BiMap<String, Field>> fields;
    static Map<CtClass, BiMap<String, CtField>> ctFields;

    static ClassPool pool;

    private static void downloadMappings(File mappingsFile, String url, HttpRequest req) {
        Granite.instance.getLogger().warn("Downloading from " + url);
        if (req.code() == 404) {
            //throw new RuntimeException("GitHub 404 error whilst trying to download");
        } else if (req.code() == 200) {
            req.receive(mappingsFile);
            Granite.instance.getServerConfig().set("latest-mappings-etag", req.eTag());
            Granite.instance.getServerConfig().save();
        }
    }

    public static void load() {
        try {
            File mappingsFile = new File(Granite.instance.getServerConfig().getMappingsFile().getAbsolutePath());
            String url = "https://raw.githubusercontent.com/GraniteTeam/GraniteMappings/sponge/1.8.3.json";
            try {
                HttpRequest req = HttpRequest.get(url);

                if (Granite.instance.getServerConfig().getAutomaticMappingsUpdating()) {
                    Granite.instance.getLogger().info("Querying Granite for updates");
                    if (!mappingsFile.exists()) {
                        Granite.instance.getLogger().warn("Could not find mappings.json");
                        downloadMappings(mappingsFile, url, req);
                    } else if (!Objects.equals(req.eTag(), Granite.instance.getServerConfig().getLatestMappingsEtag())) {
                        Granite.instance.getLogger().info("Update found");
                        downloadMappings(mappingsFile, url, req);
                    }
                }
            } catch (HttpRequest.HttpRequestException e) {
                Granite.instance.getLogger().warn("Could not reach Granite mappings, falling back to local");

                if (!mappingsFile.exists()) {
                    Granite.instance.getLogger()
                            .warn("Could not find local mappings file. Obtain it (somehow) and place it in the server's root directory called \"mappings.json\"");
                    Throwables.propagate(e);
                } else {
                    Granite.error(e);
                }
            }
            file = ConfigFactory.parseReader(
                    new InputStreamReader(
                            new FileInputStream(mappingsFile)
                    ),
                    ConfigParseOptions.defaults().setSyntax(ConfigSyntax.JSON)
            );
        } catch (java.io.IOException e) {
            Throwables.propagate(e);
        }

        classes = HashBiMap.create();
        ctClasses = HashBiMap.create();

        methods = new HashMap<>();
        ctMethods = new HashMap<>();

        fields = new HashMap<>();
        ctFields = new HashMap<>();

        pool = Granite.getInstance().getClassPool();

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
            Throwables.propagate(e);
        }
    }

    public static CtClass getCtClass(String humanClassName) {
        if (!ctClasses.containsKey(humanClassName)) throw new MappingNotFoundException("Could not find CtClass " + humanClassName);
        return ctClasses.get(humanClassName);
    }

    public static CtClass getCtClass(Class<?> clazz) {
        try {
            return pool.get((clazz.getName().contains("$$") ? clazz.getSuperclass() : clazz).getName());
        } catch (NotFoundException e) {
            throw new MappingNotFoundException("Could not find CtClass " + clazz.getName());
        }
    }

    public static String getClassName(CtClass ctClass) {
        if (!ctClasses.inverse().containsKey(ctClass)) {
            throw new MappingNotFoundException("Could not find CtClass " + ctClass.getName());
        }
        return ctClasses.inverse().get(ctClass);
    }

    public static String getClassName(Class<?> clazz) {
        if (classes.inverse().containsKey(clazz)) {
            return classes.inverse().get(clazz);
        } else {
            try {
                return getClassName(pool.get(clazz.getName()));
            } catch (NotFoundException e) {
                throw new MappingNotFoundException("Could not find CtClass " + clazz.getName());
            }
        }
    }

    public static Class<?> getClass(String humanClassName) {
        if (classes.containsKey(humanClassName)) {
            return classes.get(humanClassName);
        } else {
            try {
                if (!ctClasses.containsKey(humanClassName)) {
                    throw new MappingNotFoundException("Could not find CtClass " + humanClassName);
                }
                Class<?> clazz = Class.forName(ctClasses.get(humanClassName).getName());
                classes.put(humanClassName, clazz);
                return clazz;
            } catch (ClassNotFoundException e) {
                throw new MappingNotFoundException("Could not find class " + humanClassName);
            }
        }
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
                    throw new MappingNotFoundException("Could not find field " + clazz.getName() + "." + humanFieldName);
                }
            }
        } else if (!clazz.getName().equals("java.lang.Object")) {
            try {
                return getField(clazz.getSuperclass(), humanFieldName);
            } catch (NotFoundException e) {
                throw new MappingNotFoundException("Could not find superclass");
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
                        throw new MappingNotFoundException("Could not find method " + clazz.getName() + "." + methodName);
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

    public static CtMethod getCtMethod(CtClass clazz, String methodName) {
        if (ctMethods.containsKey(clazz) && ctMethods.get(clazz).containsKey(methodName)) {
            return ctMethods.get(clazz).get(methodName);
        }

        try {
            for (CtClass interfac : clazz.getInterfaces()) {
                CtMethod imethod = getCtMethod(interfac, methodName);
                if (imethod != null) {
                    return imethod;
                }
            }

            CtClass superClass = clazz.getSuperclass();

            if (superClass != null) {
                CtMethod method = getCtMethod(superClass, methodName);
                if (method != null) {
                    return method;
                }
            }
        } catch (NotFoundException e) {
            throw new MappingNotFoundException("Could not find method " + clazz.getName() + "." + methodName);
        }
        return null;
    }

    public static CtMethod getCtMethod(String clazz, String methodName) {
        return getCtMethod(getCtClass(clazz), methodName);
    }

    public static CtField getCtField(CtClass clazz, String humanFieldName) {
        try {
            if (!ctFields.containsKey(clazz)) {
                throw new MappingNotFoundException("Could not find class " + clazz.getName());
            }
            CtField ctField = ctFields.get(clazz).get(humanFieldName);

            if (ctField == null && !clazz.getName().equals("java.lang.Object")) {
                return getCtField(clazz.getSuperclass(), humanFieldName);
            }

            return ctField;
        } catch (NotFoundException e) {
            throw new MappingNotFoundException("Could not find field " + clazz.getName() + "." + humanFieldName);
        }
    }

    public static CtField getCtField(String clazz, String humanCtfieldName) {
        return getCtField(getCtClass(clazz), humanCtfieldName);
    }

    public static CtField getCtField(Class<?> clazz, String humanCtfieldName) {
        return getCtField(getCtClass(clazz), humanCtfieldName);
    }

    public static Object invoke(Object object, String methodName, Object... args) {
        return invoke(object, getMethod(getCtClass(object.getClass()), methodName), args);
    }


    public static Object invoke(Object object, MethodHandle handle, Object... args) {
        try {
            return handle.invokeWithArguments(ArrayUtils.add(args, 0, object));
        } catch (Throwable throwable) {
            Throwables.propagate(throwable);
        }
        return null;
    }

    public static Object invokeStatic(Class<?> clazz, String methodName, Object... args) {
        try {
            return getMethod(getCtClass(clazz), methodName).invokeWithArguments(args);
        } catch (Throwable throwable) {
            if (!(throwable instanceof RuntimeException)) {
                throw new RuntimeException(throwable);
            } else {
                throw (RuntimeException) throwable;
            }
        }
    }

    public static Object invokeStatic(String className, String methodName, Object... args) {
        return invokeStatic(getClass(className), methodName, args);
    }

    public static class MappingNotFoundException extends RuntimeException {

        public MappingNotFoundException(String s) {
            super(s);
        }
    }
}
