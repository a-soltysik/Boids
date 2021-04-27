package boids.gui;

import boids.drawables.Drawable;
import boids.objects.LowPolyBackground;
import boids.objects.Obstacle;
import boids.objects.Predator;
import boids.objects.Prey;

import java.util.ArrayList;

public class AnimationObjects {
    private final ArrayList<Drawable> objects = new ArrayList<>();
    private final AnimationPanel panel;

    public AnimationObjects(AnimationPanel panel) {
        this.panel = panel;
    }

    public void prepareObjects() {
       var background = new LowPolyBackground(panel, 10, 0.05f);
        objects.add(background);

        for (int i = 0; i < 2; i++) {
            Predator.addPredator(panel,objects);
        }
        for (int i = 0; i < 100; i++) {
            Prey.addPrey(panel,objects);
        }
        for (int i=0; i<10; i++) {
            Obstacle.addObstacle(panel, objects);
        }

    }
    public ArrayList<Drawable> getList() {
        return objects;
    }
}