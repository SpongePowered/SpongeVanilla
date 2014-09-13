package org.granitemc.granite.reflect;

import java.lang.reflect.Method;

public interface HookListener {
    /**
     * Called when a hook is activated (the hooked method is called)
     * @param method The method that was called
     * @param proxyCallback A method which should be called in order to call the vanilla method (NOT method!)
     * @param args The arguments passed to the method
     * @return The object that (should be) returned from the method, or null for the return value to be ignored, the method passed on to the next hook, and ultimately, the original method.
     */
    public Object activate(Method method, Method proxyCallback, Object[] args);
}
