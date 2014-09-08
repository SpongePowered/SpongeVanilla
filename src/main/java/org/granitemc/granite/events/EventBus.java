package org.granitemc.granite.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventBus {
    /**
     * If this is set to false, errors/methods found won't be logged.
     */
    private boolean silent;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface EventSubscribed {

    }

    /**
     * All the handlers
     */
    private List<Class<?>> handlers;

    /**
     * Posts an event
     * @param e the event to post
     * @throws IllegalAccessException if the found valid event handler is private
     * @throws IllegalArgumentException if the found valid event handler doesn't have a single AlfheimEvent param
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws InstantiationException if an instantiation problem occurs
     */
    public void post(Event e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (handlers != null) {
            List<Method> processed = new ArrayList<Method>(100000);
            for (Class<? > clazz : handlers)  {
                if (!silent)
                    System.out.println(String.format("Found handler class : %s", clazz));

                for (Method m : clazz.getMethods()) {
                    if (m.isAnnotationPresent(EventBus.EventSubscribed.class) && !processed.contains(m))  {
                        if (!silent)
                            System.out.println(String.format("The found declared  %s in handler %s was valid", m, clazz));

                        m.invoke(clazz.newInstance(), e);
                        processed.add(m);
                    } else {

                    }
                }
            }
        } else {
            throw new IllegalArgumentException("This EventBus was not initialized.");
        }
    }

    /**
     * Registers an event handler
     * @param eventHandler the event handler
     */
    public void register(Class<?> eventHandler) {
        if (handlers != null)
            handlers.add(eventHandler);
        else
            throw new IllegalArgumentException("This EventBus was not initialized.");
    }

    /**
     * Unregisters an event handler
     * @param eventHandler the event handler
     */
    public void unregister(Class<?> eventHandler) {
        if (handlers != null && !handlers.isEmpty()) {
            handlers.remove(handlers.indexOf(eventHandler));
        } else {
            throw new IllegalArgumentException("This EventBus was not initialized.");
        }
    }

    /**
     * Creates a new event bus with the given list of handlers
     * @param handlers the handlers list
     */
    public EventBus(List<Class<?>> handlers) {
        this.handlers = handlers;
    }
}
