package Synthesizer;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class AvailableInstruments {

    public AvailableInstruments() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            instruments = synthesizer.getAvailableInstruments();
            File xmlFile = new File("src/xml/instruments.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            Node rootNode = doc.getFirstChild();
            doc.normalize();

            treeNode = getNodes(rootNode);
            jTree1 = new JTree(treeNode);

            jTree1.setDragEnabled(true);
            jTree1.setTransferHandler(new JTreeTransferHandler());

        } catch (SAXException ex) {
            Logger.getLogger(AvailableInstruments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AvailableInstruments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AvailableInstruments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(AvailableInstruments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DefaultMutableTreeNode getNodes(Node node) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node.getNodeName());
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);

            if (item.hasChildNodes()) {
                treeNode.add(getNodes(item));
            } else {
                String text = item.getTextContent();
                String[] values = text.split("\\s+");

                for (int j = 0; j < values.length; j++) {
                    int value = Integer.valueOf(values[j]);
                    if (instruments.length >= value && value >= 0) {
                        InstrumentNode tempNode;
                        tempNode = new InstrumentNode(value, instruments[value].getName());
                        treeNode.add(tempNode);
                    }
                }
            }
        }
        return treeNode;
    }

    public JTree getJTree() {
        
        return jTree1;
    }

    class JTreeTransferHandler extends TransferHandler {

        Boolean isTrue = false;
        int midiCode;

        @Override
        public int getSourceActions(JComponent comp) {
            return MOVE;
        }

        @Override
        public Transferable createTransferable(JComponent comp) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
            if (node.isLeaf()) {
                InstrumentNode inode = (InstrumentNode) node;
                midiCode = inode.getMidiCode();
                isTrue = true;
                return new StringSelection(Integer.toString(midiCode));
            }
            return null;
        }
    }

    class InstrumentNode extends DefaultMutableTreeNode {

        String name;
        int midiCode, volume, octave;

        public InstrumentNode(int midiCode, String name) {
            super(name);
            this.name = name;
            this.midiCode = midiCode;
        }

        public void setMidiCode(int midiCode) {
            this.midiCode = midiCode;
        }

        public int getMidiCode() {
            return midiCode;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public int getVolume() {
            return volume;
        }

        public void setOctave(int octave) {
            this.octave = octave;
        }

        public int getOctave() {
            return octave;
        }
    }

    private Synthesizer synthesizer;
    private Instrument[] instruments;
    private JTree jTree1;
    private DefaultMutableTreeNode treeNode;
}
