package boids.drawables;

import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static boids.drawables.Prey.preys;

public class Predator extends Boid {
    private static final Random rnd = ThreadLocalRandom.current();
    protected static final ArrayList<Integer> predators = new ArrayList<>();
    private static final float desiredSeparation = 20f;
    private static final float maxSpeed = 30f;
    private static final float maxAcceleration = 8f;
    private static final float fovRadius = 100f;

    public Predator(Vector2 position){
        super(20f,position, Color.orange);
    }

    private Vector2 attraction(ArrayList<Drawable> objects) {

        Vector2 steer = Vector2.ZERO;
        int count = 0;

        for(var i : preys) {
            Prey prey = (Prey) objects.get(i);
            float distance = Vector2.distance(this.position, prey.position);
            if (distance < fovRadius ) {
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
            if (steer.magnitude() > 0) {
                steer.limit(maxAcceleration);
            }
        }
        return steer;
    }

    private Vector2 separate(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;
        for (var i : predators) {
            if (i == index) {
                continue;
            }
            Predator predator = (Predator) objects.get(i);
            float distance = Vector2.distance(this.position, predator.position);
            if (distance < desiredSeparation) {
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
            if (steer.magnitude() > 0) {
                steer.limit(maxAcceleration);
            }
        }
        return steer;
    }


    public static void addPredator(AnimationPanel panel, ArrayList<Drawable> objects){
        var predator = new Predator(new Vector2(
                (float) rnd.nextInt(panel.getWidth()),
                (float) rnd.nextInt(panel.getHeight())
        ));

        predator.velocity = new Vector2(
                (float) rnd.nextInt(30),
                (float) rnd.nextInt(30)
        );

        for (int i = 0; i < predator.vertices.length; i++) {
            predator.vertices[i] = predator.vertices[i].add(predator.position);
        }

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

    @Override
    public void update(Animation animation, double frameTime) {
        acceleration = Vector2.ZERO;
        acceleration = acceleration.add(separate(animation.getObjects()));
        acceleration = acceleration.add(attraction(animation.getObjects()));
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


