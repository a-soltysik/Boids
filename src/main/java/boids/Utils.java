package boids;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    private static final Random random = ThreadLocalRandom.current();
    public static final float PI = 3.14159265359f;
    public static final float PI_2 = 1.57079632679f;

    private Utils() {

    }

    public static BufferedImage createCompatibleImage(int width, int height) {

        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(width, height);
    }

    public static float randomFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static long randomLong() {
        return random.nextLong();
    }

    //https://embeddeduse.com/2019/08/26/qt-compare-two-floats/
    public static boolean isEqual(float f1, float f2) {
        float diff = Math.abs(f1 - f2);
        if (diff <= 1.0e-5f) {
            return true;
        }
        return diff <= 1.0e-5f * Math.max(Math.abs(f1), Math.abs(f2));

    }

    //https://stackoverflow.com/questions/11930594/calculate-atan2-without-std-functions-or-c99
    public static float fastAtan(float x) {
        /*final float b = 0.596227f;
        if (x >= 0)
            return (b * x + x * x) / (1 + 2 * b * x + x * x);
        else {
            x *= -1;
            return -(b * x + x * x) / (1 + 2 * b * x + x * x);
        }*/
        final double a = 0.6403882032;
        if (x >= 0)
            return (float) ((a * x + x * x + x * x * x)
                    / (1 + (a + 1) * x + (a + 1) * x * x + x * x * x));
        else {
            x *= -1;
            return (float) (-(a * x + x * x + x * x * x)
                    / (1 + (a + 1) * x + (a + 1) * x * x + x * x * x));
        }
    }

    //https://en.wikipedia.org/wiki/Atan2
    public static float fastAtan2(float y, float x) {
        if (x > 0) {
            return fastAtan(y / x);
        }
        if (x < 0 && y >= 0) {
            return fastAtan(y / x) + PI;
        }
        if (x < 0 && y < 0) {
            return fastAtan(y / x) - PI;
        }
        if (x == 0 && y > 0) {
            return PI_2;
        }
        if (x == 0 && y < 0) {
            return -PI_2;
        }
        throw new ArithmeticException("atan2 is not defined for y and x equal to 0");
    }
}
