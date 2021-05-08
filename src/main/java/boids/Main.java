package boids;

import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.gui.OptionsPanel;


import javax.swing.*;
import java.awt.*;

public class Main {
    static int i = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Boids");
        AnimationPanel panel = new AnimationPanel();
        OptionsPanel optionsPanel = new OptionsPanel(panel);
        JButton button = new JButton("Resume");
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(100, frame.getHeight()));

        button.addActionListener(o-> {
            if (i % 2 == 0) {
                panel.resumeAnimation();
                button.setText("Pause");
                optionsPanel.button1.setEnabled(false);
            } else {
                panel.pauseAnimation();
                button.setText("Resume");
                optionsPanel.button1.setEnabled(true);
            }
            i++;
        });

        panel.setPreferredSize(new Dimension(1000, 500));
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.WEST, button);
        frame.getContentPane().add(BorderLayout.EAST,optionsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        panel.startAnimation(Animation.UNLIMITED_FPS);

    }
}
