package boids.objects;

import boids.Utils;
import boids.drawables.Drawable;
import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.gui.GuiParameters;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Prey extends Boid {

    protected static final ArrayList<Integer> preysIndices = new ArrayList<>();
    private static float maxSpeed;
    private static float maxAcceleration;
    private static final float desiredSeparation = 30f;

    private static float separationWeight;
    private static float alignmentWeight;
    private static float cohesionWeight;
    private static float escapeWeight;
    private static float avoidObstaclesWeight;


    public Prey(Vector2 position) {
        super(10f, position,
                new FOV(340, 60f, 50),
                Color.darkGray
        );
    }

    private Vector2 separation(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for (var i : preysIndices) {
            if (i == index) {
                continue;
            }
            Prey prey = (Prey) objects.get(i);
            if (fov.isIntersecting(prey.position, desiredSeparation)) {
                float distance = Vector2.distance(position, prey.position);
                Vector2 diff = this.position.subtract(prey.position);
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
        steer = steer.multiply(separationWeight);
        return steer;
    }
    private Vector2 alignment(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for (var i : preysIndices) {
            if (i == index) {
                continue;
            }
            Prey prey = (Prey) objects.get(i);
            if (fov.isIntersecting(prey.position)) {
                steer = steer.add(prey.velocity);
                count++;
            }
        }
        if (count > 0) {
            steer = steer.divide(count);
            if (steer.magnitude() > 0) {
                steer.normalize();
                steer = steer.multiply(maxSpeed);
            }
            steer = steer.subtract(velocity);
            if (steer.magnitude() > 0) {
                steer.limit(maxAcceleration);
            }
        }
        steer = steer.multiply(alignmentWeight);
        return steer;
    }
    private Vector2 cohesion(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for (var i : preysIndices) {
            if (i == index) {
                continue;
            }
            Prey prey = (Prey) objects.get(i);
            if (fov.isIntersecting(prey.position)) {
                steer = steer.add(prey.position);
                count++;
            }
        }
        if (count > 0) {
            steer = steer.divide(count);
            steer = steer.subtract(position);
            if (steer.magnitude() > 0) {
                steer.normalize();
                steer = steer.multiply(maxSpeed);
            }
            steer = steer.subtract(velocity);
            steer.limit(maxAcceleration);
        }
        steer = steer.multiply(cohesionWeight);
        return steer;
    }
    private Vector2 escape(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for (var i : Predator.predatorsIndices) {
            if (i == index) {
                continue;
            }
            Predator predator = (Predator) objects.get(i);
            if (fov.isIntersecting(predator.getPosition())) {
                float distance = Vector2.distance(position, predator.getPosition());
                Vector2 diff = this.position.subtract(predator.getPosition());
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
        steer = steer.multiply(escapeWeight);
        return steer;
    }
    public static void addPrey(AnimationPanel panel, ArrayList<Drawable> objects){
        boolean intersects;
        Prey prey;
        do {
            intersects = false;
            prey = new Prey(new Vector2(
                    Utils.randomFloat(0f, panel.getWidth()),
                    Utils.randomFloat(0f, panel.getHeight())
            ));
            for (var i : Obstacle.obstaclesIndices) {
                if (prey.position.isInside(((Obstacle)objects.get(i)).getBoundingBox())) {
                    intersects = true;
                }
            }
        } while (intersects);

        prey.velocity = new Vector2(
                Utils.randomFloat(0f, 40f),
                Utils.randomFloat(0f, 40f));

        int i;
        for (i=0; i<objects.size(); i++) {
            if (objects.get(i) == null) {
                objects.set(i, prey);
                break;
            }
        }
        if (i == objects.size()) {
            objects.add(prey);
        }
        preysIndices.add(i);
        prey.setIndex(i);
    }

    public static void removePrey(ArrayList<Drawable> objects) {
        if (preysIndices.size() > 0) {
            objects.set(preysIndices.get(preysIndices.size() - 1), null);
            preysIndices.remove(preysIndices.size() - 1);
        }
    }
    public static float getAverageVelocity(ArrayList<Drawable> objects){
        float averageVelocity = 0;
        int count = 0;
        for (var i : preysIndices) {
            Prey prey = (Prey) objects.get(i);
            averageVelocity += prey.velocity.magnitude();
            count++;
        }
        if (count > 0) {
            averageVelocity /= count;
        }
        return averageVelocity;
    }

    public static void updateParameters() {
        cohesionWeight = GuiParameters.preyCohesionWeight.getValue();
        separationWeight = GuiParameters.preySeparationWeight.getValue();
        alignmentWeight = GuiParameters.preyAlignmentWeight.getValue();
        maxSpeed = GuiParameters.preyMaxSpeed.getValue();
        maxAcceleration = GuiParameters.preyMaxAcceleration.getValue();
        escapeWeight = (separationWeight + alignmentWeight + cohesionWeight) * 4;
        avoidObstaclesWeight =(separationWeight + alignmentWeight + cohesionWeight) * 10;
    }

    public static ArrayList<Integer> getPreysIndices() {
        return preysIndices;
    }

    public static int getPreysNumber() {
        return preysIndices.size();
    }

    @Override
    public void update(Animation animation, double frameTime) {
        updateParameters();
        //updateFOV();
        acceleration = Vector2.ZERO;
        acceleration = acceleration.add(separation(animation.getObjects()));
        acceleration = acceleration.add(alignment(animation.getObjects()));
        acceleration = acceleration.add(cohesion(animation.getObjects()));
        acceleration = acceleration.add(escape(animation.getObjects()));
        acceleration = acceleration.add(obstacleAvoidance(animation.getObjects(),
                maxSpeed, maxAcceleration, avoidObstaclesWeight));
        velocity = velocity.add(acceleration.multiply(frameTime));
        if (velocity.magnitude() > 0) {
            velocity.limit(maxSpeed);
        }
        super.update(animation, frameTime);
    }
}
