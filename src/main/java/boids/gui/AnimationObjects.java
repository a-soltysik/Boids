package boids.gui;

import boids.drawables.Drawable;
import boids.drawables.LowPolyBackground;
import boids.drawables.Predator;
import boids.drawables.Prey;

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
        for (int i = 0; i < 1000; i++) {
            Prey.addPrey(panel,objects);
        }

        for (int i = 0; i < 3; i++) {
            Predator.addPredator(panel,objects);
        }
    }
    public ArrayList<Drawable> getList() {
        return objects;
    }
}
