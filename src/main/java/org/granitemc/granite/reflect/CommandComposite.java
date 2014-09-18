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

import org.apache.commons.lang3.ArrayUtils;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.command.CommandContainer;
import org.granitemc.granite.api.command.CommandInfo;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.api.plugin.PluginContainer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.reflect.Method;

public class CommandComposite extends ProxyComposite {
    public CommandComposite() {
        super(Mappings.getClass("n.m.command.ServerCommandManager"), new Class[]{});

        addHook("executeCommand(n.m.command.ICommandSender;String)", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                String[] commandArgs = ((String) args[1]).split(" ");

                CommandSender sender = (CommandSender) MinecraftUtils.wrap(args[0]);

                for (PluginContainer plugin : Granite.getPlugins()) {
                    for (CommandContainer command : plugin.getCommands().values()) {
                        if (command.getName().equalsIgnoreCase(commandArgs[0])) {
                            dispatchCommand(command, sender, commandArgs);
                            hook.setWasHandled(true);
                        }
                    }
                }

                for (PluginContainer plugin : Granite.getPlugins()) {
                    for (CommandContainer command : plugin.getCommands().values()) {
                        for (String alias : command.getAliases()) {
                            if (alias.equalsIgnoreCase(commandArgs[0])) {
                                dispatchCommand(command, sender, commandArgs);
                                hook.setWasHandled(true);

                            }
                        }
                    }
                }

                return 0;
            }
        });
    }

    public void dispatchCommand(CommandContainer command, CommandSender sender, String[] args) {
        CommandInfo info = new CommandInfo();
        info.commandSender = sender;
        info.args = ArrayUtils.subarray(args, 1, args.length);
        info.usedCommandName = args[0];
        info.command = command;

        command.invoke(info);
    }
}
