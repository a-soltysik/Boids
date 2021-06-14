package boids.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class OptionsPanel extends JPanel {
    private final JButton submitButton = new JButton("Wczytaj");
    private final HintTextField textField = new HintTextField("Nazwa pliku");
    private final BooleanCheckbox writeToFileCheckBox = new BooleanCheckbox(GuiParameters.writeToFile);
    private final ArrayList<JSlider> sliders = new ArrayList<>();

    public OptionsPanel() {
        setPreferredSize(new Dimension(350, getPreferredSize().height));
        setLayout(new BorderLayout());

        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));
        slidersPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Opcje");
        menuBar.add(optionsMenu);

        BooleanCheckbox showFovCheckBox = new BooleanCheckbox(GuiParameters.showFov);
        BooleanCheckbox showVelocityCheckBox = new BooleanCheckbox(GuiParameters.showVelocity);
        BooleanCheckbox antialiasingCheckBox = new BooleanCheckbox(GuiParameters.antialiasing);
        optionsMenu.add(writeToFileCheckBox);
        optionsMenu.add(showFovCheckBox);
        optionsMenu.add(showVelocityCheckBox);
        optionsMenu.add(antialiasingCheckBox);

        IntegerSlider preySlider = new IntegerSlider(GuiParameters.preyNumber);
        IntegerSlider predatorSlider = new IntegerSlider(GuiParameters.predatorNumber);
        IntegerSlider obstacleSlider = new IntegerSlider(GuiParameters.obstacleNumber);
        FloatSlider separationSlider = new FloatSlider(GuiParameters.preySeparationWeight);
        FloatSlider cohesionSlider = new FloatSlider(GuiParameters.preyCohesionWeight);
        FloatSlider alignmentSlider = new FloatSlider(GuiParameters.preyAlignmentWeight);
        FloatSlider maxSpeedSlider = new FloatSlider(GuiParameters.preyMaxSpeed);
        FloatSlider backgroundSpeedSlider = new FloatSlider(GuiParameters.backGroundSpeed);
        FloatSlider preyFovRadiusSlider = new FloatSlider(GuiParameters.preyFovRadius);
        FloatSlider preyFovAngleSlider = new FloatSlider(GuiParameters.preyFovAngleDeg);
        FloatSlider predatorFovRadiusSlider = new FloatSlider(GuiParameters.predatorFovRadius);
        FloatSlider predatorFovAngleSlider = new FloatSlider(GuiParameters.predatorFovAngleDeg);
        sliders.add(preySlider);
        sliders.add(predatorSlider);
        sliders.add(obstacleSlider);
        sliders.add(separationSlider);
        sliders.add(cohesionSlider);
        sliders.add(alignmentSlider);
        sliders.add(maxSpeedSlider);
        sliders.add(backgroundSpeedSlider);
        sliders.add(preyFovRadiusSlider);
        sliders.add(preyFovAngleSlider);
        sliders.add(predatorFovRadiusSlider);
        sliders.add(predatorFovAngleSlider);

        submitButton.addActionListener(o -> GuiParameters.fileName = textField.getText() + ".csv");

        maxSpeedSlider.addChangeListener(o ->
        {
            GuiParameters.predatorMaxSpeed.setValue((GuiParameters.preyMaxSpeed.getValue() / 5) * 3);
            GuiParameters.predatorMaxAcceleration.setValue(GuiParameters.predatorMaxSpeed.getValue() / 10);
            GuiParameters.preyMaxAcceleration.setValue(GuiParameters.preyMaxSpeed.getValue() / 10);
        });

        JPanel fileNamePanel = new JPanel();
        fileNamePanel.setLayout(new BoxLayout(fileNamePanel, BoxLayout.X_AXIS));
        textField.setPreferredSize(new Dimension(getPreferredSize().width, submitButton.getHeight()));
        fileNamePanel.add(textField);
        fileNamePanel.add(submitButton);
        slidersPanel.add(fileNamePanel);
        slidersPanel.add(Box.createVerticalStrut(15));

        for (JSlider slider : sliders) {
            slidersPanel.add(slider);
            slidersPanel.add(Box.createVerticalStrut(15));
        }
        JScrollPane scrollPane = new JScrollPane(slidersPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(BorderLayout.CENTER, scrollPane);
        add(BorderLayout.NORTH, menuBar);

        slidersPanel.setPreferredSize(new Dimension(getPreferredSize().width - scrollPane.getVerticalScrollBar().getPreferredSize().width, slidersPanel.getPreferredSize().height));
    }

    public void setWriteToFileEnabled(boolean writeToFile) {
        writeToFileCheckBox.setEnabled(writeToFile);
    }

    private static class IntegerSlider extends JSlider{
        private final GuiParameters.IntegerParameter parameter;
        public IntegerSlider(GuiParameters.IntegerParameter parameter) {
            this.parameter = parameter;
            setMinimum(parameter.min);
            setMaximum(parameter.max / parameter.tick);
            setValue(parameter.getValue() / parameter.tick);
            setCustomLabels();
            setPaintLabels(true);
            addChangeListener(o -> {
                parameter.setValue(getValue() * parameter.tick);
                setCustomLabels();
            });
        }

        private void setCustomLabels() {
            setLabelTable(new Hashtable<>(Map.of(
                    getMinimum(), new JLabel(Integer.toString(parameter.min)),
                    (getMinimum() + getMaximum()) / 2, new JLabel(parameter.name + ": " + parameter.getValue()),
                    getMaximum(), new JLabel(Integer.toString(parameter.max))
            )));
        }
    }

    private static class FloatSlider extends JSlider {
        private final GuiParameters.FloatParameter parameter;
        public FloatSlider(GuiParameters.FloatParameter parameter) {
            this.parameter = parameter;
            float scale = 1 / parameter.tick;
            setMinimum(Math.round(parameter.min * scale));
            setMaximum(Math.round(parameter.max * scale));
            setValue(Math.round (parameter.getValue() * scale));
            setCustomLabels();
            setPaintLabels(true);
            addChangeListener(o -> {
                parameter.setValue(getValue() / scale);
                setCustomLabels();
            });
        }

        private void setCustomLabels() {
            setLabelTable(new Hashtable<>(Map.of(
                    getMinimum(), new JLabel(Float.toString(parameter.min)),
                    (getMinimum() + getMaximum()) / 2, new JLabel(parameter.name + ": " + parameter.getValue()),
                    getMaximum(), new JLabel(Float.toString(parameter.max))
            )));
        }
    }

    private static class BooleanCheckbox extends JCheckBoxMenuItem {
        public BooleanCheckbox(GuiParameters.BooleanParameter parameter) {
            super(parameter.name);
            setSelected(parameter.getValue());
            addChangeListener(o -> parameter.setValue(isSelected()));
        }
    }

    //https://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint
    private static class HintTextField extends JTextField {
        private final String hint;
        public HintTextField(String hint) {
            this.hint = hint;
        }
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (getText().length() == 0) {
                int h = getHeight();
                ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Insets ins = getInsets();
                FontMetrics fm = g.getFontMetrics();
                int c0 = getBackground().getRGB();
                int c1 = getForeground().getRGB();
                int m = 0xfefefefe;
                int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
                g.setColor(new Color(c2, true));
                g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
            }
        }
    }
}
