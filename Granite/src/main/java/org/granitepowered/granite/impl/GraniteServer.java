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

package org.granitepowered.granite.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCEntityPlayerMP;
import org.granitepowered.granite.mc.MCServer;
import org.granitepowered.granite.mc.MCServerConfigurationManager;
import org.granitepowered.granite.mc.MCWorld;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.net.ChannelListener;
import org.spongepowered.api.net.ChannelRegistrationException;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.WorldGenerator;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.*;

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

public class GraniteServer extends Composite<MCServer> implements Server {

    static String version;

    public GraniteServer() {
        super(Mappings.getClass("DedicatedServer"), new Class[]{File.class}, new File("worlds/"));
        obj.startServerThread();
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        Set<Player> players = new HashSet<>();

        for (MCEntityPlayerMP playerEntity : getSCM().fieldGet$playerEntityList()) {
            players.add((Player) wrap(playerEntity));
        }

        return players;
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
        return ImmutableList
                .<World>copyOf(
                        Iterables.transform(Arrays.asList((MCWorld[]) obj.fieldGet$worldServers()), new MinecraftUtils.WrapFunction<GraniteWorld>()));
    }

    @Override
    public Optional<World> getWorld(final UUID uuid) {
        return Optional.fromNullable(Iterables.find(getWorlds(), new Predicate<World>() {
            public boolean apply(World world) {
                return world.getUniqueId().equals(uuid);
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
    public Optional<World> loadWorld(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean unloadWorld(World world) {
        throw new NotImplementedException("");
    }

    @Override
    public World createWorld(String s, WorldGenerator worldGenerator, long l) {
        // TODO: Multiworld
        throw new NotImplementedException("");
    }

    @Override
    public World createWorld(String s, WorldGenerator worldGenerator) {
        // TODO: Multiworld
        throw new NotImplementedException("");
    }

    @Override
    public World createWorld(String s) {
        // TODO: Multiworld
        throw new NotImplementedException("");
    }

    @Override
    public int getRunningTimeTicks() {
        // TODO: add
        throw new NotImplementedException("");
    }

    @Override
    public void broadcastMessage(Message message) {
        getSCM().sendChatMsg(MinecraftUtils.graniteToMinecraftChatComponent(message));
    }

    @Override
    public Optional<InetSocketAddress> getBoundAddress() {
        return Optional.fromNullable(new InetSocketAddress(obj.fieldGet$hostname(), obj.fieldGet$serverPort()));
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
    public Message getMotd() {
        return Messages.of(obj.fieldGet$motd());
    }

    @Override
    public void shutdown(Message kickMessage) {
        throw new NotImplementedException("");
    }

    private MCServerConfigurationManager getSCM() {
        return obj.fieldGet$serverConfigManager();
    }

    @Override
    public void registerChannel(Object plugin, ChannelListener listener, String channel) throws ChannelRegistrationException {
        throw new NotImplementedException("");
    }

    @Override
    public List<String> getRegisteredChannels() {
        throw new NotImplementedException("");
    }
}
