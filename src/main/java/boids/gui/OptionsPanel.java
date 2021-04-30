package boids.gui;

import boids.drawables.LowPolyBackground;
import boids.drawables.Obstacle;
import boids.drawables.Predator;
import boids.drawables.Prey;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OptionsPanel extends JPanel {
    public JButton button1;
    private JSlider preySlider = new JSlider(200, 800, 200);
    private JSlider predatorSlider = new JSlider(2, 10, 2);
    private JSlider obstacleSlider = new JSlider(0, 8, 0);
    private JSlider cohesionSlider = new JSlider(0, 100, (int) Prey.cohesionWeight*10);
    private JSlider separationSlider = new JSlider(0, 100,  (int) Prey.separationWeight*10);
    private JSlider alignmentSlider = new JSlider(0, 100, (int) Prey.alignmentWeight*10);
    private JSlider maxSpeedSlider = new JSlider(20, 120, (int) Prey.maxSpeed);
    private JSlider backgroundSpeedSlider = new JSlider(0, 5, (int) LowPolyBackground.speed*10);
    private ArrayList<JSlider> sliders = new ArrayList<JSlider>();
    private ArrayList<JLabel> labels = new ArrayList<JLabel>();
    private AnimationPanel panel;
    private int count = 0;
    private int i = 0;

    public OptionsPanel(AnimationPanel panel){
        this.panel=panel;
        this.setPreferredSize(new Dimension(300,800));
        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));

        sliders.add(preySlider);
        sliders.add(predatorSlider);
        sliders.add(obstacleSlider);
        sliders.add(separationSlider);
        sliders.add(cohesionSlider);
        sliders.add(alignmentSlider);
        sliders.add(maxSpeedSlider);
        sliders.add(backgroundSpeedSlider);

        button1 = new JButton("save data to file");
        button1.setPreferredSize(new Dimension(300,50));
        button1.setEnabled(false);
        button1.addActionListener(o-> {
            if (i % 2 == 0) {
                Animation.writeToFile=true;
                button1.setText("stop saving data to file");
                blockSliders();

            } else {
                Animation.writeToFile=false;
                button1.setText("save data to file");
                unblockSliders();
            }
            i++;
        });

        maxSpeedSlider.setPaintTicks(true);
        maxSpeedSlider.setMajorTickSpacing(10);
        maxSpeedSlider.setPaintLabels(true);
        maxSpeedSlider.setPreferredSize(new Dimension(300,50));

        JLabel maxSpeedLabel = new JLabel();
        maxSpeedLabel.setText("max boids Speed: "+maxSpeedSlider.getValue());
        maxSpeedSlider.addChangeListener(o->{maxSpeedLabel.setText("max boids Speed: "+maxSpeedSlider.getValue());
        Prey.maxSpeed = maxSpeedSlider.getValue();
        Predator.maxSpeed=(Prey.maxSpeed/5)*3;
        Predator.maxAcceleration=Predator.maxSpeed/10;
        Prey.maxAcceleration=Prey.maxSpeed/10;});

        cohesionSlider.setPaintTicks(true);
        cohesionSlider.setMajorTickSpacing(10);
        cohesionSlider.setPaintLabels(true);
        cohesionSlider.setPreferredSize(new Dimension(300,50));

        JLabel cohesionLabel = new JLabel();
        cohesionLabel.setText("cohesionWeight: "+cohesionSlider.getValue());
        cohesionSlider.addChangeListener(o->{cohesionLabel.setText("cohesionWeight: "+cohesionSlider.getValue());
        Prey.cohesionWeight = (float) cohesionSlider.getValue()/10;});

        separationSlider.setPaintTicks(true);
        separationSlider.setMajorTickSpacing(10);
        separationSlider.setPaintLabels(true);
        separationSlider.setPreferredSize(new Dimension(300,50));

        JLabel separationLabel = new JLabel();
        separationLabel.setText("separationWeight: "+separationSlider.getValue());
        separationSlider.addChangeListener(o->{separationLabel.setText("separationWeight: "+separationSlider.getValue());
        Prey.separationWeight=(float) separationSlider.getValue()/10;});

        alignmentSlider.setPaintTicks(true);
        alignmentSlider.setMajorTickSpacing(10);
        alignmentSlider.setPaintLabels(true);
        alignmentSlider.setPreferredSize(new Dimension(300,50));

        JLabel alignmentLabel = new JLabel();
        alignmentLabel.setText("alignmentWeight: "+alignmentSlider.getValue());
        alignmentSlider.addChangeListener(o->{alignmentLabel.setText("alignmentWeight: "+alignmentSlider.getValue());
        Prey.alignmentWeight= (float) alignmentSlider.getValue()/10;});
        
        
        predatorSlider.setPaintTicks(true);
        predatorSlider.setMajorTickSpacing(1);
        predatorSlider.setPaintLabels(true);
        predatorSlider.setPreferredSize(new Dimension(300,50));

        JLabel predatorLabel = new JLabel();
        predatorLabel.setText("predators: "+predatorSlider.getValue());
        predatorSlider.addChangeListener(o->{predatorLabel.setText("predators: "+predatorSlider.getValue());
            try {
                changePredatorNumber(predatorSlider.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
        }
        });

        preySlider.setPaintTicks(true);
        preySlider.setMajorTickSpacing(100);
        preySlider.setPaintLabels(true);
        preySlider.setPreferredSize(new Dimension(300,50));

        JLabel preyLabel = new JLabel();
        preyLabel.setText("preys: "+preySlider.getValue());
        preySlider.addChangeListener(o->{preyLabel.setText("preys: "+preySlider.getValue());
            try {
                changePreyNumber(preySlider.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        obstacleSlider.setPaintTicks(true);
        obstacleSlider.setMajorTickSpacing(1);
        obstacleSlider.setPaintLabels(true);
        obstacleSlider.setPreferredSize(new Dimension(300,50));

        JLabel obstacleLabel = new JLabel();
        obstacleLabel.setText("obstacles: "+obstacleSlider.getValue());
        obstacleSlider.addChangeListener(o->{obstacleLabel.setText("obstacles: "+obstacleSlider.getValue());
            try {
                changeObstacleNumber(obstacleSlider.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        backgroundSpeedSlider.setPaintTicks(true);
        backgroundSpeedSlider.setMajorTickSpacing(1);
        backgroundSpeedSlider.setPaintLabels(true);
        backgroundSpeedSlider.setPreferredSize(new Dimension(300,50));

        JLabel backgroundSpeedLabel = new JLabel();
        backgroundSpeedLabel.setText("backgroundSpeed: "+backgroundSpeedSlider.getValue());
        backgroundSpeedSlider.addChangeListener(o->{backgroundSpeedLabel.setText("backgroundSpeed: "+backgroundSpeedSlider.getValue());
            LowPolyBackground.speed =(float) backgroundSpeedSlider.getValue()/10;
        });

        labels.add(preyLabel);
        labels.add(predatorLabel);
        labels.add(obstacleLabel);
        labels.add(separationLabel);
        labels.add(cohesionLabel);
        labels.add(alignmentLabel);
        labels.add(maxSpeedLabel);
        labels.add(backgroundSpeedLabel);
        this.add(button1);

        int j =0;

        for (JSlider slider : sliders){
            this.add(slider);
            this.add(labels.get(j));
            j++;
        }

        this.setVisible(true);

    }
    private void changePreyNumber(int sliderValue) throws InterruptedException {
        count = 0;
        int currentNumber = 0;
        while (!Animation.changeNumber) {
            Thread.sleep(1);
            count++;
            if (count > 25) return;
        }
        currentNumber = sliderValue - Prey.preyNumber;
        if (currentNumber == 0) return;
        if (currentNumber > 0) {
            for (int i = 0; i < currentNumber; i++) {
                Prey.addPrey(panel, AnimationObjects.objects);
            }
            Prey.preyNumber += currentNumber;
            Animation.changeNumber = false;
        }
        if (currentNumber < 0) {
            currentNumber *= -1;
            for (int i = 0; i < currentNumber; i++) {
                Prey.removePrey(AnimationObjects.objects);
            }
            Animation.changeNumber = false;
            Prey.preyNumber -= currentNumber;
        }
    }
    
        private void changePredatorNumber( int sliderValue) throws InterruptedException {
            count=0;
            int currentNumber = 0;
            while (!Animation.changeNumber){
                Thread.sleep(1);
                count++;
                if (count>25) return;
            }
            currentNumber = sliderValue - Predator.predatorNumber;
            if (currentNumber == 0) return;
            if (currentNumber > 0) {
                for (int i = 0; i < currentNumber; i++) {
                    Predator.addPredator(panel, AnimationObjects.objects);
                }
                Predator.predatorNumber += currentNumber;
                Animation.changeNumber = false;
            }
            if (currentNumber<0){
                currentNumber *= -1;
                for (int i=0;i<currentNumber;i++){
                    Predator.removePredator(AnimationObjects.objects);
                }
                Animation.changeNumber = false;
                Predator.predatorNumber -= currentNumber;
            }

    }
    private void changeObstacleNumber( int sliderValue) throws InterruptedException {
        count = 0;
        int currentNumber = 0;
        while (!Animation.changeNumber) {
            Thread.sleep(1);
            count++;
            if (count > 25) return;
        }
        currentNumber = sliderValue - Obstacle.obstacleNumber;
        if (currentNumber == 0) return;
        if (currentNumber > 0) {
            for (int i = 0; i < currentNumber; i++) {
                Obstacle.addObstacle(panel, AnimationObjects.objects);
            }
            Obstacle.obstacleNumber += currentNumber;
            Animation.changeNumber = false;
        }
        if (currentNumber < 0) {
            currentNumber *= -1;
            for (int i = 0; i < currentNumber; i++) {
                Obstacle.removeObstacle(AnimationObjects.objects);
            }
            Animation.changeNumber = false;
            Obstacle.obstacleNumber -= currentNumber;
        }
    }
    private void blockSliders(){
        if (Animation.writeToFile){
            for (JSlider slider : sliders){
                slider.setEnabled(false);
            }
        }
    }
    private void unblockSliders(){
        if (!Animation.writeToFile){
            for (JSlider slider : sliders){
                slider.setEnabled(true);
            }
        }
    }
}
