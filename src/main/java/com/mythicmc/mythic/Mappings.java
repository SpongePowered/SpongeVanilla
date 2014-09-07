package com.mythicmc.mythic;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Mappings {
    private static Map<String, Class<?>> classes;
    private static Map<String, Map<String, ?>> methods;
	private static Map<String, Map<String,?>> fields;

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
        
        for(Map.Entry<String, Object> entry : mappings.getObject("mappings.fields").unwrapped().entrySet()) {
        	fields.put(entry.getKey(), (Map<String,?>) entry.getValue());
        }
    }

    public static Class<?> getClassByHumanName(String humanName) {
        if (classes == null) {
            load();
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
            return null;
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
            return null;
        }
    }
    
    public static Object call(Object object, String humanClassName, String humanMethodName, Object... args) {
        Method method = getMethod(humanClassName, humanMethodName);

        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object call(Object object, Class<?> clazz, String humanMethodName, Object... args) {
        Method method = getMethod(clazz, humanMethodName);

        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
