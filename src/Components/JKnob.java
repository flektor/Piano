package Components;
// Imports for the GUI classes.

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * JKnob.java - A knob component. The knob can be rotated by dragging a spot on
 * the knob around in a circle. The knob will report its position in radians
 * when asked.
 *
 * @author Grant William Braught
 * @author Dickinson College
 * @version 12/4/2000
 */
public class JKnob extends JComponent implements MouseListener, MouseMotionListener {

    private int radius = 150;
    private int spotRadius = radius * 20 / 100;

    private double theta, value, prevTheta, min=0, max=999;
    private Color knobColor;
    private Color spotColor;

    private boolean pressedOnSpot;

    /**
     * No-Arg constructor that initializes the position of the knob to 0 radians
     * (Up).
     */
    public JKnob() {
        this(0);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Constructor that initializes the position of the knob to the specified
     * angle in radians.
     *
     * @param initAngle the initial angle of the knob.
     */
    public JKnob(double initTheta) {
        this(initTheta, Color.gray, Color.black);
    }

    /**
     * Constructor that initializes the position of the knob to the specified
     * position and also allows the colors of the knob and spot to be specified.
     *
     * @param initAngle the initial angle of the knob.
     * @param initColor the color of the knob.
     * @param initSpotColor the color of the spot.
     */
    public JKnob(double initTheta, Color initKnobColor,
            Color initSpotColor) {

        theta = initTheta;
        pressedOnSpot = false;
        knobColor = initKnobColor;
        spotColor = initSpotColor;

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        spotRadius = radius * 30 / 100;
    }

    /**
     * Paint the JKnob on the graphics context given. The knob is a filled
     * circle with a small filled circle offset within it to show the current
     * angular position of the knob.
     *
     * @param g The graphics context on which to paint the knob.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        // Draw the knob.
        g2.setColor(knobColor);
        g2.fillOval(0, 0, 2 * radius, 2 * radius);
//        g2.setColor(spotColor);
//        g2.drawOval(0, 0, 2 * radius, 2 * radius);

        // Find the center of the spot.
        Point pt = getSpotCenter();
        int xc = (int) pt.getX();
        int yc = (int) pt.getY();

        // Draw the spot.
        g2.setColor(spotColor);

        g2.fillOval(xc - spotRadius, yc - spotRadius,
                2 * spotRadius, 2 * spotRadius);

    }

    /**
     * Return the ideal size that the knob would like to be.
     *
     * @return the preferred size of the JKnob.
     */
    public Dimension getPreferredSize() {
        return new Dimension(2 * radius, 2 * radius);
    }

    /**
     * Return the minimum size that the knob would like to be. This is the same
     * size as the preferred size so the knob will be of a fixed size.
     *
     * @return the minimum size of the JKnob.
     */
    public Dimension getMinimumSize() {
        return new Dimension(2 * radius, 2 * radius);
    }

    /**
     * Get the current anglular position of the knob.
     *
     * @return the current anglular position of the knob.
     */
    public double getAngle() {
        return theta;
    }

    /**
     * Calculate the x, y coordinates of the center of the spot.
     *
     * @return a Point containing the x,y position of the center of the spot.
     */
    private Point getSpotCenter() {

        // Calculate the center point of the spot RELATIVE to the
        // center of the of the circle.
        int r = radius - spotRadius;

        int xcp = (int) (r * Math.sin(theta));
        int ycp = (int) (r * Math.cos(theta));

        // Adjust the center point of the spot so that it is offset
        // from the center of the circle.  This is necessary becasue
        // 0,0 is not actually the center of the circle, it is  the 
        // upper left corner of the component!
        int xc = radius + xcp;
        int yc = radius - ycp;

        // Create a NEW Point to return since we can't  
        // return 2 values!
        return new Point(xc, yc);
    }

    /**
     * Determine if the mouse click was on the spot or not. If it was return
     * true, otherwise return false.
     *
     * @return true if x,y is on the spot and false if not.
     */
    private boolean isOnSpot(Point pt) {
        return (pt.distance(getSpotCenter()) < spotRadius);
    }

    // Methods from the MouseListener interface.
    /**
     * Empy method because nothing happens on a click.
     *
     * @param e reference to a MouseEvent object describing the mouse click.
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Empty method because nothing happens when the mouse enters the Knob.
     *
     * @param e reference to a MouseEvent object describing the mouse entry.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Empty method because nothing happens when the mouse exits the knob.
     *
     * @param e reference to a MouseEvent object describing the mouse exit.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * When the mouse button is pressed, the dragging of the spot will be
     * enabled if the button was pressed over the spot.
     *
     * @param e reference to a MouseEvent object describing the mouse press.
     */
    public void mousePressed(MouseEvent e) {

        Point mouseLoc = e.getPoint();
        pressedOnSpot = isOnSpot(mouseLoc);
    }

    /**
     * When the button is released, the dragging of the spot is disabled.
     *
     * @param e reference to a MouseEvent object describing the mouse release.
     */
    public void mouseReleased(MouseEvent e) {
        pressedOnSpot = false;
    }

    // Methods from the MouseMotionListener interface.
    /**
     * Empty method because nothing happens when the mouse is moved if it is not
     * being dragged.
     *
     * @param e reference to a MouseEvent object describing the mouse move.
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Compute the NEW angle for the spot and repaint the knob. The new angle is
     * computed based on the new mouse position.
     *
     * @param e reference to a MouseEvent object describing the mouse drag.
     */
    public void mouseDragged(MouseEvent e) {

        if (pressedOnSpot) {
            prevTheta = theta;
            int mx = e.getX();
            int my = e.getY();

            // Compute the x, y position of the mouse RELATIVE
            // to the center of the knob.
            int mxp = mx - radius;
            int myp = radius - my;

            // Compute the NEW angle of the knob from the
            // new x and y position of the mouse.  
            // Math.atan2(...) computes the angle at which
            // x,y lies from the positive y axis with cw rotations
            // being positive and ccw being negative.
            theta = Math.atan2(mxp, myp);
            if (prevTheta < 0) {
                if (prevTheta < theta) {
                    value++;
                } else {
                    value--;
                }
            } else if (prevTheta > 0) {
                if (prevTheta > theta) {
                    value--;
                } else {
                    value++;
                }
            }
            if (value < min) {
                value = min;
            } else if (value > max) {
                value = max;
            }
            repaint();

        }
    }

    /**
     * Here main is used simply as a test method. If this file is executed "java
     * JKnob" then this main() method will be run. However, if another file uses
     * a JKnob as a component and that file is run then this main is ignored.
     */
    public static void main(String[] args) {

        JFrame myFrame = new JFrame("JKnob Test method");

        Container thePane = myFrame.getContentPane();

        // Add a JKnob to the pane.
        thePane.add(new JKnob());

        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        myFrame.pack();
        myFrame.show();
    }
}
