package boids.drawables;

import boids.drawables.geometry.DPolygon;
import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public abstract class Boid implements Drawable{
    protected FOV fov;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 acceleration;
    protected final DPolygon shape;
    protected float size;
    protected int index;

    public Boid(float size, Vector2 position, FOV fov, Color color) {
        this.size = size;
        this.position = position;
        this.fov = fov;
        velocity = Vector2.ZERO;
        shape = new DPolygon(
                new Vector2[]{
                        new Vector2(0f, 0f),
                        new Vector2(0.5f * size, 1f * size),
                        new Vector2(0f, 0.75f * size),
                        new Vector2(-0.5f * size, 1f * size)},
                position, color);
    }

    private void rotate() {
        if (velocity.magnitude() == 0) {
            return;
        }
        Vector2 direction = position.subtract(shape.getVertices()[2]);
        Vector2 newDirection = velocity;

        if (direction.isZero() || newDirection.isZero()) {
            return;
        }
        float angle = direction.directAngle(newDirection);
        shape.rotate(angle);
        fov.rotate(angle);
    }

    private void remainOnScreen(Rectangle frame) {
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
        remainOnScreen(animation.getDimensions());
        shape.moveTo(position);
        rotate();
        fov.setPosition(position);
        fov.setDirection(velocity);
    }

    @Override
    public void render(Graphics2D g2d) {
        shape.render(g2d);
    }

    protected void setIndex(int index) {
        this.index = index;
    }
}
