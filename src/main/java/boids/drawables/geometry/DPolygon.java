package boids.drawables.geometry;

import boids.Utils;
import boids.drawables.Drawable;
import boids.gui.Animation;
import boids.math.OpenSimplex2F;
import boids.math.Vector2;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Arrays;

public class DPolygon implements Drawable {
    protected final Color color;
    protected final Vector2[] vertices;
    protected Vector2 position;

    public DPolygon(Vector2[] vertices, Vector2 position, Color color) {
        this.vertices = new Vector2[vertices.length];
        for (int i=0; i< vertices.length; i++) {
            this.vertices[i] = vertices[i].add(position);
        }
        this.position = position;
        this.color = color;
    }
    public DPolygon(int numberOfSides, float radius, Vector2 position, boolean random, Color color) {
        this.position = position;
        this.color = color;
        vertices = new Vector2[numberOfSides];
        generateRegularPolygon(radius);
        if (random) {
            addNoise();
        }
    }

    private void generateRegularPolygon(float radius) {
        float angle = 0f;
        for (int i = 0; i < vertices.length; i++, angle += 2 * Math.PI / vertices.length) {
            vertices[i] = new Vector2(
                    position.x + (float) (Math.sin(angle) * radius),
                    position.y + (float) (Math.cos(angle) * radius)
            );
        }
    }

    private void addNoise() {
        final float NOISE_RESOLUTION = 0.02f;
        OpenSimplex2F noise = new OpenSimplex2F(Utils.randomLong());
        for (int i = 0; i < vertices.length; i++) {
            Vector2 point = vertices[i].subtract(position);
            double value = noise.noise2(point.x * NOISE_RESOLUTION, point.y * NOISE_RESOLUTION);
            value = (value + 1) / 2;
            value = value * (1.2f - 0.8f) + 0.8f;
            point = point.multiply((float) value);
            vertices[i] = point.add(position);
        }
    }

    public void moveTo(Vector2 newPosition) {
        Vector2 diff = newPosition.subtract(position);
        for (int i=0; i< vertices.length; i++) {
            vertices[i] = vertices[i].add(diff);
        }
        position = new Vector2(newPosition);
    }

    public void rotate(float angle) {
        for (int i=0; i<vertices.length; i++) {
            if (!vertices[i].equals(position)) {
                vertices[i] = vertices[i].rotated(position, angle);
            }
        }
    }

    public Vector2[] getVertices() {
        return Arrays.copyOf(vertices, vertices.length);
    }

    @Override
    public void update(Animation animation, double frameTime) {

    }

    @Override
    public void render(Graphics2D g2d) {
        Path2D shape = new Path2D.Float();
        shape.moveTo(vertices[0].x, vertices[0].y);
        for (int i=1; i<vertices.length; i++) {
            shape.lineTo(vertices[i].x, vertices[i].y);
        }
        shape.closePath();
        g2d.setColor(color);
        g2d.fill(shape);
    }
}
