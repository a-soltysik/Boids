package boids.gui;

public class GuiParameters {
    public static volatile String fileName = "plik.csv";
    public static volatile int preyNumber = 200;
    public static volatile int predatorNumber = 2;
    public static volatile int obstacleNumber = 0;
    public static volatile float predatorMaxSpeed = 30f;
    public static volatile float predatorMaxAcceleration = 8f;
    public static volatile float preyMaxSpeed = 50f;
    public static volatile float preyMaxAcceleration = 10f;
    public static volatile float preyAlignmentWeight = 1.5f;
    public static volatile float preySeparationWeight = 2f;
    public static volatile float preyCohesionWeight = 1.5f;
    public static volatile float backGroundSpeed = 0.1f;
    public static volatile boolean writeToFile = false;
    public static volatile boolean isEndOfProgram = false;

    private GuiParameters (){
    }
}
