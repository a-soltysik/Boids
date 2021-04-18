package boids.math;

import java.util.Objects;

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

    public Vector2(Vector2 vec) {
        x = vec.x;
        y = vec.y;
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

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public float magnitude() {
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public void limit(float newMagnitude) {
        float mag = magnitude();
        if (mag != 0) {
            if (mag > newMagnitude) {
                normalize();
                x *= newMagnitude;
                y *= newMagnitude;
            }
        }
    }

    public Vector2 limited(float magnitude) {
        float mag = magnitude();
        if (mag != 0) {
            if (mag > magnitude) {
                return this.normalized().multiply(magnitude);
            } else {
                return new Vector2(this);
            }
        }
        return Vector2.ZERO;
    }

    public void normalize(){
        float mag = magnitude();
        if (mag != 0) {
            x /= mag;
            y /= mag;
        }
        else throw new ArithmeticException("Magnitude is 0");
    }
    public Vector2 normalized() {
        float mag = magnitude();
        if (mag != 0) {
            return new Vector2(x / mag, y / mag);
        }
        else throw new ArithmeticException("Magnitude is 0");
    }
    public float dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public static float angleBetween(Vector2 v1, Vector2 v2) {
        if (v1.isZero() || v2.isZero()) {
            throw new ArithmeticException("Angle between zero vectors in undefined");
        }
        float value = v1.dot(v2) / (v1.magnitude() * v2.magnitude());
        value = Math.max(value, -1f);
        value = Math.min(value, 1f);
        return (float) Math.acos(value);
    }

    public float directAngle(Vector2 vec) {
        if (this.isZero() || vec.isZero()) {
            throw new ArithmeticException("Angle between zero vectors in undefined");
        }
        float angle = (float) Math.atan2(x * vec.y - vec.x * y, x * vec.x + y * vec.y);
        if (angle >= 0) {
            return angle;
        } else {
            return (float) (2 * Math.PI + angle);
        }
    }

    public Vector2 rotated(Vector2 point, float angle) {
        float dx = x - point.x;
        float dy = y - point.y;
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Vector2(
                point.x + (dx * cos - dy * sin),
                point.y + (dx * sin + dy * cos)
        );
    }

    public static float distance(Vector2 v1, Vector2 v2) {
        return (float) Math.sqrt((v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y));
    }

    public Vector2 divide(float a) {
        if (a == 0) {
            throw new ArithmeticException("Divisor is 0");
        }
        return new Vector2(x / a, y / a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Float.compare(vector2.x, x) == 0 && Float.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
