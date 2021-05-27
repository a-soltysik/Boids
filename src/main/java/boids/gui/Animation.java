package boids.gui;

import boids.CSVWriter;
import boids.drawables.Drawable;
import boids.objects.Predator;
import boids.objects.Prey;
import boids.math.Rectangle;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

public class Animation {
    private AnimationPanel frame;
    private double frameTime;
    private int fps;
    private final int preferredFps;
    private final int TIME_SCALE = 1_000_000_000;
    private final String preyHeader = "Prey average velocity";
    private final String predatorHeader = "Predator average velocity";
    private AnimationObjects objects;
    private CSVWriter writer;

    private volatile boolean paused = true;

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
                if (writer != null) {
                    writer.close();
                }
                System.exit(0);
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
        while (!GuiParameters.isEndOfProgram.getValue()) {
            if (!paused) {
                update();
                render();
                write();
                objects.addObjects();
            }
            if (paused){
                objects.addObjects();
                createFile();
                render();
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

        while (!GuiParameters.isEndOfProgram.getValue()) {

            if (!paused) {
                update();
                write();
                if (timeToRender >= preferredFrameTime) {
                    objects.addObjects();
                    render();
                    timeToRender = 0;
                    current_fps++;
                }
            }
            if (paused){
                createFile();
                objects.addObjects();
                render();
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
        //objects.getList().forEach(o -> o.update(this, frameTime));
        objects.updateParameters();
        objects.getList().stream().parallel().filter(Objects::nonNull).forEach(o -> o.update(this, frameTime));
    }

    private void render() {
        try {
            SwingUtilities.invokeAndWait(() -> frame.paintImmediately(0, 0, frame.getWidth(), frame.getHeight()));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private void createFile(){
        if (writer == null || !writer.fileName.equals(GuiParameters.fileName)){
            if (GuiParameters.writeToFile.getValue()){
                if (writer != null) {
                    writer.close();
                }
                try {
                    writer = new CSVWriter(GuiParameters.fileName, GuiParameters.dirName, 100, new String[]{preyHeader, predatorHeader});
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    GuiParameters.writeToFile.setValue(false);
                }
            }
        }
    }
    private void write(){
        if (GuiParameters.writeToFile.getValue()){
            writer.addToBuffer(preyHeader, Prey.getAverageVelocity(getObjects()));
            writer.addToBuffer(predatorHeader, Predator.getAverageVelocity(getObjects()));
        }
    }

    public void render(Graphics2D g2d) {
        objects.getList().stream().filter(Objects::nonNull).forEach(o -> o.render(g2d));
        g2d.setColor(Color.GREEN);
        g2d.drawString("FPS: " + fps + "", 5, 15);
        g2d.drawString("Liczba obiektów: " + objects.getList().stream().filter(Objects::nonNull).count(), 5, 30);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    public ArrayList<Drawable> getObjects() {
        return objects.getList();
    }

    public Rectangle getDimensions() {
        return frame.getDimensions();
    }
}
