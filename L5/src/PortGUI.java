import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.time.Instant;
import java.util.Random;

public class PortGUI {
    private TextArea area;
    private Button connect1, disconnect1, send, connect2, disconnect2;
    private Stage stage;

    private Label inLabel, outLabel;

    private TextField portIn;
    private TextField portOut;

    SerialConnect serialPort1, serialPort2;

    public PortGUI(Stage stage) {
        serialPort1 = new SerialConnect();
        serialPort2 = new SerialConnect();
        this.stage = stage;
    }

    public String getPortIn() {
        return portIn.getText().toString();
    }

    public String getPortOut() {
        return portOut.getText().toString();
    }

    public void setGUI(Pane pane, int y) {
        // Поле для ввода текста в первый порт
        area = new TextArea();
        area.setMinSize(590, 20);
        area.setMaxSize(590, 100);
        area.setLayoutX(5);
        area.setLayoutY(y + 30);
        pane.getChildren().add(area);

        // Выбор порта для приёма данных
        portIn = new TextField();
        portIn.setLayoutX(5);
        portIn.setLayoutY(5);
        portIn.setMaxWidth(120);
        pane.getChildren().add(portIn);

        inLabel = new Label("Сюда");
        inLabel.setLayoutX(500);
        inLabel.setLayoutY(10);
        pane.getChildren().add(inLabel);

        // Кнопка, по нажатию на которую происходит подключение к порту
        connect1 = new Button("Установить соединение");
        connect1.setLayoutX(130);
        connect1.setLayoutY(5);
        pane.getChildren().add(connect1);

        connect1.setOnAction(connectListener1);

        // Кнопка, по нажатию на которую происходит разрыв подключения к порту
        disconnect1 = new Button("Разорвать соединение");
        disconnect1.setLayoutX(306);
        disconnect1.setLayoutY(5);
        pane.getChildren().add(disconnect1);

        disconnect1.setOnAction(disconnectListener1);
        disconnect1.setDisable(true);




        // Выбор порта для приёма данных
        portOut = new TextField();
        portOut.setLayoutX(5);
        portOut.setLayoutY(5 + 35);
        portOut.setMaxWidth(120);
        pane.getChildren().add(portOut);

        outLabel = new Label("Отсюда");
        outLabel.setLayoutX(500);
        outLabel.setLayoutY(10 + 35);
        pane.getChildren().add(outLabel);

        // Кнопка, по нажатию на которую отправляются данные в другой порт
        send = new Button("Отправить");
        send.setLayoutX(480);
        send.setLayoutY(y + 140);
        send.setMinSize(115, 47);
        pane.getChildren().add(send);

        send.setOnAction(sendListener2);
        send.setDisable(true);

        // Кнопка, по нажатию на которую происходит подключение к порту
        connect2 = new Button("Установить соединение");
        connect2.setLayoutX(130);
        connect2.setLayoutY(5 + 35);
        pane.getChildren().add(connect2);

        connect2.setOnAction(connectListener2);

        // Кнопка, по нажатию на которую происходит разрыв подключения к порту
        disconnect2 = new Button("Разорвать соединение");
        disconnect2.setLayoutX(306);
        disconnect2.setLayoutY(5 + 35);
        pane.getChildren().add(disconnect2);

        disconnect2.setOnAction(disconnectListener2);
        disconnect2.setDisable(true);


        stage.setOnHiding( event -> { serialPort1.close(); serialPort2.close(); } );

    }

    /**
     * Отвечает за подключение к порту
     */
    private EventHandler<ActionEvent> connectListener1 = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (serialPort1.isPortConnected() == false) {
                try {
                    serialPort1.open(getPortIn(), area);

                    connect1.setDisable(true);
                    disconnect1.setDisable(false);
                    portIn.setDisable(true);

                } catch(RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удаётся подсоединиться к порту!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    };

    /**
     * Отвечает за разрыв соединения с портом
     */
    private EventHandler<ActionEvent> disconnectListener1 = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (serialPort1.isPortConnected() == true) {
                try {
                    serialPort1.close();

                    connect1.setDisable(false);
                    disconnect1.setDisable(true);
                    portIn.setDisable(false);

                } catch(RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удаётся разорвать соединение с портом!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    };


    /**
     * Отвечает за отправку сообщения
     */
    private EventHandler<ActionEvent> sendListener2 = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                serialPort2.getSerialPort().setParams(9600, 5, 1, SerialPort.PARITY_NONE);
                serialPort2.getSerialPort().writeString(area.getText());

                area.clear();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Отвечает за подключение к порту
     */
    private EventHandler<ActionEvent> connectListener2 = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (serialPort2.isPortConnected() == false) {
                try {
                    serialPort2.open(getPortOut(), area);

                    connect2.setDisable(true);
                    disconnect2.setDisable(false);
                    send.setDisable(false);
                    portOut.setDisable(true);

                } catch(RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удаётся подсоединиться к порту!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    };

    /**
     * Отвечает за разрыв соединения с портом
     */
    private EventHandler<ActionEvent> disconnectListener2 = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (serialPort2.isPortConnected() == true) {
                try {
                    serialPort2.close();

                    connect2.setDisable(false);
                    disconnect2.setDisable(true);
                    send.setDisable(true);
                    portOut.setDisable(false);

                } catch(RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удаётся разорвать соединение с портом!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    };
}
