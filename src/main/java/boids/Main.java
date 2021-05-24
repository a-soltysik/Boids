package boids;

import boids.gui.Animation;
import boids.gui.AnimationPanel;
import boids.gui.GuiParameters;
import boids.gui.OptionsPanel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class Main {
    static int i = 0;

    public static void main(String[] args) {
        Locale.setDefault(Locale.getDefault());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {

        }
        JFrame frame = new JFrame("Boids");
        AnimationPanel panel = new AnimationPanel();
        OptionsPanel optionsPanel = new OptionsPanel();
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
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (GuiParameters.writeToFile) {
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Czy chcesz zakończyć symulację?",
                            "Tytuł", JOptionPane.YES_NO_OPTION
                    );
                    if (choice == JOptionPane.YES_OPTION) {
                        GuiParameters.isEndOfProgram = true;
                    }
                } else {
                    GuiParameters.isEndOfProgram = true;
                }

            }
        });

        panel.startAnimation(Animation.UNLIMITED_FPS);

    }
}
