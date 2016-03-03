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
package org.spongepowered.server.world;

import com.google.common.collect.Lists;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Level;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.world.DimensionManager;

import java.util.Hashtable;
import java.util.List;

public class VanillaDimensionManager extends DimensionManager {

    public static void unloadWorlds(Hashtable<Integer, long[]> worldTickTimes) {
        for (int id : unloadQueue) {
            WorldServer w = worlds.get(id);
            try {
                if (w != null) {
                    w.saveAllChunks(true, null);
                } else {
                    SpongeImpl.getLogger().log(Level.ERROR, "Unexpected world unload - world {} is already unloaded", id);
                }
            } catch (MinecraftException e) {
                e.printStackTrace();
            } finally {
                if (w != null) {
                    SpongeImpl.postEvent(SpongeEventFactory.createUnloadWorldEvent(Cause.of(NamedCause.source(Sponge.getServer())),
                            (org.spongepowered.api.world.World) w));
                    w.flush();
                    setWorld(id, null);
                }
            }
        }
        unloadQueue.clear();
    }

    public static Integer[] getIDs(boolean check) {
        if (check) {
            List<World> allWorlds = Lists.newArrayList(weakWorldMap.keySet());
            allWorlds.removeAll(worlds.values());
            for (World w : allWorlds) {
                leakedWorlds.add(System.identityHashCode(w));
            }
            for (World w : allWorlds) {
                int leakCount = leakedWorlds.count(System.identityHashCode(w));
                if (leakCount == 5) {
                    SpongeImpl.getLogger().log(Level.WARN, "The world {} ({}) may have leaked: first encounter (5 occurences).\n", System
                            .identityHashCode(w), w.getWorldInfo().getWorldName());
                } else if (leakCount % 5 == 0) {
                    SpongeImpl.getLogger().log(Level.WARN, "The world {} ({}) may have leaked: seen {} times.\n", System.identityHashCode(w),
                            w.getWorldInfo().getWorldName(), leakCount);
                }
            }
        }
        return getIDs();
    }

    public static void unloadWorld(int id) {
        final WorldServer world = getWorldFromDimId(id);
        if (world != null && !((WorldProperties) world.getWorldInfo()).doesKeepSpawnLoaded()) {
            unloadQueue.add(id);
        }
    }
}
