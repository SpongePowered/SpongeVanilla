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
    String getName();

    void sendMessage(String message);

    List<Player> getPlayers();

    //TODO: May be important?
    void setUserMessage(String var1);

    boolean canStructuresSpawn();

    boolean isHardcore();

    int getOpPermissionLevel();

    void saveAllWorlds(boolean var1);

    void stopServer();

    String getServerHostname();

    boolean isServerRunning();

    void initiateShutdown();

    File getDataDirectory();

    //TODO: Add hook for eventual scheduler
    void tick();

    boolean getAllowNether();

    String getHostname();

    void setHostname(String hostname);

    int getPort();

    String getMotd();

    String getMinecraftVersion();

    int getCurrentPlayerCount();

    int getMaxPlayers();

    boolean isDebuggingEnabled();

    String getServerModName();

    KeyPair getKeyPair();

    void setKeyPair(KeyPair keyPair);

    int getServerPort();

    void setServerPort(int port);

    String getServerOwner();

    void setServerOwner(String serverOwner);

    boolean isSinglePlayer();

    String getFolderName();

    void setFolderName(String folderName);

    boolean allowSpawnMonsters();

    boolean canCreateBonusChest(boolean var1);

    void deleteWorldAndStopServer();

    boolean isSnooperEnabled();

    boolean isDedicatedServer();

    boolean getCanSpawnAnimals();

    void setCanSpawnAnimals(boolean var1);

    boolean getCanSpawnNPCs();

    void setCanSpawnNPCs(boolean var1);

    boolean isPvpEnabled();

    void setAllowPvp(boolean var1);

    boolean isFlightAllowed();

    void setAllowFlight(boolean var1);

    boolean isCommandBlockEnabled();

    boolean getGuiEnabled();

    int getTickCounter();

    int getSpawnProtectionSize();

    boolean getForceGamemode();

    void setForceGamemode(boolean var1);

    Proxy getServerProxy();

    long getCurrentTimeMillis();
}
