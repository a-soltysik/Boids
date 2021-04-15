package boids.drawables;

import boids.gui.Animation;
import boids.math.Vector2;

import java.awt.*;
import java.awt.geom.Path2D;

public interface Drawable {
    void update(Animation animation, double frameTime);

    void render(Graphics2D g2d);

    static Path2D drawShape(Vector2[] vertices) {
        Path2D shape = new Path2D.Float();
        shape.moveTo(vertices[0].x, vertices[0].y);
        for (int i=1; i<vertices.length; i++) {
            shape.lineTo(vertices[i].x, vertices[i].y);
        }
        shape.closePath();
        return shape;
    }
}
