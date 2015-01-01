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

import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.Order;

import java.lang.reflect.Method;

public class GraniteEventHandler {
    private Object instance;
    private Class<? extends Event> eventType;
    private PluginContainer pluginContainer;
    private Order order;
    private boolean ignoreCancelled;
    private Method method;

    public GraniteEventHandler(Object instance, Class<? extends Event> eventType, PluginContainer pluginContainer, Order order, boolean ignoreCancelled, Method method) {
        this.instance = instance;
        this.eventType = eventType;
        this.pluginContainer = pluginContainer;
        this.order = order;
        this.ignoreCancelled = ignoreCancelled;
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Class<? extends Event> getEventType() {
        return eventType;
    }

    public void setEventType(Class<? extends Event> eventType) {
        this.eventType = eventType;
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public void setPluginContainer(PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }

    public void setIgnoreCancelled(boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
