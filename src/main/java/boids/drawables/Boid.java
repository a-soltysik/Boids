package boids.drawables;

import boids.math.Vector2;

import java.awt.*;

public class Boid implements Drawable{

    private Vector2 velocity;
    private Vector2 position;
    private final Vector2[] vertices;
    float size;

    public Boid(float size, Vector2 position) {
        this.size = size;
        this.position = position;
        velocity = Vector2.ZERO;
        vertices = new Vector2[4];
        initializeShape();
    }

    private void initializeShape() {
        vertices[0] = new Vector2(0f, 0f);
        vertices[1] = new Vector2(0.5f * size, 1f * size);
        vertices[2] = new Vector2(0f, 0.75f * size);
        vertices[3] = new Vector2(-0.5f * size, 1f * size);

    }
    @Override
    public void update(double frameTime) {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.fill(Drawable.drawShape(vertices));
    }
}
