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
import org.granitemc.granite.api.chat.ChatComponentBuilder;
import org.granitemc.granite.api.event.EventQueue;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.api.plugin.PluginContainer;

import java.io.File;
import java.util.List;

public interface API {
    /**
     * Returns a plugin by name
     * @param name The name
     */
    PluginContainer getPluginContainer(String name);

    /**
     * Returns a plugin by main class object
     * @param plugin The main class object
     */
    PluginContainer getPluginContainer(Object plugin);

    /**
     * Returns a plugin by plugin class
     * @param pluginClass The plugin class
     */
    PluginContainer getPluginContainerByClass(Class<?> pluginClass);

    /**
     * Loads a plugin from a .jar
     * @param file The .jar file to load from
     */
    void loadPluginFromJar(File file);

    /**
     * Returns all the currently loaded plugins
     */
    List<PluginContainer> getPlugins();

    /**
     * Returns the log4j logger
     */
    Logger getLogger();

    /**
     * Returns a {@link org.granitemc.granite.api.chat.ChatComponentBuilder}
     */
    ChatComponentBuilder getChatComponentBuilder();

    /**
     * Creates an {@link org.granitemc.granite.api.item.IItemStack}
     * @param type The {@link org.granitemc.granite.api.block.ItemType} to create an ItemStack of
     */
    IItemStack createItemStack(ItemType type, int amount);

    /**
     * Returns the {@link org.granitemc.granite.api.Server} of this server
     */
    Server getServer();

    /**
     * Returns the server {@link org.granitemc.granite.api.event.EventQueue}
     */
    EventQueue getEventQueue();
}
