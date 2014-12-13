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
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.*;
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

import static org.granitepowered.granite.utils.MinecraftUtils.*;

public class GraniteServer extends Composite<MCServer> implements Game, Server {
    public static String version;

    public GraniteServer() {
        super(Mappings.getClass("DedicatedServer"), new Class[]{File.class}, new File("worlds/"));
        obj.startServerThread();
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
        // TODO
        throw new NotImplementedException("");
    }

    @Override
    public GameRegistry getRegistry() {
        return Granite.instance.getGameRegistry();
    }

    @Override
    public ServiceManager getServiceManager() {
        // TODO
        throw new NotImplementedException("");
    }

    @Override
    public Scheduler getScheduler() {
        // TODO
        throw new NotImplementedException("");
    }

    @Override
    public CommandService getCommandDispatcher() {
        // TODO
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

        for (MCEntityPlayerMP playerEntity : getSCM().fieldGet$playerEntityList()) {
            ret.add((Player) wrap(playerEntity));
        }

        return ret;
    }

    @Override
    public int getMaxPlayers() {
        return getSCM().fieldGet$maxPlayers();
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
        // See MinecraftServer.worldServers, and for expansion, see MinecraftServer line 270 (this.worldServers = new WorldServer[3];)
        return ImmutableList.<World>copyOf(Iterables.transform(Arrays.asList(obj.fieldGet$worldServers()), new MinecraftUtils.WrapFunction<GraniteWorld>()));
    }

    @Override
    public Optional<World> getWorld(final UUID uuid) {
        return Optional.fromNullable(Iterables.find(getWorlds(), new Predicate<World>() {
            public boolean apply(World input) {
                return input.getUniqueID().equals(uuid);
            }
        }, null));
    }

    @Override
    public Optional<World> getWorld(final String s) {
        return Optional.fromNullable(Iterables.find(getWorlds(), new Predicate<World>() {
            public boolean apply(World input) {
                return input.getName().equals(s);
            }
        }, null));
    }

    @Override
    public int getRunningTimeTicks() {
        // TODO: add
        throw new NotImplementedException("");
    }

    @Override
    public void broadcastMessage(Message message) {
        // TODO
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
        return getSCM().fieldGet$whitelistEnforced();
    }

    @Override
    public void setHasWhitelist(boolean b) {
       getSCM().fieldSet$whitelistEnforced(b);
    }

    @Override
    public boolean getOnlineMode() {
        return obj.fieldGet$onlineMode();
    }

    @Override
    public Message.Text getMOTD() {
        // TODO
        throw new NotImplementedException("");
    }

    private MCServerConfigurationManager getSCM() {
        return obj.fieldGet$serverConfigManager();
    }
}
