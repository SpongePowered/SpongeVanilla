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
import org.granitemc.granite.api.event.Event;
import org.granitemc.granite.api.event.EventHandlerContainer;
import org.granitemc.granite.api.event.On;

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

    public PluginContainer(Class<?> clazz) {
        annotation = clazz.getAnnotation(Plugin.class);

        commands = new HashMap<>();
        events = new HashMap<>();

        this.mainClass = clazz;
    }

    public void instantiatePluginClass() {
        try {
            this.instance = mainClass.getConstructor(PluginContainer.class).newInstance(this);
        } catch (NoSuchMethodException e) {
            try {
                this.instance = mainClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        registerCommandHandler(instance);
        registerEventHandler(instance);
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
     * Returns the instance of the plugin class that was created on load
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
     * @param handler The handler to register, this can be any object, but should have at least one {@link org.granitemc.granite.api.event.On} annotation
     */
    public void registerEventHandler(Object handler) {
        Class<?> clazz = handler.getClass();

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(On.class)) {
                EventHandlerContainer evt = new EventHandlerContainer(this, handler, m);

                if (!events.containsKey(evt.getEventType())) {
                    events.put(evt.getEventType(), new ArrayList<EventHandlerContainer>());
                }

                events.get(evt.getEventType()).add(evt);
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
}
