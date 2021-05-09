package boids.math;

import boids.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2Test {

    @Test
    public void addTest() {
        Vector2 a = new Vector2(1f, 2f);
        Vector2 b = new Vector2(2f, 1f);
        assertEquals(new Vector2(3f, 3f), a.add(b));
    }

    @Test
    public void subtractTest() {
        Vector2 a = new Vector2(5f, 3f);
        Vector2 b = new Vector2(10f, 2f);
        assertEquals(new Vector2(-5f, 1f), a.subtract(b));
    }

    @Test
    public void multiplyTest() {
        Vector2 a = new Vector2(2f, 3f);
        assertEquals(a, a.multiply(1f));
        assertEquals(new Vector2(5f, 7.5f), a.multiply(2.5f));
        assertEquals(Vector2.ZERO, a.multiply(0f));
    }

    @Test
    public void isZero() {
        Vector2 a = new Vector2(0f, 0f);
        assertTrue(a.isZero());
        a = new Vector2(3f, 1.5f);
        assertFalse(a.isZero());
    }

    @Test
    public void magnitudeTest() {
        Vector2 a = new Vector2(3f, 4f);
        assertEquals(0, Float.compare(a.magnitude(), 5f));
        a = new Vector2(-3f, -4f);
        assertEquals(0, Float.compare(a.magnitude(), 5f));
    }

    @Test
    public void limitTest() {
        Vector2 a = new Vector2(100f, 0f);
        a.limit(2f);
        assertEquals(new Vector2(2f, 0f), a);
        a = new Vector2(1f, 1f);
        a.limit(3f);
        assertEquals(new Vector2(1f, 1f), a);
    }

    @Test
    public void limitedTest() {
        Vector2 a = new Vector2(100f, 0f);
        assertEquals(new Vector2(2f, 0f), a.limited(2f));
        a = new Vector2(1f, 1f);
        assertEquals(new Vector2(1f, 1f), a.limited(3f));
    }

    @Test
    public void normalize() {
        Vector2 a = new Vector2(100f, 0f);
        a.normalize();
        assertEquals(new Vector2(1f, 0f), a);
        a = new Vector2(0f, 0.5f);
        a.normalize();
        assertEquals(new Vector2(0f, 1f), a);
    }

    @Test
    public void normalized() {
        Vector2 a = new Vector2(100f, 0f);
        assertEquals(new Vector2(1f, 0f), a.normalized());
        a = new Vector2(0f, 0.5f);
        assertEquals(new Vector2(0f, 1f), a.normalized());
    }

    @Test
    public void dot() {
        Vector2 a = new Vector2(3f, 2f);
        Vector2 b = new Vector2(-4f, 2f);
        assertEquals(0, Float.compare(a.dot(b), -8f));
    }

    @Test
    public void cross() {
        Vector2 a = new Vector2(3f, 2f);
        Vector2 b = new Vector2(-4f, 2f);
        assertEquals(0, Float.compare(a.cross(b), 14f));
    }

    @Test
    public void directAngle() {
        Vector2 a = Vector2.ZERO;
        Vector2 b = new Vector2(1f, 1f);
        ArithmeticException e = assertThrows(ArithmeticException.class, () -> a.directAngle(b));
        assertEquals("Angle between zero vectors is undefined", e.getMessage());
        Vector2 c = new Vector2(0f, 1f);
        Vector2 d = new Vector2(1f, 0f);
        assertEquals(0, Float.compare(c.directAngle(d), -Utils.PI_2 + 2 * Utils.PI));
        assertEquals(0, Float.compare(d.directAngle(c), Utils.PI_2));

    }

    @Test
    public void rotated() {
        Vector2 a = new Vector2(1f, 0f);
        assertEquals(new Vector2(0f, 1f), a.rotated(new Vector2(0f, 0f), (float) (Math.PI / 2)));
    }

    @Test
    public void distance() {
        Vector2 a = new Vector2(1f, 1f);
        Vector2 b = new Vector2(5f, 4f);
        assertEquals(0, Float.compare(Vector2.distance(a, b), Vector2.distance(b, a)));
        assertEquals(0, Float.compare(Vector2.distance(a, b), 5f));
    }

    @Test
    public void divide() {
        Vector2 a = new Vector2(2f, 3f);
        assertEquals(a.divide(1f), a);
        assertEquals(a.divide(2), new Vector2(1f, 1.5f));
        ArithmeticException e = assertThrows(ArithmeticException.class, () -> a.divide(0f));
        assertEquals("Divisor is 0", e.getMessage());
    }

    @Test
    public void isInsideTest() {
        Vector2 a = new Vector2(3f, 4f);
        Rectangle rec = new Rectangle(new Vector2(0f, 0f), new Vector2(10f, 10f));
        assertTrue(a.isInside(rec));
        a = new Vector2(11f, 2f);
        assertFalse(a.isInside(rec));
    }

    @Test
    public void equalsTest() {
        Vector2 a = new Vector2(0f, 0f);
        assertEquals(a, Vector2.ZERO);
        assertEquals(Vector2.ZERO, a);
        a = new Vector2(1f, 2f);
        Vector2 b = new Vector2(1f, 2f);
        assertEquals(a, b);
        assertEquals(b, a);
        b = new Vector2(1.1f, 2.1f);
        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    public void toStringTest() {
        Vector2 a = new Vector2(3f, 4f);
        assertEquals("Vector2{x=3.0, y=4.0}", a.toString());
    }
}
