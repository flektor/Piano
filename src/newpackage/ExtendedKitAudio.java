package newpackage;

import Synth.Row;
import Synth.Row.NodeLabel;
import inst.SimpleSampleInst;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jm.JMC;
import jm.audio.*;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

/**
 * A short example which generates a drum kit pattern and writes to a MIDI file
 * called ExtendedKit.mid
 *
 * @author Andrew Brown
 */
public final class ExtendedKitAudio implements JMC {
    /////////////
    //Attributes
    /////////////
    // audio instruments

    private static SimpleSampleInst kickInst = new SimpleSampleInst("src/newpackage/Kick.au", FRQ[36], true);
    private static SimpleSampleInst snareInst = new SimpleSampleInst("src/newpackage/Snare.au", FRQ[38], true);
    private static SimpleSampleInst hatsInst = new SimpleSampleInst("src/newpackage/Hats.au", FRQ[42], true);
    private static SimpleSampleInst openHatsInst = new SimpleSampleInst("src/newpackage/HHOpen.au", FRQ[46], false);
    private static Instrument[] drumKit = {kickInst, snareInst, hatsInst, openHatsInst};
    // data containers
    private Score pattern1 = new Score("JMDemo - Kit");
    private Part drumsBD;
    private Part drumsSD;
    private Part drumsHHC;
    private Part drumsHHO;
    private Part drumsCY;
    private Phrase phrBD;
    private Phrase phrSD;
    private Phrase phrHHC;
    private Phrase phrHHO;
    private Phrase end = new Phrase(64.0);
    //define often used rests
    private Note restSQ = new Note(REST, SEMI_QUAVER); //rest
    private Note restC = new Note(REST, CROTCHET); //rest
    private static JPanel jPanel1, jPanel2;
    private static Row rowBD, rowSD, rowHHC, rowHHO;

    ////////////
    //main
    ////////////	
    public static void main(String[] args) {
        ExtendedKitAudio e = new ExtendedKitAudio();
        JScrollPane jScrollPane = new JScrollPane(jPanel1);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);

        frame.add(jPanel2, BorderLayout.SOUTH);
        frame.setSize(1000, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void setValues() {
        pattern1 = new Score("JMDemo - Kit");
        drumsBD = new Part("Kick", 0, 9); // 9 = MIDI channel 10
        drumsSD = new Part("Snare", 1, 9); // 9 = MIDI channel 10
        drumsHHC = new Part("Hats Closed", 2, 9); // 9 = MIDI channel 10
        drumsHHO = new Part("Hats Open", 3, 9); // 9 = MIDI channel 10
        drumsCY = new Part("Cymbal", 3, 9); // 9 = MIDI channel 10
        phrBD = new Phrase(0.0);
        phrSD = new Phrase(0.0);
        phrHHC = new Phrase(0.0);
        phrHHO = new Phrase(0.0);
    }

    public void export() {
        //create an instance of the drum rhythm

        // set tempo
        //pattern1.setTempo(100);
        // normalise the jMusic score dynamics
        Mod.normalise(pattern1);

        //write an audio file of the score using the drumKit instruments
        Write.au(pattern1, "ExtendedDrums.au", drumKit);

    }

    private ExtendedKitAudio() {
        setValues();
        JButton jButton1 = new JButton("save");
        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setValues();

                ArrayList<NodeLabel> l = rowHHC.getEnabledNodes();
                doPhrase(l, phrHHC);

                l = rowSD.getEnabledNodes();
                doPhrase(l, phrSD);

                l = rowBD.getEnabledNodes();
                doPhrase(l, phrBD);

                doScore();
                export();
            }

        });
        jPanel2 = new JPanel();

        jPanel2.add(jButton1);

        rowBD = new Row(100);
        rowSD = new Row(100);
        rowHHC = new Row(100);
        rowHHO = new Row(100);

        jPanel1 = new JPanel(null);
        Dimension size1 = rowBD.getPreferredSize();

        rowBD.setBounds(0, 0, (int) size1.getWidth(), (int) size1.getHeight());

        Dimension size2 = rowSD.getPreferredSize();

        rowSD.setBounds(0, 50, (int) size2.getWidth(), (int) size2.getHeight());

        Dimension size3 = rowHHC.getPreferredSize();

        rowHHC.setBounds(0, 100, (int) size3.getWidth(), (int) size3.getHeight());

        Dimension size4 = rowHHO.getPreferredSize();

        rowHHO.setBounds(0, 150, (int) size4.getWidth(), (int) size4.getHeight());

        jPanel1.add(rowBD);

        jPanel1.add(rowSD);

        jPanel1.add(rowHHC);

        jPanel1.add(rowHHO);

        Dimension size = new Dimension((int) size1.getWidth(), 400);

        jPanel1.setPreferredSize(size);

    }

    private void doBassDrum(ArrayList<NodeLabel> list) {
        //Create basic kick pattern
        System.out.println("Doing kick drum. . .");

        for (int a = 0; a < list.size(); a++) {
            double time1 = list.get(a).getStartingTime();
            double time2 = 0;

            if (a == 0 && time1 > 0) {
                Note note2 = new Note(REST, time1 * 0.1);
                phrBD.add(note2);
            }
            Note note1 = new Note(42, 0.1, 120);
            phrBD.add(note1);

            if (a + 1 < list.size()) {
                time2 = list.get(a + 1).getStartingTime();
                double b = time2 - time1;
                Note note2 = new Note(REST, b * 0.1);
                phrBD.add(note2);

            }
        }
    }

    private void doPhrase(ArrayList<NodeLabel> list, Phrase phrase) {
        double tick = 0.125;
        for (int i = 0; i < list.size(); i++) {
            double time1 = list.get(i).getStartingTime();
            if (i == 0) {
                double t = (time1 - time1 % tick) * tick + time1 % tick;
                Note note1 = new Note(42, tick, 120);
                if (time1 == 0) {
                    phrase.add(note1);
                } else {
                    Note note2 = new Note(REST, t);
                    phrase.add(note2);
                    phrase.add(note1);
                }
            } else {
                double time2 = list.get(i - 1).getStartingTime();
                double t2 = (time1 - time2) * tick - tick;
                Note note2 = new Note(REST, t2);
                System.err.println("time2 " + t2);
                phrase.add(note2);
                Note note1 = new Note(42, tick, 120);
                phrase.add(note1);
            }
        }
    }

    private void doHiHats(ArrayList<NodeLabel> list) {
        System.out.println("Doing Hi Hats. . .");

        for (int a = 0; a < list.size(); a++) {
            double time1 = list.get(a).getStartingTime();
            System.out.println("time=" + time1);
            double time2 = 0;
            if (a == 0 && time1 > 0) {
                Note note2 = new Note(REST, time1 * 0.1);
                phrHHC.add(note2);
            }
            Note note1 = new Note(42, 0.1, 120);
            phrHHC.add(note1);
            if (a + 1 < list.size()) {
                time2 = list.get(a + 1).getStartingTime();
                double b = time2 - time1;
                Note note2 = new Note(REST, b * 0.1);
                phrHHC.add(note2);

            }
            Mod.accents(phrHHC, 2.0);
        }
    }

    private void doScore() {

        // add phrases to the parts
        System.out.println("Assembling. . .");
        drumsBD.addPhrase(phrBD);
        drumsSD.addPhrase(phrSD);
        drumsHHC.addPhrase(phrHHC);
//        drumsHHO.addPhrase(phrHHO);
//        drumsCY.addPhrase(end);

//        // add the drum parts to a score.
        pattern1.addPart(drumsBD);
        pattern1.addPart(drumsSD);
        pattern1.addPart(drumsHHC);
//        pattern1.addPart(drumsHHO);
//        pattern1.addPart(drumsCY);
    }
}
