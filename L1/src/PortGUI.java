import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortException;

public class PortGUI {
    private TextArea area;
    private ChoiceBox baudrate;
    private ChoiceBox databits;
    private ChoiceBox stopbit;
    private ChoiceBox parity;
    private ChoiceBox port;
    private Button s, c, d;
    private Stage stage;

    SerialConnect serialPort;

    public PortGUI(Stage stage) {
        serialPort = new SerialConnect();
        this.stage = stage;
    }

    public TextArea getArea() {
        return area;
    }

    public int getBaudrate() {
        return Integer.parseInt(baudrate.getValue().toString());
    }

    public int getDatabits() {
        return Integer.parseInt(databits.getValue().toString());
    }

    public int getStopbit() {
        if (stopbit.getValue().toString() == "1.5") {
            return 3;
        }
        return Integer.parseInt(stopbit.getValue().toString());
    }

    public String getPort() {
        return port.getValue().toString();
    }

    public int getParity() {
        switch (parity.getValue().toString()) {
            case "PARITY_NONE": return 0;
            case "PARITY_ODD": return 1;
            case "PARITY_EVEN": return 2;
            case "PARITY_MARK": return 3;
            case "PARITY_SPACE": return 4;
            default: return 0;
        }

    }

    private void portParametresFormesDisable(boolean flag) {
        baudrate.setDisable(flag);
        databits.setDisable(flag);
        stopbit.setDisable(flag);
        parity.setDisable(flag);
    }

    public void setGUI(Pane pane, int y) {
        // Поле для ввода текста в первый порт
        area = new TextArea();
        area.setMinSize(590, 20);
        area.setMaxSize(590, 100);
        area.setLayoutX(5);
        area.setLayoutY(y + 30);
        pane.getChildren().add(area);

        // Подпись к выбору скорости передачи (baud rate)
        Label lB = new Label("Скорость передачи");
        lB.setLayoutX(5);
        lB.setLayoutY(y + 140);
        pane.getChildren().add(lB);

        // Подпись к выбору бит данных
        Label lD = new Label("Биты данных");
        lD.setLayoutX(150);
        lD.setLayoutY(y + 140);
        pane.getChildren().add(lD);

        // Подпись к выбору стоп-бита
        Label lS = new Label("Стоп-биты");
        lS.setLayoutX(250);
        lS.setLayoutY(y + 140);
        pane.getChildren().add(lS);

        // Подпись к выбору чётности (parity)
        Label lP = new Label("Чётность");
        lP.setLayoutX(335);
        lP.setLayoutY(y + 140);
        pane.getChildren().add(lP);

        // Выбор порта
        port = new ChoiceBox(FXCollections.observableArrayList("/dev/ttys001", "/dev/ttys002", "/dev/ttys004", "/dev/ttys005"));
        port.getSelectionModel().selectFirst();
        port.setLayoutX(5);
        port.setLayoutY(5);
        pane.getChildren().add(port);

        // Выбор скорости передачи (baud rate)
        baudrate = new ChoiceBox(FXCollections.observableArrayList("110", "300", "600", "1200", "4800", "9600", "14400",
                "19200", "38400", "57600", "115200", "128000", "256000"));
        baudrate.getSelectionModel().selectFirst();
        baudrate.setLayoutX(5);
        baudrate.setLayoutY(y + 160);
        pane.getChildren().add(baudrate);

        baudrate.setOnAction(changeParametersListener);

        // Выбор бит данных
        databits = new ChoiceBox(FXCollections.observableArrayList("5", "6", "7", "8"));
        databits.getSelectionModel().selectFirst();
        databits.setLayoutX(150);
        databits.setLayoutY(y + 160);
        pane.getChildren().add(databits);

        databits.setOnAction(changeParametersListener);

        // Выбор стоп-бита
        stopbit = new ChoiceBox(FXCollections.observableArrayList("1", "1.5", "2"));
        stopbit.getSelectionModel().selectFirst();
        stopbit.setLayoutX(250);
        stopbit.setLayoutY(y + 160);
        pane.getChildren().add(stopbit);

        stopbit.setOnAction(changeParametersListener);

        // Выбор чётности (parity)
        parity = new ChoiceBox(FXCollections.observableArrayList("PARITY_NONE", "PARITY_ODD", "PARITY_EVEN", "PARITY_MARK", "PARITY_SPACE"));
        parity.getSelectionModel().selectFirst();
        parity.setLayoutX(335);
        parity.setLayoutY(y + 160);
        pane.getChildren().add(parity);

        parity.setOnAction(changeParametersListener);

        // Кнопка, по нажатию на которую отправляются данные в другой порт
        s = new Button("Отправить");
        s.setLayoutX(480);
        s.setLayoutY(y + 140);
        s.setMinSize(115, 47);
        pane.getChildren().add(s);

        s.setOnAction(sendListener);
        s.setDisable(true);

        // Кнопка, по нажатию на которую происходит подключение к порту
        c = new Button("Установить соединение");
        c.setLayoutX(130);
        c.setLayoutY(5);
        pane.getChildren().add(c);

        c.setOnAction(connectListener);

        // Кнопка, по нажатию на которую происходит разрыв подключения к порту
        d = new Button("Разорвать соединение");
        d.setLayoutX(306);
        d.setLayoutY(5);
        pane.getChildren().add(d);

        d.setOnAction(disconnectListener);
        d.setDisable(true);

        portParametresFormesDisable(true);

        stage.setOnHiding( event -> { serialPort.close(); } );

    }

    /**
     * Отвечает за изменение параметров порта
     */
    private EventHandler<ActionEvent> changeParametersListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                serialPort.getSerialPort().setParams(getBaudrate(), getDatabits(), getStopbit(), getParity());
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Отвечает за отправку сообщения
     */
    private EventHandler<ActionEvent> sendListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                serialPort.getSerialPort().setParams(getBaudrate(), getDatabits(), getStopbit(), getParity());
                serialPort.getSerialPort().writeString(area.getText());
                area.clear();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Отвечает за подключение к порту
     */
    private EventHandler<ActionEvent> connectListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (serialPort.isPortConnected() == false) {
                try {
                    serialPort.open(port.getValue().toString(), area);

                    c.setDisable(true);
                    d.setDisable(false);
                    s.setDisable(false);
                    port.setDisable(true);

                    portParametresFormesDisable(false);
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
    private EventHandler<ActionEvent> disconnectListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (serialPort.isPortConnected() == true) {
                try {
                    serialPort.close();

                    c.setDisable(false);
                    d.setDisable(true);
                    s.setDisable(true);
                    port.setDisable(false);

                    portParametresFormesDisable(true);
                } catch(RuntimeException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удаётся разорвать соединение с портом!", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    };

}
