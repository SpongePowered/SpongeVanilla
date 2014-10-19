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

package org.granitemc.granite.api.plugin;

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.command.Command;
import org.granitemc.granite.api.command.CommandContainer;
import org.granitemc.granite.api.config.CfgFile;
import org.granitemc.granite.api.event.Event;
import org.granitemc.granite.api.event.EventHandlerContainer;
import org.granitemc.granite.api.event.On;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class PluginContainer {
    private Plugin annotation;
    private Class<?> mainClass;
    private Object instance;

    private Map<String, CommandContainer> commands;
    private Map<Class<? extends Event>, List<EventHandlerContainer>> events;

    private List<Method> onEnableHandlers;
    private List<Method> onDisableHandlers;

    private File dataDirectory;

    private CfgFile config;

    public PluginContainer(Class<?> clazz) {
        annotation = clazz.getAnnotation(Plugin.class);

        commands = new HashMap<>();
        events = new HashMap<>();

        this.mainClass = clazz;

        onEnableHandlers = new ArrayList<>();
        onDisableHandlers = new ArrayList<>();

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(OnEnable.class)) {
                if (m.getParameterCount() == 1 && m.getParameterTypes()[0] == PluginContainer.class) {
                    onEnableHandlers.add(m);
                } else {
                    Granite.getLogger().warn("Method " + m.getName() + " in " + clazz.getName() + "must take a single argument - a PluginContainer");
                }
            }

            if (m.isAnnotationPresent(OnDisable.class)) {
                if (m.getParameterCount() == 1 && m.getParameterTypes()[0] == PluginContainer.class) {
                    onDisableHandlers.add(m);
                } else {
                    Granite.getLogger().warn("Method " + m.getName() + " in " + clazz.getName() + "must take a single argument - a PluginContainer");
                }
            }
        }
    }

    public void enable() {
        try {
            this.instance = mainClass.getConstructor(PluginContainer.class).newInstance(this);
        } catch (NoSuchMethodException e) {
            try {
                this.instance = mainClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        registerCommandHandler(instance);
        registerEventHandler(instance);

        dataDirectory = new File(Granite.getServerConfig().getPluginDataDirectory(), getName());
        config = new CfgFile(new File(dataDirectory, "config.conf"));

        try {
            for (Method m : onEnableHandlers) {
                m.invoke(instance, this);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        try {
            for (Method m : onDisableHandlers) {
                m.invoke(instance, this);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the ID of this plugin
     */
    public String getId() {
        return annotation.id();
    }

    /**
     * Returns the name of this plugin
     */
    public String getName() {
        return annotation.name();
    }

    /**
     * Returns the version of this plugin
     */
    public String getVersion() {
        return annotation.version();
    }

    /**
     * Returns the instance of the plugin class that was created on read
     */
    public Object getInstance() {
        return instance;
    }

    /**
     * Returns the type of the plugin class
     */
    public Class<?> getMainClass() {
        return mainClass;
    }

    /**
     * Registers a command handler to this plugin
     *
     * @param handler The handler to register, this can be any object, but should have at least one {@link org.granitemc.granite.api.command.Command} annotation
     */
    public void registerCommandHandler(Object handler) {
        Class<?> clazz = handler.getClass();

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Command.class)) {
                CommandContainer command = new CommandContainer(this, handler, m);

                commands.put(command.getName(), command);
            }
        }
    }

    /**
     * Registers an event handler to this plugin
     *
     * @param handler The handler to register, this can be any object, but should have at least one {@link org.granitemc.granite.api.event.On} annotation
     */
    public void registerEventHandler(Object handler) {
        Class<?> clazz = handler.getClass();

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(On.class)) {
                EventHandlerContainer evt = new EventHandlerContainer(this, handler, m);

                if (m.getParameterCount() == 1 && m.getParameterTypes()[0] == evt.getEventType()) {

                    if (!events.containsKey(evt.getEventType())) {
                        events.put(evt.getEventType(), new ArrayList<EventHandlerContainer>());
                    }

                    events.get(evt.getEventType()).add(evt);
                } else {
                    Granite.getLogger().warn("Method " + m.getName() + " in " + clazz.getName() + " must take a single argument - a " + evt.getEventType().getName());
                }
            }
        }
    }

    /**
     * Returns a map of command names to commands
     */
    public Map<String, CommandContainer> getCommands() {
        return commands;
    }

    /**
     * Returns a map of event types to handlers
     */
    public Map<Class<? extends Event>, List<EventHandlerContainer>> getEvents() {
        return events;
    }

    public CfgFile getConfig() {
        return config;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }
}
