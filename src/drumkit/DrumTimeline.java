package drumkit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.util.ArrayList;
import javax.swing.JLabel;

public class DrumTimeline extends JLabel {

    private ArrayList<HitEvent> hitEvents;

    public DrumTimeline() {
        hitEvents = new ArrayList();
        setSize(1000, 200);
        setOpaque(true);
        repaint();
        revalidate();
    }

    @Override
    public void paintComponent(Graphics g) {
        //   super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        //    for (int i = 0; i < hitEvents.size(); i++) {
        if (hitEvents.size() > 0) {
        
            g.setClip(null);
            
            HitEvent hit = hitEvents.get(hitEvents.size() - 1);
            g.setColor(Color.red);
            int maxHeight = 120;
            int width = 1;
            int height = hit.getValue();
            int x = (int) (hit.getTime() * 0.125);
            int y = (maxHeight - height) / 2;

            g.fillRect(x, y, width, height);
           
        }
    }

    public void hit(int time, int value) {
        hitEvents.add(new HitEvent(time, value));
        repaint();
        revalidate();
    }

    public class HitEvent {

        int time, value;

        public HitEvent(int time, int value) {
            this.time = time;
            this.value = value;
        }

        public int getTime() {
            return time;
        }

        public int getValue() {
            return value;
        }

    }

    public ArrayList<HitEvent> getHitEvents() {
        return hitEvents;
    }
}
