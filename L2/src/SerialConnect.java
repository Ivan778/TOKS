import Crypting.DecryptPacket;
import javafx.scene.control.TextArea;
import jssc.*;

import java.util.ArrayList;
import java.util.List;

public class SerialConnect {
    private static SerialPort serialPort;
    private PortReader portReader = new PortReader();
    private boolean isOpened = false;
    private static TextArea area;
    private static String name;

    private static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];

        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }

        return ret;
    }

    public boolean isPortConnected() {
        return isOpened;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void open(String portName, TextArea area) {
        name = portName;
        this.area = area;
        try {
            serialPort = new SerialPort(portName);
            serialPort.openPort();
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(portReader, SerialPort.MASK_RXCHAR);
            isOpened = true;
        } catch (SerialPortException ex) {
            System.out.println(ex);
            throw new RuntimeException("Can't open port");
        }
    }

    public void close() {
        if (isOpened) {
            try {
                serialPort.closePort();
                isOpened = false;
            } catch (Exception ex) {
                System.out.println(ex);
                throw new RuntimeException("Can't close port");
            }
        }
    }

    private static class PortReader implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    byte[] packet = serialPort.readBytes(event.getEventValue());

                    boolean flag = false;
                    List<Byte> temp = new ArrayList<Byte>();

                    System.out.println(packet.length);

                    System.out.print(packet[0]);
                    for (int i = 1; i < packet.length; i++) {
                        if (packet[i] == 126 && flag == false) {
                            System.out.println();
                            System.out.print(packet[i]);
                            continue;
                        }
                        System.out.print(packet[i]);
                    }

                    temp.add((byte) 126);
                    for (int i = 1; i < packet.length; i++) {
                        if (packet[i] == 126) {
                            DecryptPacket d = new DecryptPacket(toByteArray(temp));
                            if (d.decryptHeader().get(0).equals(name)) {
                                area.appendText(d.decryptPayload());
                            }
                            temp.clear();
                        }
                        temp.add(packet[i]);
                    }

                    if (temp.size() > 0) {
                        DecryptPacket d = new DecryptPacket(toByteArray(temp));
                        if (d.decryptHeader().get(0).equals(name)) {
                            area.appendText(d.decryptPayload());
                        }
                        temp.clear();
                    }

                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    protected void finalize() {
        close();
    }

}