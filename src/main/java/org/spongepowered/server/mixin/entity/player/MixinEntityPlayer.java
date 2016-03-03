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
package org.spongepowered.server.mixin.entity.player;

import com.flowpowered.math.vector.Vector3d;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.action.SleepingEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    @Shadow @Nullable public BlockPos playerLocation;
    @Shadow private int sleepTimer;
    @Shadow @Nullable private BlockPos spawnChunk;
    @Shadow private boolean spawnForced;
    @Shadow private net.minecraft.item.ItemStack itemInUse;
    @Shadow private int itemInUseCount;

    @Shadow protected abstract void onItemUseFinish();
    @Shadow public abstract void setSpawnPoint(BlockPos pos, boolean forced);

    private static final String PERSISTED_NBT_TAG = "PlayerPersisted";

    private TIntObjectMap<BlockPos> spawnChunkMap = new TIntObjectHashMap<>();
    private TIntSet spawnForcedSet = new TIntHashSet();

    /**
     * @author Minecrell
     * @reason Return the appropriate bed location for the current dimension
     */
    @Overwrite @Nullable
    public BlockPos getBedLocation() {
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
    public boolean isSpawnForced() {
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
    private void onClonePlayerReturn(EntityPlayer oldPlayerMc, boolean respawnFromEnd, CallbackInfo ci) {
        MixinEntityPlayer oldPlayer = (MixinEntityPlayer) (Object) oldPlayerMc;
        this.spawnChunkMap = oldPlayer.spawnChunkMap;
        this.spawnForcedSet = oldPlayer.spawnForcedSet;

        final NBTTagCompound old = oldPlayer.getEntityData();
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
        this.spawnChunkMap.forEachEntry((dim, spawn) -> {
            NBTTagCompound spawnData = new NBTTagCompound();
            spawnData.setInteger("Dim", dim);
            spawnData.setInteger("SpawnX", spawn.getX());
            spawnData.setInteger("SpawnY", spawn.getY());
            spawnData.setInteger("SpawnZ", spawn.getZ());
            spawnData.setBoolean("SpawnForced", this.spawnForcedSet.contains(dim));
            spawnList.appendTag(spawnData);
            return true;
        });
        tagCompound.setTag("Spawns", spawnList);
    }


    // Event injectors

    @Inject(method = "interactWith", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/EntityPlayer"
            + ".getCurrentEquippedItem()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void onInteractWith(net.minecraft.entity.Entity entity, CallbackInfoReturnable<Boolean> cir) {
        InteractEntityEvent.Secondary event = SpongeEventFactory.createInteractEntityEventSecondary(Cause.of(NamedCause.source(this)),
                Optional.empty(), (Entity) entity);
        if (SpongeImpl.postEvent(event)) {
            cir.setReturnValue(false);
        }
    }

    private Transaction<ItemStackSnapshot> createTransaction(net.minecraft.item.ItemStack stack) {
        ItemStackSnapshot itemSnapshot = ((ItemStack) stack).createSnapshot();
        return new Transaction<>(itemSnapshot, itemSnapshot.copy());
    }

    @Inject(method = "setItemInUse", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;itemInUse:Lnet/minecraft/item/ItemStack;", opcode = Opcodes.PUTFIELD), cancellable = true)
    private void onSetItemInUse(net.minecraft.item.ItemStack stack, int duration, CallbackInfo ci) {
        // Handle logic on our own
        ci.cancel();

        UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(Cause.of(NamedCause.source(this)), duration, duration,
                createTransaction(stack));

        if (!SpongeImpl.postEvent(event)) {
            this.itemInUse = stack;
            this.itemInUseCount = event.getRemainingDuration();

            if (!this.worldObj.isRemote) {
                this.setEating(true);
            }
        }
    }

    @Inject(method = "onUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;itemInUseCount:I", opcode = Opcodes.GETFIELD))
    private void callUseItemStackTick(CallbackInfo ci) {
        UseItemStackEvent.Tick event = SpongeEventFactory.createUseItemStackEventTick(Cause.of(NamedCause.source(this)),
                this.itemInUseCount, this.itemInUseCount, createTransaction(this.itemInUse));

        this.itemInUseCount = SpongeImpl.postEvent(event) ? -1 : event.getRemainingDuration();
        if (this.itemInUseCount <= 0) {
            onItemUseFinish();
        }
    }

    @Redirect(method = "stopUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onPlayerStoppedUsing"
            + "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;I)V"))
    private void callUseItemStackStop(net.minecraft.item.ItemStack stack, World world, EntityPlayer player, int remainingDuration) {
        UseItemStackEvent.Stop event = SpongeEventFactory.createUseItemStackEventStop(Cause.of(NamedCause.source(this)),
                this.itemInUseCount, this.itemInUseCount, createTransaction(stack));

        if (!SpongeImpl.postEvent(event)) {
            stack.onPlayerStoppedUsing(world, player, remainingDuration);
        }
    }

    @Redirect(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onItemUseFinish"
            + "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"))
    private net.minecraft.item.ItemStack callUseItemStackFinish(net.minecraft.item.ItemStack stack, World world, EntityPlayer player) {
        net.minecraft.item.ItemStack result = stack.onItemUseFinish(world, player);
        Transaction<ItemStackSnapshot> resultTransaction = new Transaction<>(((ItemStack) stack).createSnapshot(),
                ((ItemStack) result).createSnapshot());

        UseItemStackEvent.Finish event = SpongeEventFactory.createUseItemStackEventFinish(Cause.of(NamedCause.source(this)),
                this.itemInUseCount, this.itemInUseCount, createTransaction(stack), resultTransaction);

        // TODO: Handle cancellation
        SpongeImpl.postEvent(event);
        return (net.minecraft.item.ItemStack) event.getItemStackResult().getFinal().createStack();
    }

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void onTrySleep(BlockPos bedPos, CallbackInfoReturnable<EntityPlayer.EnumStatus> ci) {
        SleepingEvent.Pre event = SpongeEventFactory.createSleepingEventPre(Cause.of(NamedCause.source(this)),
                ((org.spongepowered.api.world.World) this.worldObj).createSnapshot(bedPos.getX(), bedPos.getY(), bedPos.getZ()), this);
        if (SpongeImpl.postEvent(event)) {
            ci.setReturnValue(EntityPlayer.EnumStatus.OTHER_PROBLEM);
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

        IBlockState iblockstate = this.worldObj.getBlockState(this.playerLocation);

        if (this.playerLocation != null && iblockstate.getBlock() == Blocks.bed) {
            // Sponge start (Change block state after event call)
            //this.worldObj.setBlockState(this.playerLocation, iblockstate.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
            // Sponge end
            BlockPos blockpos = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);

            if (blockpos == null) {
                blockpos = this.playerLocation.up();
            }

            // Sponge start (Store position for later)
            /*this.setPosition((double) ((float) blockpos.getX() + 0.5F), (double) ((float) blockpos.getY() + 0.1F),
                    (double) ((float) blockpos.getZ() + 0.5F));*/
            newLocation = getTransform().setPosition(new Vector3d(blockpos.getX() + 0.5F, blockpos.getY() + 0.1F, blockpos.getZ() + 0.5F));
            // Sponge end
        }

        // Sponge start
        BlockSnapshot bed = getWorld().createSnapshot(VecHelper.toVector3i(this.playerLocation));
        SleepingEvent.Post event = SpongeEventFactory.createSleepingEventPost(Cause.of(NamedCause.source(this)), bed,
                Optional.ofNullable(newLocation), this, setSpawn);

        if (SpongeImpl.postEvent(event)) {
            return;
        }

        // It's time to get up!

        // Moved from above
        this.setSize(0.6F, 1.8F);
        this.worldObj.setBlockState(this.playerLocation, iblockstate.withProperty(BlockBed.OCCUPIED, false), 4);

        // Teleport player
        event.getSpawnTransform().ifPresent(this::setTransform);
        // Sponge end

        this.sleeping = false;

        if (!this.worldObj.isRemote && updateWorldFlag) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }

        this.sleepTimer = immediately ? 0 : 100;

        if (setSpawn) {
            this.setSpawnPoint(this.playerLocation, false);
        }

        // Sponge start
        SpongeImpl.postEvent(SpongeEventFactory.createSleepingEventFinish(Cause.of(NamedCause.source(this)), bed, this));
        // Sponge end
    }

    // Vanilla fixes

    /**
     * @author simon816
     * @reason Fix player's ArmorEquipable methods not setting the right slot
     */
    /*@Override
    public void setCurrentItemOrArmor(int slotIn, net.minecraft.item.ItemStack stack) {
        // Fix issue in player where it doesn't take into account selected item
        if (slotIn == 0) {
            this.inventory.mainInventory[this.inventory.currentItem] = stack;
        } else {
            this.inventory.armorInventory[slotIn - 1] = stack;
        }
    }*/

}
