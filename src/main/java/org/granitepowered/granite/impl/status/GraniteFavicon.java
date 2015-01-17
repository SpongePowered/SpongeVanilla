package org.granitepowered.granite.impl.status;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.granitepowered.granite.Granite;
import org.spongepowered.api.status.Favicon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class GraniteFavicon implements Favicon {

    String FAVICON_PREFIX = "data:image/png;base64,";
    BufferedImage bufferedImage;

    public GraniteFavicon(String image) {
        this.bufferedImage = decode(image);
    }

    public GraniteFavicon(File file) {
        try {
            this.bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GraniteFavicon(URL url) {
        try {
            this.bufferedImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GraniteFavicon(InputStream inputStream) {
        try {
            this.bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GraniteFavicon(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public BufferedImage getImage() {
        return bufferedImage;
    }

    BufferedImage decode(String image) {
        if (!image.startsWith(FAVICON_PREFIX)) {
            Granite.instance.getLogger().error("Unknown icon");
        }

        try {
            byte[] bytes = Base64.decode(image);
            BufferedImage bufferedImage = ImageIO.read(new ByteInputStream(bytes, bytes.length));
            if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
                Granite.instance.getLogger().error("Icon must be 64x64");
            } else {
                return bufferedImage;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
