package boids.gui;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private final AnimationPanel animationPanel = new AnimationPanel();
    private final OptionsPanel optionsPanel = new OptionsPanel();
    public GUI() {
        JFrame frame = new JFrame("Boids");
        PauseButton button = new PauseButton();
        button.setPreferredSize(new Dimension(75, frame.getHeight()));

        animationPanel.setPreferredSize(new Dimension(1000, 500));
        frame.getContentPane().add(BorderLayout.CENTER, animationPanel);
        frame.getContentPane().add(BorderLayout.WEST, button);
        frame.getContentPane().add(BorderLayout.EAST,optionsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        animationPanel.startAnimation(Animation.UNLIMITED_FPS);
    }

    private class PauseButton extends JButton{
        private int i = 0;
        public PauseButton() {
            setText("Start");
            addActionListener(o-> {
                if (i % 2 == 0) {
                    animationPanel.resumeAnimation();
                    optionsPanel.setWriteToFileEnabled(false);
                    setText("Pauza");
                } else {
                    animationPanel.pauseAnimation();
                    optionsPanel.setWriteToFileEnabled(true);
                    setText("Wznów");
                }
                i++;
            });
        }
    }
}
