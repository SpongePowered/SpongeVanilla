package org.granitemc.granite.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * License (MIT)
 *
 * Copyright (c) 2014. avarisc
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

public class EventBus {

    /**
     * If this is set to false, errors/methods found won't be logged.
     */
    private boolean silent;

    /**
     * All the handlers
     */
    private List<Class<?>> handlers;

    /**
     * Creates a new event bus with the given list of handlers
     *
     * @param handlers the handlers list
     */
    public EventBus(List<Class<?>> handlers) {
        this.handlers = handlers;
    }

    /**
     * Posts an event
     *
     * @param e the event to post
     * @throws IllegalAccessException    if the found valid event handler is private
     * @throws IllegalArgumentException  if the found valid event handler doesn't have a single AlfheimEvent param
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws InstantiationException    if an instantiation problem occurs
     */
    public void post(Event e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (handlers != null) {
            List<Method> processed = new ArrayList<Method>(100000);
            for (Class<?> clazz : handlers) {
                if (!silent)
                    System.out.println(String.format("Found handler class : %s", clazz));

                for (Method m : clazz.getMethods()) {
                    if (m.isAnnotationPresent(EventBus.EventSubscribed.class) && !processed.contains(m)) {
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
     *
     * @param eventHandler the event handler
     */
    public void register(Class<?> eventHandler) {
        if (handlers != null) {
            handlers.add(eventHandler);
        } else {
            throw new IllegalArgumentException("This EventBus was not initialized.");
        }
    }

    /**
     * Unregisters an event handler
     *
     * @param eventHandler the event handler
     */
    public void unregister(Class<?> eventHandler) {
        if (handlers != null && !handlers.isEmpty()) {
            handlers.remove(handlers.indexOf(eventHandler));
        } else {
            throw new IllegalArgumentException("This EventBus was not initialized.");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface EventSubscribed {
    }
}
