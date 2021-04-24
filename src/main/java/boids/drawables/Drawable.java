package boids.drawables;

import boids.gui.Animation;
import boids.math.Vector2;

import java.awt.*;
import java.awt.geom.Path2D;

public interface Drawable {
    void update(Animation animation, double frameTime);

    void render(Graphics2D g2d);
}
