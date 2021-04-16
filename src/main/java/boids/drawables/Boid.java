package boids.drawables;

import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;

public abstract class Boid implements Drawable{
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 acceleration;
    protected final Vector2[] vertices;
    protected float size;
    protected int index;

    public Boid(float size, Vector2 position) {
        this.size = size;
        this.position = position;
        velocity = Vector2.ZERO;
        vertices = new Vector2[4];
        initializeShape();
    }
    public Boid(Vector2 position) {
        this(5f, position);
    }

    private void initializeShape() {
        vertices[0] = new Vector2(0f, 0f);
        vertices[1] = new Vector2(0.5f * size, 1f * size);
        vertices[2] = new Vector2(0f, 0.75f * size);
        vertices[3] = new Vector2(-0.5f * size, 1f * size);
    }

    private void rotate() {
        if (velocity.magnitude() == 0) {
            return;
        }
        Vector2 direction = position.subtract(vertices[2]);
        Vector2 newDirection = velocity;

        float angle = direction.directAngle(newDirection);

        for (int i=1; i<vertices.length; i++) {
            vertices[i] = vertices[i].rotated(position, angle);
        }
    }

    private void remainOnScreen(Rectangle frame) {
        Vector2 oldPosition = new Vector2(position);

        if (position.x > frame.max.x) {
            position.x = frame.min.x;
        } else if (position.x < frame.min.x) {
            position.x = frame.max.x;
        }
        if (position.y > frame.max.y) {
            position.y = frame.min.y;
        } else if (position.y < frame.min.y) {
            position.y = frame.max.y;
        }

        if (!position.equals(oldPosition)) {
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i].add(position.subtract(oldPosition));
            }
        }
    }

    @Override
    public void update(Animation animation, double frameTime) {
        position = position.add(velocity.multiply(frameTime));
        for (int i = 0; i < vertices.length; i++) {
           vertices[i] = vertices[i].add(velocity.multiply(frameTime));
        }
        remainOnScreen(animation.getDimensions());
        rotate();

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.fill(Drawable.drawShape(vertices));
    }

    protected void setIndex(int index) {
        this.index = index;
    }
}
