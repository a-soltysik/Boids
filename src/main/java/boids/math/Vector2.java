package boids.math;

import static java.lang.Math.*;

public class Vector2 {

    public float x;
    public float y;

    public static final Vector2 ZERO = new Vector2(0f, 0f);

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(float xy) {
        this(xy, xy);
    }

    public Vector2() {
        this(0f, 0f);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }
    public Vector2 subtract(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }
    public Vector2 multiply(float a) {
        return new Vector2(x * a, y * a);
    }
    public Vector2 multiply(double a) {
        return new Vector2((float) (x * a), (float) (y * a));
    }
    public void multiply(Vector2 v) {
        x *= v.x;
        y *= v.y;
    }
    public float magnitude() {
        return (float)sqrt(pow(x, 2) + pow(y, 2));
    }

    public Vector2 normalize() {
        float mag = magnitude();
        if (mag != 0) {
            return new Vector2(x /= mag, y /= mag);
        }
        else throw new IllegalArgumentException("Divisor is 0");
    }
    public double dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public static double angleBetween(Vector2 v1, Vector2 v2) {
        return acos(v1.dot(v2) / (v1.magnitude() * v2.magnitude()));
    }

    public Vector2 divide(float a) {
        if (a == 0) {
            throw new IllegalArgumentException("Divisor is 0");
        }
        return new Vector2(x / a, y / a);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
