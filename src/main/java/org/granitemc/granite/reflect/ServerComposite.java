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

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.utils.Mappings;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ServerComposite extends Composite implements CommandSender {
    public static ServerComposite instance;

    public static ServerComposite init() {
        Mappings.invoke(null, "n.m.init.Bootstrap", "func_151354_b");

        return instance = new ServerComposite(new File("."));
    }

    public ServerComposite(File worldsLocation) {
        super(Mappings.getClass("n.m.server.dedicated.DedicatedServer"), worldsLocation);

        // Inject logger, I don't think this is needed but I'll do it anyway just to be on the safe side
        Field loggerField = Mappings.getField("n.m.server.MinecraftServer", "logger");
        ReflectionUtils.forceStaticAccessible(loggerField);
        try {
            loggerField.set(null, GraniteAPI.getLogger());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // Create command composite
        CommandComposite commandComposite = new CommandComposite();

        // Inject command composite
        Field commandManagerField = Mappings.getField("n.m.server.MinecraftServer", "commandManager");
        ReflectionUtils.forceStaticAccessible(commandManagerField);
        try {
            commandManagerField.set(this.parent, commandComposite.parent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        addHook(new HookListener() {
            @Override
            public Object activate(Method method, Method proxyCallback, Hook hook, Object[] args) {
                // This is needed, for some reason. I don't know. Ask Jason.
                if (method.getReturnType().equals(Mappings.getClass("n.m.server.MinecraftServer"))) {
                    hook.setWasHandled(true);
                    return parent;
                }
                return null;
            }
        });

        // Start this baby
        invoke("n.m.server.MinecraftServer", "startServerThread");
    }

    @Override
    public String getName() {
        return (String) invoke("n.m.command.ICommandSender", "getName");
    }

    @Override
    public void sendMessage(String message) {
        GraniteAPI.getLogger().info(message);
    }
}
