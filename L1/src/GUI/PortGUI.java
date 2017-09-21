package GUI;

import COMPortController.COMPortController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import jssc.SerialPort;

public class PortGUI {
    private String portName;

    public TextArea tF = new TextArea();;
    private ChoiceBox baudrate;
    private ChoiceBox databits;
    private ChoiceBox stopbit;
    private ChoiceBox parity;

    private COMPortController portController;

    public PortGUI(String portName) {
        this.portName = portName;
        portController = new COMPortController(tF);
        portController.openPort(portName);
        portController.setParams(110, 5, 1, 0);
    }

    public TextArea gettF() {
        return tF;
    }

    public void setGUI(Pane pane, int y) {
        // Поле для ввода текста в первый порт
        tF.setMinSize(590, 20);
        tF.setMaxSize(590, 100);
        tF.setLayoutX(5);
        tF.setLayoutY(y + 30);
        pane.getChildren().add(tF);

        // Подпись первого порта
        Label p = new Label(portName);
        p.setLayoutX(5);
        p.setLayoutY(y + 6);
        pane.getChildren().add(p);

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

        // Выбор скорости передачи (baud rate)
        baudrate = new ChoiceBox(FXCollections.observableArrayList("110", "300", "600", "1200", "4800", "9600", "14400",
                "19200", "38400", "57600", "115200", "128000", "256000"));
        baudrate.getSelectionModel().selectFirst();
        baudrate.setLayoutX(5);
        baudrate.setLayoutY(y + 160);
        pane.getChildren().add(baudrate);

        // Выбор бит данных
        databits = new ChoiceBox(FXCollections.observableArrayList("5", "6", "7", "8"));
        databits.getSelectionModel().selectFirst();
        databits.setLayoutX(150);
        databits.setLayoutY(y + 160);
        pane.getChildren().add(databits);

        // Выбор стоп-бита
        stopbit = new ChoiceBox(FXCollections.observableArrayList("1", "1.5", "2"));
        stopbit.getSelectionModel().selectFirst();
        stopbit.setLayoutX(250);
        stopbit.setLayoutY(y + 160);
        pane.getChildren().add(stopbit);

        // Выбор чётности (parity)
        parity = new ChoiceBox(FXCollections.observableArrayList("PARITY_NONE", "PARITY_ODD", "PARITY_EVEN", "PARITY_MARK", "PARITY_SPACE"));
        parity.getSelectionModel().selectFirst();
        parity.setLayoutX(335);
        parity.setLayoutY(y + 160);
        pane.getChildren().add(parity);

        Button s = new Button("Отправить");
        s.setLayoutX(480);
        s.setLayoutY(y + 140);
        s.setMinSize(115, 47);
        pane.getChildren().add(s);

        s.setOnAction(buttonListener);

    }

    /**
     * Обрабатывает нажатия на кнопку отправить: посылает данные в порт если это возможно
     */
    private EventHandler<ActionEvent> buttonListener = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            int b = Integer.parseInt(baudrate.getValue().toString());
            int d = Integer.parseInt(databits.getValue().toString());
            int s = Integer.parseInt(stopbit.getValue().toString());
            int p = 1;

            switch (parity.getValue().toString()) {
                case "PARITY_NONE": p = 0; break;
                case "PARITY_ODD": p = 1; break;
                case "PARITY_EVEN": p = 2; break;
                case "PARITY_MARK": p = 3; break;
                case "PARITY_SPACE": p = 4; break;
            }

            portController.setParams(b, d, s, p);
            portController.writeToPort(tF.getText());
            tF.clear();
        }
    };

}
