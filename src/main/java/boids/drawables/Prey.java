package boids.drawables;

import boids.gui.Animation;
import boids.gui.AnimationObjects;
import boids.gui.AnimationPanel;
import boids.gui.GuiParameters;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Prey extends Boid {

    private static final Random rnd = ThreadLocalRandom.current();
    public static final ArrayList<Integer> preys = new ArrayList<>();
    private static float maxSpeed = 50f;
    private static float maxAcceleration = 10f;
    private static float desiredSeparation = 30f;
    private static float separationWeight = 2f;
    private static float alignmentWeight = 1.5f;
    private static float cohesionWeight = 1.5f;
    private static float escapeWeight = (separationWeight+alignmentWeight+cohesionWeight)*4;
    private static float avoidObstaclesWeight =(separationWeight+alignmentWeight+cohesionWeight)*10;


    public Prey(Vector2 position) {
        super(10f, position,
                new FOV(340, 60f, 50),
                Color.darkGray
        );
    }


    private Vector2 separation(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for (var i : preys) {
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

        for (var i : preys) {
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

        for (var i : preys) {
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

        for (var i : Predator.predators) {
            if (i == index) {
                continue;
            }
            Predator predator = (Predator) objects.get(i);
            if (fov.isIntersecting(predator.position)) {
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
        steer = steer.multiply(escapeWeight);
        return steer;
    }
    public static void addPrey(AnimationPanel panel, ArrayList<Drawable> objects){
        boolean intersects;
        Prey prey;
        do {
            intersects = false;
            prey = new Prey(new Vector2(
                    (float) rnd.nextInt(panel.getWidth()),
                    (float) rnd.nextInt(panel.getHeight())
            ));
            for (var i : Obstacle.obstacles) {
                if (prey.position.isInside(((Obstacle)objects.get(i)).getBoundingBox())) {
                    intersects = true;
                }
            }
        } while (intersects);

        prey.velocity = new Vector2(
                (float) rnd.nextInt(40),
                (float) rnd.nextInt(40)
        );

        for (int i = 0; i < prey.vertices.length; i++) {
            prey.vertices[i] = prey.vertices[i].add(prey.position);
        }

        objects.add(prey);
        preys.add(objects.size() - 1);
        prey.setIndex(objects.size() - 1);
    }

    public static void removePrey(ArrayList<Drawable> objects) {
        if (preys.size() > 0) {
            objects.remove((int) preys.get(preys.size() - 1));
            preys.remove(preys.size() - 1);
        }
    }
    public static float getAverageVelocity(ArrayList<Drawable> objects){
        float averageVelocity = 0;
        int count = 0;
        for (var i : preys) {
            Prey prey = (Prey) objects.get(i);
            averageVelocity += prey.velocity.magnitude();
            count++;
        }
        if (count > 0) {
            averageVelocity /= count;
        }
        return averageVelocity;
    }

    @Override
    public void update(Animation animation, double frameTime) {
        cohesionWeight = GuiParameters.preyCohesionWeight;
        separationWeight = GuiParameters.preySeparationWeight;
        alignmentWeight = GuiParameters.preyAlignmentWeight;
        maxSpeed = GuiParameters.preyMaxSpeed;
        maxAcceleration = GuiParameters.preyMaxAcceleration;
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

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
    }

}
