package Synth;

public class Note {

    public int octave = 0, volume, pitch, note,
            C, Chash, D, Dhash, E, F, Fhash, G, Ghash, A, Ahash, B;
    public int[] notes, noteshash;

    public Note() {
        setOctave(2);
        notes = new int[]{C, Chash, D, Dhash, E, F, Fhash, G, Ghash, A, Ahash, B};
    }

    public int[] getNotes() {
        return notes;
    }

    public int[] getNoteshash() {
        return noteshash;
    }

    public void setOctave(int octave) {
        octave += 2;
        if (octave < 0) {
            octave = 0;
        } else if (octave > 10) {
            octave = 10;
        }
        this.octave = octave;
        pitch = note + octave * 12;

//        C = 0 + octave * 12;
//        Chash = 1 + octave * 12;
//        D = 2 + octave * 12;
//        Dhash = 3 + octave * 12;
//        E = 4 + octave * 12;
//        F = 5 + octave * 12;
//        Fhash = 6 + octave * 12;
//        G = 7 + octave * 12;
//        Ghash = 8 + octave * 12;
//        A = 9 + octave * 12;
//        Ahash = 10 + octave * 12;
//        B = 11 + octave * 12;
    }

    public void setPitch(int value) {
        this.pitch = value;
        note = value % 12;
        octave = value / 12;
    }

//    public void setNote(int note) {
//        this.note = note;
//        pitch = note * 12 * octave;
//        
//            
//    }
    public int getOctave() {
        return octave-2;
    }

    public int getPitch() {
        return pitch;
    }

    public int getNote() {
        return note;
    }
    
    public void setVolume(int volume){
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
    

    @Override
    public String toString() {
        switch (note) {
            case 0:
                return "C";
            case 1:
                return "C#";
            case 2:
                return "D";
            case 3:
                return "D#";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "F#";
            case 7:
                return "G";
            case 8:
                return "G#";
            case 9:
                return "A";
            case 10:
                return "A#";
            case 11:
                return "B";
            default:
                return "error";
        }
    }
    private boolean hash = false;
}
//String[] noteString = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
//
//int octave = (initialNote / 12) - 1;
//int noteIndex = (initialNote % 12);
//String note = noteString[noteIndex];




//
//PUBLIC class Notes {
//    public static void main (String [] args) {
//        String notes = "C C#D D#E F F#G G#A A#B ";
//        int octave;
//        String note;
//        for (int noteNum = 0; noteNum < 128; noteNum++) {
//            octave = noteNum / 12 - 1;
//            note = notes.substring(
//                (noteNum % 12) * 2,
//                (noteNum % 12) * 2 + 2);
//            System.out.println (
//                "Note number " + noteNum +
//                " is octave " + octave +
//                " and note " + note);
//        }
//    }
//}