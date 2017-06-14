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
package org.spongepowered.server.mixin.core.server.dedicated;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.server.console.ConsoleCommandCompleter;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServer$2")
public abstract class MixinConsoleHandler {

    @Shadow(remap = false, aliases = {"field_72428_a", "this$0"}) @Final
    private DedicatedServer server;

    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRun(CallbackInfo ci) {
        final Terminal terminal = TerminalConsoleAppender.getTerminal();

        if (terminal != null) {
            LineReader reader = LineReaderBuilder.builder()
                    .appName("SpongeVanilla")
                    .terminal(terminal)
                    .completer(new ConsoleCommandCompleter(this.server))
                    .build();
            reader.unsetOpt(LineReader.Option.INSERT_TAB);

            TerminalConsoleAppender.setReader(reader);

            try {
                String line;
                while (!this.server.isServerStopped() && this.server.isServerRunning()) {
                    try {
                        line = reader.readLine("> ");
                    } catch (EndOfFileException e) {
                        // Continue reading after EOT
                        continue;
                    }

                    if (line == null) {
                        break;
                    }

                    line = line.trim();
                    if (!line.isEmpty()) {
                        this.server.addPendingCommand(line, this.server);
                    }
                }
            } catch (UserInterruptException e) {
                this.server.initiateShutdown();
            } finally {
                TerminalConsoleAppender.setReader(null);
            }

            ci.cancel();
        }
    }

}
