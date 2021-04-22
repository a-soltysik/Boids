package boids.drawables;

import boids.drawables.geometry.DSegment;
import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FOV implements Drawable{
    private Color color = Color.green;
    private final float angle;
    private final float radius;
    private Vector2[] rays;
    private Vector2 position = Vector2.ZERO;
    private Vector2 direction = Vector2.ZERO;

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
        if (rays[0].isZero() || point.subtract(position).isZero()) {
            return false;
        }
        float angle = rays[0].directAngle(point.subtract(position));
        return angle >= 0 && angle <= this.angle;
    }

    public boolean isIntersecting(Vector2 point) {
        return isIntersecting(point, this.radius);
    }

    public boolean isIntersecting(Rectangle boundingBox, Vector2 ray) {
        ray = ray.normalized().multiply(radius);
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
        return false;
    }

    public Vector2 findPathAway(Rectangle boundingBox) {
        Vector2[] newRays = Arrays.copyOf(rays, rays.length);
        rearrangeRays();
        for (Vector2 ray : rays) {
            if (!isIntersecting(boundingBox, ray)) {
                rays = newRays;
                return ray;
            }
        }
        rays = newRays;

        return direction.multiply(-1f);
    }

    private boolean onSegment(Vector2 p, Vector2 q, Vector2 r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    private int orientation(Vector2 p, Vector2 q, Vector2 r) {
        float val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) {
            return 0;
        }
        return (val > 0) ? 1 : 2;
    }

    public void rotate(float angle) {
        for (int i=0; i<rays.length; i++) {
            rays[i] = rays[i].rotated(Vector2.ZERO, angle);
        }
    }

    //https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
    private boolean isIntersecting(Vector2 start1, Vector2 end1, Vector2 start2, Vector2 end2)
    {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(start1, end1, start2);
        int o2 = orientation(start1, end1, end2);
        int o3 = orientation(start2, end2, start1);
        int o4 = orientation(start2, end2, end1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // start1, end1 and start2are colinear and start2lies on segment p1end1
        if (o1 == 0 && onSegment(start1, start2, end1)) return true;

        // start1, end1 and end2 are colinear and end2 lies on segment p1end1
        if (o2 == 0 && onSegment(start1, end2, end1)) return true;

        // start2, end2 and start1 are colinear and start1 lies on segment start2end2
        if (o3 == 0 && onSegment(start2, start1, end2)) return true;

        // start2, end2 and end1 are colinear and end1 lies on segment start2end2
        if (o4 == 0 && onSegment(start2, end1, end2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    private void rearrangeRays() {
        Vector2[] newRays = new Vector2[rays.length];
        int i = (rays.length + 1) / 2;
        int j = i - 1;
        int it = 0;
        while (it < newRays.length) {
            if (j >= 0) {
                newRays[it++] = rays[j--];
            }
            if (i < newRays.length) {
                newRays[it++] = rays[i++];
            }
        }
        rays = newRays;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
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
