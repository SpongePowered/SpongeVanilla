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

package org.granitepowered.granite.impl.text.format;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.text.format.TextStyle;

public abstract class GraniteTextStyle implements TextStyle {
    public static ImmutableMap<String, GraniteTextStyle> styles = ImmutableMap.<String, GraniteTextStyle>builder()
            .put("BOLD", new GraniteTextStyle.Base(Base.TextStyleType.BOLD))
            .put("ITALIC", new GraniteTextStyle.Base(Base.TextStyleType.ITALIC))
            .put("UNDERLINE", new GraniteTextStyle.Base(Base.TextStyleType.UNDERLINE))
            .put("STRIKETHROUGH", new GraniteTextStyle.Base(Base.TextStyleType.STRIKETHROUGH))
            .put("OBFUSCATED", new GraniteTextStyle.Base(Base.TextStyleType.OBFUSCATED))
            .put("RESET", new GraniteTextStyle.Base(Base.TextStyleType.RESET))
            .build();

    boolean bold;
    boolean italic;
    boolean underline;
    boolean strikethrough;
    boolean obfuscated;

    public GraniteTextStyle(boolean bold, boolean italic, boolean underline, boolean strikethrough, boolean obfuscated) {
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
    }

    @Override
    public boolean isComposite() {
        int count = 0;
        if (bold) count++;
        if (italic) count++;
        if (underline) count++;
        if (strikethrough) count++;
        if (obfuscated) count++;

        return count > 1;
    }

    @Override
    public boolean is(TextStyle style) {
        // TODO: Figure out what these things do
        throw new NotImplementedException("");
    }

    @Override
    public TextStyle negate() {
        // TODO: Figure out what these things do
        throw new NotImplementedException("");
    }

    @Override
    public TextStyle and(TextStyle... styles) {
        // TODO: Figure out what these things do
        throw new NotImplementedException("");
    }

    @Override
    public TextStyle andNot(TextStyle... styles) {
        // TODO: Figure out what these things do
        throw new NotImplementedException("");
    }

    public static class Base extends GraniteTextStyle implements TextStyle.Base {
        TextStyleType type;

        public Base(TextStyleType type) {
            super(
                    type == TextStyleType.BOLD,
                    type == TextStyleType.ITALIC,
                    type == TextStyleType.UNDERLINE,
                    type == TextStyleType.STRIKETHROUGH,
                    type == TextStyleType.OBFUSCATED
            );

            this.type = type;
        }

        @Override
        public String getName() {
            return type.name();
        }

        @Override
        @SuppressWarnings("deprecated")
        public Optional<Character> getCode() {
            return Optional.of(type.code);
        }

        public static enum TextStyleType {
            BOLD('l'),
            ITALIC('o'),
            UNDERLINE('n'),
            STRIKETHROUGH('m'),
            OBFUSCATED('k'),
            RESET('r');

            char code;

            TextStyleType(char code) {
                this.code = code;
            }
        }
    }
}
