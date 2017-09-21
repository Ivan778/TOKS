import COMPortController.COMPortController;
import GUI.PortGUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import sun.awt.CharsetString;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setTitle("/dev/ttys001 <--> /dev/ttys002");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);

        Pane pane = new Pane();

        PortGUI g1 = new PortGUI("/dev/ttys001");
        g1.setGUI(pane, 0);

        PortGUI g2 = new PortGUI("/dev/ttys002");
        g2.setGUI(pane, 195);

        root.setTop(pane);
        primaryStage.show();
    }


}
