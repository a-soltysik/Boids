package boids.gui;

import boids.drawables.Obstacle;
import boids.drawables.Predator;
import boids.drawables.Prey;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class OptionsPanel extends JPanel {
    public JButton button1;
    private MySlider preySlider = new MySlider(200, 800, 200,100);
    private MySlider predatorSlider = new MySlider(2, 10, 2,1);
    private MySlider obstacleSlider = new MySlider(0, 8, 0,1);
    private MySlider cohesionSlider = new MySlider(0, 100, (int) GuiParameters.preyCohesionWeight*10,10);
    private MySlider separationSlider = new MySlider(0, 100,  (int) GuiParameters.preySeparationWeight*10,10);
    private MySlider alignmentSlider = new MySlider(0, 100, (int) GuiParameters.preyAlignmentWeight*10,10);
    private MySlider maxSpeedSlider = new MySlider(20, 120, (int) GuiParameters.preyMaxSpeed,10);
    private MySlider backgroundSpeedSlider = new MySlider(0, 5, (int) (GuiParameters.backGroundSpeed*10),1);
    private ArrayList<MySlider> sliders = new ArrayList<MySlider>();
    private ArrayList<JLabel> labels = new ArrayList<JLabel>();
    private AnimationPanel panel;
    public static volatile boolean writeToFile = false;
    private int count = 0;
    private int i = 0;

    public OptionsPanel(AnimationPanel panel){
        this.panel=panel;
        this.setPreferredSize(new Dimension(300,850));
        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
        JButton button2 =new JButton("Submit");
        JTextField textField = new JTextField();
        textField.setText(GuiParameters.fileName);
        textField.setPreferredSize(new Dimension(200,50));
        button2.setPreferredSize(new Dimension(100,50));
        button2.addActionListener(o-> {GuiParameters.fileName = textField.getText();});
        button2.setFocusable(false);

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
        button1.setEnabled(true);
        button1.setFocusable(false);
        button1.addActionListener(o-> {
            if (i % 2 == 0) {
               writeToFile=true;
                button1.setText("stop saving data to file");
                blockSliders();

            } else {
               writeToFile=false;
                button1.setText("save data to file");
                unblockSliders();
            }
            i++;
        });

        JLabel maxSpeedLabel = new JLabel();
        maxSpeedLabel.setText("max boids Speed: "+maxSpeedSlider.getValue());
        maxSpeedSlider.addChangeListener(o->
        {maxSpeedLabel.setText("max boids Speed: "+maxSpeedSlider.getValue());
        GuiParameters.preyMaxSpeed = maxSpeedSlider.getValue();
        GuiParameters.predatorMaxSpeed=(GuiParameters.preyMaxSpeed/5)*3;
       GuiParameters.predatorMaxAcceleration=GuiParameters.predatorMaxSpeed/10;
        GuiParameters.preyMaxAcceleration=GuiParameters.preyMaxSpeed/10;});

        JLabel cohesionLabel = new JLabel();
        cohesionLabel.setText("cohesionWeight: "+cohesionSlider.getValue());
        cohesionSlider.addChangeListener(o->
        {cohesionLabel.setText("cohesionWeight: "+cohesionSlider.getValue());
        GuiParameters.preyCohesionWeight = (float) cohesionSlider.getValue()/10;});

        JLabel separationLabel = new JLabel();
        separationLabel.setText("separationWeight: "+separationSlider.getValue());
        separationSlider.addChangeListener(o->
        {separationLabel.setText("separationWeight: "+separationSlider.getValue());
        GuiParameters.preySeparationWeight=(float) separationSlider.getValue()/10;});

        JLabel alignmentLabel = new JLabel();
        alignmentLabel.setText("alignmentWeight: "+alignmentSlider.getValue());
        alignmentSlider.addChangeListener(o->
        {alignmentLabel.setText("alignmentWeight: "+alignmentSlider.getValue());
        GuiParameters.preyAlignmentWeight= (float) alignmentSlider.getValue()/10;});

        JLabel predatorLabel = new JLabel();
        predatorLabel.setText("predators: "+predatorSlider.getValue());
        predatorSlider.addChangeListener(o->
        {
            JSlider source3 = (JSlider) o.getSource();
            predatorLabel.setText("predators: "+source3.getValue());
             GuiParameters.predatorNumber = source3.getValue();

        });

        JLabel preyLabel = new JLabel();
        preyLabel.setText("preys: "+preySlider.getValue());
        preySlider.addChangeListener(o->{
            JSlider source2 = (JSlider) o.getSource();
            preyLabel.setText("preys: "+source2.getValue());

            GuiParameters.preyNumber =(source2.getValue());

        });

        JLabel obstacleLabel = new JLabel();
        obstacleLabel.setText("obstacles: "+obstacleSlider.getValue());
        obstacleSlider.addChangeListener(o->{
            JSlider source1 = (JSlider) o.getSource();
            obstacleLabel.setText("obstacles: "+source1.getValue());

                GuiParameters.obstacleNumber = (source1.getValue());

        });

        JLabel backgroundSpeedLabel = new JLabel();
        backgroundSpeedLabel.setText("backgroundSpeed: "+backgroundSpeedSlider.getValue());
        backgroundSpeedSlider.addChangeListener(o->{backgroundSpeedLabel.setText("backgroundSpeed: "+backgroundSpeedSlider.getValue());
            GuiParameters.backGroundSpeed =(float) backgroundSpeedSlider.getValue()/10;
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
        this.add(button2);
        this.add(textField);
        int j =0;
        for (MySlider slider : sliders){
            this.add(slider);
            this.add(labels.get(j));
            j++;
        }
        this.setVisible(true);
    }

    private void blockSliders(){
        if (writeToFile){
            for (MySlider slider : sliders){
                slider.setEnabled(false);
            }
        }
    }
    private void unblockSliders(){
        if (!writeToFile){
            for (MySlider slider : sliders){
                slider.setEnabled(true);
            }
        }
    }
}
