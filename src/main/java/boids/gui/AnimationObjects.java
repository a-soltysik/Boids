package boids.gui;

import boids.drawables.*;

import java.util.ArrayList;

public class AnimationObjects {

    private ArrayList<Drawable> objects = new ArrayList<>();
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
    public void addObjects(){
        while(Prey.preys.size() < GuiParameters.preyNumber) {
            Prey.addPrey(panel, objects);
        }
        while(Prey.preys.size() > GuiParameters.preyNumber) {
            Prey.removePrey(objects);
        }
        while(Obstacle.obstacles.size() < GuiParameters.obstacleNumber) {
            Obstacle.addObstacle(panel, objects);
        }
        while(Obstacle.obstacles.size() > GuiParameters.obstacleNumber) {
            Obstacle.removeObstacle(objects);
        }
        while(Predator.predators.size() < GuiParameters.predatorNumber) {
            Predator.addPredator(panel, objects);
        }
        while(Predator.predators.size() > GuiParameters.predatorNumber) {
            Predator.removePredator(objects);
        }
    }
    public  ArrayList<Drawable> getList() {
        return objects;
    }
}