package org.granitemc.granite.reflect.composite;

import javassist.util.proxy.MethodHandler;
import org.granitemc.granite.reflect.ReflectionUtils;
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * **************************************************************************************
 * License (MIT)
 * <p/>
 * Copyright (c) 2014. Granite Team
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * **************************************************************************************
 */

public class ProxyComposite extends Composite {
    private Map<String, List<Hook>> hooks;
    private List<Hook> globalHooks;

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
                try {
                    if (hooks.containsKey(Mappings.getMethodSignature(thisMethod))) {
                        for (Hook hook : hooks.get(Mappings.getMethodSignature(thisMethod))) {
                            Object ret = hook.listener.activate(self, thisMethod, proceed, hook, args);
                            if (hook.wasHandled) {
                                return ret;
                            }
                        }
                    }
                } catch (Mappings.MappingNotFoundException ignored) {
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
                    e.getCause().printStackTrace();
                }
                return null;
            }
        }, createIdentical, constructorArgTypes, constructorArgs);
    }

    /*public ProxyComposite(Object parent, Object... args) {
        this(parent, ReflectionUtils.getTypes(args), args);
    }*/
}
