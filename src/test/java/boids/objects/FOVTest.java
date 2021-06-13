package boids.objects;

import boids.math.Rectangle;
import boids.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FOVTest {
    @Test
    public void isIntersectingPointTest(){
        FOV fov = new FOV(180f,50f,18);
        fov.setPosition(new Vector2(0f,0f));

        assertTrue(fov.isIntersecting(new Vector2(50f,0f)));
        assertTrue(fov.isIntersecting(new Vector2(-50f,0f)));
        assertTrue(fov.isIntersecting(new Vector2(0f,-50f)));

        assertFalse(fov.isIntersecting(new Vector2(0f,50f)));
        assertFalse(fov.isIntersecting(new Vector2(51f,0f)));
        assertFalse(fov.isIntersecting(new Vector2(-51f,0f)));

        assertTrue(fov.isIntersecting(new Vector2(-10f,-10f)));
        assertTrue(fov.isIntersecting(new Vector2(10f,-10f)));
        assertFalse(fov.isIntersecting(new Vector2(10f,10f)));
        assertFalse(fov.isIntersecting(new Vector2(-10f,10f)));
    }

    @Test
    public void isIntersectingBoundingBoxTest(){
        FOV fov = new FOV(180f,50f,18);
        fov.setPosition(new Vector2(0f,0f));
        boolean isIntersecting = false;
        for( var ray : fov.getRays()){
            if (fov.isIntersecting(new Rectangle(
                    new Vector2(-10, -10),
                    new Vector2(-20, -20)
            ), ray)){
                isIntersecting = true;
                break;
            }
        }

        assertTrue(isIntersecting);

        isIntersecting = false;
        for( var ray : fov.getRays()){
            if (fov.isIntersecting(new Rectangle(
                    new Vector2(20, 20),
                    new Vector2(10, 10)
            ), ray)){
                isIntersecting = true;
                break;
            }
        }
        assertFalse(isIntersecting);
    }

}
