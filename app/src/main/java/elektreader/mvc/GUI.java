package elektreader.mvc;

import elektreader.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/app.fxml"));
            Scene scene = new Scene(root, 800, 400);
            stage.setTitle(App.class.getPackageName());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
