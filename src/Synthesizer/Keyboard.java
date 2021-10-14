package Synthesizer;

import Synth.Note;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.MOVE;
import synthesizer.Key;

public class Keyboard extends JPanel implements MouseListener {

    /*
     The height parameter may change value to display correct .
     */
    public Keyboard(SynthA synthesizer, int display, int numOfWhiteKeys, int width, int height) {
        this.numOfWhiteKeys = numOfWhiteKeys;
        this.synthesizer = synthesizer;
        numOfBlackKeys = numOfWhiteKeys / 7 * 5;
        setLayout(null);
        setTransferHandler(new KeyboardTransferHandler());
        setSize(new Dimension(width, height));
        if (display == 1) {
            createVerticalDisplay(width, height);
        } else {
            createHorizontalDisplay(width, height);
        }
        keyboard = bubbleSort(keyboard);
        setOctave(octave);

    }

    private void createHorizontalDisplay(int width, int height) {
        whiteKeyX = width / numOfWhiteKeys;
        blackKeyX = whiteKeyX * 2 / 3;
        int wtemp = width;
        while (blackKeyX % 2 == 0 || whiteKeyX % 2 != 0) {
            width++;
            whiteKeyX = width / numOfWhiteKeys;
            blackKeyX = whiteKeyX * 2 / 3;
            if (blackKeyX % 2 != 0 && whiteKeyX % 2 == 0) {
                System.err.print(" ** keyboard display problem \n"
                        + " the width of white key must devide perfect by 2\n"
                        + " the width of black key must NOT devide perfect by 2\n"
                        + " keyboard width changed from " + wtemp + " to " + width + "\n"
                        + " black key width = " + blackKeyX + "\n"
                        + " white key width = " + whiteKeyX + "\n"
                        + " end of message! **\n");
            }
        }
        whiteKeyY = height;
        blackKeyY = whiteKeyY * 3 / 5;

        /*
         Create and locate black keys
         Black keys must added in the panel before white keys to stay on top
         */
        int count = 0;
        for (int i = 0; i < numOfWhiteKeys; i++) {
            int j = i % 7;
            if (j == 1 || j == 2 || j == 4 || j == 5 || j == 6) {

                Note note = new Note();
                Key key = new Key(Key.BLACK);
                note.setPitch(i + count);
                note.setVolume(100);
                key.setNote(note);
                key.addMouseListener(this);

                key.setBounds(i * whiteKeyX - blackKeyX / 2 - 1, 0, blackKeyX, blackKeyY);
                key.setSize(blackKeyX, blackKeyY);
                add(key);
                keyboard.add(key);
                count++;
            }

        }
        /*
         Create and locate white keys
         height variable contains the total height of keyboard
         */
        count = 0;
        for (int i = 0; i < numOfWhiteKeys; i++) {
            int j = i % 7;
            if (j == 1 || j == 2 || j == 4 || j == 5 || j == 6) {
                count++;
            }
            Key key = new Key(Key.WHITE);
            Note note = new Note();
            note.setVolume(100);
            note.setPitch(i + count);
            key.setNote(note);
            key.addMouseListener(this);

            key.setBounds(i * whiteKeyX, 0, whiteKeyX, whiteKeyY);
            key.setSize(whiteKeyX, whiteKeyY);
            add(key);
            keyboard.add(key);
        }
    }

    private void createVerticalDisplay(int width, int height) {
        whiteKeyY = height / numOfWhiteKeys;
        blackKeyY = whiteKeyY / 2;
        int htemp = height;
        while (blackKeyY % 2 != 0 || height % blackKeyY != 0 || whiteKeyY % 2 != 0) {
            height++;
            whiteKeyY = height / numOfWhiteKeys;
            blackKeyY = whiteKeyY / 2;
            if (blackKeyY % 2 == 0 && height % blackKeyY == 0 && whiteKeyY % 2 == 0) {
                System.err.print(" ** keyboard display problem \n"
                        + " the height of keys must devide perfectly by 2\n"
                        + " keyboard height changed from " + htemp + " to " + height + "\n"
                        + " black key height = " + blackKeyY
                        + " white key height = " + whiteKeyY
                        + " end of message! **\n");
            }
        }
        whiteKeyX = width;
        blackKeyX = whiteKeyX * 3 / 5;

        /*
         Create and locate black keys
         Black keys must added in the panel before white keys to stay on top
         */
        int count1 = numOfBlackKeys - 1;    // for display
        int count2 = 0;                     // for note
        for (int i = 0; i < numOfWhiteKeys; i++) {
            int j = i % 7;
            if (j == 1 || j == 2 || j == 4 || j == 5 || j == 6) {

                Note note = new Note();
                Key key = new Key(Key.BLACK);
                note.setPitch(i + count2);
                key.setNote(note);
                key.addMouseListener(this);

                key.setBounds(0, (numOfWhiteKeys - i + count1) * blackKeyY, blackKeyX, blackKeyY);
                key.setSize(blackKeyX, blackKeyY);
                add(key);
                keyboard.add(key);
                count1--;
                count2++;
            }
        }
        /*
         Create and locate white keys
         height variable contains the total height of keyboard
         */
        height = (numOfBlackKeys + numOfWhiteKeys) * blackKeyY;
        count1 = 0;
        int y = 0;
        for (int i = 0; i < numOfWhiteKeys; i++) {
            int j = i % 7;

            if (j == 1 || j == 2 || j == 4 || j == 5 || j == 6) {
                count1++;
            }
            Key key = new Key(Key.WHITE);
            Note note = new Note();

            note.setPitch(i + count1);
            key.setNote(note);
            key.addMouseListener(this);

            if (j == 1 || j == 4 || j == 5) {
                y = whiteKeyY;
            } else {
                y = whiteKeyY * 75 / 100;
            }
            height -= y;
            key.setBounds(0, height, whiteKeyX, y);
            key.setSize(whiteKeyX, y);
            add(key);
            keyboard.add(key);
        }
    }

    public ArrayList<Key> bubbleSort(ArrayList<Key> list) {
        for (int pass = 1; pass < list.size(); pass++) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).getNote().getPitch() > list.get(i + 1).getNote().getPitch()) {
                    Key temp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, temp);
                }
            }
        }
        return list;
    }

    public void setOctave(int octave) {
        int count = 0;
        for (Key k : keyboard) {
            k.getNote().setOctave(octave + count / 12);
            count++;
        }
    }

    public ArrayList<Key> getKeys() {
        return keyboard;
    }

    public int getNumOfWhiteKeys() {
        return numOfWhiteKeys;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Key key = (Key) e.getSource();
        Note note = key.getNote();
        synthesizer.playNote(note);
        key.activate(true);
        repaint();
        revalidate();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Key key = (Key) e.getSource();
        Note note = key.getNote();
        key.activate(false);
        synthesizer.stopNote(note);
        repaint();
        revalidate();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Key key = (Key) e.getSource();
        key.focus(true);
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Key key = (Key) e.getSource();
        key.focus(false);
        repaint();
        revalidate();
    }

    

    class KeyboardTransferHandler extends TransferHandler {

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
            synthesizer.setInstrument(Integer.valueOf(data));

            return true;
        }
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        Keyboard keys = new Keyboard(new SynthA(), 1, 21, 100, 500);
        f.add(keys);

        f.setSize(500, 1000);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private ArrayList<Key> keyboard = new ArrayList<>();
    private int numOfWhiteKeys, numOfBlackKeys;
    private int whiteKeyY, whiteKeyX, blackKeyY, blackKeyX;
    private SynthA synthesizer;
    private int octave = 0;
}
