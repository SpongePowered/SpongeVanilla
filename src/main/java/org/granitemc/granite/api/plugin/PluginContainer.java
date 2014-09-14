package org.granitemc.granite.api.plugin;

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

import org.granitemc.granite.api.command.Command;
import org.granitemc.granite.api.command.CommandContainer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class PluginContainer {
    private Plugin annotation;
    private Class<?> mainClass;
    private Object instance;

    private Map<String, CommandContainer> commands;

    public PluginContainer(Class<?> clazz) {
        annotation = clazz.getAnnotation(Plugin.class);
        this.mainClass = clazz;
        try {
            this.instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        commands = new HashMap<>();

        registerCommandHandler(instance);
    }

    public String getId() {
        return annotation.id();
    }

    public String getName() {
        return annotation.name();
    }

    public String getVersion() {
        return annotation.version();
    }

    public Object getInstance() {
        return instance;
    }

    public Class<?> getMainClass() {
        return mainClass;
    }

    public void registerCommandHandler(Object handler) {
        Class<?> clazz = handler.getClass();

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Command.class)) {
                CommandContainer command = new CommandContainer(this, handler, m);

                commands.put(command.getName(), command);
            }
        }
    }

    public Map<String, CommandContainer> getCommands() {
        return commands;
    }
}
