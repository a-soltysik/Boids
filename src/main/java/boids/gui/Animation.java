package boids.gui;

import boids.write_to_file.CSVWriter;
import boids.drawables.Drawable;
import boids.drawables.Predator;
import boids.drawables.Prey;
import boids.math.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Animation {
    private AnimationPanel frame;
    private double frameTime;
    private int fps;
    private final int preferredFps;
    private final int TIME_SCALE = 1_000_000_000;
    private final boolean running = true;
    private static String fileName = "target/generated-sources/test.csv";
    private static int bufferSize=100;
    String preyHeader = "Prey average velocity";
    String predatorHeader = "Predator average velocity";
    private String[] headers = {preyHeader,predatorHeader};
    private ArrayList<Float> averageVelocities= new ArrayList<Float>();
    private AnimationObjects objects;
    private CSVWriter writer = new CSVWriter(fileName,bufferSize,headers);

    private volatile boolean paused = false;

    public static final int UNLIMITED_FPS = 0;

    public Animation(int fps) {
        if (fps < 0) {
            throw new IllegalArgumentException();
        }
        preferredFps = fps;
    }

    public void start(AnimationPanel panel) {
        frame = panel;
        objects = new AnimationObjects(frame);
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                objects.prepareObjects();
                if (preferredFps == UNLIMITED_FPS) {
                    unlimitedLoop();
                } else {
                    loop();
                }
                return null;
            }
        }.execute();
    }


    private void unlimitedLoop() {
        long currentTime;
        long previousTime = System.nanoTime();
        long time = 0;
        long frameTimeNanos;
        int current_fps = 0;
        writer.setIndex(preyHeader);
        writer.setIndex(predatorHeader);
        while (running) {
            if (!paused) {
                update();
                render();
                write();
            }

            currentTime = System.nanoTime();
            frameTimeNanos = currentTime - previousTime;
            previousTime = currentTime;
            time += frameTimeNanos;
            frameTime = (double) frameTimeNanos / TIME_SCALE;
            current_fps++;
            if (time >= TIME_SCALE) {
                time = 0;
                fps = current_fps;
                current_fps = 0;
            }
        }
    }

    private void loop() {
        long currentTime;
        long previousTime = System.nanoTime();
        long time = 0;
        int timeToRender = 0;
        int current_fps = 0;
        long frameTimeNanos;
        final int preferredFrameTime = TIME_SCALE / preferredFps;

        while (running) {

            if (!paused) {
                update();
                if (timeToRender >= preferredFrameTime) {
                    render();
                    timeToRender = 0;
                    current_fps++;
                }
            }

            currentTime = System.nanoTime();
            frameTimeNanos = currentTime - previousTime;
            previousTime = currentTime;
            time += frameTimeNanos;
            timeToRender += frameTimeNanos;
            frameTime = (double) frameTimeNanos / TIME_SCALE;

            if (time > TIME_SCALE) {
                time = 0;
                fps = current_fps;
                current_fps = 0;
            }
        }
    }


    private void update() {
        objects.getList().forEach(o -> o.update(this, frameTime));
    }

    private void render() {
        try {
            SwingUtilities.invokeAndWait(() -> frame.paintImmediately(0, 0, frame.getWidth(), frame.getHeight()));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private void write(){
        writer.addToBuffer(preyHeader,Prey.getAverageVelocity(getObjects()));
        writer.addToBuffer(predatorHeader,Predator.getAverageVelocity(getObjects()));
       }


    public void render(Graphics2D g2d) {
        objects.getList().forEach(o -> o.render(g2d));
        g2d.setColor(Color.GREEN);
        g2d.drawString("FPS: " + fps + "", 5, 15);
        g2d.drawString("Liczba obiektów: " + objects.getList().size(), 5, 30);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    public ArrayList<Drawable> getObjects() {
        return objects.getList();
    }

    public Rectangle getDimensions() {
        return frame.getRectangle();
    }
}
