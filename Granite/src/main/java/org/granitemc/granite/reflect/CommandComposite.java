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

package org.granitemc.granite.reflect;

import org.apache.commons.lang3.ArrayUtils;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.command.CommandContainer;
import org.granitemc.granite.api.command.CommandInfo;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.plugin.PluginContainer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandComposite extends ProxyComposite {

    public CommandComposite() {
        super(Mappings.getClass("ServerCommandManager"), new Class[]{});

        addHook("executeCommand", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                String[] commandArgs = ((String) args[1]).split(" ");
                if (commandArgs[0].startsWith("/")) {
                    commandArgs[0] = commandArgs[0].substring(1);
                }

                CommandSender sender = (CommandSender) MinecraftUtils.wrap(args[0]);

                hook.setWasHandled(false);

                for (PluginContainer plugin : Granite.getPlugins()) {
                    for (CommandContainer command : plugin.getCommands().values()) {
                        if (command.getName().equalsIgnoreCase(commandArgs[0])) {
                            dispatchCommand(command, sender, commandArgs, args[0]);
                            hook.setWasHandled(true);
                            return 0;
                        }
                    }
                }

                if (!hook.getWasHandled()) {
                    for (PluginContainer plugin : Granite.getPlugins()) {
                        for (CommandContainer command : plugin.getCommands().values()) {
                            for (String alias : command.getAliases()) {
                                if (alias.equalsIgnoreCase(commandArgs[0])) {
                                    dispatchCommand(command, sender, commandArgs, args[0]);
                                    hook.setWasHandled(true);
                                    return 0;
                                }
                            }
                        }
                    }
                }

                return 0;
            }
        });
    }

    public void dispatchCommand(CommandContainer command, CommandSender sender, String[] args, Object nativeSender) {
        CommandInfo info = new CommandInfo();
        info.commandSender = sender;
        info.args = ArrayUtils.subarray(args, 1, args.length);
        info.usedCommandName = args[0];
        info.command = command;

        List<Player> targets = new ArrayList<>();
        for (int i = 0; i < info.args.length; i++) {
            String arg = info.args[i];
            for (Object nativeTarget : (List<Object>) Mappings.invoke(null, "PlayerSelector", "matchPlayers", nativeSender, arg, Mappings.getClass("EntityPlayerMP"))) {
                targets.add((Player) MinecraftUtils.wrap(nativeTarget));
                info.args = ArrayUtils.remove(info.args, i);
            }
        }

        if (targets.size() == 0) {
            if (sender instanceof Player) {
                targets.add((Player) sender);
            }
        }

        info.targets = targets;

        command.invoke(info);
    }
}
