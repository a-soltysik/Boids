package boids.gui;

import boids.drawables.*;

import java.util.ArrayList;
import java.util.Collections;

public class AnimationObjects {
    private final ArrayList<Drawable> objects = new ArrayList<>();
    private final AnimationPanel panel;

    public AnimationObjects(AnimationPanel panel) {
        this.panel = panel;
    }

    public void prepareObjects() {
       var background = new LowPolyBackground(panel, 10, 0.05f);
        objects.add(background);

        /*for (int i = 0; i < 2; i++) {
            Predator.addPredator(panel,objects);
        }*/
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