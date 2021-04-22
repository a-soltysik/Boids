package boids.drawables;

import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public abstract class Boid implements Drawable{
    protected Color color;
    protected FOV fov;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 acceleration;
    protected final Vector2[] vertices;
    protected float size;
    protected int index;

    public Boid(float size, Vector2 position, FOV fov, Color color) {
        this.size = size;
        this.position = position;
        this.color = color;
        this.fov = fov;
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

    private void rotate() {
        if (velocity.magnitude() == 0) {
            return;
        }
        Vector2 direction = position.subtract(vertices[2]);
        Vector2 newDirection = velocity;

        if (direction.isZero() || newDirection.isZero()) {
            return;
        }
        float angle = direction.directAngle(newDirection);

        for (int i=1; i<vertices.length; i++) {
            vertices[i] = vertices[i].rotated(position, angle);
        }
        fov.rotate(angle);
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

    private Obstacle findWorst(ArrayList<Drawable> objects) {
        float minDistance = Float.MAX_VALUE;
        Obstacle minObstacle = null;
        for (var i : Obstacle.obstacles) {
            Obstacle obstacle = (Obstacle) objects.get(i);
            if (fov.isIntersecting(obstacle.getBoundingBox(), velocity)) {
                float distance;
                if ((distance = Vector2.distance(position, obstacle.getPosition())) < minDistance) {
                    minDistance = distance;
                    minObstacle = obstacle;
                }
            }
        }
        return minObstacle;
    }

    protected Vector2 obstacleAvoidance(ArrayList<Drawable> objects, float maxSpeed, float maxAcceleration, float avoidObstaclesWeight) {
        Vector2 steer = Vector2.ZERO;
        Obstacle toAvoid = findWorst(objects);
        if (toAvoid == null) {
            return steer;
        }
        if (position.isInside(toAvoid.getBoundingBox())) {
            steer = toAvoid.getPosition().subtract(velocity);
        } else {
            steer = fov.findPathAway(toAvoid.getBoundingBox());
        }
        if (steer.magnitude() > 0) {
            steer.normalize();
            steer = steer.multiply(maxSpeed);
        }
        steer = steer.subtract(velocity);
        steer.limit(maxAcceleration);

        steer = steer.multiply(avoidObstaclesWeight);
        return steer;
    }

    @Override
    public void update(Animation animation, double frameTime) {
        position = position.add(velocity.multiply(frameTime));
        for (int i = 0; i < vertices.length; i++) {
           vertices[i] = vertices[i].add(velocity.multiply(frameTime));
        }
        remainOnScreen(animation.getDimensions());
        fov.setPosition(position);
        fov.setDirection(velocity);
        rotate();
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(Drawable.drawShape(vertices));
    }

    protected void setIndex(int index) {
        this.index = index;
    }
}
