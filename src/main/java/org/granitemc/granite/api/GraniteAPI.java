package org.granitemc.granite.api;

import org.granitemc.granite.api.plugin.Plugin;
import org.granitemc.granite.api.plugin.PluginContainer;
import org.granitemc.granite.events.EventBus;

import java.util.ArrayList;

/**
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
 */

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class GraniteAPI {

    private static GraniteAPI instance;
    private static EventBus eventbus;

    public static GraniteAPI instance() {
        if (instance == null) {
            instance = new GraniteAPI();
        }
        return instance;
    }

    public static EventBus eventBus() {
        if (eventbus == null) {
            eventbus = new EventBus(new ArrayList<Class<?>>());
        }
        return eventbus;
    }

    public boolean isPlugin(Object obj) {
        return isClassPlugin(obj.getClass());
    }

    public boolean isClassPlugin(Class<?> class1) {
        return class1.getAnnotation(Plugin.class) != null;
    }

    public Plugin getPlugin(Object obj) {
        return getClassPlugin(obj.getClass());
    }

    public Plugin getClassPlugin(Class<?> class1) {
        return class1.getAnnotation(Plugin.class);
    }

    public PluginContainer loadPlugin(Object obj) {
        return loadClassPlugin(obj.getClass());
    }

    public PluginContainer loadClassPlugin(Class<?> clazz) {
        PluginContainer ret = new PluginContainer();
        ret.loadFromPlugin(clazz);
        return ret;
    }

}
