package boids.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI {
    private final AnimationPanel animationPanel = new AnimationPanel();
    private final OptionsPanel optionsPanel = new OptionsPanel();
    public GUI() {
        JFrame frame = new JFrame("Boids");
        PauseButton button = new PauseButton();
        button.setPreferredSize(new Dimension(75, frame.getHeight()));

        animationPanel.setPreferredSize(new Dimension(1000, 563));
        frame.getContentPane().add(BorderLayout.CENTER, animationPanel);
        frame.getContentPane().add(BorderLayout.WEST, button);
        frame.getContentPane().add(BorderLayout.EAST,optionsPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (GuiParameters.writeToFile.getValue()) {
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            GuiParameters.isEndOfProgram.name,
                            GuiParameters.isEndOfProgram.name,
                            JOptionPane.YES_NO_OPTION
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        GuiParameters.isEndOfProgram.setValue(true);
                    }
                } else {
                    GuiParameters.isEndOfProgram.setValue(true);
                }
            }
        });

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
