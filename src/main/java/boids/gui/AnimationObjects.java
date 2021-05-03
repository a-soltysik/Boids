package boids.gui;

import boids.drawables.*;

import java.util.ArrayList;

public class AnimationObjects {

    public static volatile ArrayList<Drawable> objects = new ArrayList<>();
    private final AnimationPanel panel;


    public AnimationObjects(AnimationPanel panel) {
        this.panel = panel;
    }

    public void prepareObjects() {
       var background = new LowPolyBackground(panel, 10, GuiParameters.backGroundSpeed);
        objects.add(background);

        for (int i = 0; i < GuiParameters.predatorNumber; i++) {
            Predator.addPredator(panel,objects);
        }
        for (int i = 0; i < GuiParameters.preyNumber ; i++) {
            Prey.addPrey(panel,objects);
        }
        for (int i=0; i<GuiParameters.obstacleNumber; i++) {
            Obstacle.addObstacle(panel, objects);
        }

    }
    public static ArrayList<Drawable> getList() {
        return objects;
    }
}