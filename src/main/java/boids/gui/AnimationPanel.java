package boids.gui;

import boids.math.Rectangle;
import boids.math.Vector2;

import javax.swing.*;
import java.awt.*;

public class AnimationPanel extends JPanel {

    private Animation animation;
    private Rectangle rectangle;

    public void startAnimation(int fps) {
        rectangle = new Rectangle(Vector2.ZERO, new Vector2(getWidth(), getHeight()));
        animation = new Animation(fps);
        animation.start(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //Antialiasing to panel
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        animation.render(g2d);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
