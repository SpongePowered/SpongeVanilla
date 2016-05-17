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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.entity.EntityDummy;
import org.spongepowered.common.interfaces.entity.IMixinEntity;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public abstract class MixinEntity implements IMixinEntity {

    @Nullable private NBTTagCompound customEntityData;

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("RETURN"), remap = false)
    private void onConstructed(World world, CallbackInfo ci) {
        if (!((Object) this instanceof EntityDummy)) {
            SpongeImpl.postEvent(SpongeEventFactory.createConstructEntityEventPost(Cause.of(NamedCause.source(world)),
                    this, this.getType(), this.getTransform()));
        }
    }

    @Override
    public final NBTTagCompound getEntityData() {
        if (this.customEntityData == null) {
            this.customEntityData = new NBTTagCompound();
        }
        return this.customEntityData;
    }

    @Inject(method = "readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V"))
    private void preReadFromNBTInject(NBTTagCompound tagCompound, CallbackInfo ci) {
        if (tagCompound.hasKey(NbtDataUtil.FORGE_DATA)) {
            this.customEntityData = tagCompound.getCompoundTag(NbtDataUtil.FORGE_DATA);
        }
    }

    @Inject(method = "func_189511_e(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeEntityToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V"))
    private void preWriteToNBTInject(NBTTagCompound tagCompound, CallbackInfoReturnable<NBTTagCompound> ci) {
        if (this.customEntityData != null) {
            tagCompound.setTag(NbtDataUtil.FORGE_DATA, this.customEntityData);
        }
    }

}
