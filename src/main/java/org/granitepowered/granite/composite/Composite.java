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

package org.granitepowered.granite.composite;

import org.granitepowered.granite.mc.MCInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Composite<T extends MCInterface> {
    public static Map<Class<? extends Composite>, Map<Object, Composite>> instanceMap = new HashMap<>();
    public T obj;

    public Composite(Object obj) {
        this.obj = (T) obj;
    }

    public Composite(T obj) {
        this.obj = obj;
    }

    public Composite(Class<?> clazz, Class<?>[] constructorArgTypes, Object... constructorArgs) {
        try {
            this.obj = (T) clazz.getConstructor(constructorArgTypes).newInstance(constructorArgs);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /*public Composite(Class<?> clazz, Object... constructorArgs) {
        this(clazz, ReflectionUtils.getTypes(constructorArgs), constructorArgs);
    }*/

    /*public Object invoke(Object instance, MethodHandle m, Object... args) {
        return Mappings.invoke(instance, m, args);
    }

    public Object invoke(MethodHandle m, Object... args) {
        return invoke(obj, m, args);
    }

    public Object invoke(String methodName, Object... args) {
        return invoke(Mappings.getMethod(clazz, methodName), args);
    }*/

    /*public Object invoke(String className, SignatureParser.MethodSignature methodSignature, Object... args) {
        return invoke(Mappings.getMethod(className, methodSignature), args);
    }*/

    /*public Object fieldGet(Object instance, String fieldName) {
        try {
            return Mappings.getField(instance.getClass(), fieldName).get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object fieldGet(String fieldName) {
        return fieldGet(obj, fieldName);
    }


    public void fieldSet(Object instance, String fieldName, Object value) {
        try {
            Mappings.getField(instance.getClass(), fieldName).set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void fieldSet(String fieldName, Object value) {
        fieldSet(obj, fieldName, value);
    }*/


    public static <T extends Composite> T new_(MCInterface parent, Class<T> compositeType) {
        if (!instanceMap.containsKey(compositeType)) {
            instanceMap.put(compositeType, new HashMap<Object, Composite>());
        }
        if (instanceMap.get(compositeType).containsKey(parent)) {
            return (T) instanceMap.get(compositeType).get(parent);
        } else {
            T composite = null;
            try {
                for (Constructor c : compositeType.getConstructors()) {
                    if (c.getParameterTypes().length == 1 && c.getParameterTypes()[0].isAssignableFrom(parent.getClass())) {
                        composite = (T) c.newInstance(parent);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            instanceMap.get(compositeType).put(parent, composite);
            return composite;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Composite composite = (Composite) o;

        return obj.equals(composite.obj);
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }
}
