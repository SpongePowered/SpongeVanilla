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

import javassist.util.proxy.MethodHandler;
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Composite {
    public Object parent;
    public Class<?> clazz;

    public Map<String, List<Hook>> hooks;
    public List<Hook> globalHooks;

    public void addHook(String methodSignature, HookListener listener) {
        if (!hooks.containsKey(Mappings.expandShortcuts(methodSignature))) {
            hooks.put(Mappings.expandShortcuts(methodSignature), new ArrayList<Hook>());
        }

        Hook hook = new Hook();
        hook.listener = listener;
        hook.methodSignature = Mappings.expandShortcuts(methodSignature);
        hooks.get(Mappings.expandShortcuts(methodSignature)).add(hook);
    }

    public void addHook(HookListener listener) {
        Hook hook = new Hook();
        hook.listener = listener;
        globalHooks.add(hook);
    }

    public Composite(Object parent, Object... args) {
        hooks = new HashMap<>();
        globalHooks = new ArrayList<>();
        Class<?>[] types = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }

        this.clazz = ReflectionUtils.extractClass(parent);

        final Composite me = this;

        this.parent = ReflectionUtils.createProxy(parent, new MethodHandler() {
            @Override
            // This method may be invoked thousands of times per second... MAKE IT FAST
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                try {
                    if (hooks.containsKey(Mappings.getMethodSignature(thisMethod))) {
                        for (Hook hook : hooks.get(Mappings.getMethodSignature(thisMethod))) {
                            Object ret = hook.listener.activate(thisMethod, proceed, hook, args);
                            if (hook.wasHandled) {
                                return ret;
                            }
                        }
                    }
                } catch (Mappings.MappingNotFoundException ignored) {
                }

                for (Hook hook : globalHooks) {
                    Object ret = hook.listener.activate(thisMethod, proceed, hook, args);
                    if (hook.wasHandled) {
                        return ret;
                    }
                }

                try {
                    return proceed.invoke(self, args);
                } catch (InvocationTargetException e) {
                    e.getCause().printStackTrace();
                }
                return null;
            }
        }, parent == null, types, args);
    }

    public Object invoke(Object instance, Method m, Object... args) {
        try {
            return ReflectionUtils.invoke(instance, m, args);
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
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

    public void fieldSet(String fieldName, Object value){
        fieldSet(clazz, fieldName, value);
    }
}
