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
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import java.util.Optional;

import javax.annotation.Nullable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    @Shadow public abstract CombatTracker getCombatTracker();
    @Shadow public abstract void setHeldItem(EnumHand hand, @Nullable ItemStack stack);
    @Shadow public abstract int getItemInUseCount();
    @Shadow public abstract void resetActiveHand();
    @Shadow public abstract EnumHand getActiveHand();

    @Shadow @Nullable protected ItemStack activeItemStack;
    @Shadow protected int activeItemStackUseCount;

    protected MixinEntityLivingBase(World world) {
        super(world);
    }

    @Inject(method = "setActiveHand", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;activeItemStack:Lnet/minecraft/item/ItemStack;"))
    private void onSetActiveItemStack(EnumHand hand, CallbackInfo ci, ItemStack stack) {
        Sponge.getCauseStackManager().pushCause(this);
        UseItemStackEvent.Start event = SpongeEventFactory.createUseItemStackEventStart(Sponge.getCauseStackManager().getCurrentCause(),
                stack.getMaxItemUseDuration(), stack.getMaxItemUseDuration(), Optional.of(hand), ItemStackUtil.snapshotOf(stack));
        if (SpongeImpl.postEvent(event)) {
            ci.cancel();
        } else {
            this.activeItemStackUseCount = event.getRemainingDuration();
        }
        Sponge.getCauseStackManager().popCause();
    }

    @Redirect(method = "setActiveHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxItemUseDuration()I"))
    private int getItemDuration(ItemStack stack) {
        return this.activeItemStackUseCount; // We've already set the new duration
    }

    @Redirect(method = "updateActiveHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getItemInUseCount()I", ordinal = 0))
    private int onGetRemainingItemDuration(EntityLivingBase self) {
        Sponge.getCauseStackManager().pushCause(this);
        UseItemStackEvent.Tick event = SpongeEventFactory.createUseItemStackEventTick(Sponge.getCauseStackManager().getCurrentCause(),
                this.activeItemStackUseCount, this.activeItemStackUseCount, Optional.of(self.getActiveHand()), ItemStackUtil.snapshotOf(this.activeItemStack));
        SpongeImpl.postEvent(event);
        Sponge.getCauseStackManager().popCause();
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
        Sponge.getCauseStackManager().pushCause(this);
        UseItemStackEvent.Finish event = SpongeEventFactory.createUseItemStackEventFinish(Sponge.getCauseStackManager().getCurrentCause(),
                this.activeItemStackUseCount, this.activeItemStackUseCount, Optional.of(self.getActiveHand()), ItemStackUtil.snapshotOf(this.activeItemStack));
        SpongeImpl.postEvent(event);
        Sponge.getCauseStackManager().popCause();
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
        Sponge.getCauseStackManager().pushCause(this);
        UseItemStackEvent.Replace event = SpongeEventFactory.createUseItemStackEventReplace(Sponge.getCauseStackManager().getCurrentCause(),
                this.activeItemStackUseCount, this.activeItemStackUseCount, Optional.of(hand), activeItemStackSnapshot,
                new Transaction<>(activeItemStackSnapshot, ItemStackUtil.snapshotOf(stack)));

        if (SpongeImpl.postEvent(event)) {
            Sponge.getCauseStackManager().popCause();
            return; // Don't touch the held item if event is cancelled
        }
        Sponge.getCauseStackManager().popCause();

        if (!event.getItemStackResult().isValid()) {
            return;
        }

        setHeldItem(hand, ItemStackUtil.fromSnapshotToNative(event.getItemStackResult().getFinal()));
    }

    @Redirect(method = "stopActiveHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;"
            + "onPlayerStoppedUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;I)V")) // stopActiveHand
    private void onStopPlayerUsing(ItemStack stack, World world, EntityLivingBase self, int duration) {
        Sponge.getCauseStackManager().pushCause(this);
        if (!SpongeImpl.postEvent(SpongeEventFactory.createUseItemStackEventStop(Sponge.getCauseStackManager().getCurrentCause(),
                duration, duration, Optional.of(self.getActiveHand()), ItemStackUtil.snapshotOf(stack)))) {
            stack.onPlayerStoppedUsing(world, self, duration);
        }
        Sponge.getCauseStackManager().popCause();
    }

    @Inject(method = "resetActiveHand", at = @At("HEAD"))
    private void onResetActiveHand(CallbackInfo ci) {
        Sponge.getCauseStackManager().pushCause(this);
        SpongeImpl.postEvent(SpongeEventFactory.createUseItemStackEventReset(Sponge.getCauseStackManager().getCurrentCause(),
                this.activeItemStackUseCount, this.activeItemStackUseCount, Optional.of(self.getActiveHand()), ItemStackUtil.snapshotOf(this.activeItemStack)));
        Sponge.getCauseStackManager().popCause();
    }

}
