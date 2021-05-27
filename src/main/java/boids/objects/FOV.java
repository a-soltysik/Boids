package boids.objects;

import boids.Utils;
import boids.drawables.Drawable;
import boids.drawables.geometry.DSegment;
import boids.gui.Animation;
import boids.math.Rectangle;
import boids.math.Vector2;

import java.awt.*;
import java.util.Arrays;

public class FOV implements Drawable {
    private final Color color = Color.green;
    private float angle;
    private float radius;
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
        return isIntersecting(position, ray.add(position), new Vector2(boundingBox.min.x, boundingBox.max.y), boundingBox.min);
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

    public void rotate(float angle) {
        for (int i=0; i<rays.length; i++) {
            rays[i] = rays[i].rotated(Vector2.ZERO, angle);
        }
    }

    public void updateFOV(float angleDeg, float radius) {
        if(!Utils.isEqual((float) Math.toRadians(angleDeg), this.angle) || !Utils.isEqual(radius, this.radius)) {
            this.angle = (float) Math.toRadians(angleDeg);
            this.radius = radius;
            int raysNumber = Math.max(3, Math.round(50 * angleDeg / 360));
            rays = new Vector2[raysNumber];

            float alpha = angle / 2;
            float angleToRotate = new Vector2(0f,-1f).directAngle(direction);
            for (int i=0; i<rays.length; i++) {
                rays[i] = new Vector2((float) (-Math.sin(alpha) * radius), (float) (-Math.cos(alpha) * radius)).rotated(Vector2.ZERO, angleToRotate);
                alpha -= angle / (raysNumber - 1);
            }
        }
    }

    //https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
    private boolean isIntersecting(Vector2 start1, Vector2 end1, Vector2 start2, Vector2 end2) {
        Vector2 CmP = start2.subtract(start1);
        Vector2 r = end1.subtract(start1);
        Vector2 s = end2.subtract(start2);

        float CmPxr = CmP.cross(r);
        float CmPxs = CmP.cross(s);
        float rxs = r.cross(s);


        if (CmPxr == 0f) {
            return ((start2.x - start1.x < 0f) != (start2.x - end1.x < 0f))
                    || ((start2.y - start1.y < 0f) != (start2.y - end1.y < 0f));
        }

        if (rxs == 0f) {
            return false;
        }

        float rxsr = 1 / rxs;
        float t = CmPxs * rxsr;
        float u = CmPxr * rxsr;

        return (t >= 0f) && (t <= 1f) && (u >= 0f) && (u <= 1f);
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
