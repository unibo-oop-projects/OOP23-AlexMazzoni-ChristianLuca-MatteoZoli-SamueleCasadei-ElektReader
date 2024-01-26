package elektreader.mvc;

import java.io.IOException;

import elektreader.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //VBox root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/app.fxml"));
        //Scene scene = new Scene(root, 500, 250);
        stage.setTitle(App.class.getPackageName());
        //stage.setScene(scene);
        stage.show();
    }
}
