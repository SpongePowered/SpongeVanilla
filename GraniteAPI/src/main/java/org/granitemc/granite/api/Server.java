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

import org.granitemc.granite.api.command.CommandSender;

import java.io.File;
import java.net.Proxy;
import java.security.KeyPair;
import java.util.List;

public interface Server extends CommandSender {
    /**
     * Returns a list of online players
     */
    List<Player> getPlayers();

    /**
     * Returns whether structures can spawn
     */
    boolean canStructuresSpawn();

    /**
     * Returns whether the server is hardcore
     */
    boolean isHardcore();

    /**
     * Saves all worlds
     * @param outputLogMessage Whether to output a message
     */
    void saveAllWorlds(boolean outputLogMessage);

    /**
     * Stops the server
     */
    void stopServer();

    /**
     * Returns the server's hostname
     */
    String getServerHostname();

    /**
     * Returns whether the server is running
     */
    // TODO: doesn't this always return true? is it even needed?
    boolean isServerRunning();

    /**
     * Returns the server data directory
     */
    File getDataDirectory();

    /**
     * Returns whether to allow nether
     */
    boolean getAllowNether();

    /**
     * Returns the server's hostname
     */
    // TODO: isn't this the same as #getServerHostname?
    String getHostname();

    /**
     * Returns the port the server is running on
     */
    int getPort();

    /**
     * Returns the server's MotD (the message that will be shown in chat to players when joining)
     */
    String getMotd();

    /**
     * Returns the version of Minecraft this plugin runs on
     */
    String getMinecraftVersion();

    /**
     * Returns the amount of players currently online
     */
    int getCurrentPlayerCount();

    /**
     * Returns the maximum amount of players this server can have
     */
    int getMaxPlayers();

    /**
     * Returns this server's key pair
     */
    KeyPair getKeyPair();

    /**
     * Returns the port the server is running on
     */
    // TODO: isn't this the same as #getPort?
    int getServerPort();

    /**
     * Returns the folder name of this server
     */
    String getFolderName();

    /**
     * Returns whether this server can spawn monsters
     */
    boolean allowSpawnMonsters();

    /**
     * Returns whether Mojang's snooper is enabled
     */
    boolean isSnooperEnabled();

    /**
     * Returns whether this server can spawn animals
     */
    boolean getCanSpawnAnimals();

    /**
     * Sets whether this server can spawn animals
     * @param spawnAnimals Whether this server can spawn animals
     */
    void setCanSpawnAnimals(boolean spawnAnimals);

    /**
     * Returns whether this server can spawn NPCs
     */
    boolean getCanSpawnNPCs();

    /**
     * Sets whether this server can spawn NPCs
     * @param spawnNPCs Whether this server can spawn NPCs
     */
    void setCanSpawnNPCs(boolean spawnNPCs);

    /**
     * Returns whether PvP is enabled
     */
    boolean isPvpEnabled();

    /**
     * Sets whether PvP is enabled
     * @param allowPvP Whether PvP is enabled
     */
    void setAllowPvp(boolean allowPvP);

    /**
     * Returns whether flight is allowed (hovering over the ground for more than 4 seconds via mods - doesn't count creative flying)
     */
    boolean isFlightAllowed();

    /**
     * Sets whether flight is allowed (hovering over the ground for more than 4 seconds via mods - doesn't count creative flying)
     * @param allowFlight Whether flight is allowed
     */
    void setAllowFlight(boolean allowFlight);

    /**
     * Returns whether command blocks are enabled
     */
    boolean isCommandBlockEnabled();

    /**
     * Returns whether the GUI is enabled
     */
    boolean getGuiEnabled();

    /**
     * Returns the current tick count this server is on
     */
    int getTickCounter();

    /**
     * Returns the size of spawn protection
     */
    int getSpawnProtectionSize();

    // TODO: check if needed and what they do
    //boolean getForceGamemode();

    //void setForceGamemode(boolean forceGamemode);

    /**
     * Returns the proxy this server is using
     */
    Proxy getServerProxy();
}
