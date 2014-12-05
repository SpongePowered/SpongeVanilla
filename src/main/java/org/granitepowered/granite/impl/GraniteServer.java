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

package org.granitepowered.granite.impl;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.ProxyComposite;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.service.scheduler.Scheduler;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.world.World;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.*;

public class GraniteServer extends ProxyComposite implements Game, Server {
    public static String version;

    public GraniteServer() {
        super(Mappings.getClass("DedicatedServer"), new Class[]{File.class}, new File("worlds/"));
        invoke("startServerThread");
    }

    @Override
    public Platform getPlatform() {
        return Platform.SERVER;
    }

    @Override
    public Optional<Server> getServer() {
        return Optional.of((Server) this);
    }

    @Override
    public PluginManager getPluginManager() {
        return Granite.instance.getPluginManager();
    }

    @Override
    public EventManager getEventManager() {
        throw new NotImplementedException("");
    }

    @Override
    public GameRegistry getRegistry() {
        return Granite.instance.getGameRegistry();
    }

    @Override
    public ServiceManager getServiceManager() {
        throw new NotImplementedException("");
    }

    @Override
    public Scheduler getScheduler() {
        throw new NotImplementedException("");
    }

    @Override
    public CommandService getCommandDispatcher() {
        throw new NotImplementedException("");
    }

    @Override
    public String getAPIVersion() {
        return "Sponge v1.1-SNAPSHOT";
    }

    @Override
    public String getImplementationVersion() {
        return "Granite " + version;
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        Set<Player> ret = new HashSet<>();

        for (Object playerEntity : (List) fieldGet(getSCM(), "playerEntityList")) {
            ret.add((Player) MinecraftUtils.wrapComposite(playerEntity));
        }

        return ret;
    }

    @Override
    public int getMaxPlayers() {
        return (int) fieldGet(getSCM(), "maxPlayers");
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        for (Player player : getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) {
                return Optional.of(player);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<Player> getPlayer(String s) {
        for (Player player : getOnlinePlayers()) {
            if (player.getName().equals(s)) {
                return Optional.of(player);
            }
        }
        return Optional.absent();
    }

    @Override
    public Collection<World> getWorlds() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<World> getWorld(UUID uuid) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<World> getWorld(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public void broadcastMessage(Message<?> message) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<InetSocketAddress> getBoundAddress() {
        // TODO: Check if this is possible to get
        //return Optional.fromNullable(new InetSocketAddress((String) fieldGet("hostname"), (int) fieldGet("port")));
        throw new NotImplementedException("");
    }

    @Override
    public boolean hasWhitelist() {
        return (boolean) fieldGet(getSCM(), "whiteListEnforced");
    }

    @Override
    public void setHasWhitelist(boolean b) {
        fieldSet(getSCM(), "whiteListEnforced", b);
    }

    @Override
    public boolean getOnlineMode() {
        return (boolean) fieldGet("onlineMode");
    }

    @Override
    public Message.Text getMOTD() {
        throw new NotImplementedException("");
    }

    private Object getSCM() {
        return fieldGet("serverConfigManager");
    }
}
