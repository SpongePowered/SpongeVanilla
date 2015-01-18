package org.granitepowered.granite.impl.status;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.granitepowered.granite.Granite;
import org.spongepowered.api.status.Favicon;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import javax.imageio.ImageIO;

public class GraniteFavicon implements Favicon {

    String FAVICON_PREFIX = "data:image/png;base64,";
    BufferedImage bufferedImage;

    public GraniteFavicon(String image) throws IOException {
        if (isValid(image)) {
            this.bufferedImage = ImageIO.read(new ByteArrayInputStream(image.getBytes(Charsets.UTF_8)));
        }
    }

    public GraniteFavicon(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        String encoded = new String(bytes, Charsets.UTF_8);

        if (isValid(encoded)) {
            this.bufferedImage = ImageIO.read(new ByteArrayInputStream(encoded.getBytes(Charsets.UTF_8)));
        }
    }

    public GraniteFavicon(URL url) throws IOException {
        if (isValid(IOUtils.toString(url, Charsets.UTF_8))) {
            this.bufferedImage = ImageIO.read(new ByteArrayInputStream(IOUtils.toString(url, Charsets.UTF_8).getBytes(Charsets.UTF_8)));
        }
    }

    public GraniteFavicon(InputStream inputStream) throws IOException {
        String encoded = IOUtils.toString(inputStream, Charsets.UTF_8);
        if (isValid(encoded)) {
            this.bufferedImage = ImageIO.read(inputStream);
        }
    }

    public GraniteFavicon(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        String encoded = byteArrayOutputStream.toString("UTF-8");
        byteArrayOutputStream.close();
        if (isValid(encoded)) {
            this.bufferedImage = bufferedImage;
        }
    }

    @Override
    public BufferedImage getImage() {
        return bufferedImage;
    }

    private boolean isValid(String image) {
        if (image != null) {
            if (image.startsWith(FAVICON_PREFIX)) {
                if (bufferedImage.getWidth() == 64 && bufferedImage.getHeight() == 64) {
                    return true;
                } else {
                    Granite.instance.getLogger()
                            .error("Image must be 64x64. Currently it is " + bufferedImage.getWidth() + "x" + bufferedImage.getHeight());
                }
            } else {
                Granite.instance.getLogger().error("Unknown icon");
            }
        } else {
            Granite.instance.getLogger().error("The buffered image returned null when encoding to String");
        }
        return false;
    }
}
