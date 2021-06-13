package boids.objects;

import boids.Utils;
import boids.drawables.Drawable;
import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.gui.GuiParameters;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Predator extends Boid {
    protected static final ArrayList<Integer> predatorsIndices = new ArrayList<>();
    private static final float desiredSeparation = 80f;
    private static float maxSpeed;
    private static float maxAcceleration;
    private static final float separationWeight = 2f;
    private static final float attractionWeight = 5f;
    private static final float avoidObstaclesWeight = 10f;

    public Predator(Vector2 position){
        super(20f,position,
                new FOV(GuiParameters.predatorFovAngleDeg.getValue(), GuiParameters.predatorFovRadius.getValue()),
                Color.orange
        );
    }

    private Vector2 attraction(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for(var i : Prey.preysIndices) {
            Prey prey = (Prey) objects.get(i);
            if (fov.isIntersecting(prey.getPosition())) {
                steer = steer.add(prey.getPosition());
                count++;
            }
        }
        if(count > 0) {
            steer = steer.divide(count);
            steer = steer.subtract(position);
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

        for (var i : predatorsIndices) {
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
            for (var i : Obstacle.obstaclesIndices) {
                if (predator.position.isInside(((Obstacle)objects.get(i)).getBoundingBox())) {
                    intersects = true;
                }
            }
        } while (intersects);

        predator.velocity = new Vector2(
                Utils.randomFloat(0f, 40f),
                Utils.randomFloat(0f, 40f)
        );

        int i;
        for (i=0; i<objects.size(); i++) {
            if (objects.get(i) == null) {
                objects.set(i, predator);
                break;
            }
        }
        if (i == objects.size()) {
            objects.add(predator);
        }
        predatorsIndices.add(i);
        predator.setIndex(i);
    }
    public static void removePredator(ArrayList<Drawable> objects) {
        if (predatorsIndices.size() > 0) {
            objects.set(predatorsIndices.get(predatorsIndices.size() - 1), null);
            predatorsIndices.remove(predatorsIndices.size() - 1);
        }
    }
    public static float getAverageVelocity(ArrayList<Drawable> objects){
        float averageVelocity = 0;
        int count = 0;
        for (var i : predatorsIndices) {
            Predator predator = (Predator) objects.get(i);
            averageVelocity += predator.velocity.magnitude();
            count++;
        }
        if (count > 0) {
            averageVelocity /= count;
        }
        return averageVelocity;
    }

    public static void updateParameters() {
        maxSpeed = GuiParameters.predatorMaxSpeed.getValue();
        maxAcceleration = GuiParameters.predatorMaxAcceleration.getValue();
    }

    public static int getPredatorsNumber() {
        return predatorsIndices.size();
    }

    @Override
    public void update(Animation animation, double frameTime) {
        updateParameters();
        fov.updateFOV(GuiParameters.predatorFovAngleDeg.getValue(), GuiParameters.predatorFovRadius.getValue());
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
}


