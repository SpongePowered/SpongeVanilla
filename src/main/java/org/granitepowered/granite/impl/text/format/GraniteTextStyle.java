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
import com.google.common.collect.Sets;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraniteTextStyle implements TextStyle {
    public static ImmutableMap<String, GraniteTextStyle> styles = ImmutableMap.<String, GraniteTextStyle>builder()
            .put("BOLD", new GraniteTextStyle.Base(Base.TextStyleType.BOLD))
            .put("ITALIC", new GraniteTextStyle.Base(Base.TextStyleType.ITALIC))
            .put("UNDERLINE", new GraniteTextStyle.Base(Base.TextStyleType.UNDERLINE))
            .put("STRIKETHROUGH", new GraniteTextStyle.Base(Base.TextStyleType.STRIKETHROUGH))
            .put("OBFUSCATED", new GraniteTextStyle.Base(Base.TextStyleType.OBFUSCATED))
            .put("RESET", new GraniteTextStyle.Base(Base.TextStyleType.RESET))
            .build();

    Map<Base.TextStyleType, TextStyleMode> values;

    public GraniteTextStyle(TextStyleMode bold, TextStyleMode italic, TextStyleMode underline, TextStyleMode strikethrough, TextStyleMode obfuscated) {
        values = new HashMap<>();
        values.put(Base.TextStyleType.BOLD, bold);
        values.put(Base.TextStyleType.ITALIC, italic);
        values.put(Base.TextStyleType.UNDERLINE, underline);
        values.put(Base.TextStyleType.STRIKETHROUGH, strikethrough);
        values.put(Base.TextStyleType.OBFUSCATED, obfuscated);
    }

    public GraniteTextStyle(HashMap<Base.TextStyleType, TextStyleMode> values) {
        this.values = values;
    }

    @Override
    public boolean isComposite() {
        int count = 0;

        for (TextStyleMode mode : values.values()) {
            if (mode != TextStyleMode.NEUTRAL) count++;
        }

        return count > 1;
    }

    @Override
    public boolean is(TextStyle style) {
        for (Map.Entry<Base.TextStyleType, TextStyleMode> entry : ((GraniteTextStyle) style).values.entrySet()) {
            if (!values.containsKey(entry.getKey())) return false;
            if (values.get(entry.getKey()) != entry.getValue()) return false;
        }
        return true;
    }

    @Override
    public TextStyle negate() {
        HashMap<Base.TextStyleType, TextStyleMode> newValues = new HashMap<>();
        for (Map.Entry<Base.TextStyleType, TextStyleMode> entry : values.entrySet()) {
            newValues.put(entry.getKey(), entry.getValue().negate());
        }

        return new GraniteTextStyle(newValues);
    }

    @Override
    public TextStyle and(TextStyle... styles) {
        HashMap<Base.TextStyleType, TextStyleMode> newValues = new HashMap<>(values);
        for (TextStyle style : styles) {
            for (Map.Entry<Base.TextStyleType, TextStyleMode> entry : ((GraniteTextStyle) style).values.entrySet()) {
                newValues.put(entry.getKey(), newValues.get(entry.getKey()).add(entry.getValue()));
            }
        }
        return new GraniteTextStyle(newValues);
    }

    @Override
    public TextStyle andNot(TextStyle... styles) {
        List<TextStyle> newStyles = new ArrayList<>();
        for (TextStyle style : styles) {
            newStyles.add(style.negate());
        }

        return and(newStyles.toArray(new TextStyle[newStyles.size()]));
    }

    public static enum TextStyleMode {
        APPLIED,
        NEGATED,
        NEUTRAL;

        public TextStyleMode add(TextStyleMode other) {
            switch (this) {
                case NEUTRAL:
                    return other;
                case APPLIED:
                    if (other == NEGATED) return NEUTRAL;
                    return APPLIED;
                default:
                    if (other == APPLIED) return NEUTRAL;
                    return NEGATED;
            }
        }

        public TextStyleMode negate() {
            switch (this) {
                case NEUTRAL:
                    return APPLIED;
                case APPLIED:
                    return NEGATED;
                default:
                    return NEUTRAL;
            }
        }
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
        public char getCode() {
            return type.code;
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
