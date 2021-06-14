package boids.objects;

import boids.Utils;
import boids.drawables.Drawable;
import boids.drawables.geometry.DPolygon;
import boids.gui.AnimationPanel;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Obstacle extends DPolygon {

    protected static final ArrayList<Integer> obstaclesIndices = new ArrayList<>();

    private Rectangle boundingBox;

    public Obstacle(Vector2 position, float radius, int resolution) {
        super(resolution, radius, position, true, Color.gray);
        setBoundingBox();
    }

    public static void addObstacle(AnimationPanel panel, List<Drawable> objects) {
        boolean intersects;
        int max_tries = 100;
        int current_tries = 0;
        do {
            intersects = false;
            current_tries++;
            var obstacle = new Obstacle(new Vector2(
                    Utils.randomFloat(0f, panel.getDimensions().max.x),
                    Utils.randomFloat(0f, panel.getDimensions().max.y)
            ), 35f, 21);

            for (var i : obstaclesIndices) {
                if (obstacle.boundingBox.intersects(((Obstacle)objects.get(i)).boundingBox)) {
                    intersects = true;
                    break;
                }
            }
            intersects = intersects || !obstacle.boundingBox.isInside(panel.getDimensions());
            if (!intersects) {
                int i;
                for (i=0; i<objects.size(); i++) {
                    if (objects.get(i) == null) {
                        objects.set(i, obstacle);
                        break;
                    }
                }
                if (i == objects.size()) {
                    objects.add(obstacle);
                }
                obstaclesIndices.add(i);
            }
        } while (intersects && current_tries <= max_tries);
    }

    public static void removeObstacle(List<Drawable> objects) {
        if (obstaclesIndices.size() > 0) {
            objects.set(obstaclesIndices.get(obstaclesIndices.size() - 1), null);
            obstaclesIndices.remove(obstaclesIndices.size() - 1);
        }
    }


    private void setBoundingBox() {
        boundingBox = new Rectangle(
                new Vector2(Float.MAX_VALUE, Float.MAX_VALUE),
                new Vector2(Float.MIN_VALUE, Float.MIN_VALUE)
        );
        Vector2[] vertices = getVertices();
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

    public static int getObstaclesNumber() {
        return obstaclesIndices.size();
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(boundingBox);
    }

    public Vector2 getPosition() {
        return new Vector2(position);
    }
}
