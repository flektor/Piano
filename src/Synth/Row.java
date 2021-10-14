package Synth;

import Components.Label;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import synthesizer.Key;

public class Row extends JPanel {

    private JPanel jPanel1;
    private static int width, height;
    private ArrayList<NodeLabel> labels = new ArrayList<>();
    private M1 ml;
    private NodeLabel root;
    private ArrayList enabledNodes = new ArrayList<>();

    public Row(int cols) {
        setLayout(new BorderLayout());
        jPanel1 = new JPanel(null);
        NodeLabel label, prev = null;

        ml = new M1();
        width = 30;
        height = 30;

        for (int i = 0; i < cols; i++) {
            label = new NodeLabel(1, 2, 30);
            if (prev != null) {
                label.setPrev(prev);
                label.getPrev().setNext(label);
            } else {
                root = label;
            }
            label.addMouseMotionListener(ml);
            label.addMouseListener(ml);
            label.setLocation(i * width, 0);
            label.setSize(new Dimension(width, height));
            label.setColor(Color.GRAY, true);
            labels.add(label);
            jPanel1.add(label);
            prev = label;
        }
        jPanel1.repaint();
        jPanel1.revalidate();
        add(jPanel1);
        setPreferredSize(new Dimension(60 * cols, 50));

    }

    public void setEnableAt(int time) {
        time = time * 2 /30;
        System.out.println(time);
        NodeLabel current = labels.get(time);
        current.setEnable(true);
        enabledNodes.add(current);
    }

    public ArrayList<NodeLabel> getEnabledNodes() {
        return bubbleSort(enabledNodes);
    }

    public ArrayList<NodeLabel> bubbleSort(ArrayList<NodeLabel> list) {
        for (int pass = 1; pass < list.size(); pass++) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).getStartingTime() > list.get(i + 1).getStartingTime()) {
                    NodeLabel temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                }
            }
        }
        return list;
    }

    public JPanel getjPanel() {
        return jPanel1;
    }

    public NodeLabel getNode(int index) {
        return (NodeLabel) jPanel1.getComponent(index);
    }

    public int getLength() {
        int count = 0;
        NodeLabel label = root;
        while (label != null) {
            label = label.getNext();
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws AWTException {

        JFrame frame = new JFrame();
        Row row = new Row(100);
        JScrollPane jScrollPane = new JScrollPane(row);
        frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(1000, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class M1 extends MouseAdapter implements MouseMotionListener {

        private Boolean resize = false, isSmaller = false;
        private int side = 4, prevWidth;
        int startingPointX, prevPointX;

        @Override
        public void mouseClicked(MouseEvent e) {
            NodeLabel current = (NodeLabel) e.getSource();

            if (current.isEnable()) {
                NodeLabel prev = current.getPrev();
                NodeLabel next = current.getNext();
                current.setEnable(false);
                enabledNodes.remove(current);
                if (!prev.isEnable()) {
                    if (prev.getWidth() < width) {
                        if (!prev.getPrev().isEnable()) {
                            prev.setSize(width, height);
                            int w = current.getWidth();
                            for (int i = 1; i < w / width + 1; i++) {
                                current.setSize(width, height);
                                current.setLocation(prev.getX() + width * i, prev.getY());
                                if (i < w / width) {
                                    addLabelAfter(current);
                                    current = current.getNext();
                                }
                            }
                        } else {
                            int d = width - prev.getWidth();
                            System.out.println("d:" + d);
                            prev.setSize(prev.getWidth() + d, height);
                            prev.setLocation(prev.getPrev().getX() + prev.getPrev().getWidth(), prev.getY());
                            current.setSize(prev.getWidth() + d, height);
                            current.setLocation(prev.getWidth() + prev.getX(), current.getY());

                            int w = current.getWidth();
                            for (int i = 1; i < w / width + 1; i++) {
                                current.setSize(width, height);
                                current.setLocation(prev.getX() + prev.getWidth(), prev.getY());
                                if (i < w / width) {
                                    addLabelAfter(current);
                                    current = current.getNext();
                                }

                            }
                        }
                    }
                }
                if (!next.isEnable()) {
                    if (next.getWidth() < width) {
                        current.setSize(current.getWidth() + next.getWidth(), height * 2);
                        removeLabel(next);
                    }
                }
            } else {
                current.setEnable(true);
                enabledNodes.add(current);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            NodeLabel current = (NodeLabel) e.getSource();
            Point point = e.getPoint();
            NodeLabel next = current.getNext();
            NodeLabel prev = current.getPrev();
            int pointX = (int) point.getX();

            if (resize) {
                if (startingPointX > current.getWidth() - side) {
                    if (pointX > prevPointX) {        // deksia pleyra pros deksia   ,megalwnei to label 
                        while (next.getWidth() == 0) {
                            removeLabel(next);
                            next = current.getNext();
                        }
                        current.setSize(current.getWidth() + 1, height);
                        // robot.mouseMove( (int)next.getLocationOnScreen().getX() - 1 , (int)next.getLocationOnScreen().getY() +height/2);
                        next.setLocation(next.getX() + 1, next.getY());
                        next.setSize(next.getWidth() - 1, height);
                        prevPointX = pointX;
                        startingPointX++;

                    } else if (pointX < prevPointX && current.getWidth() > 2 * side) { // deksia pleyra pros aristera  ,mikrenei to label 
                        if (next.getWidth() >= width) {
                            addLabelBefore(next);
                            return;
                        }
                        current.setSize(current.getWidth() - 1, height);
                        //   robot.mouseMove( (int)current.getLocationOnScreen().getX() - 1 , (int)current.getLocationOnScreen().getY() +height/2);
                        next.setLocation(next.getX() - 1, next.getY());
                        next.setSize(next.getWidth() + 1, height);
                        prevPointX = pointX;
                        startingPointX--;
                    }
                }
                if (startingPointX < side) {
                    if (pointX < prevPointX) {                                          // aristerh pleyra pros aristera  ,megalwnei to label 
                        while (prev.getWidth() == 0) {
                            removeLabel(prev);
                            prev = current.getPrev();
                        }
                        current.setSize(current.getWidth() + 1, height);
                        current.setLocation(current.getX() - 1, current.getY());
                        prev.setSize(prev.getWidth() - 1, height);
                        prevPointX = pointX;

                    } else if (pointX > prevPointX && current.getWidth() > 2 * side) { // aristerh pleyra pros deksia   ,mikrenei to label 
                        if (prev.getWidth() >= width) {
                            addLabelBefore(current);
                            return;
                        }
                        current.setSize(current.getWidth() - 1, height);
                        current.setLocation(current.getX() + 1, current.getY());
                        prev.setSize((int) prev.getWidth() + 1, height);
                        prevPointX = pointX;
                    }
                }
            }
        }

        // adds a new node in the list ,before a specific node
        public void addLabelBefore(NodeLabel label) {
            NodeLabel prev = label.getPrev();
            NodeLabel l = new NodeLabel(1, 2, 30);
            l.addMouseMotionListener(ml);
            l.addMouseListener(ml);
            l.setColor(Color.GRAY, true);
            l.setLocation(label.getX(), label.getY());
            l.setSize(0, height);
            l.setPrev(prev);
            l.setNext(label);
            label.setPrev(l);
            jPanel1.add(l);
            prev.setNext(l);
            prev = l;
        }

        // adds a new node in the list ,after a specific node
        public void addLabelAfter(NodeLabel label) {
            NodeLabel next = label.getNext();
            NodeLabel l = new NodeLabel(1, 2, 30);
            l.addMouseMotionListener(ml);
            l.addMouseListener(ml);
            l.setColor(Color.GRAY, true);
            l.setLocation(label.getX(), label.getY());
            l.setSize(0, height);
            l.setPrev(label);
            l.setNext(next);
            label.setNext(l);
            jPanel1.add(l);
            next.setPrev(l);
            next = l;
        }

        // removes new node (LabelNode) from the list
        public void removeLabel(NodeLabel label) {
            NodeLabel next = label.getNext();
            NodeLabel prev = label.getPrev();
            prev.setNext(next);
            next.setPrev(prev);
            jPanel1.remove(label);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            NodeLabel label = (NodeLabel) e.getSource();
            Point point = e.getPoint();
            int pointX = (int) point.getX();
            if (label.isEnable() && (pointX < side || pointX > label.getWidth() - side)) {
                resize = true;
                startingPointX = (int) point.getX();
                prevPointX = (int) point.getX();
                prevWidth = ((NodeLabel) e.getSource()).getWidth();
                isSmaller = false;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            NodeLabel label = (NodeLabel) e.getSource();
            Point point = e.getPoint();
            int pointX = (int) point.getX();
            if ((pointX < side || pointX > label.getWidth() - side)) {
                resize = false;

            }
        }
    }

    public static class NodeLabel extends Label {

        private NodeLabel prev, next;
        private Boolean isEnable = false;
        private int time;

        public Boolean isEnable() {
            return isEnable;
        }

        public void setEnable(Boolean isEnable) {
            this.isEnable = isEnable;
            if (isEnable) {
                setColor(new Color(255, 204, 0), true);
                setVariables(1, 5, 30);
            } else {
                setColor(Color.GRAY, true);
                setVariables(1, 2, 30);
            }
        }

        public void setEnableAt(Boolean isEnable,int time) {
            this.time = time;
            this.isEnable = isEnable;
            if (isEnable) {
                setColor(new Color(255, 204, 0), true);
                setVariables(1, 5, 30);
            } else {
                setColor(Color.GRAY, true);
                setVariables(1, 2, 30);
            }
        }
        
        public NodeLabel getPrev() {
            return prev;
        }

        public NodeLabel getNext() {
            return next;
        }

        public void setPrev(NodeLabel prev) {
            this.prev = prev;
        }

        public void setNext(NodeLabel next) {
            this.next = next;
        }

        public NodeLabel(int s, int n, int f) {
            super(s, n, f);
        }

        public double getStartingTime() {
            return time;
        }
    }
}
