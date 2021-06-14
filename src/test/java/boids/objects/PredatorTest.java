package boids.objects;

import boids.drawables.Drawable;
import boids.gui.AnimationPanel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PredatorTest {

        @Test
        public void addPredatorTest(){
            AnimationPanel panel = new AnimationPanel();
            ArrayList<Drawable> objects = new ArrayList<>();
            Predator.addPredator(panel, objects);
            assertEquals(1,objects.size());
        }
        @Test
        public void removePredatorTest(){
            AnimationPanel panel = new AnimationPanel();
            ArrayList<Drawable> objects = new ArrayList<>();
            Predator.addPredator(panel, objects);
            Predator.removePredator(objects);
            assertNull(objects.get(0));
        }
        @Test
        public void getAverageVelocityTest(){
            AnimationPanel panel = new AnimationPanel();
            ArrayList<Drawable> objects = new ArrayList<>();
            Predator.addPredator(panel, objects);
            Predator.addPredator(panel,objects);
            Predator Predator1 = (Predator) objects.get(0);
            Predator Predator2 = (Predator) objects.get(1);
            float averageVelocity = 0;
            averageVelocity = Predator1.velocity.magnitude() + Predator2.velocity.magnitude();
            averageVelocity /= 2;
            assertEquals(averageVelocity,Predator.getAverageVelocity(objects));
        }
        @Test
        public void getPredatorIndicesTest(){
            AnimationPanel panel = new AnimationPanel();
            ArrayList<Drawable> objects = new ArrayList<>();
            Predator.addPredator(panel, objects);
            assertEquals(objects.size()-1,Predator.getPredatorsIndices().get(0));
        }



    }


