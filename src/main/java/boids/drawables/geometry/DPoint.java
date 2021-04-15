package boids.drawables.geometry;

import boids.drawables.Drawable;
import boids.gui.Animation;
import boids.math.Vector2;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class DPoint implements Drawable {

    private final Vector2 point;
    private final Color color;
    private final float size;

    public DPoint(Vector2 point, float size, Color color) {
        this.point = point;
        this.size = size;
        this.color = color;
    }

    public DPoint(Vector2 point, Color color) {
        this(point, 5f, color);
    }


    @Override
    public void update(Animation animation, double frameTime) {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(new Ellipse2D.Float(point.x, point.y, size, size));
    }
}
