package org.granitemc.granite.reflect;

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.utils.Mappings;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ServerComposite extends Composite {
    public static ServerComposite init() {
        Mappings.invoke(null, "n.m.init.Bootstrap", "func_151354_b");

        return new ServerComposite(new File("."));
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
            public Object activate(Method method, Method proxyCallback, Object[] args) {
                // This is needed, for some reason. I don't know. Ask Jason.
                if (method.getReturnType().equals(Mappings.getClass("n.m.server.MinecraftServer"))) {
                    return parent;
                }
                return null;
            }
        });

        // Start this baby
        invoke("n.m.server.MinecraftServer", "startServerThread");
    }
}
