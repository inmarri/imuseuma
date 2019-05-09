package org.serial;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author carmine lia
 * 
 */
public interface Controller {

    public static enum SerialName {

        COM1("COM1"), COM2("COM2"), COM3("COM3"), COM4("COM4"), TTYS0(
        "/dev/ttyS0"), TTYS1("/dev/ttyS1"), TTYUSB0("/dev/ttyUSB0"), TTYUSB1(
        "/dev/ttyUSB1");
        private final String name;

        SerialName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum Baud {

        R2400(2400), R4800(4800), R9600(9600), R19200(19200), R38400(38400), R57600(
        57600), R115200(115200);
        private final int rate; // kbps

        Baud(int rate) {
            this.rate = rate;
        }

        public int getRate() {
            return rate;
        }
    }

    void connect();

    void close();

    void send(String msg);

    InputStream getInputStream();

    OutputStream getOutputStream();

    SerialPort getSerialPort();

    String getSerialName();

    void setSerialName(String serialName);

    void setBaudRate(Integer baudRate);

    Integer getBaudRate();
}
