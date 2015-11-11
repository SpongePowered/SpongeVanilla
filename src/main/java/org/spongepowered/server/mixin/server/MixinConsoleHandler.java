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
package org.spongepowered.server.mixin.server;

import jline.console.ConsoleReader;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.api.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.server.console.ConsoleCommandCompleter;
import org.spongepowered.server.console.ConsoleFormatter;
import org.spongepowered.server.launch.console.TerminalConsoleAppender;

import java.io.IOException;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServer$2")
public abstract class MixinConsoleHandler {

    @Shadow(remap = false, aliases = {"field_72428_a", "this$0"})
    private DedicatedServer server;

    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false)
    public void onRun(CallbackInfo ci) {
        final ConsoleReader reader = TerminalConsoleAppender.getReader();

        if (reader != null) {
            TerminalConsoleAppender.setFormatter(ConsoleFormatter::format);
            reader.addCompleter(new ConsoleCommandCompleter(this.server));

            String line;
            while (!this.server.isServerStopped() && this.server.isServerRunning()) {
                try {
                    line = reader.readLine("> ");

                    if (line != null) {
                        line = line.trim();
                        if (!line.isEmpty()) {
                            this.server.addPendingCommand(line, this.server);
                        }
                    }
                } catch (IOException e) {
                    SpongeImpl.getLogger().error("Exception handling console input", e);
                }
            }

            ci.cancel();
        } else {
            TerminalConsoleAppender.setFormatter(Texts::stripCodes);
        }
    }

}
