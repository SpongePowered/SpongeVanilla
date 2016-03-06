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
package org.spongepowered.server.mixin.entity;

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
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.StaticMixinHelper;

import java.util.Optional;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    @Shadow public abstract CombatTracker getCombatTracker();
    @Shadow public abstract net.minecraft.item.ItemStack getHeldItem(EnumHand hand);
    @Shadow public abstract EnumHand func_184600_cs();
    @Shadow public abstract boolean func_184587_cr();
    @Shadow public abstract int getItemInUseCount();
    @Shadow protected abstract void updateItemUse(net.minecraft.item.ItemStack p_184584_1_, int p_184584_2_);
    @Shadow public abstract void setHeldItem(EnumHand hand, net.minecraft.item.ItemStack stack);
    @Shadow protected net.minecraft.item.ItemStack field_184627_bm;
    @Shadow protected int field_184628_bn;

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

            // Store cause for drop event which is called after this event
            if (sourceCreator.isPresent()) {
                StaticMixinHelper.dropCause = Cause.of(NamedCause.source(this), NamedCause.of("Attacker", source), NamedCause.owner(sourceCreator.get()));
            } else {
                StaticMixinHelper.dropCause = Cause.of(NamedCause.source(this), NamedCause.of("Attacker", source));
            }
        }
    }

    @Overwrite
    public void func_184598_c(EnumHand hand) {
        net.minecraft.item.ItemStack itemstack = this.getHeldItem(hand);

        if (itemstack != null && !this.func_184587_cr()) {

            UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(Cause.of(NamedCause.source(this)), itemstack.getMaxItemUseDuration(), itemstack.getMaxItemUseDuration(),
                    ((ItemStack) itemstack).createSnapshot());

            if (!SpongeImpl.postEvent(event)) {
                this.field_184627_bm = itemstack;
                this.field_184628_bn = event.getRemainingDuration();

                if (!this.worldObj.isRemote) {
                    int i = 1;

                    if (hand == EnumHand.OFF_HAND) {
                        i |= 2;
                    }

                    this.dataWatcher.set(EntityLivingBase.HAND_STATES, (byte) i);
                }
            }
        }
    }


    @Overwrite
    private void func_184608_ct() {
        if (this.func_184587_cr()) {
            net.minecraft.item.ItemStack itemstack = this.getHeldItem(this.func_184600_cs());

            if (itemstack == this.field_184627_bm) {
                UseItemStackEvent.Tick event = SpongeEventFactory.createUseItemStackEventTick(Cause.of(NamedCause.source(this)),
                        this.field_184628_bn, this.field_184628_bn, ((ItemStack) this.field_184627_bm).createSnapshot());

                SpongeImpl.postEvent(event);

                this.field_184628_bn = event.getRemainingDuration();

                if (!event.isCancelled() && this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0) {
                    this.updateItemUse(this.field_184627_bm, 5);
                }

                if (--this.field_184628_bn == 0 && !this.worldObj.isRemote) {
                    this.onItemUseFinish();
                }
            } else {
                this.func_184602_cy();
            }
        }
    }

    @Overwrite
    public void onItemUseFinish() {
        if (this.field_184627_bm != null && this.func_184587_cr()) {

            UseItemStackEvent.Tick tickEvent = SpongeEventFactory.createUseItemStackEventTick(Cause.of(NamedCause.source(this)),
                    this.field_184628_bn, this.field_184628_bn, ((ItemStack) this.field_184627_bm).createSnapshot());
            SpongeImpl.postEvent(tickEvent);
            if (tickEvent.getRemainingDuration() != 0) {
                this.field_184628_bn = tickEvent.getRemainingDuration();
                return;
            }

            if (!tickEvent.isCancelled()) {
                this.updateItemUse(this.field_184627_bm, 16);
            }

            UseItemStackEvent.Finish finishEvent = SpongeEventFactory.createUseItemStackEventFinish(Cause.of(NamedCause.source(this)),
                    this.field_184628_bn, this.field_184628_bn, ((ItemStack) this.field_184627_bm).createSnapshot());

            SpongeImpl.postEvent(finishEvent);

            if (finishEvent.getRemainingDuration() != 0) {
                this.field_184628_bn = finishEvent.getRemainingDuration();
                return;
            }

            if (!finishEvent.isCancelled()) {

                net.minecraft.item.ItemStack itemstack = this.field_184627_bm.onItemUseFinish(this.worldObj, (EntityLivingBase) (Object) this);

                if (itemstack != null && itemstack.stackSize == 0) {
                    itemstack = null;
                }

                ItemStackSnapshot currentSnapshot = ((ItemStack) this.field_184627_bm).createSnapshot();
                ItemStackSnapshot replacementSnapshot = itemstack == null ? ItemStackSnapshot.NONE : ((ItemStack) itemstack).createSnapshot();

                UseItemStackEvent.Replace replaceEvent = SpongeEventFactory.createUseItemStackEventReplace(Cause.of(NamedCause.source(this)),
                        this.field_184628_bn, this.field_184628_bn, ((ItemStack) this.field_184627_bm).createSnapshot(),
                        new Transaction<>(currentSnapshot, replacementSnapshot));

                if (!SpongeImpl.postEvent(replaceEvent)) {
                    ItemStack stack = replaceEvent.getItemStackResult().getFinal().createStack();

                    this.setHeldItem(this.func_184600_cs(), stack.getItem() == ItemTypes.NONE ? null : (net.minecraft.item.ItemStack) stack);
                }
            }
            this.func_184602_cy();
        }
    }

    @Overwrite
    public void func_184597_cx() {
        UseItemStackEvent.Stop event = SpongeEventFactory.createUseItemStackEventStop(Cause.of(NamedCause.source(this)),
                this.field_184628_bn, this.field_184628_bn, this.createSnapshot(this.field_184627_bm));

        if (!SpongeImpl.postEvent(event) && this.field_184627_bm != null) {
            this.field_184627_bm.onPlayerStoppedUsing(this.worldObj, (EntityLivingBase) (Object) this, this.getItemInUseCount());
        }

        this.func_184602_cy();
    }

    @Overwrite
    public void func_184602_cy() {
        if (!this.worldObj.isRemote) {
            this.dataWatcher.set(EntityLivingBase.HAND_STATES, Byte.valueOf((byte)0));
        }

        SpongeImpl.postEvent(SpongeEventFactory.createUseItemStackEventReset(Cause.of(NamedCause.source(this)),
                this.field_184628_bn, this.field_184628_bn, this.createSnapshot(this.field_184627_bm)));

        this.field_184627_bm = null;
        this.field_184628_bn = 0;
    }

    private ItemStackSnapshot createSnapshot(net.minecraft.item.ItemStack stack) {
        return stack  == null ? ItemStackSnapshot.NONE : ((ItemStack) stack).createSnapshot();
    }
}
