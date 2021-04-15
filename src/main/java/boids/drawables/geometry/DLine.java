package boids.drawables.geometry;

import boids.drawables.Drawable;
import boids.gui.Animation;
import boids.math.Vector2;

import java.awt.*;
import java.awt.geom.Line2D;

public class DLine implements Drawable {

    private final Vector2 start;
    private final Vector2 end;
    private final Color color;

    public DLine(Vector2 start, Vector2 end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }
    @Override
    public void update(Animation animation, double frameTime) {

    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.draw(new Line2D.Float(
                start.x, start.y,
                end.x, end.y
        ));
    }

}