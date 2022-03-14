package com.lotzy.skcrew.map.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ImageUtils {

    public static BufferedImage editGraphics(BufferedImage image, Consumer<Graphics2D> edit) {
        Graphics2D g2d = image.createGraphics();
        try { edit.accept(g2d); } catch (Exception ignored) {}
        g2d.dispose();
        return image;
    }


    public static BufferedImage drawImage(BufferedImage image, BufferedImage base, int x, int y) {
        return editGraphics(base, g2d -> g2d.drawImage(image, x, y, null));
    }

    public static BufferedImage resizeImage(BufferedImage image, int x, int y) {
        return (x == 0 || y == 0) ? null : editGraphics(new BufferedImage(Math.abs(x), Math.abs(y), BufferedImage.TYPE_INT_ARGB), g2d -> g2d.drawImage(image, Math.max(0, -x), Math.max(0, -y), x, y, null));
    }

}
