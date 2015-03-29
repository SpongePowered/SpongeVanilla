/*
 * This file is part of Granite, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered>
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
package org.spongepowered.granite.text;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.TextFactory;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.granite.text.format.GraniteTextColor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NonnullByDefault
public class GraniteTextFactory implements TextFactory {

    private static final Map<Character, EnumChatFormatting> chatChars = Maps.newHashMap();
    private static final Pattern FORMATTING_PATTERN = Pattern.compile(GraniteText.COLOR_CHAR + "([0-9A-FK-OR])", CASE_INSENSITIVE);

    static {
        chatChars.put((char) 0, EnumChatFormatting.BLACK);
        chatChars.put((char) 1, EnumChatFormatting.DARK_BLUE);
        chatChars.put((char) 2, EnumChatFormatting.DARK_GREEN);
        chatChars.put((char) 3, EnumChatFormatting.DARK_AQUA);
        chatChars.put((char) 4, EnumChatFormatting.DARK_RED);
        chatChars.put((char) 5, EnumChatFormatting.DARK_PURPLE);
        chatChars.put((char) 6, EnumChatFormatting.GOLD);
        chatChars.put((char) 7, EnumChatFormatting.GRAY);
        chatChars.put((char) 8, EnumChatFormatting.DARK_GRAY);
        chatChars.put((char) 9, EnumChatFormatting.BLUE);
        chatChars.put("a".charAt(0), EnumChatFormatting.GREEN);
        chatChars.put("b".charAt(0), EnumChatFormatting.AQUA);
        chatChars.put("c".charAt(0), EnumChatFormatting.RED);
        chatChars.put("d".charAt(0), EnumChatFormatting.LIGHT_PURPLE);
        chatChars.put("e".charAt(0), EnumChatFormatting.YELLOW);
        chatChars.put("f".charAt(0), EnumChatFormatting.WHITE);
        chatChars.put("k".charAt(0), EnumChatFormatting.OBFUSCATED);
        chatChars.put("l".charAt(0), EnumChatFormatting.BOLD);
        chatChars.put("m".charAt(0), EnumChatFormatting.STRIKETHROUGH);
        chatChars.put("n".charAt(0), EnumChatFormatting.UNDERLINE);
        chatChars.put("o".charAt(0), EnumChatFormatting.ITALIC);
        chatChars.put("r".charAt(0), EnumChatFormatting.RESET);
    }

    private static void applyStyle(TextBuilder builder, char code) {
        EnumChatFormatting formatting = chatChars.get(code);
        if (formatting != null) {
            switch (formatting) {
                case BOLD:
                    builder.style(TextStyles.BOLD);
                    break;
                case ITALIC:
                    builder.style(TextStyles.ITALIC);
                    break;
                case UNDERLINE:
                    builder.style(TextStyles.UNDERLINE);
                    break;
                case STRIKETHROUGH:
                    builder.style(TextStyles.STRIKETHROUGH);
                    break;
                case OBFUSCATED:
                    builder.style(TextStyles.OBFUSCATED);
                    break;
                case RESET:
                    builder.color(TextColors.NONE);
                    builder.style(TextStyles.RESET);
                    break;
                default:
                    builder.color(GraniteTextColor.of(formatting));
            }
        }
    }

    private static Text.Literal parseLegacyMessage(String text, int pos, Matcher matcher, TextBuilder.Literal parent) {
        String content = text.substring(pos, matcher.start());
        parent.content(content);

        TextBuilder.Literal builder = Texts.builder("");
        applyStyle(builder, matcher.group(1).charAt(0));

        int end = matcher.end();
        while (true) {
            if (!matcher.find()) {
                builder.content(text.substring(end));
                return builder.build();
            } else if (end == matcher.start()) {
                applyStyle(builder, matcher.group(1).charAt(0));
                end = matcher.end();
            } else {
                break;
            }
        }

        builder.append(parseLegacyMessage(text, end, matcher, builder));
        return builder.build();
    }

    @Override
    public Text parseJson(String json) throws IllegalArgumentException {
        try {
            return ((GraniteChatComponent) IChatComponent.Serializer.jsonToComponent(json)).toText();
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Failed to parse JSON", e);
        }
    }

    @Override
    public Text parseJsonLenient(String json) throws IllegalArgumentException {
        return parseJson(json); // TODO
    }

    @Override
    public String toPlain(Text text) {
        return ((GraniteText) text).toPlain();
    }

    @Override
    public String toJson(Text text) {
        return ((GraniteText) text).toJson();
    }

    @Override
    public char getLegacyChar() {
        return GraniteText.COLOR_CHAR;
    }

    @Override
    public Text.Literal parseLegacyMessage(String text, char code) {
        if (text.length() <= 1) {
            return Texts.of(text);
        }

        Matcher matcher = (code == GraniteText.COLOR_CHAR ? FORMATTING_PATTERN :
                Pattern.compile(code + "([0-9A-FK-OR])", CASE_INSENSITIVE)).matcher(text);
        if (!matcher.find()) {
            return Texts.of(text);
        }

        return parseLegacyMessage(text, 0, matcher, Texts.builder(""));
    }

    @Override
    public String stripLegacyCodes(String text, char code) {
        if (code == GraniteText.COLOR_CHAR) {
            return FORMATTING_PATTERN.matcher(text).replaceAll("");
        }

        return text.replaceAll("(?i)" + code + "[0-9A-FK-OR]", "");
    }

    @Override
    public String replaceLegacyCodes(String text, char from, char to) {
        if (from == GraniteText.COLOR_CHAR) {
            return FORMATTING_PATTERN.matcher(text).replaceAll(to + "$1");
        }

        return text.replaceAll("(?i)" + from + "([0-9A-FK-OR])", to + "$1");
    }

    @Override
    public String toLegacy(Text text, char code) {
        return ((GraniteText) text).toLegacy(code);
    }

}

