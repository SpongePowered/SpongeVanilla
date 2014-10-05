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

package org.granitemc.granite.reflect.composite;

import org.granitemc.granite.reflect.ReflectionUtils;
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class Composite {
    public static Map<Class<? extends Composite>, Map<Object, Composite>> instanceMap = new HashMap<>();
    public Object parent;
    public Class<?> clazz;

    public Composite(Object parent) {
        this.parent = parent;
        this.clazz = ReflectionUtils.extractClass(parent);
    }

    public Composite(Class<?> clazz, Class<?>[] constructorArgTypes, Object... constructorArgs) {
        try {
            this.parent = clazz.getConstructor(constructorArgTypes).newInstance(constructorArgs);
            this.clazz = clazz;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Composite(Class<?> clazz, Object... constructorArgs) {
        this(clazz, ReflectionUtils.getTypes(constructorArgs), constructorArgs);
    }

    public Object invoke(Object instance, Method m, Object... args) {
        try {
            return ReflectionUtils.invoke(instance, m, args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object invoke(Method m, Object... args) {
        return invoke(parent, m, args);
    }

    public Object invoke(String methodSignature, Object... args) {
        return invoke(Mappings.getMethod(clazz, methodSignature), args);
    }

    public Object invoke(String className, String methodSignature, Object... args) {
        return invoke(Mappings.getMethod(className, methodSignature), args);
    }

    public Object fieldGet(Object instance, Class<?> clazz, String fieldName) {
        try {
            return Mappings.getField(clazz, fieldName).get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object fieldGet(Class<?> clazz, String fieldName) {
        return fieldGet(parent, clazz, fieldName);
    }

    public Object fieldGet(String className, String fieldName) {
        return fieldGet(Mappings.getClass(className), fieldName);
    }

    public Object fieldGet(String fieldName) {
        return fieldGet(clazz, fieldName);
    }

    public void fieldSet(Object instance, Class<?> clazz, String fieldName, Object value) {
        try {
            Mappings.getField(clazz, fieldName).set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void fieldSet(Class<?> clazz, String fieldName, Object value) {
        fieldSet(parent, clazz, fieldName, value);
    }

    public void fieldSet(String className, String fieldName, Object value) {
        fieldSet(Mappings.getClass(className), fieldName, value);
    }

    public void fieldSet(String fieldName, Object value) {
        fieldSet(clazz, fieldName, value);
    }

    public static <T extends Composite> T new_(Object parent, Class<T> compositeType) {
        if (!instanceMap.containsKey(compositeType)) {
            instanceMap.put(compositeType, new HashMap<Object, Composite>());
        }
        if (instanceMap.get(compositeType).containsKey(parent)) {
            return (T) instanceMap.get(compositeType).get(parent);
        } else {
            T composite = null;
            try {
                composite = compositeType.getConstructor(Object.class).newInstance(parent);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            instanceMap.get(compositeType).put(compositeType, composite);
            return composite;
        }
    }
}
