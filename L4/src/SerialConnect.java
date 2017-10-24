import javafx.scene.control.TextArea;
import jssc.*;

public class SerialConnect {

    private static SerialPort serialPort;
    private PortReader portReader = new PortReader();
    private boolean isOpened = false;
    private static TextArea area;

    public boolean isPortConnected() {
        return isOpened;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void open(String portName, TextArea area) {
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
                    //area.setText(serialPort.readString(event.getEventValue()));

                    StringBuilder message =  new StringBuilder(serialPort.readString(event.getEventValue()));
                    System.out.println(message.toString());

                    for (int i = 0; i < message.length(); i++) {
                        if (message.charAt(i) == '~') {
                            message.deleteCharAt(i);
                            message.deleteCharAt(i - 1);
                            i--;
                        }
                    }

                    area.setText(message.toString());

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