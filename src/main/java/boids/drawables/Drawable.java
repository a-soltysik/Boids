package boids.drawables;

import boids.gui.Animation;

import java.awt.*;

public interface Drawable {
    void update(Animation animation, double frameTime);

    void render(Graphics2D g2d);
}
