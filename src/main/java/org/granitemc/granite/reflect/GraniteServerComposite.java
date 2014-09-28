package org.granitemc.granite.reflect;

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

import com.google.common.collect.Lists;
import org.granitemc.granite.GraniteAPI;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Player;
import org.granitemc.granite.api.Server;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.security.KeyPair;
import java.util.List;

public class GraniteServerComposite extends ProxyComposite implements Server {
    public static GraniteServerComposite instance;

    private SCMComposite scm;

    public static GraniteServerComposite init() {
        return instance = new GraniteServerComposite(new File("."));
    }

    public GraniteServerComposite(File worldsLocation) {
        super(Mappings.getClass("n.m.server.dedicated.DedicatedServer"), new Class[]{File.class}, worldsLocation);

        injectLogger();
        injectCommand();
        injectSelf();
        injectSCM();

        addHook("tick()", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                GraniteAPI.instance.tick();
                return null;
            }
        });

        // Start this baby
        invoke("n.m.server.MinecraftServer", "startServerThread");
    }

    private void injectSCM() {
        final GraniteServerComposite me = this;

        // Inject SCM
        addHook("func_152361_a(n.m.server.management.ServerConfigurationManager)", new HookListener() {
            SCMComposite comp = null;

            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                // The arg is null, since we replaced that above
                if (comp == null) {
                    comp = new SCMComposite(me);
                }

                try {
                    proxyCallback.invoke(self, Mappings.getClass("n.m.server.management.ServerConfigurationManager").cast(comp.parent));
                    hook.setWasHandled(true);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                scm = comp;

                return null;
            }
        });
    }

    private void injectLogger() {
        // Inject logger, I don't think this is needed but I'll do it anyway just to be on the safe side
        Field loggerField = Mappings.getField("n.m.server.MinecraftServer", "logger");
        ReflectionUtils.forceStaticAccessible(loggerField);
        try {
            loggerField.set(null, Granite.getLogger());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void injectCommand() {
        // Create command composite
        CommandComposite commandComposite = new CommandComposite();

        // Inject command composite
        Field commandManagerField = Mappings.getField("n.m.server.MinecraftServer", "commandManager");
        ReflectionUtils.forceStaticAccessible(commandManagerField);
        try {
            commandManagerField.set(this.parent, commandComposite.parent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void injectSelf() {
        addHook(new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                // This is needed, for some reason. I don't know. Ask Jason.
                if (method.getReturnType().equals(Mappings.getClass("n.m.server.MinecraftServer"))) {
                    hook.setWasHandled(true);
                    return parent;
                }
                return null;
            }
        });
    }

    @Override
    public String getName() {
        return (String) invoke("n.m.command.ICommandSender", "getName");
    }

    @Override
    public void sendMessage(String message) {
        Granite.getLogger().info(message);
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> ret = Lists.newArrayList();
        try {
            List<Object> playerObjs = (List<Object>) Mappings.getField("n.m.server.management.ServerConfigurationManager", "playerEntityList").get(scm.parent);

            for (Object o : playerObjs) {
                Player p = new GranitePlayer(o);
                ret.add(p);
                // TODO: cache - so the instances are the same
            }

            return ret;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO: May be important?
    public void setUserMessage(String message) {
        //Obf: b
        invoke("setUserMessage(String)", message);
    }

    @Override
    public boolean canStructuresSpawn() {
        //Obf: l
        return (boolean) invoke("canStructuresSpawn");
    }

    @Override
    public boolean isHardcore() {
        //Obf: o
        return (boolean) invoke("isHardcore");
    }

    public int getOpPermissionLevel() {
        //Obf: p
        return (Integer) invoke("getOpPermissionLevel");
    }

    @Override
    public void saveAllWorlds(boolean outputLogMessage) {
        //Obf: a
        invoke("saveAllWorlds(boolean)", outputLogMessage);
    }

    @Override
    public void stopServer() {
        //Obf: r
        invoke("stopServer");
    }

    @Override
    public String getServerHostname() {
        //Obf: s
        return (String) invoke("getServerHostname");
    }

    @Override
    public boolean isServerRunning() {
        //Obf: t
        return (boolean) invoke("isServerRunning");
    }

    public void initiateShutdown() {
        //Obf: u
        invoke("initiateShutdown");
    }

    @Override
    public File getDataDirectory() {
        //Obf: w
        return (File) invoke("getDataDirectory");
    }


    //TODO: Add hook for eventual scheduler
    public void tick() {
        //Obf: y
        invoke("tick");
    }

    @Override
    public boolean getAllowNether() {
        //Obf: A
        return (boolean) invoke("getAllowNether");
    }


    //TODO: Could be useful?
    /*public File getFile(String file) {
        //Obf: d
        return (File) invoke("getFile(String");
    }*/

    @Override
    public String getHostname() {
        //Obf: C
        return (String) invoke("getHostname");
    }

    public void setHostname(String hostname) {
        //Obf: c
        invoke("setHostname(String)", hostname);
    }

    @Override
    public int getPort() {
        //Obf: D
        return (Integer) invoke("getPort");
    }

    @Override
    public String getMotd() {
        //Obf: E
        return (String) invoke("getMotd");
    }

    @Override
    public String getMinecraftVersion() {
        //Obf: F
        return (String) invoke("getMinecraftVersion");
    }

    @Override
    public int getCurrentPlayerCount() {
        //Obf: G
        return (Integer) invoke("getCurrentPlayerCount");
    }

    @Override
    public int getMaxPlayers() {
        //Obf: H
        return (Integer) invoke("getMaxPlayers");
    }

    /*public String[] getAllUsernames() {
        //Obf: I
        return (String[]) invoke("getAllUsernames");
    }*/

    /*public String handleRConCommand(String command) {
        //Obf: g
        return (String) invoke("handleRConCommand(String)", command);
    }*/

    public boolean isDebuggingEnabled() {
        //Obf: L
        return (boolean) invoke("isDebuggingEnabled");
    }

    public String getServerModName() {
        //Obf: getServerModName
        return (String) invoke("getServerModName");
    }

    @Override
    public KeyPair getKeyPair() {
        //Obf: P
        return (KeyPair) invoke("getKeyPair");
    }

    public void setKeyPair(KeyPair keyPair) {
        //Obf: a
        invoke("setKeyPair(KeyPair", keyPair);
    }

    @Override
    public int getServerPort() {
        //Obf: Q
        return (Integer) invoke("getServerPort");
    }

    public void setServerPort(int port) {
        //Obf: b
        invoke("setServerPort(int)", port);
    }

    public String getServerOwner() {
        //Obf R
        return (String) invoke("getServerOwner");
    }

    public void setServerOwner(String serverOwner) {
        //Obf: j
        invoke("setServerOwner(String)", serverOwner);
    }

    public boolean isSinglePlayer() {
        //Obf: N
        return (boolean) invoke("isSinglePlayer");
    }

    @Override
    public String getFolderName() {
        //Obf: T
        return (String) invoke("getFolderName");
    }

    public void setFolderName(String folderName) {
        //Obf: k
        invoke("setFolderName(String)", folderName);
    }

    @Override
    public boolean allowSpawnMonsters() {
        //Obf: V
        return (boolean) invoke("allowSpawnMonsters");
    }

    public boolean canCreateBonusChest(boolean var1) {
        //Obf: c
        return (boolean) invoke("canCreateBonusChest(boolean)", var1);
    }

    public void deleteWorldAndStopServer() {
        //Obf: Z
        invoke("deleteWorldAndStopServer");
    }

    @Override
    public boolean isSnooperEnabled() {
        //Obf: ac
        return (boolean) invoke("isSnooperEnabled");
    }

    public boolean isDedicatedServer() {
        //Obf: ad
        return (boolean) invoke("isDedicatedServer");
    }

    @Override
    public boolean getCanSpawnAnimals() {
        //Obf: ae
        return (boolean) invoke("getCanSpawnAnimals");
    }

    @Override
    public void setCanSpawnAnimals(boolean spawnAnimals) {
        //Obf: d
        invoke("setCanSpawnAnimals(boolean)", spawnAnimals);
    }

    @Override
    public boolean getCanSpawnNPCs() {
        //Obf: af
        return (boolean) invoke("getCanSpawnNPCs");
    }

    @Override
    public void setCanSpawnNPCs(boolean spawnNPCs) {
        //Obf: e
        invoke("setCanSpawnNPCs(boolean)", spawnNPCs);
    }

    @Override
    public boolean isPvpEnabled() {
        //Obf: ag
        return (boolean) invoke("isPVPEnabled");
    }

    @Override
    public void setAllowPvp(boolean allowPvP) {
        //Obf: f
        invoke("setAllowPvp", allowPvP);
    }

    @Override
    public boolean isFlightAllowed() {
        //Obf: ah
        return (boolean) invoke("isFlightAllowed");
    }

    @Override
    public void setAllowFlight(boolean allowFlight) {
        //Obf: g
        invoke("setAllowFlight(boolean)", allowFlight);
    }

    @Override
    public boolean isCommandBlockEnabled() {
        //Obf: ai
        return (boolean) invoke("isCommandBlockEnabled");
    }

    @Override
    public boolean getGuiEnabled() {
        //Obf: aq
        return (boolean) invoke("getGuiEnabled");
    }

    @Override
    public int getTickCounter() {
        //Obf: ar
        return (Integer) invoke("getTickCounter");
    }

    @Override
    public int getSpawnProtectionSize() {
        //Obf: au
        return (Integer) invoke("getSpawnProtectionSize");
    }

    public boolean getForceGamemode() {
        //Obf: av
        return (boolean) invoke("getForceGamemode");
    }

    public void setForceGamemode(boolean forceGamemode) {
        //Obf: i
        invoke("setForceGamemode(boolean)", forceGamemode);
    }

    @Override
    public Proxy getServerProxy() {
        //Obf: aw
        return (Proxy) invoke("getServerProxy");
    }

    public long getCurrentTimeMillis() {
        //Obf: ax
        return (long) invoke("getCurrentTimeMillis");
    }

    //TODO: What does this do? getBuildHeight? always returns 256
    /*public int aI() {
        //Obf: aI
        return (Integer) invoke("aI");
    }*/

    public Object worldServerForDimension(int var1) {
        return invoke("n.m.server.MinecraftServer", "worldServerForDimension(int)", var1);
    }

    public boolean isOnServerThread() {
        return fieldGet("n.m.server.MinecraftServer", "serverThread") == Thread.currentThread();
    }
}
