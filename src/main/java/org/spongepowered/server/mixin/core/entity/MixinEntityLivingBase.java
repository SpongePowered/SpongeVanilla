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
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
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
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.interfaces.entity.IMixinEntity;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;
import org.spongepowered.common.text.SpongeTexts;

import java.util.Optional;

import javax.annotation.Nullable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    @Shadow public abstract CombatTracker getCombatTracker();
    @Shadow public abstract void setHeldItem(EnumHand hand, @Nullable ItemStack stack);
    @Shadow public abstract int getItemInUseCount();
    @Shadow public abstract void resetActiveHand();

    @Shadow @Nullable protected ItemStack activeItemStack;
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

    @Inject(method = "setActiveHand", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;activeItemStack:Lnet/minecraft/item/ItemStack;"))
    private void onSetActiveItemStack(EnumHand hand, CallbackInfo ci, ItemStack stack) {
        UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(Cause.of(NamedCause.source(this)),
                stack.getMaxItemUseDuration(), stack.getMaxItemUseDuration(), ItemStackUtil.snapshotOf(stack));
        if (SpongeImpl.postEvent(event)) {
            ci.cancel();
        } else {
            this.activeItemStackUseCount = event.getRemainingDuration();
        }
    }

    @Redirect(method = "setActiveHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxItemUseDuration()I"))
    private int getItemDuration(ItemStack stack) {
        return this.activeItemStackUseCount; // We've already set the new duration
    }

    @Redirect(method = "updateActiveHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getItemInUseCount()I", ordinal = 0))
    private int onGetRemainingItemDuration(EntityLivingBase self) {
        UseItemStackEvent.Tick event = SpongeEventFactory.createUseItemStackEventTick(Cause.of(NamedCause.source(this)),
                this.activeItemStackUseCount, this.activeItemStackUseCount, ItemStackUtil.snapshotOf(this.activeItemStack));
        SpongeImpl.postEvent(event);

        // Because the item usage will only finish if activeItemStackUseCount == 0 and decrements it first, it should be >= 1
        this.activeItemStackUseCount = Math.max(event.getRemainingDuration(), 1);

        if (event.isCancelled()) {
            // Get prepared for some cool hacks: We're within the condition for updateItemUse
            // So if we don't want it to call the method we just pass a value that makes the
            // condition evaluate to false, so an integer >= 25
            return 26;
        }

        return getItemInUseCount();
    }

    @Inject(method = "onItemUseFinish", cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;updateItemUse(Lnet/minecraft/item/ItemStack;I)V"))
    private void onUpdateItemUse(CallbackInfo ci) {
        UseItemStackEvent.Finish event = SpongeEventFactory.createUseItemStackEventFinish(Cause.of(NamedCause.source(this)),
                this.activeItemStackUseCount, this.activeItemStackUseCount, ItemStackUtil.snapshotOf(this.activeItemStack));
        SpongeImpl.postEvent(event);

        if (event.getRemainingDuration() > 0) {
            this.activeItemStackUseCount = event.getRemainingDuration();
            ci.cancel();
        } else if (event.isCancelled()) {
            resetActiveHand();
            ci.cancel();
        }
    }

    @Redirect(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;"
            + "setHeldItem(Lnet/minecraft/util/EnumHand;Lnet/minecraft/item/ItemStack;)V"))
    private void onSetHeldItem(EntityLivingBase self, EnumHand hand, @Nullable ItemStack stack) {
        ItemStackSnapshot activeItemStackSnapshot = ItemStackUtil.snapshotOf(this.activeItemStack);

        UseItemStackEvent.Replace event = SpongeEventFactory.createUseItemStackEventReplace(Cause.of(NamedCause.source(this)),
                this.activeItemStackUseCount, this.activeItemStackUseCount, activeItemStackSnapshot,
                new Transaction<>(activeItemStackSnapshot, ItemStackUtil.snapshotOf(stack)));

        if (SpongeImpl.postEvent(event)) {
            return; // Don't touch the held item if event is cancelled
        }

        if (!event.getItemStackResult().isValid()) {
            return;
        }

        setHeldItem(hand, ItemStackUtil.fromSnapshotToNative(event.getItemStackResult().getFinal()));
    }

    @Redirect(method = "stopActiveHand",at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;"
            + "onPlayerStoppedUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;I)V"))
    private void onStopPlayerUsing(ItemStack stack, World world, EntityLivingBase self, int duration) {
        if (!SpongeImpl.postEvent(SpongeEventFactory.createUseItemStackEventStop(Cause.of(NamedCause.source(this)),
                duration, duration, ItemStackUtil.snapshotOf(stack)))) {
            stack.onPlayerStoppedUsing(world, self, duration);
        }
    }

    @Inject(method = "resetActiveHand", at = @At("HEAD"))
    private void onResetActiveHand(CallbackInfo ci) {
        SpongeImpl.postEvent(SpongeEventFactory.createUseItemStackEventReset(Cause.of(NamedCause.source(this)),
                this.activeItemStackUseCount, this.activeItemStackUseCount, ItemStackUtil.snapshotOf(this.activeItemStack)));
    }

}
