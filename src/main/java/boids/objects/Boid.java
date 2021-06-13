package boids.objects;

import boids.drawables.Drawable;
import boids.drawables.geometry.DPolygon;
import boids.drawables.geometry.DSegment;
import boids.gui.Animation;
import boids.gui.GuiParameters;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public abstract class Boid extends DPolygon{
    protected FOV fov;
    protected Vector2 velocity;
    protected Vector2 acceleration;
    protected int index;

    public Boid(float size, Vector2 position, FOV fov, Color color) {
        super(new Vector2[]{
                        new Vector2(0f, 0f),
                        new Vector2(0.5f * size, 1f * size),
                        new Vector2(0f, 0.75f * size),
                        new Vector2(-0.5f * size, 1f * size)},
                position, color);
        this.position = position;
        this.fov = fov;
        velocity = Vector2.ZERO;
    }

    private void rotate() {
        if (velocity.magnitude() == 0) {
            return;
        }
        //Vector2 direction = position.subtract(getVertices()[2]);
        Vector2 direction = fov.getDirection();
        //System.out.println("v: " + fov.getDirection());
        //System.out.println("direction: " + position.subtract(getVertices()[2]));
        Vector2 newDirection = velocity;

        if (direction.isZero() || newDirection.isZero()) {
            return;
        }
        float angle = direction.directAngle(newDirection);
        rotate(angle);
        fov.rotate(angle);
    }

    private void remainOnScreen(Rectangle frame) {
        Vector2 newPosition = new Vector2(position);
        if (newPosition.x > frame.max.x) {
            newPosition.x = frame.min.x;
        } else if (newPosition.x < frame.min.x) {
            newPosition.x = frame.max.x;
        }
        if (newPosition.y > frame.max.y) {
            newPosition.y = frame.min.y;
        } else if (newPosition.y < frame.min.y) {
            newPosition.y = frame.max.y;
        }
        if (!newPosition.equals(position)) {
            moveTo(newPosition);
        }
    }

    private Obstacle findWorst(ArrayList<Drawable> objects) {
        float minDistance = Float.MAX_VALUE;
        Obstacle minObstacle = null;
        for (var i : Obstacle.obstaclesIndices) {
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

    public Vector2 getPosition() {
        return new Vector2(position);
    }

    @Override
    public void update(Animation animation, double frameTime) {
        Vector2 newPosition = position.add(velocity.multiply(frameTime));
        moveTo(newPosition);
        rotate();
        remainOnScreen(animation.getDimensions());
        fov.setPosition(newPosition);
        fov.setDirection(velocity);
    }

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
        if (GuiParameters.showVelocity.getValue()) {
            new DSegment(position, position.add(velocity), Color.green).render(g2d);
        }
        if (GuiParameters.showFov.getValue()) {
            fov.render(g2d);
        }
    }

    protected void setIndex(int index) {
        this.index = index;
    }
}
