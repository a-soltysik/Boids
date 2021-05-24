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
        var background = new LowPolyBackground(panel, 10, GuiParameters.backGroundSpeed.getValue());
        objects.add(background);

        for (int i = 0; i < GuiParameters.predatorNumber.getValue(); i++) {
            Predator.addPredator(panel,objects);
        }
        for (int i = 0; i < GuiParameters.preyNumber.getValue() ; i++) {
            Prey.addPrey(panel,objects);
        }
        for (int i=0; i<GuiParameters.obstacleNumber.getValue(); i++) {
            Obstacle.addObstacle(panel, objects);
        }
    }
    public void addObjects(){
        while(Prey.getPreysNumber() < GuiParameters.preyNumber.getValue()) {
            Prey.addPrey(panel, objects);
        }
        while(Prey.getPreysNumber() > GuiParameters.preyNumber.getValue()) {
            Prey.removePrey(objects);
        }
        while(Obstacle.getObstaclesNumber() < GuiParameters.obstacleNumber.getValue()) {
            Obstacle.addObstacle(panel, objects);
        }
        while(Obstacle.getObstaclesNumber() > GuiParameters.obstacleNumber.getValue()) {
            Obstacle.removeObstacle(objects);
        }
        while(Predator.getPredatorsNumber() < GuiParameters.predatorNumber.getValue()) {
            Predator.addPredator(panel, objects);
        }
        while(Predator.getPredatorsNumber() > GuiParameters.predatorNumber.getValue()) {
            Predator.removePredator(objects);
        }
    }
    public ArrayList<Drawable> getList() {
        return objects;
    }
}