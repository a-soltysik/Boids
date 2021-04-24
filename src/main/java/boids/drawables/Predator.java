package boids.drawables;

import boids.Utils;
import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static boids.drawables.Prey.preys;

public class Predator extends Boid {
    protected static final ArrayList<Integer> predators = new ArrayList<>();
    private static final float desiredSeparation = 80f;
    private static final float maxSpeed = 30f;
    private static final float maxAcceleration = 8f;
    public static float separationWeight = 2f;
    public static float attractionWeight = 5f;
    public static float avoidObstaclesWeight = 10f;

    public Predator(Vector2 position){
        super(20f,position,
                new FOV(90, 100f, 10),
                Color.orange
        );
    }

    private Vector2 attraction(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for(var i : preys) {
            Prey prey = (Prey) objects.get(i);
            if (fov.isIntersecting(prey.position)) {
               steer = steer.add(prey.position);
                count++;
            }
        }
        if(count > 0) {
            steer = steer.divide(count);
            steer = steer.subtract(this.position);
            if (steer.magnitude() > 0) {
                steer.normalize();
                steer = steer.multiply(maxSpeed);
            }
            steer = steer.subtract(velocity);
            steer.limit(maxAcceleration);
        }
        return steer.multiply(attractionWeight);
    }

    private Vector2 separation(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for (var i : predators) {
            if (i == index) {
                continue;
            }
            Predator predator = (Predator) objects.get(i);
            if (fov.isIntersecting(predator.position, desiredSeparation)) {
                float distance = Vector2.distance(this.position, predator.position);
                Vector2 diff = this.position.subtract(predator.position);
                if (distance * distance > 0) {
                    diff = diff.divide(distance * distance);
                }
                steer = steer.add(diff);
                count++;
            }
        }
        if (count > 0) {
            steer = steer.divide(count);
            if (steer.magnitude() > 0) {
                steer.normalize();
                steer = steer.multiply(maxSpeed);
            }
            steer = steer.subtract(this.velocity);
            steer.limit(maxAcceleration);
        }
        return steer.multiply(separationWeight);
    }

    public static void addPredator(AnimationPanel panel, ArrayList<Drawable> objects){
        boolean intersects;
        Predator predator;
        do {
            intersects = false;
            predator = new Predator(new Vector2(
                    Utils.randomFloat(0f, panel.getWidth()),
                    Utils.randomFloat(0f, panel.getHeight())
            ));
            for (var i : Obstacle.obstacles) {
                if (predator.position.isInside(((Obstacle)objects.get(i)).getBoundingBox())) {
                    intersects = true;
                }
            }
        } while (intersects);

        predator.velocity = new Vector2(
                Utils.randomFloat(0f, 40f),
                Utils.randomFloat(0f, 40f)
        );

        objects.add(predator);
        predators.add(objects.size() - 1);
        predator.setIndex(objects.size() - 1);
    }
    public static void removePredator(ArrayList<Drawable> objects) {
        if (predators.size() > 0) {
            objects.remove((int) predators.get(predators.size() - 1));
            predators.remove(predators.size() - 1);
        }
    }
    public static float getAverageVelocity(ArrayList<Drawable> objects){
        float averageVelocity = 0;
        int count = 0;
        for (var i : predators) {
            Predator predator = (Predator) objects.get(i);
        averageVelocity += predator.velocity.magnitude();
        count++;
        }
        if (count > 0) {
            averageVelocity /= count;
        }
        return averageVelocity;
    }

    @Override
    public void update(Animation animation, double frameTime) {
        acceleration = Vector2.ZERO;
        acceleration = acceleration.add(separation(animation.getObjects()));
        acceleration = acceleration.add(attraction(animation.getObjects()));
        acceleration = acceleration.add(obstacleAvoidance(animation.getObjects(),
                maxSpeed, maxAcceleration, avoidObstaclesWeight));
        velocity = velocity.add(acceleration.multiply(frameTime));
        if (velocity.magnitude() > 0) {
            velocity.limit(maxSpeed);
        }
        super.update(animation, frameTime);
    }

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
    }
}


