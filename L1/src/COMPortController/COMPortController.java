package COMPortController;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javafx.scene.control.TextArea;

public class COMPortController {
    private static SerialPort port;
    private static PortListener listener = new PortListener();
    private static boolean isOpened = false;
    private static TextArea textArea;
    private static String name;


    public COMPortController(TextArea textArea) {
        this.textArea = textArea;
        //System.out.println(textArea);
    }


    /**
     * Открывает порт
     * @param name имя порта
     */
    public void openPort(String name) {
        this.name = name;
        try {
            // Открываем порт
            port = new SerialPort(name);
            port.openPort();
            port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            port.addEventListener(listener, SerialPort.MASK_RXCHAR);

            isOpened = true;
        }
        catch (SerialPortException e) {
            System.out.println(e);
            throw new RuntimeException("Нельзя открыть порт!!!");
        }

    }

    /**
     * Устанавливает параметры порта
     * @param baudrate скорость передачи
     * @param databits биты данных
     * @param stopBits стоп-бит
     * @param parity чётность
     */
    public void setParams(Integer baudrate, Integer databits, Integer stopBits, Integer parity) {
        try {
            port.setParams(baudrate, databits, stopBits, parity);
        } catch (SerialPortException e) {
            System.out.println(e);
            throw new RuntimeException("Проблемы с установкой параметров порта!");
        }
    }

    /**
     * Записывает в порт строку
     * @param data строка, которыю нужно записать в порт
     */
    public void writeToPort(String data) {
        try {
            port.writeString(data);
        } catch (Exception ex) {
            System.out.println(ex);
            throw new RuntimeException("Невозможно писать в порт!!!");
        }
    }

    /**
     * Закрывает порт, если это возможно
     */
    public void close() {
        if (isOpened) {
            try {
                port.closePort();
                isOpened = false;
            } catch (Exception ex) {
                System.out.println(ex);
                throw new RuntimeException("Нельзя закрыть порт!!!");
            }
        }
    }

    private static class PortListener implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) try {
                textArea.setText(port.readString());
                System.out.println(name);
            } catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        port.closePort();
    }
}
