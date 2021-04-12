package boids.gui;

import boids.drawables.Drawable;
import boids.drawables.LowPolyBackground;
import boids.drawables.Obstacle;

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
        for (int i = 0; i < 5; i++) {
            Obstacle.addObstacle(panel, objects);
        }
    }
    public ArrayList<Drawable> getList() {
        return objects;
    }
}
