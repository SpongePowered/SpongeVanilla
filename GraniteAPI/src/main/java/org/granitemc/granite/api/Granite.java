package org.granitemc.granite.api;

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

import org.apache.logging.log4j.Logger;
import org.granitemc.granite.api.block.ItemType;
import org.granitemc.granite.api.event.EventQueue;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.plugin.PluginContainer;

import java.io.File;
import java.util.List;

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class Granite {
    // Set dynamically via reflection at runtime
    private static API impl;

    /**
     * @see API#getPlugin(String)
     */
    public static PluginContainer getPlugin(String name) {
        return impl.getPlugin(name);
    }

    /**
     * @see API#getPlugins()
     */
    public static List<PluginContainer> getPlugins() {
        return impl.getPlugins();
    }

    /**
     * @see API#getPlugin(Object)
     */
    public static PluginContainer getPlugin(Object plugin) {
        return impl.getPlugin(plugin);
    }

    /**
     * @see API#getPlugin(Class)
     */
    public static PluginContainer getPlugin(Class<?> pluginClass) {
        return impl.getPlugin(pluginClass);
    }

    /**
     * @see API#loadPluginFromJar(java.io.File)
     */
    public static void loadPluginFromJar(File file) {
        impl.loadPluginFromJar(file);
    }

    /**
     * @see API#getLogger()
     */
    public static Logger getLogger() {
        return impl.getLogger();
    }

    /**
     * @see API#getServer()
     */
    public static Server getServer() {
        return impl.getServer();
    }

    /**
     * @see API#getEventQueue()
     */
    public static EventQueue getEventQueue() {
        return impl.getEventQueue();
    }

    /**
     * @see API#createItemStack(org.granitemc.granite.api.block.ItemType, int)
     */
    public static ItemStack createItemStack(ItemType type, int amount) {
        return impl.createItemStack(type, amount);
    }
}
