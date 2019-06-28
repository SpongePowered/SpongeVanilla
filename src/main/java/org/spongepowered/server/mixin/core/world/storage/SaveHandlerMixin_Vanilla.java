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
package org.spongepowered.server.mixin.core.world.storage;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.bridge.world.storage.SaveHandlerBridge;

import java.io.File;
import java.io.FileInputStream;

@Mixin(SaveHandler.class)
public abstract class SaveHandlerMixin_Vanilla implements SaveHandlerBridge {

    @Redirect(method = "loadWorldInfo",
            at = @At(value = "INVOKE", target= "Lnet/minecraft/world/storage/SaveFormatOld;"
                    + "getWorldData(Ljava/io/File;Lnet/minecraft/util/datafix/DataFixer;)Lnet/minecraft/world/storage/WorldInfo;"), require = 2)
    private WorldInfo onGetOldWorldInfo(File file, DataFixer fixer) {
        try {
            NBTTagCompound root = CompressedStreamTools.readCompressed(new FileInputStream(file));
            NBTTagCompound data = root.getCompoundTag("Data");
            WorldInfo info = new WorldInfo(fixer.process(FixTypes.LEVEL, data));

            this.bridge$loadDimensionAndOtherData((SaveHandler) (Object) this, info, root);
            try {
                this.bridge$loadSpongeDatData(info);
            } catch (Exception ex) {
                SpongeImpl.getLogger().error("Exception reading Sponge level data", ex);
                return null;
            }

            return info;
        } catch (Exception exception) {
            SpongeImpl.getLogger().error("Exception reading " + file, exception);
            return null;
        }
    }

}
