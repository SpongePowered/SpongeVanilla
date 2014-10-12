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

import javassist.util.proxy.MethodHandler;
import org.granitemc.granite.reflect.ReflectionUtils;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.SignatureParser;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyComposite extends Composite {
    private Map<String, List<Hook>> hooks;
    private List<Hook> globalHooks;

    public String createSignature(MethodHandle methodHandle) {
        String name = Mappings.getMethodName(clazz, methodHandle);

        return name + methodHandle.type().toMethodDescriptorString();
    }

    public void addHook(MethodHandle methodHandle, HookListener listener) {
        String key = createSignature(methodHandle);
        if (!hooks.containsKey(key)) {
            hooks.put(key, new ArrayList<Hook>());
        }

        Hook hook = new Hook();
        hook.listener = listener;
        hook.method = methodHandle;
        hooks.get(key).add(hook);
    }

    public void addHook(String methodName, HookListener listener) {
        addHook(Mappings.getMethod(parent.getClass().getSuperclass(), methodName), listener);
    }

    public void addHook(HookListener listener) {
        Hook hook = new Hook();
        hook.listener = listener;
        globalHooks.add(hook);
    }

    public ProxyComposite(Class<?> clazz, Class<?>[] constructorArgTypes, Object... constructorArgs) {
        this(clazz, false, constructorArgTypes, constructorArgs);
    }

    public ProxyComposite(Object parent, boolean createIdentical, Class<?>[] constructorArgTypes, Object... constructorArgs) {
        super(ReflectionUtils.extractClass(parent));

        if (parent instanceof Class) {
            createIdentical = false;
        }

        hooks = new HashMap<>();
        globalHooks = new ArrayList<>();

        this.parent = ReflectionUtils.createProxy(parent, new MethodHandler() {
            @Override
            // This method may be invoked thousands of times per second... MAKE IT FAST
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                thisMethod.setAccessible(true);
                try {
                    MethodHandle mh = MethodHandles.lookup().unreflect(thisMethod);
                    if (hooks.containsKey(createSignature(mh))) {
                        for (Hook hook : hooks.get(createSignature(mh))) {
                            Object ret = hook.listener.activate(self, thisMethod, proceed, hook, args);
                            if (hook.wasHandled) {
                                return ret;
                            }
                        }
                    }

                    for (Hook hook : globalHooks) {
                        Object ret = hook.listener.activate(self, thisMethod, proceed, hook, args);
                        if (hook.wasHandled) {
                            return ret;
                        }
                    }

                    try {
                        return proceed.invoke(self, args);
                    } catch (InvocationTargetException e) {
                        throw e.getCause();
                    }
                } catch (Throwable t) {
                    if (!Mappings.getClass("ThreadQuickExitException").isInstance(t)) {
                        t.printStackTrace();
                    }
                    throw t;
                }
            }
        }, createIdentical, constructorArgTypes, constructorArgs);
    }

    /*public ProxyComposite(Object parent, Object... args) {
        this(parent, ReflectionUtils.getTypes(args), args);
    }*/
}
