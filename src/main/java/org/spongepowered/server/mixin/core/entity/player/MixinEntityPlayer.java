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
package org.spongepowered.server.mixin.core.entity.player;

import com.flowpowered.math.vector.Vector3d;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.action.SleepingEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayer;
import org.spongepowered.common.mixin.core.entity.MixinEntityLivingBase;
import org.spongepowered.common.util.VecHelper;

import java.util.Optional;

import javax.annotation.Nullable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase implements IMixinEntityPlayer {

    @Shadow public InventoryPlayer inventory;
    @Shadow protected boolean sleeping;
    @Shadow @Nullable public BlockPos bedLocation;
    @Shadow private int sleepTimer;
    @Shadow @Nullable private BlockPos spawnChunk;
    @Shadow private boolean spawnForced;
    @Shadow public abstract void setSpawnPoint(BlockPos pos, boolean forced);

    private static final String PERSISTED_NBT_TAG = "PlayerPersisted";

    private Int2ObjectOpenHashMap<BlockPos> spawnChunkMap = new Int2ObjectOpenHashMap<>();
    private IntSet spawnForcedSet = new IntOpenHashSet();

    /**
     * @author Minecrell
     * @reason Return the appropriate bed location for the current dimension
     */
    @Overwrite @Nullable
    public BlockPos getBedLocation() { // getBedLocation
        return getBedLocation(this.dimension);
    }

    @Override
    public BlockPos getBedLocation(int dimension) {
        return dimension == 0 ? this.spawnChunk : this.spawnChunkMap.get(dimension);
    }

    /**
     * @author Minecrell
     * @reason Return the appropriate spawn forced flag for the current dimension
     */
    @Overwrite
    public boolean isSpawnForced() { // isSpawnForced
        return isSpawnForced(this.dimension);
    }

    @Override
    public boolean isSpawnForced(int dimension) {
        return dimension == 0 ? this.spawnForced : this.spawnForcedSet.contains(dimension);
    }

    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void onSetSpawnPoint(BlockPos pos, boolean forced, CallbackInfo ci) {
        if (this.dimension != 0) {
            setSpawnChunk(pos, forced, this.dimension);
            ci.cancel();
        }
    }

    public void setSpawnChunk(@Nullable BlockPos pos, boolean forced, int dimension) {
        if (dimension == 0) {
            if (pos != null) {
                this.spawnChunk = pos;
                this.spawnForced = forced;
            } else {
                this.spawnChunk = null;
                this.spawnForced = false;
            }
        } else if (pos != null) {
            this.spawnChunkMap.put(dimension, pos);
            if (forced) {
                this.spawnForcedSet.add(dimension);
            } else {
                this.spawnForcedSet.remove(dimension);
            }
        } else {
            this.spawnChunkMap.remove(dimension);
            this.spawnForcedSet.remove(dimension);
        }
    }

    @Inject(method = "clonePlayer", at = @At("RETURN"))
    private void onClonePlayerReturn(EntityPlayer oldPlayer, boolean respawnFromEnd, CallbackInfo ci) {
        this.spawnChunkMap = ((MixinEntityPlayer) (Object) oldPlayer).spawnChunkMap;
        this.spawnForcedSet = ((MixinEntityPlayer) (Object) oldPlayer).spawnForcedSet;

        final NBTTagCompound old = ((MixinEntityPlayer) (Object) oldPlayer).getEntityData();
        if (old.hasKey(PERSISTED_NBT_TAG)) {
            this.getEntityData().setTag(PERSISTED_NBT_TAG, old.getCompoundTag(PERSISTED_NBT_TAG));
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    private void onReadEntityFromNBT(NBTTagCompound tagCompound, CallbackInfo ci) {
        final NBTTagList spawnList = tagCompound.getTagList("Spawns", NbtDataUtil.TAG_COMPOUND);
        for (int i = 0; i < spawnList.tagCount(); i++) {
            final NBTTagCompound spawnData = spawnList.getCompoundTagAt(i);
            int spawnDim = spawnData.getInteger("Dim");
            this.spawnChunkMap.put(spawnDim,
                    new BlockPos(spawnData.getInteger("SpawnX"), spawnData.getInteger("SpawnY"), spawnData.getInteger("SpawnZ")));
            if (spawnData.getBoolean("SpawnForced")) {
                this.spawnForcedSet.add(spawnDim);
            }
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void onWriteEntityToNBT(NBTTagCompound tagCompound, CallbackInfo ci) {
        final NBTTagList spawnList = new NBTTagList();

        ObjectIterator<Int2ObjectMap.Entry<BlockPos>> itr = this.spawnChunkMap.int2ObjectEntrySet().fastIterator();
        while (itr.hasNext()) {
            Int2ObjectMap.Entry<BlockPos> entry = itr.next();
            int dim = entry.getIntKey();
            BlockPos spawn = entry.getValue();

            NBTTagCompound spawnData = new NBTTagCompound();
            spawnData.setInteger("Dim", dim);
            spawnData.setInteger("SpawnX", spawn.getX());
            spawnData.setInteger("SpawnY", spawn.getY());
            spawnData.setInteger("SpawnZ", spawn.getZ());
            spawnData.setBoolean("SpawnForced", this.spawnForcedSet.contains(dim));
            spawnList.appendTag(spawnData);
        }

        tagCompound.setTag("Spawns", spawnList);
    }


    // Event injectors

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void onTrySleep(BlockPos bedPos, CallbackInfoReturnable<EntityPlayer.SleepResult> ci) {
        SleepingEvent.Pre event = SpongeEventFactory.createSleepingEventPre(Cause.of(NamedCause.source(this)),
                ((org.spongepowered.api.world.World) this.world).createSnapshot(bedPos.getX(), bedPos.getY(), bedPos.getZ()), this);
        if (SpongeImpl.postEvent(event)) {
            ci.setReturnValue(EntityPlayer.SleepResult.OTHER_PROBLEM);
        }
    }

    /**
     * @author Minecrell
     * @reason Implements the post sleeping events.
     */
    @Overwrite
    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
        // Sponge start (Set size after event call)
        //this.setSize(0.6F, 1.8F);
        Transform<org.spongepowered.api.world.World> newLocation = null;
        // Sponge end

        IBlockState iblockstate = this.world.getBlockState(this.bedLocation);

        if (this.bedLocation != null && iblockstate.getBlock() == Blocks.BED) {
            // Sponge start (Change block state after event call)
            //this.world.setBlockState(this.playerLocation, iblockstate.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
            // Sponge end
            BlockPos blockpos = BlockBed.getSafeExitLocation(this.world, this.bedLocation, 0);

            if (blockpos == null) {
                blockpos = this.bedLocation.up();
            }

            // Sponge start (Store position for later)
            /*this.setPosition((double) ((float) blockpos.getX() + 0.5F), (double) ((float) blockpos.getY() + 0.1F),
                    (double) ((float) blockpos.getZ() + 0.5F));*/
            newLocation = getTransform().setPosition(new Vector3d(blockpos.getX() + 0.5F, blockpos.getY() + 0.1F, blockpos.getZ() + 0.5F));
            // Sponge end
        }

        // Sponge start
        BlockSnapshot bed = getWorld().createSnapshot(VecHelper.toVector3i(this.bedLocation));
        SleepingEvent.Post event = SpongeEventFactory.createSleepingEventPost(Cause.of(NamedCause.source(this)), bed,
                Optional.ofNullable(newLocation), this, setSpawn);
        if (SpongeImpl.postEvent(event)) {
            return;
        }

        // Moved from above
        this.setSize(0.6F, 1.8F);
        if (newLocation != null) {
            // Set property only if bed still exists
            this.world.setBlockState(this.bedLocation, iblockstate.withProperty(BlockBed.OCCUPIED, false), 4);
        }

        // Teleport player
        event.getSpawnTransform().ifPresent(this::setLocationAndAngles);
        // Sponge end

        this.sleeping = false;

        if (!this.world.isRemote && updateWorldFlag) {
            this.world.updateAllPlayersSleepingFlag();
        }

        this.sleepTimer = immediately ? 0 : 100;

        if (setSpawn) {
            this.setSpawnPoint(this.bedLocation, false);
        }

        // Sponge start
        SpongeImpl.postEvent(SpongeEventFactory.createSleepingEventFinish(event.getCause(), bed, this));
        // Sponge end
    }

}
