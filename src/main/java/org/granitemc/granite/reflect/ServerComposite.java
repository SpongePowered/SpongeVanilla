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
import com.google.common.util.concurrent.ListenableFuture;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Player;
import org.granitemc.granite.api.Server;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.security.KeyPair;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

public class ServerComposite extends ProxyComposite implements Server, CommandSender {
    public static ServerComposite instance;

    private Object serverConfigurationManager;

    public static ServerComposite init() {
        Mappings.invoke(null, "n.m.init.Bootstrap", "func_151354_b");

        return instance = new ServerComposite(new File("."));
    }

    public ServerComposite(File worldsLocation) {
        super(Mappings.getClass("n.m.server.dedicated.DedicatedServer"), new Class[] {File.class}, worldsLocation);

        // Inject logger, I don't think this is needed but I'll do it anyway just to be on the safe side
        Field loggerField = Mappings.getField("n.m.server.MinecraftServer", "logger");
        ReflectionUtils.forceStaticAccessible(loggerField);
        try {
            loggerField.set(null, Granite.getLogger());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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

        addHook(new HookListener() {
            @Override
            public Object activate(Method method, Method proxyCallback, Hook hook, Object[] args) {
                // This is needed, for some reason. I don't know. Ask Jason.
                if (method.getReturnType().equals(Mappings.getClass("n.m.server.MinecraftServer"))) {
                    hook.setWasHandled(true);
                    return parent;
                }
                return null;
            }
        });


        // Start this baby
        invoke("n.m.server.MinecraftServer", "startServerThread");
    }

    public String getName() {
        return (String) invoke("n.m.command.ICommandSender", "getName");
    }

    public void sendMessage(String message) {
        Granite.getLogger().info(message);
    }

    public List<Player> getPlayers() {
        List<Player> ret = Lists.newArrayList();
        try {
            List<Object> playerObjs = (List<Object>) Mappings.getField("n.m.server.management.ServerConfigurationManager", "playerEntityList").get(getServerConfigurationManager());

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

    private Object getServerConfigurationManager() {
        if (serverConfigurationManager == null) {
            // Get server config manager
            Field configurationManagerField = Mappings.getField("n.m.server.MinecraftServer", "configurationManager");
            configurationManagerField.setAccessible(true);
            try {
                serverConfigurationManager = configurationManagerField.get(parent);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return serverConfigurationManager;
    }

    //TODO: work out what this returns new instance off
    /*public cl h() {
        //Obf: h
        return (cl) invoke("h");
    }*/

    //TODO: Is this needed?
    public boolean startServer() {
        //Obf: i
        return (boolean) invoke("startServer");
    }

    //TODO: Is this needed?
    public void convertMapIfNeeded(String var1) {
        //Obf: a
        invoke("convertMapIfNeeded(String)", var1);
    }

    public void setUserMessage(String var1) {
        //Obf: b
        invoke("setUserMessage(String)", var1);
    }

    //TODO: get class are
    /*public void loadAllWorlds(String var1, String var2, long var3, are var5, String var6) {
        //Obf: a
        invoke("loadAllWorlds(String;String;long;areString)", var1, var2, var3, var5, var6);
    }*/

    //TODO: is this needed?
    public void initialWorldChunkLoad() {
        //Obf: k
        invoke("initialWorldChunkLoad");
    }

    //TODO: work out what this does and get class bqy
    /*public void a(String var1, bqy var2) {
        //Obf: a
        invoke("a(String;bqy)", var1, var2);
    }*/

    public boolean canStructuresSpawn() {
        //Obf: l
        return (boolean) invoke("canStructuresSpawn");
    }

    /*public arc getGameType() {
        //Obf: m
        return (arc) invoke("getGameType");
    }*/

    //TODO: i think this is what it does :S
    /*public vt getDifficulty() {
        //Obf: n
        return (vt) invoke("getDifficulty");
    }*/

    public boolean isHardcore() {
        //Obf: o
        return (boolean) invoke("isHardcore");
    }

    public int getOpPermissionLevel() {
        //Obf: p
        return (Integer) invoke("getOpPermissionLevel");
    }

    public void outputPercentRemaining(String var1, int var2) {
        //Obf: a_
        invoke("outputPercentRemaining(String;int)", var1, var2);
    }

    public void clearCurrentTask() {
        //Obf: q
        invoke("clearCurrentTask");
    }

    public void saveAllWorlds(boolean var1) {
        //Obf: a
        invoke("saveAllWorlds(boolean)", var1);
    }

    public void stopServer() {
        //Obf: r
        invoke("stopServer");
    }

    public String getServerHostname() {
        //Obf: s
        return (String) invoke("getServerHostname");
    }

    public boolean isServerRunning() {
        //Obf: t
        return (boolean) invoke("isServerRunning");
    }

    public void initiateShutdown() {
        //Obf: u
        invoke("initiateShutdown");
    }

    //TODO: is this needed?
    public void run() {
        //Obf: run
        invoke("run");
    }

    //TODO: is this needed?
    public File getDataDirectory() {
        //Obf: w
        return (File) invoke("getDataDirectory");
    }

    //TODO: Get class np
    /*public void loadServerIcon(np var1) {
        //Obf: a
        invoke("loadServerIcon(np)", var1);
    }*/

    //TODO: is this needed?
    public void systemExitNow() {
        //Obf: x
        invoke("systemExitNow");
    }

    //TODO: get class b
    /*public void finalTick(b var1) {
        //Obf: a
        invoke("finalTick(b)", var1);
    }*/

    //TODO: is this needed?
    public void tick() {
        //Obf: y
        invoke("tick");
    }

    //TODO: is this needed?
    public void updateTimeLightAndEntities() {
        //Obf: z
        invoke("updateTimeLightAndEntities");
    }

    public boolean getAllowNether() {
        //Obf: A
        return (boolean) invoke("getAllowNether");
    }

    public void main(String[] args) {
        //Obf: main
        invoke("main(String[]");
    }

    //TODO: find suitable name and work out what it does
    /*public void a(pm var1) {
        //Obf: a
        invoke("a");
    }*/

    //TODO: is this needed?
    public void startServerThread() {
        //Obf: B
        invoke("startServerThread");
    }

    //TODO: Could be useful?
    public File getFile(String file) {
        //Obf: d
        return (File) invoke("getFile(String");
    }

    public String getHostname() {
        //Obf: C
        return (String) invoke("getHostname");
    }

    //TODO: Get qt class
    /*public qt worldServerForDimension(int dimensionId) {
        //Obf: a
        return (qt) invoke("worldServerForDimension(int)", dimensionId);
    }*/

    public void setHostname(String hostname) {
        //Obf: c
        invoke("setHostname(String)", hostname);
    }

    public int getPort() {
        //Obf: D
        return (Integer) invoke("getPort");
    }

    public String getMotd() {
        //Obf: E
        return (String) invoke("getMotd");
    }

    public String getMinecraftVersion() {
        //Obf: F
        return (String) invoke("getMinecraftVersion");
    }

    public int getCurrentPlayerCount() {
        //Obf: G
        return (Integer) invoke("getCurrentPlayerCount");
    }

    public int getMaxPlayers() {
        //Obf: H
        return (Integer) invoke("getMaxPlayers");
    }

    public String[] getAllUsernames() {
        //Obf: I
        return (String[]) invoke("getAllUsernames");
    }

    public String handleRConCommand(String command) {
        //Obf: g
        return (String) invoke("handleRConCommand(String)", command);
    }

    public boolean isDebuggingEnabled() {
        //Obf: L
        return (boolean) invoke("isDebuggingEnabled");
    }

    public String getServerModName() {
        //Obf: getServerModName
        return (String) invoke("getServerModName");
    }

    //TODO: Get class b
    /*public b addServerInfoToCrashReport(b var1) {
        //Obf: b
        return (b) invoke("addServerInfoToCrashReport(b)", var1);
    }*/

    //TODO get classes ae and dt
    /*public List getPossibleCompletions(ae var1, String var2, dt var3) {
        //Obf: a
        return (List) invoke("getPossibleCompletions(ae;String;dt)", var1, var2, var3);
    }*/

    //TODO: Is this needed?
    /*public MinecraftServer getServer() {
        //Obf: M
        return (MinecraftServer) invoke("getServer");
    }*/

    //TODO: What does this do?
    public boolean N() {
        //Obf: N
        return (boolean) invoke("N");
    }

    public String getCommandSenderName() {
        //Obf: d_
        return (String) invoke("getCommandSenderName");
    }

    //TODO: Get class ho
    /*public void addChatMessage(ho var1) {
        //Obf: a
        invoke("addChatMessage(ho)", var1);
    }*/

    public boolean canCommandSenderUseCommand(int var1, String var2) {
        //Obf a
        return (boolean) invoke("canCommandSenderUseCommand(ont;String)", var1, var2);
    }

    //TODO: get class ad
    /*public ad getCommandManager() {
        //Obf: O
        return (ad) invoke("getCommandManager");
    }*/

    //TODO: Is this needed?
    public KeyPair getKeyPair() {
        //Obf: P
        return (KeyPair) invoke("getKeyPair");
    }

    public void setKeyPair(KeyPair keyPair) {
        //Obf: a
        invoke("setKeyPair(KeyPair", keyPair);
    }

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

    public String getFolderName() {
        //Obf: T
        return (String) invoke("getFolderName");
    }

    public void setFolderName(String folderName) {
        //Obf: k
        invoke("setFolderName(String)", folderName);
    }

    //TODO: get class vt
    /*public void setDifficulty(vt difficulty) {
        //Obf: a
        invoke("setKeyPair(vt)", difficulty);
    }*/

    public boolean allowSpawnMonsters() {
        //Obf: V
        return (boolean) invoke("allowSpawnMonsters");
    }

    public boolean canCreateBonusChest(boolean var1) {
        //Obf: c
        return (boolean) invoke("canCreateBonusChest(boolean)", var1);
    }

    //TODO: get class bra
    /*public bra getActiveAnvilConverter() {
        //Obf: X
        return (bra) invoke("getActiveAnvilConverter");
    }*/

    public void deleteWorldAndStopServer() {
        //Obf: Z
        invoke("deleteWorldAndStopServer");
    }

    //TODO: what does this do? it used to be obf V
    public String aa() {
        //Obf: aa
        return (String) invoke("aa");
    }

    //TODO: What does this do?
    public String ab() {
        //Obf: ab
        return (String) invoke("ab");
    }

    public void a_(String var1, String var2) {
        //bf: a_
        invoke("a_(String;String");
    }

    //tODO: get class wb
    /*public void addServerStatsToSnooper(wb var1) {
        //Obf: a
        invoke("addServerStatsToSnooper(wb)", var1);
    }*/

    public boolean isSnooperEnabled() {
        //Obf: ac
        return (boolean) invoke("isSnooperEnabled");
    }

    public boolean isDedicatedServer() {
        //Obf: ad
        return (boolean) invoke("isDedicatedServer");
    }

    public boolean getCanSpawnAnimals() {
        //Obf: ae
        return (boolean) invoke("getCanSpawnAnimals");
    }

    public void setCanSpawnAnimals(boolean var1) {
        //Obf: d
        invoke("setCanSpawnAnimals(boolean)", var1);
    }

    public boolean getCanSpawnNPCs() {
        //Obf: af
        return (boolean) invoke("getCanSpawnNPCs");
    }

    public void setCanSpawnNPCs(boolean var1) {
        //Obf: e
        invoke("setCanSpawnNPCs(boolean)", var1);
    }

    public boolean isPVPEnabled() {
        //Obf: ag
        return (boolean) invoke("isPVPEnabled");
    }

    public void setAllowPvp(boolean var1) {
        //Obf: f
        invoke("setAllowPvp", var1);
    }

    public boolean isFlightAllowed() {
        //Obf: ah
        return (boolean) invoke("isFlightAllowed");
    }

    public void setAllowFlight(boolean var1) {
        //Obf: g
        invoke("setAllowFlight(boolean)", var1);
    }

    public boolean isCommandBlockEnabled() {
        //Obf: ai
        return (boolean) invoke("isCommandBlockEnabled");
    }

    //TODO: Whatdoes this do?
    public void h(boolean var1) {
        //Obf: h
        invoke("h(boolean)", var1);
    }

    //TODO: What does this do?
    public boolean aj() {
        //Obf: aj
        return (boolean) invoke("getBuildLimit");
    }

    //TODO: What does this do?
    public String ak() {
        //Obf: ak
        return (String) invoke("ak");
    }

    //TODO: What does this do?
    public void m(String var1) {
        //Obf: m
        invoke("m(String)", var1);
    }

    //TODO: What does this do? getBuildLimit?
    public int al() {
        //Obf: al
        return (Integer) invoke("al");
    }

    //TODO: What does this do? setBuildLimit?
    public void c(int var1) {
        //Obf: c
        invoke("c(int)");
    }

    //TODO: What does this do?
    public boolean am() {
        //Obf: am
        return (boolean) invoke("am");
    }

    //TODO: What does this do?
    //TODO: get class sn
    /*public sn an() {
        //Obf: an
        return (sn) invoke("an");
    }*/

    //TODO: What does this do?
    //TODO: get class sn
    /*public void a(sn var1) {
        //Obf: a
        invoke("a(sn)", var1);
    }*/

    //TODO: get class ahk
    /*public void setGameType(ahk gameType) {
        //Obf a
        invoke("setGameType", gameType);
    }*/

    //TODO: What des this do?
    //TODO: get class rc
    /*public rc ao() {
        //Obf: ao
        return (rc) invoke("ao");
    }*/

    public boolean getGuiEnabled() {
        //Obf: aq
        return (boolean) invoke("getGuiEnabled");
    }

    public int getTickCounter() {
        //Obf: ar
        return (Integer) invoke("getTickCounter");
    }

    //TODO: what does this do?
    public void as() {
        //Obf: as
        invoke("as");
    }

    //TODO: What does this do?
    //TODO: get class dt
    /*public dt c() {
        //Obf: c
        return (dt) invoke("c");
    }*/

    //TODO: get class brw
    /*public brw getCommandSenderPosition() {
        //Obf: d
        return (brw) invoke("getCommandSenderPosition");
    }*/

    //TODO: get class aqu
    /*public aqu getEntityWorld() {
        //Obf: e
        return (aqu) invoke("getEntityWorld");
    }*/

    //TODO: what does tis return?
    public GraniteEntity f() {
        //Obf: f
        return (GraniteEntity) invoke("f");
    }

    public int getSpawnProtectionSize() {
        //Obf: au
        return (Integer) invoke("getSpawnProtectionSize");
    }

    //TODO: get classes aqu, dt, ahd
    /*public boolean isBlockProtected(aqu var1, dt var2, ahd var3) {
        //Obf: a
        return (boolean) invoke("isBlockProtected(aqu;dt;ahd)", var1, var2, var3);
    }*/

    public boolean getForceGamemode() {
        //Obf: av
        return (boolean) invoke("getForceGamemode");
    }

    public void setForceGamemode(boolean var1) {
        //Obf: i
        invoke("setForceGamemode(boolean)", var1);
    }

    public Proxy getServerProxy() {
        //Obf: aw
        return (Proxy) invoke("getServerProxy");
    }

    public long getCurrentTimeMillis() {
        //Obf: ax
        return (long) invoke("getCurrentTimeMillis");
    }

    //TODO: What does this do?
    public int ay() {
        //Obf: ay
        return (Integer) invoke("ay");
    }

    //TODO: What does tis do?
    public void d(int var1) {
        //Obf: d
        invoke("d(int)", var1);
    }

    //TODO: What does this do?
    /*public ho e_() {
        //Obf: e_
        return (ho) invoke("e_");
    }*/

    //TODO: What does this do?
    public boolean az() {
        //Obf: az
        return (boolean) invoke("az");
    }

    //TODO: What does this do?
    /*public ry aD() {
        //Obf: aD
        return (ry) invoke("aD");
    }*/

    //TODO: What does this do?
    /*public np aE() {
        //Obf: aE
        return (np) invoke("aE");
    }*/

    //TODO: What does this do?
    public void aF() {
        //Obf: aF
        invoke("aF");
    }

    //TODO: What does this do? getEntityFromUUID?
    public GraniteEntity a(UUID uuid) {
        //Obf: a
        return (GraniteEntity) invoke("a(UUID)");
    }

    //TODO: What does this do?
    public boolean t_() {
        //Obf: t_
        return (boolean) invoke("t_");
    }

    //TODO: What does this do?
    //TODO: get class ag
    /*public void a(ag var1, int var2) {
        //Obf: a
        invoke("a(ag;int)", var1, var2);
    }*/

    //TODO: What does this do?
    public int aG() {
        //Obf: aG
        return (Integer) invoke("aG");
    }

    //TODO: What does this do?
    public ListenableFuture a(Callable var1) {
        //Obf: a
        return (ListenableFuture) invoke("a(Callable)", var1);
    }

    //TODO: What does this do?
    public ListenableFuture a(Runnable var1) {
        //Obf: a
        return (ListenableFuture) invoke("a(Runnable)", var1);
    }

    //TODO: What does this do?
    public boolean aH() {
        //Obf: aH
        return (boolean) invoke("aH");
    }

    //TODO: What does this do? getBuildHeight? always returns 256
    public int aI() {
        //Obf: aI
        return (Integer) invoke("aI");
    }

    //TODO: What does this do?
    public long aJ() {
        //Obf: aJ
        return (long) invoke("aJ");
    }

    //TODO: What does this do?
    public Thread aK() {
        //Obf: aK
        return (Thread) invoke("aK");
    }

}
