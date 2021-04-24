package boids.drawables;

import boids.Utils;
import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.math.OpenSimplex2F;
import boids.math.Vector2;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class LowPolyBackground implements Drawable{

    private final AnimationPanel frame;
    private final BufferedImage background;
    private final OpenSimplex2F noise;
    private final Graphics2D g2d;
    private final float moveResolution = 0.5f;
    private float colorResolution = 0.3f;
    private float speed;
    private double moveOffset = 0f;
    private double colorOffset = 0f;
    private boolean moving = false;

    private final int height;
    private final int width;

    private final Vector2[] vertices;
    private final Vector2[] shape;
    private final int[] triangles;

    public LowPolyBackground(AnimationPanel frame, int height) {
        this.frame = frame;
        this.height = height;
        this.width = (int) (height * (float) frame.getWidth() / frame.getHeight());
        background = Utils.createCompatibleImage(frame.getWidth(), frame.getHeight());
        noise = new OpenSimplex2F(Utils.randomInt(0, Integer.MAX_VALUE));
        g2d = (Graphics2D) background.getGraphics();
        shape = new Vector2[height * width];
        vertices = new Vector2[height * width];
        triangles = new int[(width - 1) * (height - 1) * 6];
        colorResolution /= width / 5f;
        initializeShape();
        generateVertices();
        fillTriangles();
    }

    public LowPolyBackground(AnimationPanel frame, int height, float speed) {
        this(frame, height);
        this.speed = speed;
        moving = true;
    }

    private void initializeShape() {
        int n=0;
        int triangle = 0;
        for (int i=0; i<height; i++) {
            for (int j = 0; j<width; j++) {

                shape[n] = new Vector2(
                        (float) frame.getWidth() * j / (width - 1),
                        (float) frame.getHeight() * i / (height - 1)
                );

                if (j != width - 1 && i != height - 1) {
                    triangles[triangle] = n;
                    triangles[triangle + 1] = n + width + 1;
                    triangles[triangle + 2] = n + width;

                    triangles[triangle + 3] = n;
                    triangles[triangle + 4] = n + 1;
                    triangles[triangle + 5] = n + width + 1;
                    triangle += 6;
                }
                n++;
            }
        }
    }

    private void generateVertices() {
        int n=0;
        float minShift = 0.75f;
        float maxShift = 1.25f;
        double y1 = moveOffset;
        double y2 = 2 * moveOffset;
        for (int i=0; i<height; i++) {
            double x1 = 2 * moveOffset;
            double x2 = moveOffset;
            for (int j = 0; j<width; j++) {
                Vector2 point = shape[n];
                if (i != 0 && j != 0 && i != height - 1 && j != width - 1) {
                    double valueX = noise.noise2(x1, y1);
                    double valueY = noise.noise2(x2, y2);

                    valueX = (valueX + 1)/2;
                    valueX = minShift + (maxShift - minShift) * valueX;

                    valueY = (valueY + 1) / 2;
                    valueY = minShift + (maxShift - minShift) * valueY;

                    point = point.subtract(shape[n - width - 1]);
                    point.x *= valueX;
                    point.y *= valueY;
                    point = point.add(shape[n - width - 1]);
                }
                vertices[n] = point;
                n++;
                x1 += moveResolution;
                x2 += 2 * moveResolution;
            }
            y1 += 2 * moveResolution;
            y2 += moveResolution;
        }
    }

    private void fillTriangles() {

        double x = colorOffset;
        double y = colorOffset;

        float minColor = 0.4f;
        float maxColor = 0.8f;

        int n = 1;

        for (int i=0; i<triangles.length; i += 3) {

            double value = noise.noise2(x, y);
            value = (value + 1) / 2;
            value = minColor + (maxColor - minColor) * value;
            value *= 100;

            Color color = Color.getHSBColor(220f / 360f, (float) value / 100, 1f);
            g2d.setColor(color);

            Path2D triangle = new Path2D.Float();
            triangle.moveTo(vertices[triangles[i]].x, vertices[triangles[i]].y);
            triangle.lineTo(vertices[triangles[i + 1]].x, vertices[triangles[i + 1]].y);
            triangle.lineTo(vertices[triangles[i + 2]].x, vertices[triangles[i + 2]].y);
            triangle.closePath();

            g2d.fill(triangle);

            x+= colorResolution;
            if (n % (2 * width - 2) == 0) {
                x = colorOffset;
                y += colorResolution;
            }
            n++;
        }
    }

    @Override
    public void update(Animation animation, double frameTime) {
        if (moving) {
            moveOffset += speed * frameTime;
            colorOffset += speed * frameTime;
            generateVertices();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        fillTriangles();
        g2d.drawImage(background, 0, 0, null);
    }
}
