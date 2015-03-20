/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite.impl.status;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import org.spongepowered.api.status.Favicon;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GraniteFavicon implements Favicon {

    private static final String FAVICON_PREFIX = "data:image/png;base64,";
    private final BufferedImage bufferedImage;

    public GraniteFavicon(BufferedImage bufferedImage) throws IOException {
        this.bufferedImage = bufferedImage;
    }

    public GraniteFavicon(String string) throws IOException {
        checkArgument(string.startsWith(FAVICON_PREFIX), "Unknown favicon format");
        ByteBuf base64 = Unpooled.copiedBuffer(string.substring(FAVICON_PREFIX.length()), Charsets.UTF_8);
        try {
            ByteBuf byteBuf = Base64.decode(base64);
            try {
                BufferedImage result = ImageIO.read(new ByteBufInputStream(byteBuf));
                checkState(result.getWidth() == 64, "favicon must be 64 pixels wide");
                checkState(result.getHeight() == 64, "favicon must be 64 pixels high");
                this.bufferedImage = result;
            } finally {
                byteBuf.release();
            }
        } finally {
            base64.release();
        }
    }

    public static Favicon load(String raw) throws IOException {
        return new GraniteFavicon(raw);
    }

    public static Favicon load(BufferedImage image) throws IOException {
        return new GraniteFavicon(image);
    }

    @Override
    public BufferedImage getImage() {
        return this.bufferedImage;
    }
}
