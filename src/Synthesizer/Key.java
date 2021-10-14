package synthesizer;

import Components.Label;
import Synth.Note;
import Synthesizer.KeyListener;
import java.awt.Color;
import java.util.ArrayList;

public class Key extends Label {

    public static final int BLACK = 0, WHITE = 1;
    
    public Key(int color) {
        this.colorCode = color;
        if (color == BLACK) {
            setColor(new Color(40, 40, 40), true);
        } else {
            setColor(new Color(200, 200, 200), true);
        }

        setVariables(1, 7, 5);
    }

    public void setNote(Note note) {
        this.note = note;
        setText(note.toString());
    }

    public Note getNote() {
        return note;
    }

    public void activate(Boolean b) {
        if (b) {
            color = getColor();
            if (colorCode == BLACK) {
                setColor(new Color(80, 0, 0), true);
            } else {
                setColor(new Color(150, 0, 0), true);
            }
        } else {
            setColor(color, true);
        }
    }

    public void focus(Boolean b) {
        if (b) {
            if (colorCode == WHITE) {
                setColor(new Color(210, 160, 160), true);
            } else {
                setColor(new Color(130, 80, 80), true);
            }
        } else {
            if (colorCode == WHITE) {
                setColor(new Color(200, 200, 200), true);
            } else {
                setColor(new Color(40, 40, 40), true);
            }
        }
    }

    public void addKeyListener(KeyListener kl) {
        keyListeners.add(kl);
    }

    public void removeKeyListener(KeyListener kl) {
        keyListeners.remove(kl);
    }

    public KeyListener getKeyListener(int index) {
        return keyListeners.get(index);
    }

    private Note note;
    private int colorCode = 0;
    private Color color;
    private ArrayList<KeyListener> keyListeners = new ArrayList<>();
    

}
