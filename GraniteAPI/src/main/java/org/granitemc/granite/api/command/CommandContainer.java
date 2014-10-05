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

/**
 *
 */
package org.granitemc.granite.api.command;

import org.granitemc.granite.api.plugin.PluginContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandContainer {
    private Command annotation;
    private Method method;

    private Object instance;

    private PluginContainer plugin;

    public CommandContainer(PluginContainer plugin, Object instance, Method method) {
        annotation = method.getAnnotation(Command.class);
        this.method = method;

        this.plugin = plugin;
        this.instance = instance;
    }

    /**
     * Returns the aliases of this command, these have a lower priority than the name
     */
    public String[] getAliases() {
        return annotation.aliases();
    }

    /**
     * Returns the name of this command (i.e. what's just after the slash)
     */
    public String getName() {
        return annotation.name();
    }

    /**
     * Returns the usage info of this command, will be used in /help
     */
    public String getInfo() {
        return annotation.info();
    }

    /**
     * Invokes the command handler with a {@link org.granitemc.granite.api.command.CommandInfo}
     *
     * @param info The command info
     */
    public void invoke(CommandInfo info) {
        try {
            method.invoke(instance, info);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        }
    }

    /**
     * Returns the instance of the command handler this command will be called on
     */
    public Object getInstance() {
        return instance;
    }

    /**
     * Returns the plugin that registered the command
     */
    public PluginContainer getPlugin() {
        return plugin;
    }
}
