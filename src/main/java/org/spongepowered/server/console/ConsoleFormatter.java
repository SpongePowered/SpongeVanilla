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

import static org.spongepowered.common.text.SpongeTexts.COLOR_CHAR;
import static org.spongepowered.server.launch.console.TerminalConsoleAppender.ANSI_RESET;

import net.minecraft.util.text.TextFormatting;
import org.spongepowered.common.text.serializer.LegacyTexts;

import java.util.function.Function;

public final class ConsoleFormatter implements Function<String, String> {

    public static final ConsoleFormatter INSTANCE = new ConsoleFormatter();

    private static final String RESET = ANSI_RESET;

    private static final String[] ansi = new String[LegacyTexts.getFormattingCount()];

    static {
        map(TextFormatting.BLACK, "\u001B[0;30;22m");
        map(TextFormatting.DARK_BLUE, "\u001B[0;34;22m");
        map(TextFormatting.DARK_GREEN, "\u001B[0;32;22m");
        map(TextFormatting.DARK_AQUA, "\u001B[0;36;22m");
        map(TextFormatting.DARK_RED, "\u001B[0;31;22m");
        map(TextFormatting.DARK_PURPLE, "\u001B[0;35;22m");
        map(TextFormatting.GOLD, "\u001B[0;33;22m");
        map(TextFormatting.GRAY, "\u001B[0;37;22m");
        map(TextFormatting.DARK_GRAY, "\u001B[0;30;1m");
        map(TextFormatting.BLUE, "\u001B[0;34;1m");
        map(TextFormatting.GREEN, "\u001B[0;32;1m");
        map(TextFormatting.AQUA, "\u001B[0;36;1m");
        map(TextFormatting.RED, "\u001B[0;31;1m");
        map(TextFormatting.LIGHT_PURPLE, "\u001B[0;35;1m");
        map(TextFormatting.YELLOW, "\u001B[0;33;1m");
        map(TextFormatting.WHITE, "\u001B[0;37;1m");
        map(TextFormatting.OBFUSCATED, "\u001B[5m");
        map(TextFormatting.BOLD, "\u001B[21m");
        map(TextFormatting.STRIKETHROUGH, "\u001B[9m");
        map(TextFormatting.UNDERLINE, "\u001B[4m");
        map(TextFormatting.ITALIC, "\u001B[3m");
        map(TextFormatting.RESET, RESET);
    }

    private static void map(TextFormatting formatting, String ansiMapping) {
        ansi[formatting.ordinal()] = ansiMapping;
    }

    private ConsoleFormatter() {
    }

    @Override
    public String apply(String text) {
        int next = text.indexOf(COLOR_CHAR);
        int last = text.length() - 1;
        if (next == -1 || next == last) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length() + 20);

        int pos = 0;
        int format;
        do {
            if (pos != next) {
                result.append(text, pos, next);
            }

            pos = next;

            format = LegacyTexts.findFormat(text.charAt(next + 1));
            if (format != -1) {
                result.append(ansi[format]);
                pos = next += 2;
            } else {
                next++;
            }

            next = text.indexOf(COLOR_CHAR, next);
        } while (next != -1 && next < last);

        return result.append(text, pos, text.length()).append(RESET).toString();
    }

}
