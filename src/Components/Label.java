package Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Label extends JLabel {

    public Label() {
        super();
        setColor(Color.GRAY, false);
        setSize(40 ,40);
        size = 10;
        numberOfLines = 1;
        factor = 255;
        setOpaque(true);
        repaint();
        revalidate();
    }

    public Label(int size, int numberOfLines, int factor) {
        this();
        setVariables(size, numberOfLines, factor);
    }

    public void setColor(Color c, boolean b) {
        color = c;
        outline = b;
        repaint();
        revalidate();
    }

    public void setVariables(int size, int numberOfLines, int factor) {
        this.size = size;
        this.numberOfLines = numberOfLines;
        this.factor = factor;
        setRGB(outline, factor);
        setColor(color, outline);
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x,y);
        this.x = x;
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);

        if (outline) {
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();
            int a = color.getAlpha();
            Color color1 = color;

            for (int n = numberOfLines; n > 0; n--) {
                setRGB(false, factor);
                color1 = new Color(r, g, b, a);
                gr.setColor(color1);
                gr.fillRect(x - n * size, 0, size, y);
                gr.fillRect(0, y - n * size, x, size);
            }

            gr.setColor(color);
            gr.fillRect(0, 0, x - numberOfLines * size, y - numberOfLines * size);

            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();
            for (int n = numberOfLines; n >= 0; n--) {
                setRGB(true, factor);
                color1 = new Color(r, g, b, a);
                gr.setColor(color1);
                gr.fillRect(0, 0, n * size, y - n * size);
                gr.fillRect(0, 0, x - n * size, n * size);
            }

        } else {
            gr.setColor(color);
            gr.fillRect(0, 0, x, y);
        }
    }

    private void setRGB(Boolean shadow, int factor) {
        if (shadow) {
            r += factor;
            g += factor;
            b += factor;
        } else {
            r -= factor;
            g -= factor;
            b -= factor;
        }
        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }

    }

//    public static void main(String[] args) {
//
//        JFrame frame = new JFrame();
//        Label label = new Label();
//        label.setColor(Color.CYAN, true);
//        label.setSize(600, 600);
//        frame.setLayout(new BorderLayout());
//        frame.getContentPane().add(label);
//        frame.setSize(615, 640);
//        frame.repaint();
//        frame.revalidate();
//        frame.setVisible(true);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }

    private Color color;
    private boolean outline = false;
    private int x, y, r, g, b, size, numberOfLines, factor;
}
