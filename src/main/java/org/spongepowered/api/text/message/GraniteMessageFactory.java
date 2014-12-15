/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.spongepowered.api.text.message;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.granitepowered.granite.impl.text.message.GraniteMessageBuilder;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.translation.Translation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class GraniteMessageFactory implements MessageFactory {
    // This is in a different package due to Sponge for some reason making MessageFactory package private

    @Override
    public MessageBuilder<?> createEmptyBuilder() {
        return new GraniteMessageBuilder.GraniteTextMessageBuilder("");
    }

    @Override
    public MessageBuilder.Text createTextBuilder(String text) {
        return new GraniteMessageBuilder.GraniteTextMessageBuilder(text);
    }

    @Override
    public MessageBuilder.Translatable createTranslatableBuilder(Translation translation, Object[] args) {
        return new GraniteMessageBuilder.GraniteTranslatableMessageBuilder(translation, args);
    }

    @Override
    public MessageBuilder.Selector createSelectorBuilder(String selector) {
        // TODO: Translation API
        throw new NotImplementedException("");
    }

    @Override
    public MessageBuilder.Score createScoreBuilder(Object score) {
        // TODO: Translation API
        throw new NotImplementedException("");
    }

    @Override
    public Message.Text createPlain(String text) {
        return createTextBuilder(text).build();
    }

    @Override
    public char getColorChar() {
        return '§';
    }

    @Override
    public Message.Text parseLegacyMessage(String message, char color) {
        MessageBuilder.Text builder = new GraniteMessageBuilder.GraniteTextMessageBuilder("");

        LinkedList<String> stack = new LinkedList<>(Arrays.asList(message.split(color + "")));
        builder.content(stack.removeLast());

        while (!stack.isEmpty()) {
            String element = stack.removeLast();
            char code = element.charAt(0);
            element = element.substring(1);

            MessageBuilder.Text childBuilder = new GraniteMessageBuilder.GraniteTextMessageBuilder("");
            childBuilder.content(element);

            switch (code) {
                case '0':
                    childBuilder.color(TextColors.BLACK);
                    break;
                case '1':
                    childBuilder.color(TextColors.DARK_BLUE);
                    break;
                case '2':
                    childBuilder.color(TextColors.DARK_GREEN);
                    break;
                case '3':
                    childBuilder.color(TextColors.DARK_AQUA);
                    break;
                case '4':
                    childBuilder.color(TextColors.DARK_RED);
                    break;
                case '5':
                    childBuilder.color(TextColors.DARK_PURPLE);
                    break;
                case '6':
                    childBuilder.color(TextColors.GOLD);
                    break;
                case '7':
                    childBuilder.color(TextColors.GRAY);
                    break;
                case '8':
                    childBuilder.color(TextColors.DARK_GRAY);
                    break;
                case '9':
                    childBuilder.color(TextColors.BLUE);
                    break;
                case 'a':
                    childBuilder.color(TextColors.GREEN);
                    break;
                case 'b':
                    childBuilder.color(TextColors.AQUA);
                    break;
                case 'c':
                    childBuilder.color(TextColors.RED);
                    break;
                case 'd':
                    childBuilder.color(TextColors.LIGHT_PURPLE);
                    break;
                case 'e':
                    childBuilder.color(TextColors.YELLOW);
                    break;
                case 'f':
                    childBuilder.color(TextColors.WHITE);
                    break;
                case 'k':
                    childBuilder.style(TextStyles.OBFUSCATED);
                    break;
                case 'l':
                    childBuilder.style(TextStyles.BOLD);
                    break;
                case 'm':
                    childBuilder.style(TextStyles.STRIKETHROUGH);
                    break;
                case 'n':
                    childBuilder.style(TextStyles.UNDERLINE);
                    break;
                case 'o':
                    childBuilder.style(TextStyles.ITALIC);
                    break;
                case 'r':
                    childBuilder.style(TextStyles.RESET);
                    break;
            }

            builder.append(childBuilder.build());
        }

        return builder.build();
    }

    @Override
    public String stripLegacyCodes(String message, char color) {
        return StringUtils.join(message.split(Pattern.quote(color + "") + "[0-9a-fk-or]"), "");
    }

    @Override
    public String replaceLegacyCodes(String message, char from, char to) {
        return message.replaceAll(Pattern.quote(from + "") + "([0-9a-fk-or])", Pattern.quote(to + "") + "$1");
    }
}
