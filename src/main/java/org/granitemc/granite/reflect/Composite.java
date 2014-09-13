package org.granitemc.granite.reflect;

import javassist.util.proxy.MethodHandler;
import org.granitemc.granite.utils.Mappings;

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
    public List<HookListener> globalHooks;

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
        globalHooks.add(listener);
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
                            Object ret = hook.listener.activate(thisMethod, proceed, args);
                            if (ret != null) {
                                return ret;
                            }
                        }
                    }
                } catch (Mappings.MappingNotFoundException ignored) {
                }

                for (HookListener listener : globalHooks) {
                    Object ret = listener.activate(thisMethod, proceed, args);
                    if (ret != null) {
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
        }, false, types, args);
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
}
