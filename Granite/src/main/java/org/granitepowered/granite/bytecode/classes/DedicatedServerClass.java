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

package org.granitepowered.granite.bytecode.classes;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.CallbackInfo;
import org.granitepowered.granite.bytecode.CodeInsertionMode;
import org.granitepowered.granite.bytecode.Insert;
import org.granitepowered.granite.bytecode.Position;
import org.granitepowered.granite.bytecode.Proxy;
import org.granitepowered.granite.bytecode.ProxyCallbackInfo;
import org.granitepowered.granite.impl.event.state.GraniteServerAboutToStartEvent;
import org.granitepowered.granite.impl.event.state.GraniteServerStartedEvent;
import org.granitepowered.granite.impl.event.state.GraniteServerStartingEvent;
import org.granitepowered.granite.impl.event.state.GraniteServerStoppedEvent;
import org.granitepowered.granite.impl.event.state.GraniteServerStoppingEvent;

public class DedicatedServerClass extends BytecodeClass {

    public DedicatedServerClass() {
        super("DedicatedServer");
    }

    @Proxy(methodName = "startServer")
    public Object startServer(ProxyCallbackInfo info) throws Throwable {
        Object ret = info.callback();

        Granite.getInstance().getEventManager().post(new GraniteServerStartingEvent());
        Granite.getInstance().getEventManager().post(new GraniteServerStartedEvent());

        return ret;
    }

    @Proxy(methodName = "setConfigManager")
    public void setConfigManager(ProxyCallbackInfo info) throws Throwable {
        Granite.getInstance().getEventManager().post(new GraniteServerAboutToStartEvent());

        info.callback();
    }

    @Proxy(methodName = "stopServer")
    public void stopServer(ProxyCallbackInfo info) throws Throwable {
        Granite.getInstance().getEventManager().post(new GraniteServerStoppingEvent());

        info.callback();

        Granite.getInstance().getEventManager().post(new GraniteServerStoppedEvent());
    }

    @Insert(methodName = "run", mode = CodeInsertionMode.REPLACE, position = @Position(mode = Position.PositionMode.METHOD_CALL, value = "org.apache.logging.log4j.Logger#error(java.lang.String,java.lang.Throwable)"))
    public void run(CallbackInfo info) {
        Granite.error((Throwable) info.getCallArguments()[1]);
    }

    @Proxy(methodName = "tick")
    public void tick(ProxyCallbackInfo info) throws Throwable {
        Granite.getInstance().getScheduler().tick();

        info.callback();
    }
}