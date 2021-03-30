import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class Main extends Application {

     public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Signal Flow Graph Calculator    ");
       stage.show();
    }
}
