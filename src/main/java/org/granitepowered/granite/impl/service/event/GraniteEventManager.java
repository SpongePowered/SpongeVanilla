/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.spongepowered.api.event.player.PlayerChatEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.Subscribe;

import java.lang.reflect.Method;
import java.util.Map;

public class GraniteEventManager implements EventManager {
    Multimap<Class<? extends Event>, GraniteEventHandler> handlers = MultimapBuilder.SetMultimapBuilder.hashKeys().hashSetValues().build();

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

                GraniteEventHandler handler = new GraniteEventHandler(obj, type, container, annotation.order(), annotation.ignoreCancelled());
                handlers.put(type, handler);
            }
        }
    }

    @Override
    public void unregister(Object obj) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean post(Event event) {
        throw new NotImplementedException("");
    }
}
