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
package org.spongepowered.vanilla.mixin.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;

@Mixin(value = SaveHandler.class, priority = 1001)
public abstract class MixinSaveHandler {
    @Shadow(remap = false) abstract void loadDimensionAndOtherData(SaveHandler handler, WorldInfo info, NBTTagCompound compound);
    @Shadow(remap = false) abstract void loadSpongeDatData(WorldInfo info);

    @Inject(method = "loadWorldInfo", at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onLoadWorldInfoBeforeReturn0(CallbackInfoReturnable<WorldInfo> cir, File file1, NBTTagCompound nbttagcompound,
            NBTTagCompound nbttagcompound1) throws IOException {
        final WorldInfo info = new WorldInfo(nbttagcompound1);
        loadDimensionAndOtherData((SaveHandler) (Object) this, info, nbttagcompound);
        loadSpongeDatData(info);
        cir.setReturnValue(info);
    }

    @Inject(method = "loadWorldInfo", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onLoadWorldInfoBeforeReturn1(CallbackInfoReturnable<WorldInfo> cir, File file1, NBTTagCompound nbttagcompound,
            NBTTagCompound nbttagcompound1) throws IOException {
        final WorldInfo info = new WorldInfo(nbttagcompound1);
        loadDimensionAndOtherData((SaveHandler) (Object) this, info, nbttagcompound);
        loadSpongeDatData(info);
        cir.setReturnValue(info);
    }
}
