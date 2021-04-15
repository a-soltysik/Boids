package boids.drawables;

import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Prey extends Boid{

    private static final Random rnd = ThreadLocalRandom.current();
    private static final ArrayList<Integer> preys = new ArrayList<>();
    private static final float maxSpeed = 50f;
    private static final float maxAcceleration = 10f;
    private static final float fovRadius = 60f;
    private static final float desiredSeparation = 30f;
    public static double separationWeight = 2.5;
    public static double alignmentWeight = 1.5;
    public static double cohesionWeight = 1.5;

    public Prey(Vector2 position) {
        super(position);
    }

    private Vector2 separate(ArrayList<Drawable> objects) {
        Vector2 steer = Vector2.ZERO;
        int count = 0;
        for (var i : preys) {
            if (i == index) {
                continue;
            }
            Prey prey = (Prey) objects.get(i);
            float distance = Vector2.distance(this.position, prey.position);
            if (distance < desiredSeparation) {
                Vector2 diff = this.position.subtract(prey.position);
                if (distance > 0) {
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
                steer = steer.subtract(this.velocity);
                steer.limit(maxAcceleration);
            }
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
            float distance = Vector2.distance(this.position, prey.position);
            if (distance < fovRadius) {
                steer = steer.add(prey.velocity);
                count++;
            }
        }
            if (count > 0) {
                steer = steer.divide(count);
                if (steer.magnitude() > 0) {
                    steer.normalize();
                    steer = steer.multiply(maxSpeed);
                    steer = steer.subtract(velocity);
                    steer.limit(maxAcceleration);
                }
            }
            steer = steer.multiply(alignmentWeight);
            return steer;

        }
    private Vector2 cohesion(ArrayList<Drawable> objects) {

        Vector2 target = Vector2.ZERO;
        int count = 0;

        for (var i : preys) {
            if (i == index) {
                continue;
            }
            Prey prey = (Prey) objects.get(i);
            float distance = Vector2.distance(this.position, prey.position);
            if ((distance > 0) && (distance < fovRadius)) {
                target= target.add(prey.position);
                count++;
            }
        }

        if(count == 0) return target;
        else target = target.divide(count);

        Prey prey = (Prey) objects.get(index);
        Vector2 steer = target.subtract(prey.position);
        steer.normalize();
        steer= steer.multiply(maxSpeed);
        steer=steer.subtract(velocity);
        steer.limit(maxAcceleration);
        steer = steer.multiply(cohesionWeight);
        return steer;
    }


    public static void addPrey(AnimationPanel panel, ArrayList<Drawable> objects){
        var prey = new Prey(
                new Vector2((float) rnd.nextInt(panel.getWidth()),
                        (float) rnd.nextInt(panel.getHeight())));

        prey.velocity = new Vector2(
                (float) (rnd.nextInt(40) - 20),
                (float) (rnd.nextInt(40) - 20)
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


    @Override
    public void update(Animation animation, double frameTime) {
        acceleration = Vector2.ZERO;
        acceleration = acceleration.add(separate(animation.getObjects()));
        acceleration = acceleration.add(alignment(animation.getObjects()));
        acceleration = acceleration.add(cohesion(animation.getObjects()));
        velocity = velocity.add(acceleration.multiply(frameTime));
        velocity.limit(maxSpeed);
        super.update(animation, frameTime);

    }

    @Override
    public void render(Graphics2D g2d) {
        super.render(g2d);
    }
}
