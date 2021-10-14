//package newpackage;
//
//import drumkit.Control;
//import drumkit.DrumTimeline;
//import drumkit.RecDrums;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.Enumeration;
//import java.util.TooManyListenersException;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import jssc.SerialPort;
//import jssc.SerialPortList;
//
//public class SerialClass {
//
//    private JLabel jLabel1;
//    private static JFrame jFrame;
//    private Control control;
//    private RecDrums recDrums;
//    private DrumTimeline drumTimeline;
//
//    public SerialPort serialPort;
//    /**
//     * The port we're normally going to use.
//     */
//    private static final String PORT_NAMES[] = {
//        "/dev/tty.usbserial-A9007UX1", // Mac OS X
//            "/dev/ttyUSB0", // Linux
//        "/dev/ttyACM0", // Linux
//        "COM4", // Windows
//    };
//
//    public static BufferedReader input;
//    public static OutputStream output;
//    /**
//     * Milliseconds to block while waiting for port open
//     */
//    public static final int TIME_OUT = 100;
//    /**
//     * Default bits per second for COM port.
//     */
//    public static final int DATA_RATE = 1024;
//
//    public SerialClass(Control control) {
//        this.control = control;
//        initialize();
//    }
//
//    public SerialClass(RecDrums recDrums) {
//        this.recDrums = recDrums;
//        initialize();
//    }
//
//    public SerialClass(DrumTimeline drumTimeline) {
//        this.drumTimeline = drumTimeline;
//        initialize();
//    }
//
//    public void initialize() {
//        SerialPortList portId = null;
//        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
//
////First, Find an instance of serial port as set in PORT_NAMES.
//        while (portEnum.hasMoreElements()) {
//            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
//            System.out.println(currPortId.getName());
//            for (String portName : PORT_NAMES) {
//                if (currPortId.getName().equals(portName)) {
//                    portId = currPortId;
//                    break;
//                }
//            }
//        }
//        if (portId == null) {
//            System.out.println("Could not find COM port.");
//            return;
//        }
//
//        try {
//            // open serial port, and use class name for the appName.
//            serialPort = (SerialPort) portId.open(this.getClass().getName(),
//                    TIME_OUT);
//
//// set port parameters
//            serialPort.setSerialPortParams(DATA_RATE,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//            setSerialEventHandler(serialPort);
//
//// open the streams
//            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
//            output = serialPort.getOutputStream();
//            char ch = 1;
//            output.write(ch);
//            // add event listeners
//            //   serialPort.addEventListener(this);
//            serialPort.notifyOnDataAvailable(true);
//        } catch (Exception e) {
//            System.err.println(e.toString());
//        }
//
//    }
//
////    public static void main(String[] args) throws Exception {
////        SerialClass main = new SerialClass();
////
////        main.initialize();
////
////        jFrame.setVisible(true);
////        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////        jFrame.pack();
////
////        System.out.println("Started");
////    }
//    private void readSerial() {
//        try {
//            int availableBytes = serialPort.getInputStream().available();
//            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
//            if (availableBytes > 0) {
//                // Read the serial port
//                String data = input.readLine();
//                int value = Integer.valueOf(data);
//                System.out.println(value);
//////                 
////                Connector perc = control.getDrumConnector(1);
////                control.setFocusOn(perc);
////                control.playNote(perc);
//
//                recDrums.hitHiHats(value);
//
//            }
//        } catch (IOException e) {
//        }
//    }
////But the real problem comes from the place where you should place the code.
////
////You can put inside the SerialPortEvent.DATA_AVAILABLE: event, like this:
//
//    private class SerialEventHandler implements SerialPortEventListener {
//
//        public void serialEvent(SerialPortEvent event) {
//            switch (event.getEventType()) {
//                case SerialPortEvent.DATA_AVAILABLE:
//                    readSerial();
//                    break;
//            }
//        }
//    }
////Don’t forget to register the event handler, before you run the application
//
//    /**
//     * Set the serial event handler
//     */
//    private void setSerialEventHandler(SerialPort serialPort) {
//        try {
//            // Add the serial port event listener
//            serialPort.addEventListener(new SerialEventHandler());
//            serialPort.notifyOnDataAvailable(true);
//        } catch (TooManyListenersException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }
//
//}
