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
    float size=5;
    private static final Random rnd = ThreadLocalRandom.current();

    public Boid(float size, Vector2 position) {
        this.size = size;
        this.position = position;
        velocity = Vector2.ZERO;
        vertices = new Vector2[4];
        initializeShape();
    }

    private void initializeShape() {
        vertices[0] = new Vector2(0f, 0f);
        vertices[1] = new Vector2(0.5f * size, 1f * size);
        vertices[2] = new Vector2(0f, 0.75f * size);
        vertices[3] = new Vector2(-0.5f * size, 1f * size);

    }
    public static void addBoid(AnimationPanel panel, ArrayList<Drawable> objects){
          var boid = new Boid(5,
                  new Vector2((float) rnd.nextInt(panel.getWidth()),
                          (float) rnd.nextInt(panel.getHeight())));
          boid.velocity = new Vector2((float) (rnd.nextInt(5) - 2.5), (float) (rnd.nextInt(5) - 2.5));

          for (int i = 0; i < 4; i++) {
              boid.vertices[i].add(boid.position);
          }
        objects.add(boid);
      }

    @Override
    public void update(double frameTime) {
        position.add(velocity);
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.fill(Drawable.drawShape(vertices));
    }
}
