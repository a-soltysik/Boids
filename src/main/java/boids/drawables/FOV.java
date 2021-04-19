package boids.drawables;

import boids.drawables.geometry.DSegment;
import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;

public class FOV implements Drawable{
    private final Color color = Color.green;
    private final float angle;
    private final float radius;
    private final Vector2[] rays;
    private Vector2 position = Vector2.ZERO;

    public FOV(float angleDeg, float radius, int raysNumber) {
        this.angle = (float) Math.toRadians(angleDeg);
        this.radius = radius;
        rays = new Vector2[raysNumber];

        float alpha = angle / 2;
        for (int i=0; i<rays.length; i++) {
            rays[i] = new Vector2((float) (-Math.sin(alpha) * radius), (float) (-Math.cos(alpha) * radius));
            alpha -= angle / (raysNumber - 1);
        }
    }

    public boolean isIntersecting(Vector2 point, float radius) {
        if (Vector2.distance(position, point) > radius) {
            return false;
        }
        float angle = rays[0].directAngle(point.subtract(position));
        return angle >= 0 && angle <= this.angle;
    }

    public boolean isIntersecting(Vector2 point) {
        return isIntersecting(point, this.radius);
    }
    public boolean isIntersecting(Rectangle boundingBox) {
        for (var ray : rays) {
            if (isIntersecting(position, ray.add(position), boundingBox.min, new Vector2(boundingBox.max.x, boundingBox.min.y))) {
                return true;
            }
            if (isIntersecting(position, ray.add(position), new Vector2(boundingBox.max.x, boundingBox.min.y), boundingBox.max)) {
                return true;
            }
            if (isIntersecting(position, ray.add(position), boundingBox.max, new Vector2(boundingBox.min.x, boundingBox.max.y))) {
                return true;
            }
            if (isIntersecting(position, ray.add(position), new Vector2(boundingBox.min.x, boundingBox.max.y), boundingBox.min)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIntersecting(Vector2 s1, Vector2 e1, Vector2 s2, Vector2 e2) {
        float dx1 = e1.x - s1.x;
        float dy1 = e1.y - s1.y;
        float dx2 = e2.x - s2.x;
        float dy2 = e2.y - s2.y;

        float v = -dx2 * dy1 + dx1 * dy2;
        float s = (-dy1 * (s1.x - s2.x) + dx1 * (s1.y - s2.y)) / v;
        float t = ( dx2 * (s1.y - s2.y) - dy2 * (s1.x - s2.x)) / v;

        return s >= 0 && s <= 1 && t >= 0 && t <= 1;
    }

    public void rotate(float angle) {
        for (int i=0; i<rays.length; i++) {
            rays[i] = rays[i].rotated(Vector2.ZERO, angle);
        }
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    @Override
    public void update(Animation animation, double frameTime) {

    }

    @Override
    public void render(Graphics2D g2d) {
        for (var ray : rays) {
            new DSegment(position, ray.add(position), color).render(g2d);
        }
    }
}
