package boids.objects;

import boids.drawables.Drawable;
import boids.gui.AnimationPanel;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ObstacleTest {

    @Test
    public void addObstacleTest(){
        AnimationPanel panel = new AnimationPanel();
        panel.setPreferredSize(new Dimension(1000,1000));
        ArrayList<Drawable> objects = new ArrayList<>();
        Obstacle.addObstacle(panel, objects);
        assertEquals(1,objects.size());
    }

    @Test
    public void removeObstacleTest(){
        AnimationPanel panel = new AnimationPanel();
        panel.setPreferredSize(new Dimension(1000,100));
        ArrayList<Drawable> objects = new ArrayList<>();
        Obstacle.addObstacle(panel, objects);
        Obstacle.removeObstacle(objects);
        assertNull(objects.get(0));

    }
}
