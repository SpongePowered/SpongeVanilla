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

package org.granitepowered.granite.impl.service.event;

import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.event.GraniteEvent;
import org.granitepowered.granite.impl.guice.PluginScope;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.event.Cancellable;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraniteEventManager implements EventManager {

    Multimap<Class<? extends Event>, GraniteEventHandler> handlers = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();

    private PluginScope pluginScope;

    @Inject
    public GraniteEventManager(PluginScope pluginScope) {
        this.pluginScope = pluginScope;
    }

    @Override
    public void register(Object plugin, Object obj) {
        PluginContainer container = Granite.getInstance().getPluginManager().fromInstance(plugin).or(new Supplier<PluginContainer>() {
            @Override
            public PluginContainer get() {
                throw new IllegalArgumentException("\"plugin\" is not a plugin instance");
            }
        });

        Class<?> objClass = obj.getClass();

        for (Method m : objClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                Subscribe annotation = m.getAnnotation(Subscribe.class);

                if (m.getParameterTypes().length != 1) {
                    throw new IllegalArgumentException("A handler method should only have one parameter");
                }

                Class<? extends Event> type = (Class<? extends Event>) m.getParameterTypes()[0];
                if (type.getName().startsWith("Granite")) {
                    type = (Class<? extends Event>) type.getInterfaces()[0];
                }

                GraniteEventHandler handler = new GraniteEventHandler(obj, type, container, annotation.order(), annotation.ignoreCancelled(), m);
                handlers.put(type, handler);
            }
        }
    }

    @Override
    public void unregister(Object obj) {

        Class<?> objClass = obj.getClass();

        for (Method m : objClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                Subscribe annotation = m.getAnnotation(Subscribe.class);

                if (m.getParameterTypes().length != 1) {
                    throw new IllegalArgumentException("A handler method should only have one parameter");
                }

                Class<? extends Event> type = (Class<? extends Event>) m.getParameterTypes()[0];
                if (type.getName().startsWith("Granite")) {
                    type = (Class<? extends Event>) type.getInterfaces()[0];
                }
                GraniteEventHandler handler = (GraniteEventHandler) handlers.get(type).toArray()[0];
                handlers.remove(type, handler);
            }
        }
    }

    @Override
    public boolean post(Event event) {
        Set<GraniteEventHandler> unfilteredHandlers = new HashSet<>();

        for (Map.Entry<Class<? extends Event>, GraniteEventHandler> entry : handlers.entries()) {
            if (entry.getKey().isAssignableFrom(event.getClass())) {
                unfilteredHandlers.add(entry.getValue());
            }
        }

        if (event instanceof GraniteEvent) ((GraniteEvent) event).isModifiable = false;
        if (event instanceof GraniteEvent) ((GraniteEvent) event).isCancellable = false;
        postEventWithOrder(unfilteredHandlers, event, Order.PRE);
        postEventWithOrder(unfilteredHandlers, event, Order.AFTER_PRE);

        if (event instanceof GraniteEvent) ((GraniteEvent) event).isCancellable = true;
        postEventWithOrder(unfilteredHandlers, event, Order.FIRST);

        if (event instanceof GraniteEvent) ((GraniteEvent) event).isModifiable = true;
        postEventWithOrder(unfilteredHandlers, event, Order.EARLY);
        postEventWithOrder(unfilteredHandlers, event, Order.DEFAULT);
        postEventWithOrder(unfilteredHandlers, event, Order.LATE);

        if (event instanceof GraniteEvent) ((GraniteEvent) event).isModifiable = false;
        postEventWithOrder(unfilteredHandlers, event, Order.LAST);

        if (event instanceof GraniteEvent) ((GraniteEvent) event).isCancellable = false;
        postEventWithOrder(unfilteredHandlers, event, Order.BEFORE_POST);
        postEventWithOrder(unfilteredHandlers, event, Order.POST);

        return !(event instanceof Cancellable) || ((Cancellable) event).isCancelled();
    }

    public void postEventWithOrder(Set<GraniteEventHandler> unfilteredHandlers, Event event, Order order) {
        for (GraniteEventHandler handler : unfilteredHandlers) {
            try {
                pluginScope.enter(handler.getPluginContainer());

                if (handler.getOrder() == order) {
                    if (!((GraniteEvent) event).cancelled || !handler.isIgnoreCancelled()) {
                        try {
                            handler.getMethodHandle().invoke(handler.getInstance(), event);
                        } catch (Throwable throwable) {
                            Granite.error(throwable);
                        }
                    }
                }
            } finally {
                pluginScope.exit();
            }
        }
    }
}
