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

import com.google.common.collect.Lists;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Player;
import org.granitemc.granite.api.Server;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.utils.Mappings;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ServerComposite extends Composite implements Server, CommandSender {
    public static ServerComposite instance;

    private Object serverConfigurationManager;

    public static ServerComposite init() {
        Mappings.invoke(null, "n.m.init.Bootstrap", "func_151354_b");

        return instance = new ServerComposite(new File("."));
    }

    public ServerComposite(File worldsLocation) {
        super(Mappings.getClass("n.m.server.dedicated.DedicatedServer"), true, worldsLocation);

        // Inject logger, I don't think this is needed but I'll do it anyway just to be on the safe side
        Field loggerField = Mappings.getField("n.m.server.MinecraftServer", "logger");
        ReflectionUtils.forceStaticAccessible(loggerField);
        try {
            loggerField.set(null, Granite.getLogger());
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

    public String getName() {
        return (String) invoke("n.m.command.ICommandSender", "getName");
    }

    public void sendMessage(String message) {
        Granite.getLogger().info(message);
    }

    public List<Player> getPlayers() {
        List<Player> ret = Lists.newArrayList();
        try {
            List<Object> playerObjs = (List<Object>) Mappings.getField("n.m.server.management.ServerConfigurationManager", "playerEntityList").get(getServerConfigurationManager());

            for (Object o : playerObjs) {
                Player p = new GranitePlayer(o);
                ret.add(p);
                // TODO: cache - so the instances are the same
            }

            return ret;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getServerConfigurationManager() {
        if (serverConfigurationManager == null) {
            // Get server config manager
            Field configurationManagerField = Mappings.getField("n.m.server.MinecraftServer", "configurationManager");
            configurationManagerField.setAccessible(true);
            try {
                serverConfigurationManager = configurationManagerField.get(parent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return serverConfigurationManager;
    }
}
