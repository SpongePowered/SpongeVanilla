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
package org.spongepowered.server.mixin.server.dedicated;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.ServerHangWatchdog;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHangWatchdog.class)
public abstract class MixinServerHangWatchdog {

    @Shadow private long maxTickTime;
    private long originalMaxTickTime;
    private boolean skip = true;

    @Inject(method = "<init>(Lnet/minecraft/server/dedicated/DedicatedServer;)V", at = @At("RETURN"))
    private void construct(DedicatedServer server, CallbackInfo ci) {
        this.originalMaxTickTime = this.maxTickTime;
    }

    @Inject(method = "run()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedServer;getCurrentTime()J", shift = At.Shift.BEFORE))
    private void quiet(CallbackInfo ci) {
        if (this.skip) {
            this.maxTickTime = Long.MAX_VALUE;
            this.skip = false;
        }
    }

    @Inject(method = "run()V", at = @At(value = "FIELD", target = "Lnet/minecraft/server/dedicated/ServerHangWatchdog;maxTickTime:J",
        opcode = Opcodes.GETFIELD, shift = At.Shift.BEFORE))
    private void restoreOriginalMaxTickTime(CallbackInfo ci) {
        this.maxTickTime = this.originalMaxTickTime;
    }

}
