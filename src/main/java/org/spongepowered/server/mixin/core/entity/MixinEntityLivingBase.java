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
package org.spongepowered.server.mixin.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.interfaces.entity.IMixinEntity;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;
import org.spongepowered.common.text.SpongeTexts;

import java.util.Optional;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    @Shadow public abstract CombatTracker getCombatTracker();
    @Shadow public abstract net.minecraft.item.ItemStack getHeldItem(EnumHand hand);
    @Shadow public abstract EnumHand getActiveHand();
    @Shadow public abstract boolean isHandActive();
    @Shadow public abstract int getItemInUseCount();
    @Shadow protected abstract void updateItemUse(net.minecraft.item.ItemStack p_184584_1_, int p_184584_2_);
    @Shadow public abstract void setHeldItem(EnumHand hand, net.minecraft.item.ItemStack stack);
    @Shadow protected net.minecraft.item.ItemStack activeItemStack;
    @Shadow protected int activeItemStackUseCount;

    protected MixinEntityLivingBase() {
        super(null);
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void callDestructEntityLivingBase(DamageSource source, CallbackInfo ci) {
        callDestructEntityEventDeath(source, ci);
    }

    protected void callDestructEntityEventDeath(DamageSource source, CallbackInfo ci) {
        MessageChannel originalChannel = this instanceof Player ? ((Player) this).getMessageChannel() : MessageChannel.TO_NONE;
        Text deathMessage = SpongeTexts.toText(getCombatTracker().getDeathMessage());

        Optional<User> sourceCreator = Optional.empty();

        Cause cause;
        if (source instanceof EntityDamageSource) {
            EntityDamageSource damageSource = (EntityDamageSource) source;
            IMixinEntity spongeEntity = (IMixinEntity) damageSource.getSourceOfDamage();
            sourceCreator = spongeEntity.getTrackedPlayer(NbtDataUtil.SPONGE_ENTITY_CREATOR);
        }

        if (sourceCreator.isPresent()) {
            cause = Cause.of(NamedCause.source(source), NamedCause.of("Victim", this), NamedCause.owner(sourceCreator.get()));
        } else {
            cause = Cause.of(NamedCause.source(source), NamedCause.of("Victim", this));
        }

        DestructEntityEvent.Death event = SpongeEventFactory.createDestructEntityEventDeath(
                cause, originalChannel, Optional.of(originalChannel), new MessageEvent.MessageFormatter(deathMessage), (Living) this, false
        );
        if (!SpongeImpl.postEvent(event)) {
            if (!event.isMessageCancelled()) {
                event.getChannel().ifPresent(channel -> channel.send(this, event.getMessage()));
            }
        }
    }

    @Overwrite
    public void setActiveHand(EnumHand hand) {
        net.minecraft.item.ItemStack itemstack = this.getHeldItem(hand);

        if (itemstack != null && !this.isHandActive()) {

            UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(Cause.of(NamedCause.source(this)), itemstack.getMaxItemUseDuration(), itemstack.getMaxItemUseDuration(),
                    ((ItemStack) itemstack).createSnapshot());

            if (!SpongeImpl.postEvent(event)) {
                this.activeItemStack = itemstack;
                this.activeItemStackUseCount = event.getRemainingDuration();

                if (!this.worldObj.isRemote) {
                    int i = 1;

                    if (hand == EnumHand.OFF_HAND) {
                        i |= 2;
                    }

                    this.dataManager.set(EntityLivingBase.HAND_STATES, (byte) i);
                }
            }
        }
    }


    @Overwrite
    private void updateActiveHand() {
        if (this.isHandActive()) {
            net.minecraft.item.ItemStack itemstack = this.getHeldItem(this.getActiveHand());

            if (itemstack == this.activeItemStack) {
                UseItemStackEvent.Tick event = SpongeEventFactory.createUseItemStackEventTick(Cause.of(NamedCause.source(this)),
                        this.activeItemStackUseCount, this.activeItemStackUseCount, ((ItemStack) this.activeItemStack).createSnapshot());

                SpongeImpl.postEvent(event);

                this.activeItemStackUseCount = event.getRemainingDuration();

                if (!event.isCancelled() && this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0) {
                    this.updateItemUse(this.activeItemStack, 5);
                }

                if (--this.activeItemStackUseCount == 0 && !this.worldObj.isRemote) {
                    this.onItemUseFinish();
                }
            } else {
                this.resetActiveHand();
            }
        }
    }

    @Overwrite
    public void onItemUseFinish() {
        if (this.activeItemStack != null && this.isHandActive()) {

            UseItemStackEvent.Tick tickEvent = SpongeEventFactory.createUseItemStackEventTick(Cause.of(NamedCause.source(this)),
                    this.activeItemStackUseCount, this.activeItemStackUseCount, ((ItemStack) this.activeItemStack).createSnapshot());
            SpongeImpl.postEvent(tickEvent);
            if (tickEvent.getRemainingDuration() != 0) {
                this.activeItemStackUseCount = tickEvent.getRemainingDuration();
                return;
            }

            if (!tickEvent.isCancelled()) {
                this.updateItemUse(this.activeItemStack, 16);
            }

            UseItemStackEvent.Finish finishEvent = SpongeEventFactory.createUseItemStackEventFinish(Cause.of(NamedCause.source(this)),
                    this.activeItemStackUseCount, this.activeItemStackUseCount, ((ItemStack) this.activeItemStack).createSnapshot());

            SpongeImpl.postEvent(finishEvent);

            if (finishEvent.getRemainingDuration() != 0) {
                this.activeItemStackUseCount = finishEvent.getRemainingDuration();
                return;
            }

            if (!finishEvent.isCancelled()) {

                net.minecraft.item.ItemStack itemstack = this.activeItemStack.onItemUseFinish(this.worldObj, (EntityLivingBase) (Object) this);

                if (itemstack != null && itemstack.stackSize == 0) {
                    itemstack = null;
                }

                ItemStackSnapshot currentSnapshot = ((ItemStack) this.activeItemStack).createSnapshot();
                ItemStackSnapshot replacementSnapshot = itemstack == null ? ItemStackSnapshot.NONE : ((ItemStack) itemstack).createSnapshot();

                UseItemStackEvent.Replace replaceEvent = SpongeEventFactory.createUseItemStackEventReplace(Cause.of(NamedCause.source(this)),
                        this.activeItemStackUseCount, this.activeItemStackUseCount, ((ItemStack) this.activeItemStack).createSnapshot(),
                        new Transaction<>(currentSnapshot, replacementSnapshot));

                if (!SpongeImpl.postEvent(replaceEvent)) {
                    ItemStack stack = replaceEvent.getItemStackResult().getFinal().createStack();

                    this.setHeldItem(this.getActiveHand(), stack.getItem() == ItemTypes.NONE ? null : (net.minecraft.item.ItemStack) stack);
                }
            }
            this.resetActiveHand();
        }
    }

    @Overwrite
    public void stopActiveHand() {
        UseItemStackEvent.Stop event = SpongeEventFactory.createUseItemStackEventStop(Cause.of(NamedCause.source(this)),
                this.activeItemStackUseCount, this.activeItemStackUseCount, ItemStackUtil.snapshotOf(this.activeItemStack));

        if (!SpongeImpl.postEvent(event) && this.activeItemStack != null) {
            this.activeItemStack.onPlayerStoppedUsing(this.worldObj, (EntityLivingBase) (Object) this, this.getItemInUseCount());
        }

        this.resetActiveHand();
    }

    @Overwrite
    public void resetActiveHand() {
        if (!this.worldObj.isRemote) {
            this.dataManager.set(EntityLivingBase.HAND_STATES, Byte.valueOf((byte)0));
        }

        SpongeImpl.postEvent(SpongeEventFactory.createUseItemStackEventReset(Cause.of(NamedCause.source(this)),
                this.activeItemStackUseCount, this.activeItemStackUseCount, ItemStackUtil.snapshotOf(this.activeItemStack)));

        this.activeItemStack = null;
        this.activeItemStackUseCount = 0;
    }

}
