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
package org.spongepowered.vanilla.mixin.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.IMixinTileEntity;

import javax.annotation.Nullable;

@NonnullByDefault
@Mixin(net.minecraft.tileentity.TileEntity.class)
public abstract class MixinTileEntity implements TileEntity, IMixinTileEntity {

    @Nullable private NBTTagCompound customEntityData;

    @Inject(method = "readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"))
    public void endReadFromNBTInject(NBTTagCompound tagCompound, CallbackInfo ci) {
        if (tagCompound.hasKey("ForgeData")) {
            this.customEntityData = tagCompound.getCompoundTag("ForgeData");
        }
    }

    @Inject(method = "writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V", at = @At("RETURN"))
    public void endWriteToNBTInject(NBTTagCompound tagCompound, CallbackInfo ci) {
        if (this.customEntityData != null) {
            tagCompound.setTag("ForgeData", this.customEntityData);
        }
    }

    /**
     * Gets the SpongeData NBT tag, used for additional data not stored in the
     * vanilla tag.
     *
     * <p>Modifying this tag will affect the data stored.</p>
     *
     * @return The data tag
     */
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
