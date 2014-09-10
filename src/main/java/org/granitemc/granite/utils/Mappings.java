package org.granitemc.granite.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
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
 */

public class Mappings {

    private static Map<String, Class<?>> classes;
    private static Map<String, Map<String, ?>> methods;
    private static Map<String, Map<String, ?>> fields;

    @SuppressWarnings("unchecked")
    private static void load() {
        Config mappings = ConfigFactory.parseURL(Mappings.class.getResource("/mappings.conf"));

        classes = new HashMap<>();
        for (Map.Entry<String, Object> entry : mappings.getObject("mappings.classes").unwrapped().entrySet()) {
            try {
                classes.put(entry.getKey(), Class.forName((String) entry.getValue()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        methods = new HashMap<>();
        for (Map.Entry<String, Object> entry : mappings.getObject("mappings.methods").unwrapped().entrySet()) {
            methods.put(entry.getKey(), (Map<String, ?>) entry.getValue());
        }
        fields = new HashMap<>();
        for (Map.Entry<String, Object> entry : mappings.getObject("mappings.fields").unwrapped().entrySet()) {
            fields.put(entry.getKey(), (Map<String, ?>) entry.getValue());
        }
    }

    public static Class<?> getClassByHumanName(String humanName) {
        if (classes == null) {
            load();
        }

        if (!classes.containsKey(humanName)) {
            throw new MappingNotFoundException(humanName);
        }
        return classes.get(humanName);
    }

    public static Method getMethod(String humanClassName, String humanMethodName) {
        if (classes == null || methods == null) {
            load();
        }

        Class<?> clazz = getClassByHumanName(humanClassName);
        try {
            return clazz.getMethod(String.valueOf(methods.get(humanClassName).get(humanMethodName)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new MappingNotFoundException(humanClassName + "/" + humanMethodName + "(...)");
        }
    }

    public static Method getMethod(Class<?> clazz, String humanMethodName) {
        String humanClassName = null;

        for (Map.Entry<String, Class<?>> entry : classes.entrySet()) {
            if (Objects.equals(entry.getValue().getName(), clazz.getName())) {
                humanClassName = entry.getKey();
                break;
            }
        }

        return getMethod(humanClassName, humanMethodName);
    }

    public static Field getField(String humanClassName, String humanFieldName) {
        if (classes == null || methods == null) {
            load();
        }

        Class<?> clazz = getClassByHumanName(humanClassName);
        try {
            return clazz.getField(String.valueOf(fields.get(humanClassName).get(humanFieldName)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new MappingNotFoundException(humanClassName + "/" + humanFieldName);
        }
    }

    public static Object call(Object object, String humanClassName, String humanMethodName, Object... args) {
        Method method = getMethod(humanClassName, humanMethodName);

        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    public static Object call(Object object, String humanMethodName, Object... args) {
        Class<?> clazz = object.getClass();
        Method method = getMethod(clazz, humanMethodName);

        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    public static class MappingNotFoundException extends RuntimeException {

        private String triedToAccess;

        public MappingNotFoundException(String triedToAccess) {
            super("Tried to access " + triedToAccess);
            this.triedToAccess = triedToAccess;
        }

        public String getTriedToAccess() {
            return triedToAccess;
        }
    }
}
