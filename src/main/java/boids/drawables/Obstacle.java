package boids.drawables;

import boids.drawables.geometry.DRectangle;
import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.math.OpenSimplex2F;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Obstacle implements Drawable {

    protected static final ArrayList<Integer> obstacles = new ArrayList<>();

    private final Vector2 position;
    private final Vector2[] vertices;
    private final OpenSimplex2F noise;
    private final float radius;
    private final int resolution;
    private final float NOISE_RESOLUTION = 0.02f;
    private Rectangle boundingBox;
    public static int obstacleNumber = 0;

    public Obstacle(Vector2 position, float radius, int resolution) {
        this.position = position;
        this.radius = radius;
        this.resolution = resolution;
        vertices = new Vector2[resolution];
        noise = new OpenSimplex2F((int) (Math.random() * 100000000));
        initializeVertices();
        setBoundingBox();
    }

    public static void addObstacle(AnimationPanel panel, ArrayList<Drawable> objects) {
        boolean intersects;
        int max_tries = 10;
        int current_tries = 0;
        var random = new Random();
        do {
            intersects = false;
            current_tries++;
            var obstacle = new Obstacle(new Vector2(
                    random.nextInt(panel.getWidth()),
                    random.nextInt(panel.getHeight())
            ), 35f, 21);

            for (var i : obstacles) {
                if (obstacle.boundingBox.intersects(((Obstacle)objects.get(i)).boundingBox)) {
                    intersects = true;
                    break;
                }
            }
            intersects = intersects || !obstacle.boundingBox.isInside(panel.getRectangle());
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

    private void initializeVertices() {
        generateCircle();
        addNoise();
    }

    private void generateCircle() {
        float angle = 0f;
        for (int i = 0; i < vertices.length; i++, angle += 2 * Math.PI / resolution) {
            vertices[i] = new Vector2(
                    position.x + (float) (Math.sin(angle) * radius),
                    position.y + (float) (Math.cos(angle) * radius)
            );
        }
    }

    private void addNoise() {
        for (int i = 0; i < vertices.length; i++) {
            Vector2 point = vertices[i].subtract(position);
            double value = noise.noise2(point.x * NOISE_RESOLUTION, point.y * NOISE_RESOLUTION);
            value = (value + 1) / 2;
            value = value * (1.2f - 0.8f) + 0.8f;
            point = point.multiply((float) value);
            vertices[i] = point.add(position);
        }
    }

    private void setBoundingBox() {
        boundingBox = new Rectangle(
                new Vector2(Float.MAX_VALUE, Float.MAX_VALUE),
                new Vector2(Float.MIN_VALUE, Float.MIN_VALUE)
        );

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
        return boundingBox;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void update(Animation animation, double frameTime) {
        //No update
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.gray);
        g2d.fill(Drawable.drawShape(vertices));
    }
}
