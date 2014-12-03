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
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.ProxyComposite;
import org.granitepowered.granite.mappings.Mappings;
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
import java.util.Collection;
import java.util.UUID;

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
        return null;
    }

    @Override
    public PluginManager getPluginManager() {
        return Granite.instance.getPluginManager();
    }

    @Override
    public EventManager getEventManager() {
        return null;
    }

    @Override
    public GameRegistry getRegistry() {
        return null;
    }

    @Override
    public ServiceManager getServiceManager() {
        return null;
    }

    @Override
    public Scheduler getScheduler() {
        return null;
    }

    @Override
    public CommandService getCommandDispatcher() {
        return null;
    }

    @Override
    public String getAPIVersion() {
        return "Sponge 1.1-SNAPSHOT";
    }

    @Override
    public String getImplementationVersion() {
        return "Granite v" + version;
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        return null;
    }

    @Override
    public int getMaxPlayers() {
        return 0;
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public Optional<Player> getPlayer(String s) {
        return null;
    }

    @Override
    public Collection<World> getWorlds() {
        return null;
    }

    @Override
    public Optional<World> getWorld(UUID uuid) {
        return null;
    }

    @Override
    public Optional<World> getWorld(String s) {
        return null;
    }

    @Override
    public void broadcastMessage(Message<?> message) {

    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public InetSocketAddress getBoundIP() {
        return null;
    }

    @Override
    public boolean hasWhitelist() {
        return false;
    }

    @Override
    public void setHasWhitelist(boolean b) {

    }

    @Override
    public boolean getOnlineMode() {
        return false;
    }
}
