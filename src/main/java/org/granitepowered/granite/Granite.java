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

package org.granitepowered.granite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javassist.ClassPool;
import org.granitepowered.granite.impl.GraniteGameRegistry;
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.impl.plugin.GranitePluginManager;
import org.granitepowered.granite.impl.service.event.GraniteEventManager;
import org.granitepowered.granite.impl.service.scheduler.GraniteScheduler;
import org.granitepowered.granite.impl.text.action.GraniteTextAction;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.granitepowered.granite.util.json.EntityJson;
import org.granitepowered.granite.util.json.ItemStackJson;
import org.granitepowered.granite.util.json.MessageJson;
import org.granitepowered.granite.util.json.TextActionJson;
import org.slf4j.Logger;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.SimpleServiceManager;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.command.SimpleCommandService;
import org.spongepowered.api.service.event.EventManager;

import java.io.File;

import javax.inject.Inject;

public class Granite {

    public static Granite instance;
    final GranitePluginManager pluginManager;
    final GraniteGameRegistry gameRegistry;
    final GraniteEventManager eventManager;
    final CommandService commandService;
    final ServiceManager serviceManager;
    final GraniteScheduler scheduler;
    // Not injected directly; initialization is done after classes are rewritten
    GraniteServer server;
    String version;
    String apiVersion;
    MinecraftVersion minecraftVersion;
    ServerConfig serverConfig;
    ClassPool classPool;
    Logger logger;
    Gson gson;
    File classesDir;

    @Inject
    public Granite(GranitePluginManager pluginManager,
                   GraniteGameRegistry gameRegistry,
                   GraniteEventManager eventManager, GraniteScheduler scheduler) {
        this.pluginManager = pluginManager;
        this.gameRegistry = gameRegistry;
        this.eventManager = eventManager;
        this.scheduler = scheduler;
        this.serviceManager = new SimpleServiceManager(pluginManager);
        this.commandService = new SimpleCommandService(pluginManager);
        version = "UNKNOWN";
    }

    public static Granite getInstance() {
        return instance;
    }

    public void createGson() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(GraniteMessage.class, new MessageJson());
        builder.registerTypeAdapter(GraniteTextAction.class, new TextActionJson());
        builder.registerTypeAdapter(GraniteEntity.class, new EntityJson());
        builder.registerTypeAdapter(GraniteItemStack.class, new ItemStackJson());
        gson = builder.create();
    }

    public String getVersion() {
        return version;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public MinecraftVersion getMinecraftVersion() {
        return minecraftVersion;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public GraniteGameRegistry getGameRegistry() {
        return gameRegistry;
    }

    public Logger getLogger() {
        return logger;
    }

    public GraniteServer getServer() {
        return server;
    }

    public Gson getGson() {
        return gson;
    }

    public ClassPool getClassPool() {
        return classPool;
    }

    public File getClassesDir() {
        return classesDir;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public static void error(String message, Throwable t) {
        instance.logger.error(message, t);
    }

    public static void error(Throwable t) {
        error("We did a boo-boo :'(", t);
    }

    public GraniteScheduler getScheduler() {
        return scheduler;
    }
}
