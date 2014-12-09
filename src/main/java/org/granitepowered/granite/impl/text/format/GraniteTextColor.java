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
import org.spongepowered.api.text.format.TextColor;

import java.awt.*;

public class GraniteTextColor implements TextColor.Base {
    private String name;
    private char code;
    private Color color;
    private boolean isReset;

    public static ImmutableMap<String, GraniteTextColor> colors = ImmutableMap.<String, GraniteTextColor>builder()
            .put("BLACK", new GraniteTextColor("BLACK", '0', Color.decode("0x000000"), false))
            .put("DARK_BLUE", new GraniteTextColor("DARK_BLUE", '1', Color.decode("0x0000AA"), false))
            .put("DARK_GREEN", new GraniteTextColor("DARK_GREEN", '2', Color.decode("0x00AA00"), false))
            .put("DARK_AQUA", new GraniteTextColor("DARK_AQUA", '3', Color.decode("0x00AAAA"), false))
            .put("DARK_RED", new GraniteTextColor("DARK_RED", '4', Color.decode("0xAA0000"), false))
            .put("DARK_PURPLE", new GraniteTextColor("DARK_PURPLE", '5', Color.decode("0xAA00AA"), false))
            .put("GOLD", new GraniteTextColor("GOLD", '6', Color.decode("0xFFAA00"), false))
            .put("GRAY", new GraniteTextColor("GRAY", '7', Color.decode("0xAAAAAA"), false))
            .put("DARK_GRAY", new GraniteTextColor("DARK_GRAY", '8', Color.decode("0x555555"), false))
            .put("BLUE", new GraniteTextColor("BLUE", '9', Color.decode("0x5555FF"), false))
            .put("GREEN", new GraniteTextColor("GREEN", 'a', Color.decode("0x55FF55"), false))
            .put("AQUA", new GraniteTextColor("AQUA", 'b', Color.decode("0x55FFFF"), false))
            .put("RED", new GraniteTextColor("RED", 'c', Color.decode("0xFF5555"), false))
            .put("LIGHT_PURPLE", new GraniteTextColor("LIGHT_PURPLE", 'd', Color.decode("0xFF55FF"), false))
            .put("YELLOW", new GraniteTextColor("YELLOW", 'e', Color.decode("0xFFFF55"), false))
            .put("WHITE", new GraniteTextColor("WHITE", 'f', Color.decode("0xFFFFFF"), false))

            // TODO: Find out what the hex value of RESET actually is
            .put("RESET", new GraniteTextColor("RESET", 'r', Color.decode("0xFFFFFF"), false))
            .build();

    public GraniteTextColor(String name, char code, Color color, boolean isReset) {
        this.name = name;
        this.code = code;
        this.color = color;
        this.isReset = isReset;
    }

    public String getName() {
        return name;
    }

    // Why doesn't this suppress warnings work?
    @SuppressWarnings("deprecated")
    public Optional<Character> getCode() {
        return Optional.of(code);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isReset() {
        return isReset;
    }
}
