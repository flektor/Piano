package newpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.Timer;

/**
 *
 * @author ge
 */
public class Timeline extends JPanel {

    public Timeline() {
        
        setLayout(new BorderLayout());
        
        setPreferredSize(new Dimension(1024, 400));
        initComponents();
        jLabel1.setFocusable(true);
        add(jPanel1, BorderLayout.NORTH);
        add(jPanel2, BorderLayout.CENTER);
        add(jChannels, BorderLayout.SOUTH);

        timer1.start();
        timer2.start();
    }

    public void initComponents() {
        jLabel1 = new TimeLabel();
        Dimension dim1 = getSize();
        Dimension dim2 = new Dimension((int)dim1.getWidth(), 70);
        jLabel1.setPreferredSize(dim2);
        System.err.println(dim2);

        TimelineΚeyAdapter tka = new TimelineΚeyAdapter();
        TimelineMouseAdapter tma = new TimelineMouseAdapter();

        MouseAdapter ma = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                int t = time / speed;
                int pointx = (int) me.getPoint().getX();
                int x = bigLines * widthOfSec;
                if (pointx < 0) {
                    pointx = 0;
                }
                int diff = Math.abs(x / 2 - pointx);
                if (t / 100 >= bigLines / 2) {
                    if (pointx > x / 2) {
                        t += diff;
                    } else {
                        t -= diff;
                    }
                } else {
                    t = pointx;
                }

                if (t < 0) {
                    t = 0;
                } else if (t > endTime) {
                    t = endTime;
                }

                time = t * speed;
                jScrollBar1.setValue(time);
                repaint();
                revalidate();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                int t = time;
                int pointx = (int) me.getPoint().getX();
                int x = bigLines * widthOfSec;
                if (pointx < 0) {
                    pointx = 0;
                }

                int diff = Math.abs(x / 2 - pointx);
                if (t / 100 >= bigLines / 2) {
                    if (pointx > x / 2) {
                        t += diff;
                    } else {
                        t -= diff;
                    }
                } else {
                    if (pointx > x / 2) {
                        t += diff + x / 2;
                    } else {
                        t -= diff - x / 2;
                    }
                }

                if (t < 0) {
                    t = 0;
                } else if (t > endTime) {
                    t = endTime;
                }

                dragTimerTo = t;
                isDragging = true;
                repaint();
                revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (isDragging) {
                    isDragging = false;
                    mouseClicked(me);
                }
            }
        };

        jLabel1.addMouseListener(ma);
        jLabel1.addMouseMotionListener(ma);
        jLabel1.addMouseWheelListener(tma);
        jLabel1.addKeyListener(tka);

        timer1 = new Timer(1000 / 60, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRecording || isPlaying) {
                    repaint();
                    revalidate();
                }
            }
        });

        timer2 = new Timer(1, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (isRecording) {
                    time = (int) (System.currentTimeMillis() - startTime) / 10;
                    jScrollBar1.setMaximum(time + 10);
                    jScrollBar1.setValue(time);
                }
                if (isPlaying) {
                    time = (int) (System.currentTimeMillis() - startTime) / 10;
                    jScrollBar1.setValue(time);
                    if (time >= endTime) {
                        isPlaying = false;
                    }
                }
            }
        });

        jScrollBar1 = new JScrollBar(0);
        jScrollBar1.setEnabled(true);
        jScrollBar1.setMaximum(endTime + 10);

        jScrollBar1.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                if (!isRecording && !isPlaying) {
                    time = ae.getValue();
                    repaint();
                    revalidate();
                }
                if (!isRecording && endTime > 0) {
                    repaint();
                    revalidate();
                }
            }
        });

        jLabel2 = new JLabel() {
            String timeStr;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("TimesRoman", Font.BOLD, 25));
                g2d.drawString(timeStr, 0, 20);
            }

            @Override
            public void setText(String string) {
                timeStr = string;
                repaint();
                revalidate();
            }
        };
        jlButton1 = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                JLButtonAdapter ma = (JLButtonAdapter) jlButton1.getMouseListeners()[0];
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, 30, 30);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect(1, 1, 28, 28);
                if (isRecording) {
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(Color.lightGray);
                }
                g2d.fillOval(8, 8, 14, 14);
            }
        };

        jlButton1.addMouseListener(new JLButtonAdapter(jlButton1) {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isRecording) {
                    isRecording = false;
                    endTime = time;
                } else {
                    isRecording = true;
                    isPlaying = false;
                    startTime = System.currentTimeMillis() - endTime * 10;
                    endTime = 0;
                }
                repaint();
                revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                isEntered = true;
                jlButton1.repaint();
                jlButton1.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                isEntered = false;
                jlButton1.repaint();
                jlButton1.revalidate();
            }
        });

        jlButton2 = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                JLButtonAdapter ma = (JLButtonAdapter) jlButton2.getMouseListeners()[0];
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, 30, 30);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect(1, 1, 28, 28);
                if (isPlaying) {
                    g2d.setColor(Color.lightGray.brighter());
                    g2d.fillRect(17, 8, 5, 14);
                    g2d.fillRect(8, 8, 5, 14);
                } else {
                    if (endTime > time) {
                        g2d.setColor(Color.green);
                    } else {
                        g2d.setColor(Color.lightGray);
                    }
                    Polygon poly = new Polygon(new int[]{10, 10, 20}, new int[]{7, 23, 15}, 3);
                    g2d.fillPolygon(poly);
                }
            }
        };

        jlButton2.addMouseListener(new JLButtonAdapter(jlButton2) {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (endTime > time) {
                    if (isPlaying) {
                        isPlaying = false;
                        jlButton2.setToolTipText("Play");
                    } else {
                        startTime = System.currentTimeMillis() - time * 10;
                        isPlaying = true;
                        jlButton2.setToolTipText("Pause");
                    }
                    jlButton1.repaint();
                    jlButton1.revalidate();
                    jlButton2.repaint();
                    jlButton2.revalidate();
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if (endTime > time) {
                    isEntered = true;
                    jlButton2.repaint();
                    jlButton2.revalidate();
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                isEntered = false;
                jlButton2.repaint();
                jlButton2.revalidate();
            }
        });

        jlButton3 = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                JLButtonAdapter ma = (JLButtonAdapter) jlButton3.getMouseListeners()[0];
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, 30, 30);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect(1, 1, 28, 28);
                if (ma.isPressed) {
                    g2d.setColor(Color.YELLOW);
                } else {
                    g2d.setColor(Color.lightGray);
                }
                g2d.fillRect(9, 9, 12, 12);
            }
        };

        jlButton3.addMouseListener(new JLButtonAdapter(jlButton3) {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPlaying) {
                    isPlaying = false;
                    time = 0;
                    repaint();
                    revalidate();
                }
                if (isRecording) {
                    jlButton1.getMouseListeners()[0].mouseClicked(null);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if (isPlaying || isRecording) {
                    isPressed = true;
                    if (isPlaying) {
                        time = 0;
                    }
                    jlButton3.repaint();
                    jlButton3.revalidate();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                isPressed = false;
                isEntered = false;
                jlButton3.repaint();
                jlButton3.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if (isPlaying || isRecording) {
                    isEntered = true;
                    jlButton3.repaint();
                    jlButton3.revalidate();
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                isEntered = false;
                jlButton3.repaint();
                jlButton3.revalidate();
            }
        });

        jlButton4 = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                JLButtonAdapter ma = (JLButtonAdapter) jlButton4.getMouseListeners()[0];
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, 30, 30);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect(1, 1, 28, 28);
                if (ma.isPressed) {
                    g2d.setColor(Color.green);
                } else {
                    if (!isRecording && endTime > 0) {
                        g2d.setColor(Color.orange);
                    } else {
                        g2d.setColor(Color.lightGray);
                    }
                }
                g2d.fillOval(5, 5, 20, 20);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillOval(8, 8, 14, 14);
                g2d.fillRect(17, 16, 8, 10);
                if (ma.isPressed) {
                    g2d.setColor(Color.green);
                } else {
                    if (!isRecording && endTime > 0) {
                        g2d.setColor(Color.orange);
                    } else {
                        g2d.setColor(Color.lightGray);
                    }
                }
                Polygon poly = new Polygon(new int[]{19, 28, 23}, new int[]{14, 14, 20}, 3);
                g2d.fillPolygon(poly);
            }
        };

        jlButton4.addMouseListener(new JLButtonAdapter(jlButton4) {

            @Override
            public void mousePressed(MouseEvent me) {
                if (!isRecording && endTime > 0) {
                    jlButton2.getMouseListeners()[0].mouseClicked(null);
                    isPlaying = true;
                    isPressed = true;
                    time = 0;
                    startTime = System.currentTimeMillis();
                    jlButton2.repaint();
                    jlButton2.revalidate();
                    jlButton4.repaint();
                    jlButton4.revalidate();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                isPressed = false;
                jlButton4.repaint();
                jlButton4.revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if (!isRecording && endTime > 0) {
                    isEntered = true;
                    jlButton4.repaint();
                    jlButton4.revalidate();
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if (!isRecording && endTime > 0) {
                    isEntered = false;
                    jlButton4.repaint();
                    jlButton4.revalidate();
                }
            }
        });

        jlButton5 = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                JLButtonAdapter ma = (JLButtonAdapter) jlButton5.getMouseListeners()[0];
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, 30, 30);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect(1, 1, 28, 28);
                if (ma.isPressed) {
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(Color.lightGray);
                }
                g2d.fillRect(8, 13, 14, 4);
                g2d.fillRect(13, 8, 4, 14);

            }
        };

        jlButton5.addMouseListener(new JLButtonAdapter(jlButton5) {
            @Override
            public void mouseClicked(MouseEvent me) {
                switch (speed) {
                    case 2:
                        speed = 1;
                        break;
                    case 5:
                        speed = 2;
                        break;
                    case 10:
                        speed = 5;
                        break;
                    case 20:
                        speed = 10;
                        break;
                    case 30:
                        speed = 20;
                        break;
                    case 60:
                        speed = 30;
                        break;
                }
                repaint();
                revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                isEntered = true;
                jlButton5.repaint();
                jlButton5.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                isEntered = false;
                jlButton5.repaint();
                jlButton5.revalidate();
            }
        });

        jlButton6 = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                JLButtonAdapter ma = (JLButtonAdapter) jlButton6.getMouseListeners()[0];
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, 30, 30);
                if (ma.isEntered) {
                    g2d.setColor(Color.GRAY.darker());
                } else {
                    g2d.setColor(Color.GRAY);
                }
                g2d.fillRect(1, 1, 28, 28);
                if (ma.isPressed) {
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(Color.lightGray);
                }
                g2d.fillRect(8, 13, 14, 4);
            }
        };

        jlButton6.addMouseListener(new JLButtonAdapter(jlButton6) {

            @Override
            public void mouseClicked(MouseEvent me) {
                switch (speed) {
                    case 1:
                        speed = 2;
                        break;
                    case 2:
                        speed = 5;
                        break;
                    case 5:
                        speed = 10;
                        break;
                    case 10:
                        speed = 20;
                        break;
                    case 20:
                        speed = 30;
                        break;
                    case 30:
                        speed = 60;
                        break;
                }
                repaint();
                revalidate();
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                isEntered = true;
                jlButton6.repaint();
                jlButton6.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                isEntered = false;
                jlButton6.repaint();
                jlButton6.revalidate();
            }
        });

        jLabel2.setPreferredSize(new Dimension(120, 20));

        jPanel1 = new JPanel();
        jPanel2 = new JPanel(new BorderLayout());
        jChannels = new Channels();

        jChannels.addChannel();
        jChannels.addChannel();
        jChannels.addChannel();
        jChannels.addChannel();

        dim1 = new Dimension(30, 30);
        jlButton1.setPreferredSize(dim1);
        jlButton2.setPreferredSize(dim1);
        jlButton3.setPreferredSize(dim1);
        jlButton4.setPreferredSize(dim1);
        jlButton5.setPreferredSize(dim1);
        jlButton6.setPreferredSize(dim1);

        jPanel1.add(jLabel2);
        jPanel1.add(jlButton1);
        jPanel1.add(jlButton3);
        jPanel1.add(jlButton4);
        jPanel1.add(jlButton2);
        jPanel1.add(jlButton5);
        jPanel1.add(jlButton6);
        
        jPanel2.add(new JLabel("time"), BorderLayout.WEST);
        jPanel2.add(jLabel1, BorderLayout.CENTER);
    }

    public String getTimeString(int millisecs, Boolean isTrue) {

        int secs = millisecs / 100;
        int mins = secs / 60;
        int hours = mins / 60;
        millisecs = millisecs % 100;
        secs = secs % 60;
        mins = mins % 60;
        String hoursStr = "";
        String minsStr = "";
        String secsStr = "";
        String millisecsStr = "";

        if (hours > 0) {
            hoursStr = String.valueOf(hours) + ":";
        }

        if (hours > 0 || isTrue) {
            if (mins == 0) {
                minsStr = "00:";
            } else if (mins < 10) {
                minsStr = "0" + String.valueOf(mins) + ":";
            } else {
                minsStr = String.valueOf(mins) + ":";
            }
        } else if (mins > 0) {
            minsStr = String.valueOf(mins) + ":";
        }

        if (mins > 0 || isTrue) {
            if (secs == 0) {
                secsStr = "00";
            } else if (secs < 10) {
                secsStr = "0" + String.valueOf(secs);
            } else {
                secsStr = String.valueOf(secs);
            }
        } else {
            secsStr = String.valueOf(secs);
        }
        if (isTrue) {
            if (millisecs == 0) {
                millisecsStr = ".00";
            } else if (millisecs < 10) {
                millisecsStr = ".0" + String.valueOf(millisecs);
            } else {
                millisecsStr = "." + String.valueOf(millisecs);
            }
        }
        return hoursStr + minsStr + secsStr + millisecsStr;
    }

    private class TimeLabel extends JLabel {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                g2d.drawLine(0, 0, getWidth(), 0);
                g2d.drawLine(0, 33, getWidth(), 33);

                Boolean firstPass;
                bigLines = (int) getSize().getWidth() / widthOfSec + 2;
                if (bigLines % 2 != 0) {
                    bigLines++;
                }
                //Paint Numbers and verticals lines
                int timePoint = time / speed;
                jLabel2.setText(getTimeString(time, true));
                int end = endTime / speed;
                int t = -timePoint % 100;
                if (timePoint / 100 >= bigLines / 2) {
                    for (int bl = 0; bl < bigLines; bl++) {
                        firstPass = true;
                        for (int sl = 0; sl < smallLines; sl++) {
                            int width = t + sl * widthOfSec / smallLines + bl * widthOfSec;
                            int secs = (timePoint - bigLines * 50) / 100 + bl;
                            if (firstPass) {
                                String timeString = getTimeString(secs * 100 * speed, false);
                                int l = timeString.length();
                                int m = l * 6 / 2;
                                g2d.drawString(timeString, width - m, 12);
                                g2d.drawLine(width, 15, width, 30);
                                firstPass = false;
                            } else {
                                g2d.drawLine(width, 20, width, 30);
                            }
                        }
                    }
                } else {
                    for (int bl = 0; bl < bigLines; bl++) {
                        firstPass = true;
                        for (int sl = 0; sl < smallLines; sl++) {
                            int w = sl * widthOfSec / smallLines + bl * widthOfSec;
                            if (firstPass) {
                                String secs = getTimeString(bl * 100 * speed, false);
                                int l = secs.length();
                                int m = l * 6 / 2;
                                g2d.drawString(secs, w - m, 12);
                                g2d.drawLine(w, 15, w, 30);
                                firstPass = false;
                            } else {
                                g2d.drawLine(w, 20, w, 30);
                            }
                        }
                    }
                }
                // Paint Timer point
                int x = 0;
                // green pointer - shows when the mouse dragged on timeline
                if (isDragging) {
                    int width = bigLines * widthOfSec;
                    int diff = Math.abs(dragTimerTo - time);
                    if (width > time) {
                        if (width / 2 > time) {
                            x = diff;
                        } else {
                            if (time >= dragTimerTo) {
                                x = width / 2 - diff;
                            } else {
                                x = width / 2 + diff;
                            }
                        }
                    } else {
                        if (time >= dragTimerTo) {
                            x = width / 2 - diff;
                        } else {
                            x = width / 2 + diff;
                        }
                    }
                    if (end < x) {
                        x = end;
                    }
                    g2d.setColor(Color.green);
                    g2d.drawLine(x, 0, x, 33);
                }
                // red pointer points the current time on timeline
                if (timePoint / 100 < bigLines / 2) {
                    x = timePoint;

                } else {
                    x = bigLines * widthOfSec / 2;
                }
                g2d.setColor(Color.red);
                g2d.drawLine(x, 0, x, 33);
                /* Paints the horizontal line red if it is recording 
                 or green if it is playing */
                if (isRecording) {
                    g2d.drawLine(0, 35, x, 35);
                } else {
                    g2d.setColor(Color.green);
                    if (timePoint / 100 >= bigLines / 2) {
                        int width = (end - timePoint) + x;
                        x = bigLines * widthOfSec;
                        if (end > timePoint + width) {
                            g2d.drawLine(0, 35, x, 35);
                        } else {
                            g2d.drawLine(0, 35, width, 35);
                        }
                    } else {
                        g2d.drawLine(0, 35, end, 35);
                    }
                }
            }
        };
    
    private class Channels extends JPanel {

        public Channels() {
            BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(boxLayout);
        }

        public void addChannel() {
            addChannel(new Channel());
        }

        public void addChannel(Channel channel) {
            add(channel);
            setChannelsDimentions();
        }

        public void removeChannel(Channel channel) {
            remove(channel);
            setChannelsDimentions();
        }

        public void removeChannel(int index) {
            remove(index);
            setChannelsDimentions();
        }

        void setChannelsDimentions() {

            for (int i = 0; i < getComponentCount(); i++) {
                int width = (int) jLabel1.getSize().getWidth();

                Channel channel = (Channel) getComponent(i);
                Dimension dim = new Dimension(width, channelHeight);
                channel.setPreferredSize(dim);
                channel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            }
            repaint();
            revalidate();
        }

        int channelHeight = 70;

    }

    private class Channel extends JPanel {

        JPanel jPanel1;
        JLabel jLabel1;
        JSlider jSlider1;
        ChannelAdapter channelAdapter1;
        ChannelAdapter channelAdapter2;

        public Channel() {
            setLayout(new BorderLayout());
            jPanel1 = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    int width = (int) getSize().getWidth();
                    int height = (int) getSize().getHeight();
                    RoundRectangle2D roundRect = new RoundRectangle2D.Float(1, 1, width - 2, height - 2, 8, 8);
                    g2d.setColor(jPanel1.getBackground());
                    g2d.fillRect(0, 0, width, height);
                    g2d.setColor(Color.GRAY);
                    g2d.draw(roundRect);
                    if (channelAdapter1.isClicked) {
                        Color color = new Color(0, 255, 0, 20);
                        g2d.setColor(color);
                        g2d.fill(roundRect);
                        jSlider1.setBackground(color);
                    } else {
                        jSlider1.setBackground(jPanel1.getBackground());
                    }
                }
            };

            jSlider1 = new JSlider(1);
            jSlider1.setOpaque(false);
            jPanel1.add(jSlider1);

            jLabel1 = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    int width = (int) getSize().getWidth();
                    int height = (int) getSize().getHeight();
                    RoundRectangle2D roundRect = new RoundRectangle2D.Float(1, 1, width - 2, height - 2, 8, 8);
                    g2d.setColor(jPanel1.getBackground());
                    g2d.fillRect(0, 0, width, height);
                    g2d.setColor(Color.GRAY);
                    g2d.draw(roundRect);
                    if (channelAdapter2.isClicked) {
                        g2d.setColor(new Color(0, 255, 0, 20));
                        g2d.fill(roundRect);
                    }
                }
            };

            channelAdapter1 = new ChannelAdapter(jPanel1);
            channelAdapter2 = new ChannelAdapter(jLabel1);
            jPanel1.addMouseListener(channelAdapter1);
            jLabel1.addMouseListener(channelAdapter2);

            add(jPanel1, BorderLayout.WEST);
            add(jLabel1, BorderLayout.CENTER);
            add(new JLabel(" "), BorderLayout.EAST);
            repaint();
            revalidate();
        }

        @Override
        public void setPreferredSize(Dimension dmnsn) {
            super.setPreferredSize(dmnsn); //To change body of generated methods, choose Tools | Templates.

            int width = (int) dmnsn.getWidth();
            int height = (int) dmnsn.getHeight();
            width -= 150;
            Dimension dim = new Dimension(100, height);
            jPanel1.setPreferredSize(dim);
            dim = new Dimension(width, height);
            jLabel1.setSize(dim);
            dim = new Dimension(20, height - 10);
            jSlider1.setPreferredSize(dim);
            repaint();
            revalidate();
        }
    }

    private class ChannelAdapter extends MouseAdapter {

        Boolean isClicked = false;
        Component component;

        public ChannelAdapter(Component component) {
            this.component = component;
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            isClicked = !isClicked;
            component.repaint();
            component.revalidate();

        }

        public Boolean isClicked() {
            return isClicked;
        }
    };

    private class JLButtonAdapter extends MouseAdapter {

        Boolean isEntered = false;
        Boolean isPressed = false;
        JLabel jLabel;

        public JLButtonAdapter(JLabel jLabel) {
            super();
            this.jLabel = jLabel;
        }

        public Boolean isPressed() {
            return isPressed;
        }

        public Boolean isEntered() {
            return isEntered;
        }
    }

    private class TimelineMouseAdapter extends MouseAdapter {

        @Override
        public void mouseWheelMoved(MouseWheelEvent mwe) {
            if (isCtrlPressed) {
                double value = mwe.getPreciseWheelRotation();
                if (value < 0) {
                    jlButton5.getMouseListeners()[0].mouseClicked(null);
                } else {
                    jlButton6.getMouseListeners()[0].mouseClicked(null);
                }
            }
        }
    };

    private class TimelineΚeyAdapter extends KeyAdapter {

        Boolean isSpaceReleased = true;

        @Override
        public void keyPressed(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                isCtrlPressed = true;
            } else if (ke.getKeyCode() == KeyEvent.VK_SPACE && isSpaceReleased) {
                jlButton2.getMouseListeners()[0].mouseClicked(null);
                isSpaceReleased = false;
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                isCtrlPressed = false;
            } else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                isSpaceReleased = true;
            }
        }

        public Boolean isCtrlPressed() {
            return isCtrlPressed;
        }
    };

    public static void main(String[] args) {

        JFrame jFrame = new JFrame();
        jFrame.setLayout(new BorderLayout());
        Timeline jPanel1 = new Timeline();

        jFrame.getContentPane().add(jPanel1);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
    }

    private static JLabel jLabel1, jLabel2, jlButton1, jlButton2, jlButton3, jlButton4, jlButton5, jlButton6;
    private static JPanel jPanel1,jPanel2;
    private Channels jChannels;
    private JScrollBar jScrollBar1;
    private Timer timer1, timer2;
    private long startTime;
    private int time = 200, endTime = 2500;
    private int widthOfSec = 100;
    private int bigLines, smallLines = 10;
    private int dragTimerTo = 0;
    private int speed = 5;
    private Boolean isRecording = false;
    private Boolean isPlaying = false;
    private Boolean isDragging = false;
    private Boolean isCtrlPressed = false;

}
