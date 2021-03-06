package org.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author carmine lia
 *
 */
public class Meshlium extends Observable implements Controller, SerialPortEventListener {

    private static final Logger logger = Logger.getLogger("Meshlium");
    private InputStream in;
    private OutputStream out;
    private int baud;
    private String serialName;
    private SerialPort serialPort;
    private String value;
    private StringBuilder sb = new StringBuilder();
    boolean end = false;
    private boolean isreg = false, isregnfc = false;
    private String dst = "security";

    public Meshlium() {
        this(SerialName.TTYS0, Baud.R38400);
    }

    public Meshlium(String serialName) {

        this(serialName, Baud.R38400.getRate());
    }

    public Meshlium(SerialName serialName) {

        this(serialName.getName(), Baud.R38400.getRate());
    }

    public Meshlium(SerialName serialName, Baud baud) {
        this(serialName.getName(), baud.getRate());
    }

    public Meshlium(String serialName, Integer baud) {
        this.serialName = serialName;
        this.baud = baud;
    }
    /* (non-Javadoc)
     * @see org.serial.Controller#connect()
     */

    @Override
    public void connect() {
        try {

            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialName);
            if (portIdentifier.isCurrentlyOwned()) {
                logger.log(Level.WARNING, "Port is currently in use");
            } else {
                CommPort commPort = portIdentifier.open(SerialName.TTYS0.getName(), 2000);

                if (commPort instanceof SerialPort) {
                    serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    in = serialPort.getInputStream();
                    out = serialPort.getOutputStream();

                    boolean send = false;

                    PrintStream out = null;
                    BufferedReader ins = null;
                    try {
                        Socket echoSocket = new Socket("150.214.108.58", 8080);
                        //Socket echoSocket = new Socket("10.10.10.10", 8080);
                        out = new PrintStream(echoSocket.getOutputStream());
                        ins = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                    } catch (Exception e) {
                    }

                    System.out.println("Meshlium Java Capturer");
                    sb = new StringBuilder();
                    char c;
                    while (true) {
                        try {
                            int availableBytes = in.read();
                            while (availableBytes > 0) {
                                c = (char) in.read();
                                if (c == 'x') {
                                    sb = new StringBuilder();
                                } else {
                                    sb.append(c);
                                }
                                System.out.println(sb.toString());
                                if (c == '!') { // ALT + 999
                                    //System.out.println(sb.toString());
                                    sb.deleteCharAt(sb.lastIndexOf("!"));
                                    String m = sb.toString();
                                    System.out.println(m);
                                    sb = new StringBuilder();

                                    AuroraMessage reg = AuroraMessage.AuroraMessage_Waspmote_Reg(m);
                                    AuroraMessage data = AuroraMessage.AuroraMessage_Waspmote_Data(m);
                                    //this.send("Hello from Meshlium");

                                    try {
                                        System.out.println(reg);
                                        System.out.println(data);
                                        
                                        //Send
                                        if (!isreg) {
                                            out.println(reg);
                                        }
                                        if(reg.getSender().equalsIgnoreCase("NFCSensorWaspmote") && !isregnfc){
                                            out.println(reg);
                                        }
                                        
                                        if(data.getProtocol().equalsIgnoreCase("NFCProtocol")){
                                            data.addReceiver("security");
                                            out.println(data);
                                            isregnfc = true;
                                        }

                                        if (send && !data.getProtocol().equalsIgnoreCase("NFCProtocol")) {
                                            data.addReceiver(dst);
                                            out.println(data);
                                            send = false;
                                        }


                                        System.out.println("Esperando a SOL");
                                        //Receive
                                        if (ins.ready()) {
                                            String s = ins.readLine();
                                            System.out.println("Recibido desde SOL");
                                            if (s != null) {
                                                System.out.println(s);
                                                if (s.contains("registrada correctamente")) {
                                                    System.out.println("Reg correcto o ya registrado");
                                                    isreg = true;
                                                }
                                                if (s.contains("GetSensorsRoom2")) {
                                                    send = true;
                                                    dst = new AuroraMessage(s).getSender();
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Meshlium.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    logger.log(Level.WARNING, "Only serial ports are handled by this example.");

                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            Thread.sleep(1000); // Be sure data is xferred before closing
        } catch (Exception e) {
        }
        if (out != null) {
            try {
                out.close();

            } catch (IOException e) {

                logger.log(Level.WARNING, e.getMessage());
            }
        }
        if (in != null) {
            try {
                in.close();

            } catch (IOException e) {

                logger.log(Level.WARNING, e.getMessage());
            }
        }
        if (serialPort != null) {
            try {
                serialPort.close();

            } catch (Exception e) {

                logger.log(Level.WARNING, e.getMessage());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.serial.Controller#send(java.lang.String)
     */
    @Override
    public void send(String msg) {
        try {
            byte[] message = (msg + "\r\n").getBytes();
            byte[] header = new byte[]{0x52, 1, '#', 0};
            byte[] address = {0x56, 0x78};
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            o.write(header);
            o.write(address);
            o.write(message);

            out.write(o.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(Meshlium.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*if (out != null) {

         try {
         out.write(msg.getBytes());
         // System.out.println(msg);
         } catch (IOException e) {
         logger.log(Level.WARNING, e.getMessage());
         }

         }*/
    }

    @Override
    public SerialPort getSerialPort() {
        return serialPort;
    }

    @Override
    public InputStream getInputStream() {
        return in;
    }

    @Override
    public OutputStream getOutputStream() {
        return out;
    }

    @Override
    public Integer getBaudRate() {
        return baud;
    }

    @Override
    public void setBaudRate(Integer baudRate) {
        this.baud = baudRate;
    }

    @Override
    public String getSerialName() {
        return serialName;
    }

    @Override
    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        try {
            while (in.available() > 0) {
                int input = in.read();

                // end string
                if (input == 'x') {
                    sb = new StringBuilder();
                } else {
                    sb.append((char) input);
                    if (input == '!') {
                        end = true;
                    }
                }
//
//                if ((input >= '0' && input <= '9' || input == '#') /* && start ||value >= 'A' && value <= 'Z' || value == '_'*/) {
                //sb.append((char) input);
//                }

            }
            if (end) {
                System.out.println(sb.substring(0, sb.indexOf("!")));
                System.out.println("----------");
                end = false;
            }

            byte[] message = "Hello from PC\r\n".getBytes();
            byte[] header = new byte[]{0x52, 1, '#', 0};
            byte[] address = {0x56, 0x78};
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            o.write(header);
            o.write(address);
            o.write(message);

            out.write(o.toByteArray());
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());

        }

    }

    public String getValue() {
        return value;
    }

    private void fireNotify() {
        setChanged();
        notifyObservers();
    }
}
