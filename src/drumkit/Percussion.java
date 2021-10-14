package drumkit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.MOVE;

public class Percussion extends JLabel implements Connector, MouseListener, MouseMotionListener {

    private Color color1, color2;
    private int x, y, radius;
    private Boolean isMouseOver = false;
    private Control control;
    private Percussion thisPerc;

    public Percussion(Control control, int radius) {
        thisPerc = this;
        this.control = control;
        this.radius = radius;
        color2 = new Color(111, 115, 112);

        setTransferHandler(new JLabelTransferHandler());
        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(radius + radius * 10 / 100, radius + radius * 10 / 100));
    }

    public void setOutlineColor(Color color) {
        this.color2 = color;
    }

    public Color getOutlineColor() {
        return color2;
    }

    @Override
    public void setController(Control control) {
        this.control = control;
    }

    @Override
    public void setColor(Color color) {
        this.color1 = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D outlineG2d = (Graphics2D) g.create();
        Graphics2D cyrcleG2d = (Graphics2D) g.create();

        cyrcleG2d.setColor(color1);
        outlineG2d.setColor(color2);

        outlineG2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        outlineG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        outlineG2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        outlineG2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        outlineG2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        outlineG2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        outlineG2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        outlineG2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // cyrcleG2d.fillRect(x, y, radius, radius);
        cyrcleG2d.fillOval(x + radius * 5 / 200, y + radius * 5 / 200, radius - radius * 5 / 100, radius - radius * 5 / 100);
        outlineG2d.setStroke(new BasicStroke(radius * 5 / 100));
        outlineG2d.drawOval(x + radius * 5 / 200, y + radius * 5 / 200, radius - radius * 5 / 100, radius - radius * 5 / 100);

        cyrcleG2d.dispose();
        outlineG2d.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      //  if (e != null) {

        //  }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        control.setFocusOn(this);
        control.playNote(this);
        color1 = color1.brighter();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // keyboard1.keyReleased(null);
        color1 = color1.darker();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int cursor_x = e.getX();
        int cursor_y = e.getY();

        if (Math.pow(cursor_x - radius / 2, 2) + Math.pow(cursor_y - radius / 2, 2) <= Math.pow(radius / 2, 2)) {
            if (!isMouseOver) {
                color2 = color2.brighter();
                repaint();
                isMouseOver = true;
            }
        } else {
            if (isMouseOver) {
                color2 = color2.darker();
                repaint();
                isMouseOver = false;
            }
        }
    }

    class JLabelTransferHandler extends TransferHandler {

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {

            if (!support.isDrop()) {
                return false;
            }

            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }

            boolean actionSupported = (MOVE & support.getSourceDropActions()) == MOVE;
            if (actionSupported) {
                support.setDropAction(MOVE);
                return true;
            }

            return false;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            // if we can't handle the import, say so
            if (!canImport(support)) {
                return false;
            }
            // fetch the data and bail if this fails
            String data;

            try {
                data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);

            } catch (UnsupportedFlavorException e) {

                return false;
            } catch (java.io.IOException e) {

                return false;
            }
            int midiCode = Integer.valueOf(data);
            control.setInstrument(thisPerc, midiCode);
            return true;
        }

    }
}
