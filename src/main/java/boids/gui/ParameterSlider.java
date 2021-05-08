package boids.gui;

import javax.swing.*;
import java.awt.*;

public class ParameterSlider extends JSlider {
    ParameterSlider(int minimum, int maximum, int value, int tick){
        this.setMaximum(maximum);
        this.setMinimum(minimum);
        this.setValue(value);
        this.setPaintTicks(true);
        this.setMajorTickSpacing(tick);
        this.setPaintLabels(true);
        this.setPreferredSize(new Dimension(300,50));
        this.setVisible(true);
    }
}
