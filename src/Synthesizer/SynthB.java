package Synthesizer;

import Synth.Note;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class SynthB {
    private Note note;
    private Thread thread;
    private ThreadMIDI midi;
    private Synthesizer synthesizer;
    private Instrument[] instruments;
    private int octave = 0, instrCode = 0, volume = 100;
    
    

    public SynthB(){
        try {
            synthesizer = MidiSystem.getSynthesizer();
            instruments = synthesizer.getAvailableInstruments();
            synthesizer.loadInstrument(instruments[instrCode]);

        } catch (MidiUnavailableException ex) {
            Logger.getLogger(Keyboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getInstrumentName(int i) {
        return instruments[i].getName();
    }
    
    
    public void setInstrument(int i){
        instrCode = i;
    }

    public void playNote(Note note) {
        midi = new ThreadMIDI();
        midi.setNote(note);
        try {
            synthesizer.open();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(Keyboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(synthesizer.getLatency());
        thread = new Thread(midi);
        thread.start();

    }
    
    public void stopNote(Note note){
        if(note == midi.getNote()){
            midi.stop();
        }
    }

    private class ThreadMIDI implements Runnable {

        Note note;
        MidiChannel[] channels;
        boolean isTimeEnabled = false;
        int duration = 1000;

        public ThreadMIDI() {
            channels = synthesizer.getChannels();
        }

        public void setNote(Note note) {
            this.note = note;
            isTimeEnabled = false;
        }

        public void setNote(Note note, int duration) {
            this.note = note;
            this.duration = duration;
            isTimeEnabled = true;
        }

        public Note getNote() {
            return note;
        }
        
        
        public Instrument[] getInstruments() {
            return instruments;
        }

        public void stop() {
            channels[0].noteOff(note.getPitch());
        }

        @Override
        public void run() {
            channels[0].programChange(instruments[instrCode].getPatch().getProgram());
            channels[0].noteOn(note.getPitch(), note.getVolume());
            System.out.println(note);
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ex) {
                Logger.getLogger(Keyboard.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (isTimeEnabled) {
                stop();
            }
        }

    }
}
