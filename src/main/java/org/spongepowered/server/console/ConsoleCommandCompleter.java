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
package org.spongepowered.server.console;

import static com.google.common.base.Preconditions.checkNotNull;

import jline.console.completer.Completer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.common.SpongeImpl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class ConsoleCommandCompleter implements Completer {

    private final DedicatedServer server;

    public ConsoleCommandCompleter(DedicatedServer server) {
        this.server = checkNotNull(server, "server");
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        int len = buffer.length();

        boolean prefix;
        if (buffer.isEmpty() || buffer.charAt(0) != '/') {
            buffer = '/' + buffer;
            prefix = false;
        } else {
            prefix = true;
        }

        final String input = buffer;
        @SuppressWarnings("unchecked")
        Future<List<String>> tabComplete =
                this.server.callFromMainThread(() -> this.server.getTabCompletions(this.server, input, this.server.getPosition()));

        try {
            List<String> completions = tabComplete.get();
            Collections.sort(completions);
            if (prefix) {
                candidates.addAll(completions);
            } else {
                for (String completion : completions) {
                    candidates.add(completion.charAt(0) == '/' ? completion.substring(1) : completion);
                }
            }

            int pos = buffer.lastIndexOf(' ');
            if (pos == -1) {
                return cursor - len;
            } else if (prefix) {
                return cursor - len + pos + 1;
            } else {
                return cursor - len + pos;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            SpongeImpl.getLogger().error("Failed to tab complete", e);
        }

        return cursor;
    }

}
