package boids.drawables;

import boids.gui.AnimationPanel;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Boid implements Drawable{
    private Vector2 velocity;
    private Vector2 position;
    private final Vector2[] vertices;
    float size;
    private static final Random rnd = ThreadLocalRandom.current();

    public Boid(float size, Vector2 position) {
        this.size = size;
        this.position = position;
        velocity = Vector2.ZERO;
        vertices = new Vector2[4];
        initializeShape();
    }
    public Boid(Vector2 position) {
        this(15f, position);
    }

    private void initializeShape() {
        vertices[0] = new Vector2(0f, 0f);
        vertices[1] = new Vector2(0.5f * size, 1f * size);
        vertices[2] = new Vector2(0f, 0.75f * size);
        vertices[3] = new Vector2(-0.5f * size, 1f * size);

    }

    public static void addBoid(AnimationPanel panel, ArrayList<Drawable> objects){
          var boid = new Boid(
                  new Vector2((float) rnd.nextInt(panel.getWidth()),
                          (float) rnd.nextInt(panel.getHeight())));

          boid.velocity = new Vector2((float) (rnd.nextInt(30)-10 ), (float) (rnd.nextInt(30) - 10));

          for (int i = 0; i < 4; i++) {
              boid.vertices[i].add(boid.position);
          }
        objects.add(boid);
      }

    @Override
    public void update(double frameTime) {
        position = position.add(velocity.multiply(frameTime));
        for (int i = 0; i < 4; i++) {
           vertices[i] = vertices[i].add(velocity.multiply(frameTime));
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.fill(Drawable.drawShape(vertices));
    }
}
