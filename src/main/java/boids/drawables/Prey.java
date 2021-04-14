package boids.drawables;

import boids.gui.AnimationPanel;
import boids.math.Vector2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Prey extends Boid{

    private static final Random rnd = ThreadLocalRandom.current();
    private static final ArrayList<Integer> preys = new ArrayList<>();

    public Prey(Vector2 position) {
        super(position);
    }

    public static void addPrey(AnimationPanel panel, ArrayList<Drawable> objects){
        var prey = new Prey(
                new Vector2((float) rnd.nextInt(panel.getWidth()),
                        (float) rnd.nextInt(panel.getHeight())));

        prey.velocity = new Vector2((float) (rnd.nextInt(40)-20 ), (float) (rnd.nextInt(40) - 20));

        for (int i = 0; i < prey.vertices.length; i++) {
            prey.vertices[i] = prey.vertices[i].add(prey.position);
        }
        objects.add(prey);
        preys.add(objects.size() - 1);
    }

    public static void removePrey(ArrayList<Drawable> objects) {
        if (preys.size() > 0) {
            objects.remove((int) preys.get(preys.size() - 1));
            preys.remove(preys.size() - 1);
        }
    }

    @Override
    public void update(double frameTime) {
        ///Will be added when adding rules
        super.update(frameTime);
    }
}
