package boids.math;

public class Rectangle {

    public Vector2 min;
    public Vector2 max;

    public Rectangle(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }

    public boolean intersects(Rectangle rectangle) {
        float leftX = Math.max(min.x, rectangle.min.x);
        float rightX = Math.min(max.x, rectangle.max.x);
        float topY = Math.min(min.y, rectangle.min.y);
        float bottomY = Math.max(max.y, rectangle.max.y);

        return leftX < rightX && topY < bottomY;
    }

    public boolean isInside(Rectangle rectangle) {
        return min.x >= rectangle.min.x
                && min.y >= rectangle.min.y
                && max.x <= rectangle.max.x
                && max.y <= rectangle.max.y;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
