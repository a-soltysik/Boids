package boids.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OptionsPanel extends JPanel {
    public JButton button1;
    private final JButton button2;
    private final JTextField textField;
    private final ParameterSlider cohesionSlider = new ParameterSlider(0, 100, (int) GuiParameters.preyCohesionWeight * 10, 10);
    private final ParameterSlider separationSlider = new ParameterSlider(0, 100, (int) GuiParameters.preySeparationWeight * 10, 10);
    private final ParameterSlider alignmentSlider = new ParameterSlider(0, 100, (int) GuiParameters.preyAlignmentWeight * 10, 10);
    private final ParameterSlider maxSpeedSlider = new ParameterSlider(20, 120, (int) GuiParameters.preyMaxSpeed, 10);
    private final ParameterSlider backgroundSpeedSlider = new ParameterSlider(0, 100, (int) (GuiParameters.backGroundSpeed * 10), 1);
    private final ArrayList<ParameterSlider> sliders = new ArrayList<>();
    private int i = 0;

    public OptionsPanel() {
        this.setPreferredSize(new Dimension(300, 850));
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        button2 = new JButton("Submit");
        textField = new JTextField();
        textField.setText("nazwa pliku");
        textField.setPreferredSize(new Dimension(200, 50));
        button2.setPreferredSize(new Dimension(100, 50));
        button2.addActionListener(o -> GuiParameters.fileName = textField.getText() + ".csv");
        button2.setFocusable(false);

        ParameterSlider preySlider = new ParameterSlider(200, 800, 200, 100);
        sliders.add(preySlider);
        ParameterSlider predatorSlider = new ParameterSlider(2, 10, 2, 1);
        sliders.add(predatorSlider);
        ParameterSlider obstacleSlider = new ParameterSlider(0, 8, 0, 1);
        sliders.add(obstacleSlider);
        sliders.add(separationSlider);
        sliders.add(cohesionSlider);
        sliders.add(alignmentSlider);
        sliders.add(maxSpeedSlider);
        sliders.add(backgroundSpeedSlider);

        button1 = new JButton("save data to file");
        button1.setPreferredSize(new Dimension(300, 50));
        button1.setEnabled(true);
        button1.setFocusable(false);
        button1.addActionListener(o -> {
            if (i % 2 == 0) {
                GuiParameters.writeToFile = true;
                button1.setText("stop saving data to file");
                blockOptions();

            } else {
                GuiParameters.writeToFile = false;
                button1.setText("save data to file");
                unblockOptions();
            }
            i++;
        });

        JLabel maxSpeedLabel = new JLabel();
        maxSpeedLabel.setText("max boids Speed: " + maxSpeedSlider.getValue());
        maxSpeedSlider.addChangeListener(o ->
        {
            maxSpeedLabel.setText("max boids Speed: " + maxSpeedSlider.getValue());
            GuiParameters.preyMaxSpeed = maxSpeedSlider.getValue();
            GuiParameters.predatorMaxSpeed = (GuiParameters.preyMaxSpeed / 5) * 3;
            GuiParameters.predatorMaxAcceleration = GuiParameters.predatorMaxSpeed / 10;
            GuiParameters.preyMaxAcceleration = GuiParameters.preyMaxSpeed / 10;
        });

        JLabel cohesionLabel = new JLabel();
        cohesionLabel.setText("cohesionWeight: " + cohesionSlider.getValue());
        cohesionSlider.addChangeListener(o ->
        {
            cohesionLabel.setText("cohesionWeight: " + cohesionSlider.getValue());
            GuiParameters.preyCohesionWeight = (float) cohesionSlider.getValue() / 10;
        });

        JLabel separationLabel = new JLabel();
        separationLabel.setText("separationWeight: " + separationSlider.getValue());
        separationSlider.addChangeListener(o ->
        {
            separationLabel.setText("separationWeight: " + separationSlider.getValue());
            GuiParameters.preySeparationWeight = (float) separationSlider.getValue() / 10;
        });

        JLabel alignmentLabel = new JLabel();
        alignmentLabel.setText("alignmentWeight: " + alignmentSlider.getValue());
        alignmentSlider.addChangeListener(o ->
        {
            alignmentLabel.setText("alignmentWeight: " + alignmentSlider.getValue());
            GuiParameters.preyAlignmentWeight = (float) alignmentSlider.getValue() / 10;
        });

        JLabel predatorLabel = new JLabel();
        predatorLabel.setText("predators: " + predatorSlider.getValue());
        predatorSlider.addChangeListener(o ->
        {
            JSlider source3 = (JSlider) o.getSource();
            predatorLabel.setText("predators: " + source3.getValue());
            GuiParameters.predatorNumber = source3.getValue();

        });

        JLabel preyLabel = new JLabel();
        preyLabel.setText("preys: " + preySlider.getValue());
        preySlider.addChangeListener(o -> {
            JSlider source2 = (JSlider) o.getSource();
            preyLabel.setText("preys: " + source2.getValue());

            GuiParameters.preyNumber = (source2.getValue());

        });

        JLabel obstacleLabel = new JLabel();
        obstacleLabel.setText("obstacles: " + obstacleSlider.getValue());
        obstacleSlider.addChangeListener(o -> {
            JSlider source1 = (JSlider) o.getSource();
            obstacleLabel.setText("obstacles: " + source1.getValue());

            GuiParameters.obstacleNumber = (source1.getValue());

        });

        JLabel backgroundSpeedLabel = new JLabel();
        backgroundSpeedLabel.setText("backgroundSpeed: " + backgroundSpeedSlider.getValue());
        backgroundSpeedSlider.addChangeListener(o -> {
            backgroundSpeedLabel.setText("backgroundSpeed: " + backgroundSpeedSlider.getValue());
            GuiParameters.backGroundSpeed = (float) backgroundSpeedSlider.getValue() / 10;
        });

        ArrayList<JLabel> labels = new ArrayList<>();
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
        int j = 0;
        for (ParameterSlider slider : sliders) {
            this.add(slider);
            this.add(labels.get(j));
            j++;
        }
        this.setVisible(true);
    }

    private void blockOptions() {
        if (GuiParameters.writeToFile) {
            for (ParameterSlider slider : sliders) {
                slider.setEnabled(false);
                button2.setEnabled(false);
                textField.setEnabled(false);
            }
        }
    }

    private void unblockOptions() {
        if (!GuiParameters.writeToFile) {
            for (ParameterSlider slider : sliders) {
                slider.setEnabled(true);
                button2.setEnabled(true);
                textField.setEnabled(true);
            }
        }
    }
}
