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

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import org.apache.commons.lang3.StringUtils;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.Proxy;
import org.granitepowered.granite.bytecode.ProxyCallbackInfo;
import org.granitepowered.granite.impl.event.message.GraniteCommandEvent;
import org.granitepowered.granite.mc.MC;
import org.spongepowered.api.service.command.SimpleCommandService;
import org.spongepowered.api.util.command.CommandSource;

import java.util.Arrays;

public class CommandHandlerClass extends BytecodeClass {

    public CommandHandlerClass() {
        super("CommandHandler");
    }

    @Proxy(methodName = "executeCommand")
    public Object executeCommand(ProxyCallbackInfo info) throws Throwable {
        String fullCommand = (String) info.getArguments()[1];

        if (fullCommand.startsWith("/")) {
            fullCommand = fullCommand.substring(1);
        }

        String[] commandArgs = fullCommand.split(" ");
        String commandName = commandArgs[0];
        commandArgs = Arrays.copyOfRange(commandArgs, 1, commandArgs.length);

        CommandSource sender = wrap((MC) info.getArguments()[0]);

        GraniteCommandEvent event = new GraniteCommandEvent(commandName, StringUtils.join(commandArgs, " "), sender);
        Granite.getInstance().getEventManager().post(event);

        // DO NOT MERGE THESE BLOCKS AS THIS CAUSES ISSUES!!!
        if (!event.isCancelled()) {
            SimpleCommandService dispatcher = (SimpleCommandService) Granite.getInstance().getCommandDispatcher();
            event.isCancellable = true;
            dispatcher.onCommandEvent(event);
        }

        if (!event.isCancelled()) {
            return info.callback(info.getArguments());
        } else {
            return 0;
        }
    }
}
