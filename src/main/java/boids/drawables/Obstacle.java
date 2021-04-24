package boids.drawables;

import boids.Utils;
import boids.drawables.geometry.DPolygon;
import boids.drawables.geometry.DRectangle;
import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Obstacle implements Drawable {

    protected static final ArrayList<Integer> obstacles = new ArrayList<>();

    private final DPolygon shape;
    private final Vector2 position;
    private Rectangle boundingBox;

    public Obstacle(Vector2 position, float radius, int resolution) {
        this.position = position;
        shape = new DPolygon(resolution, radius, position, true, Color.gray);
        setBoundingBox();
    }

    public static void addObstacle(AnimationPanel panel, ArrayList<Drawable> objects) {
        boolean intersects;
        int max_tries = 100;
        int current_tries = 0;
        do {
            intersects = false;
            current_tries++;
            var obstacle = new Obstacle(new Vector2(
                    Utils.randomFloat(0f, panel.getWidth()),
                    Utils.randomFloat(0f, panel.getHeight())
            ), 35f, 21);

            for (var i : obstacles) {
                if (obstacle.boundingBox.intersects(((Obstacle)objects.get(i)).boundingBox)) {
                    intersects = true;
                    break;
                }
            }
            intersects = intersects || !obstacle.boundingBox.isInside(panel.getDimensions());
            if (!intersects) {
                objects.add(obstacle);
                obstacles.add(objects.size() - 1);
            }
        } while (intersects && current_tries <= max_tries);
    }

    public static void removeObstacle(ArrayList<Drawable> objects) {
        if (obstacles.size() > 0) {
            objects.remove((int) obstacles.get(obstacles.size() - 1));
            obstacles.remove(obstacles.size() - 1);
        }
    }


    private void setBoundingBox() {
        boundingBox = new Rectangle(
                new Vector2(Float.MAX_VALUE, Float.MAX_VALUE),
                new Vector2(Float.MIN_VALUE, Float.MIN_VALUE)
        );
        Vector2[] vertices = shape.getVertices();
        for (var vertex : vertices) {
            if (vertex.x > boundingBox.max.x) {
                boundingBox.max.x = vertex.x;
            }
            if (vertex.x < boundingBox.min.x) {
                boundingBox.min.x = vertex.x;
            }
            if (vertex.y > boundingBox.max.y) {
                boundingBox.max.y = vertex.y;
            }
            if (vertex.y < boundingBox.min.y) {
                boundingBox.min.y = vertex.y;
            }
        }
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(boundingBox);
    }

    public Vector2 getPosition() {
        return new Vector2(position);
    }

    @Override
    public void update(Animation animation, double frameTime) {
        //No update
    }

    @Override
    public void render(Graphics2D g2d) {
        shape.render(g2d);
        new DRectangle(boundingBox, Color.green).render(g2d);
    }
}
