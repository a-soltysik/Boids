package boids.gui;

import boids.drawables.Drawable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Animation {
    private AnimationPanel frame;
    private double frameTime;
    private int fps;
    private final int preferredFps;
    private final int TIME_SCALE = 1_000_000_000;
    private final boolean running = true;
    private AnimationObjects objects;

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

        while (running) {
            if (!paused) {
                update();
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

    public void render(Graphics2D g2d) {
        objects.getList().forEach(o -> o.render(g2d));
        g2d.setColor(Color.GREEN);
        g2d.drawString("FPS: " + fps + "", 5, 15);
        g2d.drawString("Liczba obiektów: " + objects.getList().size(), 5, 75);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    public ArrayList<Drawable> getObjects() {
        return objects.getList();
    }
}
