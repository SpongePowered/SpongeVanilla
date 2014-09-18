package org.granitemc.granite.reflect.composite;

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

import java.lang.reflect.Method;

public interface HookListener {
    /**
     * Called when a hook is activated (the hooked method is called)
     *
     *
     * @param self          The self object
     * @param method        The method that was called
     * @param proxyCallback A method which should be called in order to call the vanilla method (NOT method!)
     * @param hook          The hook that was called. Use it to set whether the method was handled here or should be passed on.
     * @param args          The arguments passed to the method
     * @return The object that (should be) returned from the method. Will only be needed if the hook was handled.
     */
    public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args);
}
