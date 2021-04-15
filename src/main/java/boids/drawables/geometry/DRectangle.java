package boids.drawables.geometry;

import boids.drawables.Drawable;
import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;

public class DRectangle implements Drawable {

    private final DLine[] lines;

    public DRectangle(Rectangle rectangle, Color color) {
        lines = new DLine[4];
        lines[0] = new DLine(rectangle.min, new Vector2(rectangle.max.x, rectangle.min.y), color);
        lines[1] = new DLine(lines[0].getEnd(), rectangle.max, color);
        lines[2] = new DLine(lines[1].getEnd(),new Vector2(rectangle.min.x, rectangle.max.y), color);
        lines[3] = new DLine(lines[2].getEnd(), lines[0].getStart(), color);
    }

    @Override
    public void update(Animation animation, double frameTime) {

    }

    @Override
    public void render(Graphics2D g2d) {
        for (var line : lines) {
            line.render(g2d);
        }
    }
}