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
package org.spongepowered.vanilla.console;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.EnumChatFormatting;
import org.fusesource.jansi.Ansi;

import java.util.Map;
import java.util.regex.Pattern;

public final class ConsoleFormatter {

    private ConsoleFormatter() {
    }

    private static final String RESET = Ansi.ansi().reset().toString();

    private static final ImmutableMap<Pattern, String> REPLACEMENTS = ImmutableMap.<Pattern, String>builder()
            .put(compile(EnumChatFormatting.BLACK), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString())
            .put(compile(EnumChatFormatting.DARK_BLUE), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString())
            .put(compile(EnumChatFormatting.DARK_GREEN), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString())
            .put(compile(EnumChatFormatting.DARK_AQUA), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString())
            .put(compile(EnumChatFormatting.DARK_RED), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString())
            .put(compile(EnumChatFormatting.DARK_PURPLE), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString())
            .put(compile(EnumChatFormatting.GOLD), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString())
            .put(compile(EnumChatFormatting.GRAY), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString())
            .put(compile(EnumChatFormatting.DARK_GRAY), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString())
            .put(compile(EnumChatFormatting.BLUE), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString())
            .put(compile(EnumChatFormatting.GREEN), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString())
            .put(compile(EnumChatFormatting.AQUA), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString())
            .put(compile(EnumChatFormatting.RED), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString())
            .put(compile(EnumChatFormatting.LIGHT_PURPLE), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString())
            .put(compile(EnumChatFormatting.YELLOW), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString())
            .put(compile(EnumChatFormatting.WHITE), Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString())
            .put(compile(EnumChatFormatting.OBFUSCATED), Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString())
            .put(compile(EnumChatFormatting.BOLD), Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString())
            .put(compile(EnumChatFormatting.STRIKETHROUGH), Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString())
            .put(compile(EnumChatFormatting.UNDERLINE), Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString())
            .put(compile(EnumChatFormatting.ITALIC), Ansi.ansi().a(Ansi.Attribute.ITALIC).toString())
            .put(compile(EnumChatFormatting.RESET), Ansi.ansi().a(Ansi.Attribute.RESET).toString())
            .build();

    private static Pattern compile(EnumChatFormatting formatting) {
        return Pattern.compile(formatting.toString(), Pattern.LITERAL | Pattern.CASE_INSENSITIVE);
    }

    public static String format(String text) {
        for (Map.Entry<Pattern, String> entry : REPLACEMENTS.entrySet()) {
            text = entry.getKey().matcher(text).replaceAll(entry.getValue());
        }

        return text + RESET;
    }

}
