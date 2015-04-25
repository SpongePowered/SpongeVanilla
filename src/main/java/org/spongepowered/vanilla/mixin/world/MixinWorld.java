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
package org.spongepowered.vanilla.mixin.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.configuration.SpongeConfig;
import org.spongepowered.common.interfaces.IMixinWorld;
import org.spongepowered.common.world.border.PlayerBorderListener;

import java.io.File;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld implements org.spongepowered.api.world.World, IMixinWorld {

    public SpongeConfig<SpongeConfig.WorldConfig> worldConfig;

    @Shadow private net.minecraft.world.border.WorldBorder worldBorder;
    @Shadow abstract boolean isChunkLoaded(int i, int j, boolean flag);
    @Shadow List playerEntities;
    @Shadow abstract void updateAllPlayersSleepingFlag();
    @Shadow abstract Chunk getChunkFromChunkCoords(int chunkX, int chunkZ);
    @Shadow List loadedEntityList;
    @Shadow abstract void onEntityAdded(Entity entityIn);

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConstructed(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client,
            CallbackInfo ci) {
        String providerName = providerIn.getDimensionName().toLowerCase().replace(" ", "_").replace("[^A-Za-z0-9_]", "");
        this.worldConfig =
                new SpongeConfig<SpongeConfig.WorldConfig>(SpongeConfig.Type.WORLD, new File(Sponge.getConfigDirectory()
                        + File.separator + providerName
                        + File.separator + "dim0", "world.conf"),
                        "sponge");

        this.worldBorder.addListener(new PlayerBorderListener());
    }

    /**
     * @author Zidane
     *
     * Purpose: To call {@link org.spongepowered.api.event.entity.EntitySpawnEvent} from our API.
     * Reasoning: Not sure how to properly add the injection so I did an overwrite in this case. One of our mixin gurus
     * is welcome to make this better.
     */
    @Overwrite
    public boolean spawnEntityInWorld(Entity entityIn) {
        int i = MathHelper.floor_double(entityIn.posX / 16.0D);
        int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
        boolean flag = entityIn.forceSpawn;

        if (entityIn instanceof EntityPlayer)
        {
            flag = true;
        }

        if (!flag && !this.isChunkLoaded(i, j, true))
        {
            return false;
        }
        else
        {
            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                this.playerEntities.add(entityplayer);
                this.updateAllPlayersSleepingFlag();
            }

            // Sponge Start -> Fire EntitySpawnEvent
            org.spongepowered.api.entity.Entity spongeEntity = (org.spongepowered.api.entity.Entity) entityIn;
            if (Sponge.getGame().getEventManager().post(SpongeEventFactory.createEntitySpawn(Sponge.getGame(), spongeEntity, spongeEntity.getLocation())) && !flag) {
                return false;
            }
            // Sponge End

            this.getChunkFromChunkCoords(i, j).addEntity(entityIn);
            this.loadedEntityList.add(entityIn);
            this.onEntityAdded(entityIn);
            return true;
        }
    }
}
