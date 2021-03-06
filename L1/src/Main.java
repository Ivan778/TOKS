import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setTitle("/dev/ttys00x");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 600, 203);
        primaryStage.setScene(scene);

        Pane pane = new Pane();

        PortGUI g1 = new PortGUI(primaryStage);
        g1.setGUI(pane, 10);

        root.setTop(pane);
        primaryStage.show();
    }

}
