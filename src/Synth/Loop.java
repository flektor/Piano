//package Synth;
//
//import Synthesizer.Keyboard;
//import Components.Label;
//import synthesizer.Key;
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Cursor;
//import java.awt.Dimension;
//import java.awt.GridLayout;
//import java.awt.Point;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JPanel;
//
//public class Loop extends JPanel {
//
//    public Loop() {
//        initComponents();
//
////        pressKey(keyboard.get(5), 2000, 100);
////        pressKey(keyboard.get(4), 500, 200);
////        pressKey(keyboard.get(1), 1000, 800);
////        pressKey(keyboard.get(8), 900, 500);
////        repaint();
//        revalidate();
//
//    }
//
//    private void initComponents() {
//        setLayout(new BorderLayout());
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        piano = new Keyboard(false);
//        piano.setPreviewPanel(this);
//        keyboard = piano.getKeys();
//        height = piano.getHeight() / keyboard.size();
//        width = height * 2;
//        mainPanel.add(piano.getKeyboard(), BorderLayout.WEST);
//        jPanel1 = new JPanel(new GridLayout(keyboard.size(), 1));
//        jPanel1.setLocation(300, 0);
//        addColumns(jPanel1, 5);
//        addColumns(jPanel1, 11);
//        setPreferredSize(new Dimension(jPanel1.getComponentCount() * width, piano.getHeight()));
//        mainPanel.add(jPanel1);
//        add(mainPanel, BorderLayout.NORTH);
//
//    }
//
//    public void pressKey(Key key, int time, int duration) {
//        runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    thread1.sleep(time);
//                    piano.keyPressed(key);
//                    thread1.sleep(duration);
//                    piano.keyReleased(key);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Loop.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        };
//        thread1 = new Thread(runnable);
//        thread1.start();
//
//    }
//
//    public void addColumns(JPanel panel, int columns) {
//
//        int cols = panel.getComponentCount() + 1 / keyboard.size();
//
//        System.out.println(cols);
//        int x = 0, y = 0;
//        for (x = 0; x < keyboard.size(); x++) {
//            if (cols == 0) {
//                JPanel panel2 = new JPanel(null);
//                panel2.setLocation(0, x * height);
//                panel.add(panel2);
//            }
//            JPanel panel2 = (JPanel) panel.getComponent(x);
//            cols = panel2.getComponentCount();
//            for (y = 0; y < columns; y++) {
//                NoteLabel label = new NoteLabel(1, 2, 10);
//                label.setLocation((y + cols) * width, 0);
//                label.setSize(width, height);
//                label.setColor(Color.GRAY, true);
//                panel2.add(label);
//                MouseAdapter1 ma = new MouseAdapter1();
//                label.addMouseListener(ma);
//                label.addMouseMotionListener(ma);
//                noteLabels.add(label);
//            }
//        }
//    }
//
//    public Keyboard getKeys() {
//        return piano;
//    }
//
//    public JPanel getKeyboard() {
//        return piano.getKeyboard();
//    }
//
//    private class MouseAdapter1 extends MouseAdapter {
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            if (!resize) {
//                NoteLabel label = ((NoteLabel) e.getSource());
//                label.setColor(Color.GREEN, true);
//                label.setVariables(1, 5, 20);
//
//                for (int i = 0; i < keyboard.size(); i++) {
//                    JPanel p = (JPanel) jPanel1.getComponent(i);
//                    for (int j = 0; j < p.getComponentCount(); j++) {
//                        if (label == (NoteLabel) p.getComponent(j)) {
//                            piano.keyPressed(keyboard.get(keyboard.size() - 1 - i));
//                            break;
//                        }
//                    }
//                }
//                
//                repaint();
//                revalidate();
//            }
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e
//        ) {
//            if (!resize) {
//                NoteLabel label = ((NoteLabel) e.getSource());
//                if (!label.isEnabled()) {
//                    label.setVariables(1, 5, 20);
//                    label.setNote(true, Color.ORANGE);
//                } else {
//                    label.setVariables(1, 2, 10);
//                    label.setNote(false, Color.GRAY);
//                }
//                for (int i = 0; i < keyboard.size(); i++) {
//                    JPanel p = (JPanel) jPanel1.getComponent(i);
//                    for (int j = 0; j < p.getComponentCount(); j++) {
//                        if (label == (NoteLabel) p.getComponent(j)) {
//                            piano.keyReleased(keyboard.get(keyboard.size() - 1 - i));
//                            break;
//                        }
//                    }
//                }
//                repaint();
//                revalidate();
//            } else {
//                resize = false;
//            }
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//
//            NoteLabel label1 = (NoteLabel) e.getSource();
//            JPanel panel = null;
//            for (int i = 0; i < keyboard.size(); i++) {
//                JPanel p = (JPanel) jPanel1.getComponent(i);
//                for (int j = 0; j < p.getComponentCount(); j++) {
//                    if (label1 == (NoteLabel) p.getComponent(j)) {
//                        piano.keyEntered(keyboard.get(keyboard.size() - 1 - i));
//                        panel = p;
//                        break;
//                    }
//                }
//            }
//            for (int i = 0; i < panel.getComponentCount(); i++) {
//                NoteLabel label2 = ((NoteLabel) panel.getComponent(i));
//                label2.setColor(label2.getRealColor().brighter(), true);
//            }
//
//            if (label1.isEnabled) {
//                label1.setColor(Color.RED, true);
//            } else {
//                label1.setColor(Color.GREEN, true);
//            }
//            repaint();
//            revalidate();
//
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//
//            NoteLabel label = (NoteLabel) e.getSource();
//            JPanel panel = null;
//            for (int i = 0; i < keyboard.size(); i++) {
//                JPanel p = (JPanel) jPanel1.getComponent(i);
//                for (int j = 0; j < p.getComponentCount(); j++) {
//                    if (label == (NoteLabel) p.getComponent(j)) {
//                        piano.keyExited(keyboard.get(keyboard.size() - 1 - i));
//                        panel = p;
//                        break;
//                    }
//                }
//            }
//
//            for (int i = 0; i < panel.getComponentCount(); i++) {
//                NoteLabel label2 = ((NoteLabel) panel.getComponent(i));
//                label2.setColor(label2.getRealColor(), true);
//            }
//            repaint();
//            revalidate();
//
//        }
//
//        @Override
//        public void mouseMoved(MouseEvent e) {
//            Point point = e.getPoint();
//            NoteLabel label = (NoteLabel) e.getSource();
//
//            if (lastLabel == null) {
//                lastLabel = label;
//            }
//            if (label != lastLabel && lastLabel != null) {
//                lastLabel.setCursor(Cursor.getDefaultCursor());
//            }
//            int distance = (int) point.getX() % label.getWidth();
//            if ((distance < 4 || distance > label.getWidth() - 4) && label.isEnabled() && distance < label.getWidth()) {
//                cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
//                lastLabel.setCursor(cursor);
//            } else {
//                cursor = new Cursor(Cursor.DEFAULT_CURSOR);
//                lastLabel.setCursor(cursor);
//            }
//            lastLabel = label;
//        }
//
//        @Override
//        public void mouseDragged(MouseEvent e) {
//            NoteLabel label = ((NoteLabel) e.getSource());
//            Point point = e.getPoint();
//            double w = point.getX() % label.getWidth();
//            int distance = (int) point.getX() % label.getWidth() - (int) point.getX();
//            if ((w < 4 || w > label.getWidth() - 4) && label.isEnabled()) {
//                resize = true;
//                for (int i = 0; i < keyboard.size(); i++) {
//                    JPanel p = (JPanel) jPanel1.getComponent(i);
//                    for (int j = 0; j < p.getComponentCount(); j++) {
//                        if (label == p.getComponent(j)) {
//                            NoteLabel label1 = (NoteLabel) p.getComponent(j + 1);
//                            int x = (int) label1.getLocation().getX();
//                            int y = (int) label1.getLocation().getY();
//                            if (distance > 0) {
//                                if (w < 4) {
//                                    label.setSize(label.getWidth() + 1, label.getHeight());
//                                    label1.setSize(label1.getWidth() - 1, label1.getHeight());
//                                    label1.setLocation(x + 1, y);
//                                    System.out.println("dsdsssss");
//                                } else {
//                                    label.setSize(label.getWidth() - 1, label.getHeight());
//                                    label1.setSize(label1.getWidth() + 1, label1.getHeight());
//                                    label1.setLocation(x - 1, y);
//                                }
//                            } else {
//                                if (w < 4) {
//                                    label.setSize(label.getWidth() - 1, label.getHeight());
//                                    label1.setSize(label1.getWidth() + 1, label1.getHeight());
//                                    label1.setLocation(x - 1, y);
//                                } else {
//                                    label.setSize(label.getWidth() + 1, label.getHeight());
//                                    label1.setSize(label1.getWidth() - 1, label1.getHeight());
//                                    label1.setLocation(x + 1, y);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//    }
//
//    public class NoteLabel extends Label {
//
//        private Color color, tcolor;
//        private Boolean isEnabled = false;
//
//        public NoteLabel() {
//            super();
//        }
//        
//        public NoteLabel(NoteLabel noteLabel) {
//            super();
//            tcolor = color = noteLabel.getColor();
//            setLocation(noteLabel.getLocation());
//            setSize(noteLabel.getSize());
//        }
//        
//        public NoteLabel(int i, int i0, int i1) {
//            super(i, i0, i1);
//        }
//
//        public void setNote(boolean b, Color color) {
//            isEnabled = b;
//            tcolor = color;
//            this.color = color;
//            setColor(color, true);
//
//        }
//
//        @Override
//        public boolean isEnabled() {
//            return isEnabled;
//        }
//
//        @Override
//        public void setColor(Color c, boolean b) {
//            super.setColor(c, b); //To change body of generated methods, choose Tools | Templates.
//            this.color = c;
//            if (tcolor == null) {
//                tcolor = color;
//            }
//        }
//
//        public Color getRealColor() {
//            return tcolor;
//        }
//
//    }
//
//    private Keyboard piano;
//    private ArrayList<Key> keyboard = new ArrayList<>();
//    public int count = 0, time = 1000 / 4, width, height;
//    private Thread thread1;
//    private Runnable runnable;
//    private JPanel jPanel1;
//    private Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
//    public Boolean resize = false;
//    public NoteLabel label, lastLabel;
//    public ArrayList<NoteLabel> notes = new ArrayList();
//    public ArrayList<NoteLabel> noteLabels = new ArrayList();
//}
