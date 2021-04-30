package boids.gui;

import boids.drawables.*;

import java.util.ArrayList;

public class AnimationObjects {

    public static final ArrayList<Drawable> objects = new ArrayList<>();
    private final AnimationPanel panel;


    public AnimationObjects(AnimationPanel panel) {
        this.panel = panel;
    }

    public void prepareObjects() {
       var background = new LowPolyBackground(panel, 10, LowPolyBackground.speed);
        objects.add(background);

        for (int i = 0; i < Predator.predatorNumber; i++) {
            Predator.addPredator(panel,objects);
        }
        for (int i = 0; i < Prey.preyNumber ; i++) {
            Prey.addPrey(panel,objects);
        }
        for (int i=0; i<Obstacle.obstacleNumber; i++) {
            Obstacle.addObstacle(panel, objects);
        }

    }
    public ArrayList<Drawable> getList() {
        return objects;
    }
}