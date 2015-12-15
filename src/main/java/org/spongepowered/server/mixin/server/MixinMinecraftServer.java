/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.mixin.server;

import net.minecraft.crash.CrashReport;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.GameState;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.server.SpongeVanilla;
import org.spongepowered.server.world.VanillaDimensionManager;

import java.util.Hashtable;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.FutureTask;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Shadow private static Logger logger;
    @Shadow protected Queue<?> futureTaskQueue;
    @Shadow private ServerConfigurationManager serverConfigManager;
    @Shadow private Profiler theProfiler;
    @Shadow private int tickCounter;
    @Shadow abstract boolean getAllowNether();
    @Shadow abstract NetworkSystem getNetworkSystem();
    @Shadow List<?> playersOnline;

    private Hashtable<Integer, long[]> worldTickTimes = new Hashtable<Integer, long[]>();

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;addFaviconToStatusResponse"
            + "(Lnet/minecraft/network/ServerStatusResponse;)V", shift = At.Shift.AFTER))
    public void callServerStarted(CallbackInfo ci) {
        SpongeVanilla.INSTANCE.postState(GameStartedServerEvent.class, GameState.SERVER_STARTED);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;finalTick(Lnet/minecraft/crash/CrashReport;)V",
            ordinal = 0, shift = At.Shift.BY, by = -9))
    public void callServerStopping(CallbackInfo ci) {
        SpongeVanilla.INSTANCE.postState(GameStoppingServerEvent.class, GameState.SERVER_STOPPING);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;systemExitNow()V"))
    public void callServerStopped(CallbackInfo ci) {
        SpongeVanilla.INSTANCE.postState(GameStoppedServerEvent.class, GameState.SERVER_STOPPED);
        SpongeVanilla.INSTANCE.postState(GameStoppingEvent.class, GameState.GAME_STOPPING);
        SpongeVanilla.INSTANCE.postState(GameStoppedEvent.class, GameState.GAME_STOPPED);
    }

    @Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;flush()V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void callWorldUnload(CallbackInfo ci, int i, WorldServer worldserver) {
        SpongeImpl.postEvent(SpongeEventFactory.createUnloadWorldEvent(SpongeImpl.getGame(), Cause.of(NamedCause.source(this)), (World) worldserver));
    }

    @Overwrite
    public void updateTimeLightAndEntities() {
        this.theProfiler.startSection("jobs");
        Queue<?> queue = this.futureTaskQueue;

        synchronized (this.futureTaskQueue) {
            while (!this.futureTaskQueue.isEmpty()) {
                try {
                    ((FutureTask) this.futureTaskQueue.poll()).run();
                } catch (Throwable throwable2) {
                    logger.fatal(throwable2);
                }
            }
        }

        this.theProfiler.endStartSection("levels");
        int j;

        Integer[] ids = VanillaDimensionManager.getIDs(this.tickCounter % 200 == 0);

        for (j = 0; j < ids.length; ++j) {
            int id = ids[j];
            long i = System.nanoTime();

            if (j == 0 || this.getAllowNether()) {
                WorldServer worldserver = VanillaDimensionManager.getWorldFromDimId(id);
                this.theProfiler.startSection(worldserver.getWorldInfo().getWorldName());

                if (this.tickCounter % 20 == 0) {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(
                            new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(),
                                    worldserver.getGameRules().getGameRuleBooleanValue("doDaylightCycle")), worldserver.provider.getDimensionId());
                    this.theProfiler.endSection();
                }

                this.theProfiler.startSection("tick");
                CrashReport crashreport;

                try {
                    worldserver.tick();
                } catch (Throwable throwable1) {
                    crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try {
                    worldserver.updateEntities();
                } catch (Throwable throwable) {
                    crashreport = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                this.theProfiler.endSection();
                this.theProfiler.startSection("tracker");
                worldserver.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }
            this.worldTickTimes.get(id)[this.tickCounter % 100] = System.nanoTime() - i;
        }
        this.theProfiler.endStartSection("dim_unloading");
        VanillaDimensionManager.unloadWorlds(this.worldTickTimes);
        this.theProfiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.onTick();
        this.theProfiler.endStartSection("tickables");

        for (j = 0; j < this.playersOnline.size(); ++j) {
            ((IUpdatePlayerListBox) this.playersOnline.get(j)).update();
        }

        this.theProfiler.endSection();
    }

    @Overwrite
    public String getServerModName() {
        return SpongeVanilla.INSTANCE.getName();
    }

    @Overwrite
    public void addChatMessage(IChatComponent component) {
        logger.info(SpongeTexts.toLegacy(component));
    }

    public Hashtable<Integer, long[]> getWorldTickTimes() {
        return this.worldTickTimes;
    }

}
