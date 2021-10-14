package drumkit;

import Components.Label;
import Synth.Note;
import Synthesizer.SynthA;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class Control extends JPanel {

    private static SynthA synthesizer;
    private JTable jTable1;
    private DefaultTableModel model;
    private ListSelectionModel cellSelectionModel;
    private Label label1;
    private JLabel jLabel1, jLabel4;
    private JSlider jSlider1;
    private JSpinner jSpinner1, jSpinner2, jSpinner3;
    private JTextField jTextField1;
    private JPanel jPanel1;
    private int lastSelectedRow, volume, midiCode, octave, pitch, pin;
    private String name, sound;
    private Color color1 = Color.BLUE;
    private ArrayList drumConnectors;
    private ArduinoConnector arduinoConnector;

    public Control() {
        synthesizer = new SynthA();
        jPanel1 = new Settings();
        drumConnectors = new ArrayList<>();
        setLayout(new BorderLayout());

        jTable1 = createTable();
        JScrollPane jScrollPane1 = new JScrollPane(jTable1);

        Dimension size1 = jPanel1.getPreferredSize();
        Dimension size2 = jScrollPane1.getPreferredSize();
        jPanel1.setBounds(0, 0, (int) size1.getWidth(), (int) size1.getHeight());
        jScrollPane1.setBounds((int) size1.getWidth(), 0, (int) size2.getWidth(), (int) size1.getHeight());
        setPreferredSize(new Dimension((int) size2.getWidth() + (int) size1.getWidth() + 25, (int) size1.getHeight()));

        add(jScrollPane1);
        add(jPanel1);
     //   serialClass = new SerialClass(this);
    }

    public JPanel getSettingsPanel() {
        return jPanel1;
    }

    public JTable createTable() {
        Object[] data = new Object[]{"Name", "Sound", "MidiCode", "Octave", "Pitch", "Volume", "Pin"};

        model = new DefaultTableModel(null, data);
        jTable1 = new JTable(model);
        cellSelectionModel = jTable1.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    lastSelectedRow = jTable1.getSelectedRow();
                    updateColumn(lastSelectedRow);
                }
            }
        });
        return jTable1;
    }

    public void updateColumn(int index) {
        name = (String) model.getValueAt(index, 0);
        sound = (String) model.getValueAt(index, 1);
        midiCode = (int) model.getValueAt(index, 2);
        octave = (int) model.getValueAt(index, 3);
        pitch = (int) model.getValueAt(index, 4);
        volume = (int) model.getValueAt(index, 5);
        pin = (int) model.getValueAt(index, 6);
        jTextField1.setText(name);
        jSlider1.setValue(volume);
        jLabel1.setText(volume + " %");
        jLabel4.setText(sound);
        jSpinner1.setValue(octave);
        jSpinner3.setValue(pitch);
        jSpinner2.setValue(pin);
    }

    public void addConnector(Connector c, String name, int midiCode, int pitch, int volume, int pin) {
        drumConnectors.add(c);
        String sound = synthesizer.getInstrumentName(midiCode);
        octave = pitch / 12;
        Object[] data = new Object[]{name, sound, midiCode, octave, pitch, volume, pin};
        model.addRow(data);
        if (drumConnectors.size() == 1) {
            cellSelectionModel.setSelectionInterval(0, 0);
            updateColumn(0);
        }
    }
    
    public Connector getDrumConnector(int index){
        return (Connector) drumConnectors.get(index);
    }

    public void setFocusOn(Connector c) {
        for (int i = 0; i < drumConnectors.size(); i++) {
            if (drumConnectors.get(i) == c) {
                cellSelectionModel.setSelectionInterval(0, i);
                lastSelectedRow = i;
                updateColumn(i);
            }
        }
    }

    public void playNote(Connector c) {
        for (int i = 0; i < drumConnectors.size(); i++) {
            if (drumConnectors.get(i) == c) {
                midiCode = (int) model.getValueAt(lastSelectedRow, 2);
                pitch = (int) model.getValueAt(lastSelectedRow, 4);
                volume = (int) model.getValueAt(lastSelectedRow, 5);
                Note note = new Note();
                note.setVolume(volume);
                note.setPitch(pitch);
                synthesizer.setInstrument(midiCode);
                synthesizer.playNote(note);
                updateColumn(i);
            }
        }

    }

    public void setInstrument(Connector c, int midiCode) {
        for (int i = 0; i < drumConnectors.size(); i++) {
            if (drumConnectors.get(i) == c) {
                String sound = synthesizer.getInstrumentName(midiCode);
                model.setValueAt(sound, i, 1);
                model.setValueAt(midiCode, i, 2);
                setFocusOn(c);
            }
        }
    }

    private class Settings extends JPanel {

        JLabel jLabel2, jLabel3, jLabel5, jLabel6;

        JPanel jPanel1, jPanel2, jPanel3, jPanel4, jPanel5, jPanel6;

        public Settings() {
            // Volume

            jLabel1 = new JLabel(volume + " %");
            jSlider1 = new JSlider(1, 0, 100, volume);

            jSlider1.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    volume = jSlider1.getValue();
                    jLabel1.setText(volume + " %");
                    model.setValueAt(volume, lastSelectedRow, 5);
                }
            });
            jPanel1 = new JPanel(new BorderLayout());
            jPanel1.setPreferredSize(new Dimension(40, 150));
            jPanel1.add(jSlider1);
            JPanel panel = new JPanel();
            panel.add(jLabel1);
            jPanel1.add(panel, BorderLayout.SOUTH);

            // Color - Name
            label1 = new Label(1, 2, 200);
            label1.setPreferredSize(new Dimension(20, 20));
            label1.setColor(color1, true);
            label1.setOpaque(true);
            label1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("set Color rewww");

                }
            });

            // name
            jTextField1 = new JTextField(8);

            jTextField1.setEditable(false);
            jTextField1.setDocument(new PlainDocument() {
                int limit = 12;

                @Override
                public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                    if (str == null) {
                        return;
                    }

                    if ((getLength() + str.length()) <= limit) {
                        super.insertString(offset, str, attr);
                    }
                }
            });

            jTextField1.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    name = jTextField1.getText();
                    jTextField1.setEditable(false);
                    model.setValueAt(name, lastSelectedRow, 0);

                }
            });
            jTextField1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        jTextField1.setEditable(true);
                    }
                }
            });
            jTextField1.addKeyListener(new KeyAdapter() {
                String temp = jTextField1.getText();

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        name = jTextField1.getText();
                        jTextField1.setEditable(false);
                        model.setValueAt(name, lastSelectedRow, 0);
                    }
                }
            });

            jTextField1.setText("name");
            jPanel2 = new JPanel();
            jPanel2.add(label1);
            jPanel2.add(jTextField1);

            // set Octave
            jLabel3 = new JLabel("Octave");
            jSpinner1 = new JSpinner(new SpinnerNumberModel(octave, -2, 8, 1));
            jPanel3 = new JPanel();
            jPanel3.add(jLabel3);
            jPanel3.add(jSpinner1);
            jSpinner1.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    octave = (int) jSpinner1.getValue();
                    model.setValueAt(octave, lastSelectedRow, 3);

                }
            });

            // Pitch
            jLabel6 = new JLabel("Pitch");
            jSpinner3 = new JSpinner(new SpinnerNumberModel(pitch, 0, 200, 1));
            jSpinner3.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    pitch = (int) jSpinner3.getValue();
                    model.setValueAt(pitch, lastSelectedRow, 4);

                }
            });
            jPanel6 = new JPanel();
            jPanel6.add(jLabel6);
            jPanel6.add(jSpinner3);

            // midi code
            String name = synthesizer.getInstrumentName(midiCode);
            jLabel4 = new JLabel(name);
            jLabel4.setPreferredSize(new Dimension(120, 20));
            jLabel4.setToolTipText("midi code:#" + midiCode);

            jPanel4 = new JPanel();
            jPanel4.add(new JLabel("\u266B  "));
            jPanel4.add(jLabel4);

            // Pin
            jLabel5 = new JLabel("Ard.Pin");
            jSpinner2 = new JSpinner(new SpinnerNumberModel(pin, 0, 10, 1));
            jSpinner2.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    pin = (int) jSpinner2.getValue();
                    model.setValueAt(pin, lastSelectedRow, 6);
                }
            });
            jPanel5 = new JPanel();
            jPanel5.add(jLabel5);
            jPanel5.add(jSpinner2);

            // Set the position of each option
            setSize(200, 200);
            setLayout(null);
            int height = 0;
            int width = (int) jPanel1.getPreferredSize().getWidth() + 10;
            Dimension size = jPanel1.getPreferredSize();
            jPanel1.setBounds(0, height, (int) size.getWidth(), (int) size.getHeight());

            size = jPanel2.getPreferredSize();
            jPanel2.setBounds(width, 0, (int) size.getWidth(), (int) size.getHeight());
            height += (int) size.getHeight() + 10;

            size = jPanel4.getPreferredSize();
            jPanel4.setBounds(width, height, (int) size.getWidth(), (int) size.getHeight());
            height += (int) size.getHeight() + 10;

            size = jPanel6.getPreferredSize();
            jPanel6.setBounds(width, height, (int) size.getWidth(), (int) size.getHeight());
            height += (int) size.getHeight() + 10;

            size = jPanel3.getPreferredSize();
            jPanel3.setBounds(width, height, (int) size.getWidth(), (int) size.getHeight());
            height += (int) size.getHeight() + 10;
            size = jPanel5.getPreferredSize();
            jPanel5.setBounds(width, height, (int) size.getWidth(), (int) size.getHeight());

            add(jPanel1);
            add(jPanel2);
            add(jPanel3);
            add(jPanel4);
            add(jPanel5);
            add(jPanel6);
        }

    }

    public static void main(String[] args) {
        JFrame jFrame1 = new JFrame();
        jFrame1.setLayout(new BorderLayout());
        Control c = new Control();
        System.out.println(c.getPreferredSize());
        jFrame1.add(c);
        jFrame1.setSize(new Dimension(1024, 768));
        jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame1.setVisible(true);

    }
}
