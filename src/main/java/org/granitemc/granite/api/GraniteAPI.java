/**
 * This file was created as part of Granite. <br>
 * @author matheus
 */
package org.granitemc.granite.api;

import org.granitemc.granite.events.EventBus;

import java.util.ArrayList;

public class GraniteAPI {

    private static GraniteAPI instance;
    private static EventBus eventbus;

    public static GraniteAPI instance() {
        if (instance == null) {
            instance = new GraniteAPI();
        }
        return instance;
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
        return isClassPlugin(class1) ? class1.getAnnotation(Plugin.class) : null;
    }

    public PluginContainer loadPlugin(Object obj) {
        return loadClassPlugin(obj.getClass());
    }

    public PluginContainer loadClassPlugin(Class<?> clazz) {
        PluginContainer ret = new PluginContainer();
        ret.loadFromPlugin(clazz);
        return ret;
    }

    public static EventBus eventBus() {
        if (eventbus == null) {
            eventbus = new EventBus(new ArrayList<Class<?>>());
        }
        return eventbus;
    }

}
