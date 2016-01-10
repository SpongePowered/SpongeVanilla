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

import net.minecraft.util.EnumChatFormatting;
import org.fusesource.jansi.Ansi;
import org.spongepowered.common.text.serializer.LegacyTexts;

public final class ConsoleFormatter {

    private ConsoleFormatter() {
    }

    private static final String RESET = ANSI_RESET;

    private static final String[] ansi = new String[LegacyTexts.getFormattingCount()];

    static {
        map(EnumChatFormatting.BLACK, Ansi.ansi().reset().fg(Ansi.Color.BLACK).boldOff().toString());
        map(EnumChatFormatting.DARK_BLUE, Ansi.ansi().reset().fg(Ansi.Color.BLUE).boldOff().toString());
        map(EnumChatFormatting.DARK_GREEN, Ansi.ansi().reset().fg(Ansi.Color.GREEN).boldOff().toString());
        map(EnumChatFormatting.DARK_AQUA, Ansi.ansi().reset().fg(Ansi.Color.CYAN).boldOff().toString());
        map(EnumChatFormatting.DARK_RED, Ansi.ansi().reset().fg(Ansi.Color.RED).boldOff().toString());
        map(EnumChatFormatting.DARK_PURPLE, Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).boldOff().toString());
        map(EnumChatFormatting.GOLD, Ansi.ansi().reset().fg(Ansi.Color.YELLOW).boldOff().toString());
        map(EnumChatFormatting.GRAY, Ansi.ansi().reset().fg(Ansi.Color.WHITE).boldOff().toString());
        map(EnumChatFormatting.DARK_GRAY, Ansi.ansi().reset().fg(Ansi.Color.BLACK).bold().toString());
        map(EnumChatFormatting.BLUE, Ansi.ansi().reset().fg(Ansi.Color.BLUE).bold().toString());
        map(EnumChatFormatting.GREEN, Ansi.ansi().reset().fg(Ansi.Color.GREEN).bold().toString());
        map(EnumChatFormatting.AQUA, Ansi.ansi().reset().fg(Ansi.Color.CYAN).bold().toString());
        map(EnumChatFormatting.RED, Ansi.ansi().reset().fg(Ansi.Color.RED).bold().toString());
        map(EnumChatFormatting.LIGHT_PURPLE, Ansi.ansi().reset().fg(Ansi.Color.MAGENTA).bold().toString());
        map(EnumChatFormatting.YELLOW, Ansi.ansi().reset().fg(Ansi.Color.YELLOW).bold().toString());
        map(EnumChatFormatting.WHITE, Ansi.ansi().reset().fg(Ansi.Color.WHITE).bold().toString());
        map(EnumChatFormatting.OBFUSCATED, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
        map(EnumChatFormatting.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        map(EnumChatFormatting.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        map(EnumChatFormatting.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        map(EnumChatFormatting.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        map(EnumChatFormatting.RESET, RESET);
    }

    private static void map(EnumChatFormatting formatting, String ansiMapping) {
        ansi[formatting.ordinal()] = ansiMapping;
    }

    public static String format(String text) {
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
