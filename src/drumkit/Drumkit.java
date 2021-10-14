package drumkit;

import Synthesizer.SynthA;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class Drumkit extends JLabel {

    private ArrayList percussions;
    private SynthA synthesizer;
    private Control control;
    private Object data[][];

    public Drumkit() {
        data = loadDataFromXml("src/xml/drumkits.xml");
        setLayout(null);
        percussions = new ArrayList<>();
        setKit();
        setPreferredSize(new Dimension(600, 600));
    }

    public Object[][] loadDataFromXml(String fileName) {
        try {

            File xmlFile = new File(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.normalize();
            Element root = doc.getDocumentElement();

            NodeList elements = root.getElementsByTagName("percussion");
            int size = elements.getLength();
            Object data[][] = new Object[size][10];

            for (int i = 0; i < size; i++) {
                Element item = (Element) elements.item(i);
                String type = item.getAttribute("type");
                String name = item.getAttribute("name");
                int midiCode = Integer.valueOf(item.getAttribute("midiCode"));
                int pitch = Integer.valueOf(item.getAttribute("pitch"));
                int volume = Integer.valueOf(item.getAttribute("volume"));
                int pin = Integer.valueOf(item.getAttribute("pin"));
                int radius = Integer.valueOf(item.getAttribute("radius"));
                int xloc = Integer.valueOf(item.getAttribute("xloc"));
                int yloc = Integer.valueOf(item.getAttribute("yloc"));

                String rgb = item.getAttribute("color");
                String[] rgbArray = rgb.split(" ");
                int r = Integer.valueOf(rgbArray[0]);
                int g = Integer.valueOf(rgbArray[1]);
                int b = Integer.valueOf(rgbArray[2]);
                Color color = new Color(r, g, b);

                data[i][0] = type;
                data[i][1] = name;
                data[i][2] = midiCode;
                data[i][3] = pitch;
                data[i][4] = volume;
                data[i][5] = pin;
                data[i][6] = color;
                data[i][7] = radius;
                data[i][8] = xloc;
                data[i][9] = yloc;
            }
            return data;

            //String name, int midiCode, int pitch, int volume, int pin) {
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Drumkit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Drumkit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Drumkit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setKit() {

        control = new Control();
        for (int i = 0; i < data.length; i++) {
            
            
            String type = (String) data[i][0];
            String name = (String) data[i][1];
            int midiCode = (int) (data[i][2]);
            int pitch = (int) (data[i][3]);
            int volume = (int) (data[i][4]);
            int pin = (int) (data[i][5]);
            Color color = (Color) (data[i][6]);
            int radius = (int) (data[i][7]);
            int xloc = (int) (data[i][8]);
            int yloc = (int) (data[i][9]);
            
            Percussion perc = new Percussion(control, radius);
            perc.setColor(color);
            
            control.addConnector(perc, name, midiCode, pitch, volume, pin);
           
            
            Dimension size = perc.getPreferredSize();
            perc.setBounds(xloc, yloc, size.width, size.height);
            
            add(perc);
            addPercussion(perc);

        }
    }

    public JPanel getContolPanel() {
        return control;
    }

    public Percussion getPercussion(int index) {
        return (Percussion) percussions.get(index);
    }

    public ArrayList getPercussions() {
        return percussions;
    }

    public void addPercussion(Percussion perc) {
        percussions.add(perc);
    }

    public void removePercussion(Percussion perc) {
        percussions.remove(perc);
    }

}
