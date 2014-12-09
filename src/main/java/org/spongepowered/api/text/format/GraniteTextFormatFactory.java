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

package org.spongepowered.api.text.format;

import com.google.common.base.Optional;
import org.granitepowered.granite.impl.text.format.GraniteTextColor;
import org.granitepowered.granite.impl.text.format.GraniteTextStyle;

import java.util.Arrays;
import java.util.List;

public class GraniteTextFormatFactory implements TextFormatFactory {
    @Override
    public Optional<TextColor> getColorFromName(String name) {
        return Optional.fromNullable((TextColor) GraniteTextColor.valueOf(name.toUpperCase()));
    }

    @Override
    public List<TextColor> getColors() {
        return Arrays.<TextColor>asList(GraniteTextColor.values());
    }

    @Override
    public Optional<TextStyle> getStyleFromName(String name) {
        return Optional.fromNullable((TextStyle) GraniteTextStyle.styles.get(name.toUpperCase()));
    }

    @Override
    public List<TextStyle> getStyles() {
        return Arrays.<TextStyle>asList(
                GraniteTextStyle.styles.get("OBFUSCATED"),
                GraniteTextStyle.styles.get("BOLD"),
                GraniteTextStyle.styles.get("STRIKETHROUGH"),
                GraniteTextStyle.styles.get("UNDERLINE"),
                GraniteTextStyle.styles.get("ITALIC"),
                GraniteTextStyle.styles.get("RESET")
        );
    }

    @Override
    public TextStyle createStyle(TextStyle[] styles) {
        return new GraniteTextStyle.Base(GraniteTextStyle.Base.TextStyleType.RESET).and(styles);
    }
}
