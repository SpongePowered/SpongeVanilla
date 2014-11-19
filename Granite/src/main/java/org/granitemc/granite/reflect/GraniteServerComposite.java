/*
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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.reflect;

import com.google.common.collect.Lists;
import org.granitemc.granite.GraniteAPI;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Server;
import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.FormattingOutputType;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.plugin.PluginContainer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.security.KeyPair;
import java.util.List;
import java.util.UUID;

public class GraniteServerComposite extends ProxyComposite implements Server {
    public static GraniteServerComposite instance;

    private SCMComposite scm;

    public static GraniteServerComposite init() {
        return instance = new GraniteServerComposite(new File("."));
    }

    public GraniteServerComposite(File worldsLocation) {
        super(Mappings.getClass("DedicatedServer"), new Class[]{File.class}, worldsLocation);

        injectLogger();
        injectCommand();
        injectSelf();
        injectSCM();

        addHook("tick", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                GraniteAPI.instance.tick();
                return null;
            }
        });

        // Start this baby
        invoke("startServerThread");

        for (PluginContainer c : Granite.getPlugins()) {
            c.enable();
        }
    }

    private void injectSCM() {
        final GraniteServerComposite me = this;

        // Inject SCM
        addHook("setConfigManager", new HookListener() {
            SCMComposite comp = null;

            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                // The arg is null, since we replaced that above
                if (comp == null) {
                    comp = new SCMComposite(me);
                }

                try {
                    proxyCallback.invoke(self, Mappings.getClass("ServerConfigurationManager").cast(comp.parent));
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
        Field loggerField = Mappings.getField("MinecraftServer", "logger");
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
        Field commandManagerField = Mappings.getField("MinecraftServer", "commandManager");
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
                if (method.getReturnType().getName().equals(Mappings.getClass("MinecraftServer").getName())) {
                    hook.setWasHandled(true);
                    return parent;
                }
                return null;
            }
        });
    }

    @Override
    public String getName() {
        return (String) invoke("getName");
    }

    @Override
    public void sendMessage(String message) {
        Granite.getLogger().info(message);
    }

    @Override
    public void sendMessage(ChatComponent component) {
        Granite.getLogger().info(component.toPlainText(FormattingOutputType.ANSI));
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> ret = Lists.newArrayList();
        try {
            List<Object> playerObjs = (List<Object>) Mappings.getField("ServerConfigurationManager", "playerEntityList").get(scm.parent);

            for (Object o : playerObjs) {
                Player p = (Player) MinecraftUtils.wrap(o);
                ret.add(p);
            }

            return ret;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Player getPlayer(String name) {
        for (Player player : getPlayers()) {
            if (player.getName().toLowerCase().contains(name.toLowerCase())) {
                return player;
            }
        }

        return null;
    }

    @Override
    public Player getPlayerExact(String name) {
        for (Player player : getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        return null;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        for (Player player : getPlayers()) {
            if (player.getUUID().equals(uuid)) {
                return player;
            }
        }

        return null;
    }

    //TODO: May be important?
    public void setUserMessage(String message) {
        //Obf: b
        invoke("setUserMessage", message);
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
        invoke("saveAllWorlds", outputLogMessage);
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
        invoke("setHostname", hostname);
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
        invoke("setKeyPair", keyPair);
    }

    @Override
    public int getServerPort() {
        //Obf: Q
        return (Integer) invoke("getServerPort");
    }

    public void setServerPort(int port) {
        //Obf: b
        invoke("setServerPort", port);
    }

    @Override
    public String getFolderName() {
        //Obf: T
        return (String) invoke("getFolderName");
    }

    public void setFolderName(String folderName) {
        //Obf: k
        invoke("setFolderName", folderName);
    }

    @Override
    public boolean allowSpawnMonsters() {
        //Obf: V
        return (boolean) invoke("allowSpawnMonsters");
    }

    public boolean canCreateBonusChest(boolean var1) {
        //Obf: c
        return (boolean) invoke("canCreateBonusChest", var1);
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
        invoke("setCanSpawnAnimals", spawnAnimals);
    }

    @Override
    public boolean getCanSpawnNPCs() {
        //Obf: af
        return (boolean) invoke("getCanSpawnNPCs");
    }

    @Override
    public void setCanSpawnNPCs(boolean spawnNPCs) {
        //Obf: e
        invoke("setCanSpawnNPCs", spawnNPCs);
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
        invoke("setAllowFlight", allowFlight);
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
        invoke("setForceGamemode", forceGamemode);
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
        return invoke("worldServerForDimension", var1);
    }

    public boolean isOnServerThread() {
        return fieldGet("serverThread") == Thread.currentThread();
    }
}
