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
package org.spongepowered.vanilla.mixin.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.interfaces.IMixinEntity;

import javax.annotation.Nullable;

@Mixin(net.minecraft.entity.Entity.class)
public abstract class MixinEntity implements Entity, IMixinEntity {

    @Nullable private NBTTagCompound customEntityData;

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("RETURN"), remap = false)
    public void onConstructed(World world, CallbackInfo ci) {
        Sponge.getGame().getEventManager().post(SpongeEventFactory.createEntityConstructing(Sponge.getGame(), this));
    }

    @Inject(method = "readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V"), cancellable = false)
    public void preReadFromNBTInject(NBTTagCompound tagCompound, CallbackInfo ci) {
        if (tagCompound.hasKey("ForgeData")) {
            customEntityData = tagCompound.getCompoundTag("ForgeData");
        }
    }

    @Inject(method = "writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeEntityToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V"), cancellable = false)
    public void preWriteToNBTInject(NBTTagCompound tagCompound, CallbackInfo ci) {
        if (customEntityData != null) {
            tagCompound.setTag("ForgeData", customEntityData);
        }
    }

    @Override
    public final NBTTagCompound getSpongeData() {
        if (this.customEntityData == null) {
            this.customEntityData = new NBTTagCompound();
        }

        if (!this.customEntityData.hasKey("SpongeData", 10)) {
            this.customEntityData.setTag("SpongeData", new NBTTagCompound());
        }
        return this.customEntityData.getCompoundTag("SpongeData");
    }

}
