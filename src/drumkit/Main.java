package drumkit;

import Synthesizer.AvailableInstruments;
import Synthesizer.Keyboard;
import Synthesizer.SynthA;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

public class Main {

    public static void main(String[] args) {
        JFrame jFrame1 = new JFrame();
        jFrame1.setLayout(new BorderLayout());
        JPanel jPanel1 = new JPanel(null);

        SynthA synthesizer = new SynthA();
        Keyboard keyboard1 = new Keyboard(synthesizer, 0, 7, 224, 100);
        keyboard1.setOctave(2);
        jPanel1.add(keyboard1);

        Drumkit drumkit1 = new Drumkit();
        jPanel1.add(drumkit1);

        JPanel controlPanel = drumkit1.getContolPanel();
        jPanel1.add(controlPanel);

        AvailableInstruments ins = new AvailableInstruments();
        JTree jTree1 = ins.getJTree();

        JScrollPane jScrollPane1 = new JScrollPane(jTree1);
        jScrollPane1.setPreferredSize(new Dimension(220, 1024));
        jFrame1.add(jScrollPane1, BorderLayout.WEST);
        jFrame1.add(jPanel1);

        // Set Bounds
        Dimension size1 = keyboard1.getPreferredSize();
        keyboard1.setBounds(25, 50, (int) size1.getWidth() , (int) size1.getHeight());

        Dimension size2 = controlPanel.getPreferredSize();
        controlPanel.setBounds((int) size1.getWidth() + 50, 25, (int) size2.getHeight() + (int) size2.getWidth(), (int) size2.getHeight());

        Dimension size3 = drumkit1.getPreferredSize();
        drumkit1.setBounds(25, (int) size2.getHeight() + 50 , (int) size3.getWidth(), (int) size3.getHeight());

        jFrame1.setSize(new Dimension(1400, 768));
        
        jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame1.setVisible(true);
    }
}
