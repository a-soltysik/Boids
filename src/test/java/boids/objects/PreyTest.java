package boids.objects;
import boids.drawables.Drawable;
import boids.gui.AnimationPanel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PreyTest {
    @Test
    public void addPreyTest(){
        AnimationPanel panel = new AnimationPanel();
        ArrayList<Drawable> objects = new ArrayList<>();
        Prey.addPrey(panel, objects);
        assertEquals(1,objects.size());
    }
    @Test
    public void removePreyTest(){
        AnimationPanel panel = new AnimationPanel();
        ArrayList<Drawable> objects = new ArrayList<>();
        Prey.addPrey(panel, objects);
        Prey.removePrey(objects);
        assertNull(objects.get(0));
    }
    @Test
    public void getAverageVelocityTest(){
        AnimationPanel panel = new AnimationPanel();
        ArrayList<Drawable> objects = new ArrayList<>();
        Prey.addPrey(panel, objects);
        Prey.addPrey(panel,objects);
        Prey prey1 = (Prey) objects.get(0);
        Prey prey2 = (Prey) objects.get(1);
        float averageVelocity = 0;
        averageVelocity = prey1.velocity.magnitude() + prey2.velocity.magnitude();
        averageVelocity /= 2;
        assertEquals(averageVelocity,Prey.getAverageVelocity(objects));
    }
    @Test
    public void getPreyIndicesTest(){
        AnimationPanel panel = new AnimationPanel();
        ArrayList<Drawable> objects = new ArrayList<>();
        Prey.addPrey(panel, objects);
        assertEquals(objects.size()-1,Prey.getPreysIndices().get(0));
    }



}
