package boids.gui;

import boids.drawables.Predator;
import boids.drawables.Prey;


public class GuiParameters {
    public static volatile int preyNumber;
    public static volatile int predatorNumber;
    public static volatile float backGroundSpeed;
    public static volatile int obstacleNumber;
    public static volatile float predatorMaxSpeed;
    public static volatile float predatorMaxAcceleration;
    public static volatile float preyMaxSpeed;
    public static volatile float preyMaxAcceleration;
    public static volatile float preyAlignmentWeight;
    public static volatile float preySeparationWeight;
    public static volatile float preyCohesionWeight ;
    public static volatile String fileName = "target/generated-sources/test.csv";;

    public GuiParameters (){
        this.predatorNumber = 2;
        this.preyNumber = 200;
        this.obstacleNumber = 0;
        this.backGroundSpeed = 0.1f;
        this.predatorMaxSpeed = Predator.getMaxSpeed();
        this.predatorMaxAcceleration = Predator.getMaxAcceleration();
        this.preyMaxSpeed = Prey.getMaxSpeed();
        this.preyMaxAcceleration = Prey.getMaxAcceleration();
        this.preyAlignmentWeight = Prey.getAlignmentWeight();
        this.preySeparationWeight = Prey.getSeparationWeight();
        this.preyCohesionWeight = Prey.getCohesionWeight();
    }
}
