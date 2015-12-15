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

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.Entity;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.server.interfaces.IMixinEntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mixin(value = EntityPlayer.class, priority = 1001)
public abstract class MixinEntityPlayer extends EntityLivingBase implements Entity {
    private static final String PERSISTED_NBT_TAG = "PlayerPersisted";

    private HashMap<Integer, BlockPos> spawnChunkMap = Maps.newHashMap();
    private HashMap<Integer, Boolean> spawnForcedMap = Maps.newHashMap();

    @Shadow protected BlockPos spawnChunk;
    @Shadow private boolean spawnForced;
    @Shadow private net.minecraft.item.ItemStack itemInUse;
    @Shadow private int itemInUseCount;

    @Shadow protected abstract void onItemUseFinish();

    protected MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    public void onSetSpawnPoint(BlockPos pos, boolean forced, CallbackInfo ci) {
        if (this.dimension != 0) {
            setSpawnChunk(pos, forced, this.dimension);
            ci.cancel();
        }
    }

    @Inject(method = "clonePlayer", at = @At("RETURN"))
    public void onClonePlayerEnd(EntityPlayer oldPlayer, boolean respawnFromEnd, CallbackInfo ci) {
        this.spawnChunkMap = ((IMixinEntityPlayer) oldPlayer).getSpawnChunkMap();
        this.spawnForcedMap = ((IMixinEntityPlayer) oldPlayer).getSpawnForcedMap();

        final NBTTagCompound old = ((IMixinEntityPlayer) oldPlayer).getEntityData();
        if (old.hasKey(PERSISTED_NBT_TAG)) {
            ((IMixinEntityPlayer) this).getEntityData().setTag(PERSISTED_NBT_TAG, old.getCompoundTag(PERSISTED_NBT_TAG));
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At(value = "FIELD", target = "net.minecraft.entity.player.EntityPlayer"
            + ".foodStats:Lnet/minecraft/util/FoodStats;"))
    public void onReadEntityFromNBT(NBTTagCompound tagCompound, CallbackInfo ci) {
        final NBTTagList spawnlist = tagCompound.getTagList("Spawns", 10);
        for (int i = 0; i < spawnlist.tagCount(); i++) {
            final NBTTagCompound spawndata = spawnlist.getCompoundTagAt(i);
            int spawndim = spawndata.getInteger("Dim");
            this.spawnChunkMap.put(spawndim, new BlockPos(spawndata.getInteger("SpawnX"), spawndata.getInteger("SpawnY"), spawndata.getInteger("SpawnZ")));
            this.spawnForcedMap.put(spawndim, spawndata.getBoolean("SpawnForced"));
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At(value = "FIELD", target = "net.minecraft.entity.player.EntityPlayer"
            + ".foodStats:Lnet/minecraft/util/FoodStats;"))
    public void onWriteEntityToNBT(NBTTagCompound tagCompound, CallbackInfo ci) {
        final NBTTagList spawnlist = new NBTTagList();
        for (Map.Entry<Integer, BlockPos> entry : this.spawnChunkMap.entrySet()) {
            BlockPos spawn = entry.getValue();
            if (spawn == null) {
                continue;
            }
            Boolean forced = this.spawnForcedMap.get(entry.getKey());
            if (forced == null) {
                forced = false;
            }
            NBTTagCompound spawndata = new NBTTagCompound();
            spawndata.setInteger("Dim", entry.getKey());
            spawndata.setInteger("SpawnX", spawn.getX());
            spawndata.setInteger("SpawnY", spawn.getY());
            spawndata.setInteger("SpawnZ", spawn.getZ());
            spawndata.setBoolean("SpawnForced", forced);
            spawnlist.appendTag(spawndata);
        }
        tagCompound.setTag("Spawns", spawnlist);
    }

    @Inject(method = "interactWith", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/EntityPlayer"
            + ".getCurrentEquippedItem()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    public void onInteractWith(net.minecraft.entity.Entity entity, CallbackInfoReturnable<Boolean> cir) {
        InteractEntityEvent.Secondary event = SpongeEventFactory.createInteractEntityEventSecondary(SpongeImpl.getGame(),
                Cause.of(NamedCause.source(this)), Optional.empty(), (Entity) entity);
        if (SpongeImpl.postEvent(event)) {
            cir.setReturnValue(false);
        }
    }

    private Transaction<ItemStackSnapshot> createTransaction(net.minecraft.item.ItemStack stack) {
        ItemStackSnapshot itemSnapshot = ((ItemStack) stack).createSnapshot();
        return new Transaction<>(itemSnapshot, itemSnapshot.copy());
    }

    @Inject(method = "setItemInUse", at = @At(value = "FIELD", target = "itemInUse", opcode = Opcodes.PUTFIELD), cancellable = true)
    public void onSetItemInUse(net.minecraft.item.ItemStack stack, int duration, CallbackInfo ci) {
        // Handle logic on our own
        ci.cancel();

        UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(SpongeImpl.getGame(), Cause.of(NamedCause.source(this)),
                duration, duration, createTransaction(stack));

        if (!SpongeImpl.postEvent(event)) {
            this.itemInUse = stack;
            this.itemInUseCount = event.getRemainingDuration();

            if (!this.worldObj.isRemote) {
                this.setEating(true);
            }
        }
    }

    @Inject(method = "onUpdate", at = @At(value = "FIELD", target = "itemInUseCount", opcode = Opcodes.GETFIELD))
    public void callUseItemStackTick(CallbackInfo ci) {
        UseItemStackEvent.Tick event = SpongeEventFactory.createUseItemStackEventTick(SpongeImpl.getGame(), Cause.of(NamedCause.source(this)),
                this.itemInUseCount, this.itemInUseCount, createTransaction(this.itemInUse));

        this.itemInUseCount = SpongeImpl.postEvent(event) ? -1 : event.getRemainingDuration();
        if (this.itemInUseCount <= 0) {
            onItemUseFinish();
        }
    }

    @Redirect(method = "stopUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onPlayerStoppedUsing"
            + "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;I)V"))
    public void callUseItemStackStop(net.minecraft.item.ItemStack stack, World world, EntityPlayer player, int remainingDuration) {
        UseItemStackEvent.Stop event = SpongeEventFactory.createUseItemStackEventStop(SpongeImpl.getGame(), Cause.of(NamedCause.source(this)),
                this.itemInUseCount, this.itemInUseCount, createTransaction(stack));

        if (!SpongeImpl.postEvent(event)) {
            stack.onPlayerStoppedUsing(world, player, remainingDuration);
        }
    }

    @Redirect(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;onItemUseFinish"
            + "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"))
    public net.minecraft.item.ItemStack callUseItemStackFinish(net.minecraft.item.ItemStack stack, World world, EntityPlayer player) {
        net.minecraft.item.ItemStack result = stack.onItemUseFinish(world, player);
        Transaction<ItemStackSnapshot> resultTransaction = new Transaction<>(((ItemStack) stack).createSnapshot(),
                ((ItemStack) result).createSnapshot());

        UseItemStackEvent.Finish event = SpongeEventFactory.createUseItemStackEventFinish(SpongeImpl.getGame(), Cause.of(NamedCause.source(this)),
                this.itemInUseCount, this.itemInUseCount, createTransaction(stack), resultTransaction);

        // TODO: Handle cancellation
        SpongeImpl.postEvent(event);
        return (net.minecraft.item.ItemStack) event.getItemStackResult().getFinal().createStack();
    }

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    public void onTrySleep(BlockPos bedPos, CallbackInfoReturnable<EntityPlayer.EnumStatus> ci) {
        SleepingEvent.Pre event = SpongeEventFactory.createSleepingEventPre(SpongeImpl.getGame(), Cause.of(NamedCause.source(this)),
                ((org.spongepowered.api.world.World) this.worldObj).createSnapshot(bedPos.getX(), bedPos.getY(), bedPos.getZ()), this);
        if (SpongeImpl.postEvent(event)) {
            ci.setReturnValue(EntityPlayer.EnumStatus.OTHER_PROBLEM);
        }
    }

    public void setSpawnChunk(BlockPos pos, boolean forced, int dimension) {
        if (dimension == 0) {
            if (pos != null) {
                this.spawnChunk = pos;
                this.spawnForced = forced;
            } else {
                this.spawnChunk = null;
                this.spawnForced = false;
            }
            return;
        }

        if (pos != null) {
            this.spawnChunkMap.put(dimension, pos);
            this.spawnForcedMap.put(dimension, forced);
        } else {
            this.spawnChunkMap.remove(dimension);
            this.spawnForcedMap.remove(dimension);
        }
    }

    public BlockPos getBedLocation(int dimension) {
        return dimension == 0 ? this.spawnChunk : this.spawnChunkMap.get(dimension);
    }

    public boolean isSpawnForced(int dimension) {
        if (this.dimension == 0) {
            return this.spawnForced;
        }
        final Boolean forced = this.spawnForcedMap.get(dimension);
        return forced == null ? false : forced;
    }
}
