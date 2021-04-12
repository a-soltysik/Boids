package boids;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {

    private Utils() {

    }

    public static BufferedImage createCompatibleImage(int width, int height) {

        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(width, height);
    }
}
