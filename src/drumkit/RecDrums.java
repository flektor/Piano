package drumkit;

import Synth.Row;
import Synth.Row.NodeLabel;
import drumkit.DrumTimeline.HitEvent;
import inst.SimpleSampleInst;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import jm.JMC;
import jm.audio.*;
import static jm.constants.Durations.CROTCHET;
import static jm.constants.Durations.SEMI_QUAVER;
import static jm.constants.Frequencies.FRQ;
import static jm.constants.Pitches.REST;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

/**
 * A short example which generates a drum kit pattern and writes to a MIDI file
 * called ExtendedKit.mid
 *
 * @author Andrew Brown
 */
public final class RecDrums implements JMC {

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

    private RecDrums() {
        // set Timer
        timer1 = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time += 1;
            }
        });
//
//        serialClass = new SerialClass(this);
        setValues();
        jButton1 = new JButton("Rec");
        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //       isRecEnabled = !isRecEnabled;
                if (isRecEnabled) {
                    jButton1.setText("Stop");
                    System.out.println("Rec is ON!\tRecording will start on the first hit");
                } else {
                    timer1.stop();
                    jButton1.setText("Rec");
                    isRecStarted = false;
                    System.out.println("Rec is OFF!\tRecording stopped!");

                }
            }
        });

        jButton2 = new JButton("save");
        jButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRecEnabled) {
                    jButton1.getActionListeners()[0].actionPerformed(null);
                }
                setValues();
                doPhrase(rowHHC, phrHHC);
                doPhrase(rowSD, phrSD);
                doPhrase(rowBD, phrBD);
                doScore();
                export();
            }
        });
        jPanel2 = new JPanel();

        jPanel2.add(jButton1);
        jPanel2.add(jButton2);

        rowBD = new DrumTimeline();
        rowSD = new DrumTimeline();
        rowHHC = new DrumTimeline();
        rowHHO = new DrumTimeline();

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

    private void doPhrase(DrumTimeline timeline, Phrase phrase) {

        double tick = 0.0125;
        ArrayList<HitEvent> hits = timeline.getHitEvents();
        for (int i = 0; i < hits.size(); i++) {
            int prevTime = 0;
            if (i > 0) {
                prevTime = hits.get(i - 1).getTime();
            }

            int currTime = hits.get(i).getTime();
            int value = hits.get(i).getValue();

            int volume = (int) Math.log(value) * 15;

            double t = currTime - prevTime;

            Note note1 = new Note(42, tick, volume);
            if (currTime == 0) {
                phrase.add(note1);
            } else {
                Note note2 = new Note(REST, t * tick);
                phrase.add(note2);
                phrase.add(note1);
            }

        }
    }

    public void hitHiHats(int value) {
        if (isRecEnabled) {
            if (!isRecStarted) {
                isRecStarted = true;
                timer1.start();
            }
            rowHHC.hit(time, value);
            jFrame1.repaint();
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
    
    
    

    public static void main(String[] args) {
        RecDrums e = new RecDrums();
        JScrollPane jScrollPane = new JScrollPane(rowHHC);

        jFrame1 = new JFrame();
        jFrame1.setLayout(new BorderLayout());
        jFrame1.getContentPane().add(jScrollPane, BorderLayout.CENTER);

        jFrame1.add(jPanel2, BorderLayout.SOUTH);
        jFrame1.setSize(1000, 500);
        jFrame1.setVisible(true);
        jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private static JFrame jFrame1;
    private Boolean isRecEnabled = true;
    private Boolean isRecStarted = false;
    private Timer timer1;
    private int time = 0;
    private JButton jButton1, jButton2;
//    private SerialClass serialClass;

    // audio instruments
    private SimpleSampleInst kickInst = new SimpleSampleInst("src/newpackage/Kick.au", FRQ[36], true);
    private SimpleSampleInst snareInst = new SimpleSampleInst("src/newpackage/Snare.au", FRQ[38], true);
    private SimpleSampleInst hatsInst = new SimpleSampleInst("src/newpackage/Hats.au", FRQ[42], true);
    private SimpleSampleInst openHatsInst = new SimpleSampleInst("src/newpackage/HHOpen.au", FRQ[46], false);
    private Instrument[] drumKit = {kickInst, snareInst, hatsInst, openHatsInst};
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
    private static DrumTimeline rowBD, rowSD, rowHHC, rowHHO;

    private static JPanel jPanel1, jPanel2;

}
