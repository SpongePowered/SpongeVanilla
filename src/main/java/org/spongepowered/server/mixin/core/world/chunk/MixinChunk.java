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
package org.spongepowered.server.mixin.core.world.chunk;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;

@Mixin(Chunk.class)
public abstract class MixinChunk implements org.spongepowered.api.world.Chunk {

    @Shadow @Final private World world;
    @Shadow @Final public int xPosition;
    @Shadow @Final public int zPosition;

    @Override
    public boolean unloadChunk() {
        if (this.world.provider.canRespawnHere()
//                && DimensionManager.shouldLoadSpawn(this.worldObj.provider.getDimensionType().getId())
                && this.world.isSpawnChunk(this.xPosition, this.zPosition)) {
            return false;
        }

        ((WorldServer) this.world).getChunkProvider().unload((Chunk) (Object) this);
        return true;
    }

    @Inject(method = "onChunkLoad", at = @At("RETURN"))
    private void postChunkLoad(CallbackInfo ci) {
        SpongeImpl.postEvent(SpongeEventFactory.createLoadChunkEvent(Cause.of(NamedCause.source(this.world)), this));
    }

    @Inject(method = "onChunkUnload", at = @At("RETURN"))
    private void postChunkUnload(CallbackInfo ci) {
        SpongeImpl.postEvent(SpongeEventFactory.createUnloadChunkEvent(Cause.of(NamedCause.source(this.world)), this));
    }

}
