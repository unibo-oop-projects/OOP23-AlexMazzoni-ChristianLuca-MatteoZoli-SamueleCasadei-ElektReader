package elektreader.view;

import elektreader.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage mainStage) {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/app.fxml"));
            Scene scene = new Scene(root);
            mainStage.setTitle(App.class.getPackageName());
            mainStage.setScene(scene);
            mainStage.setResizable(false);

            //mainStage.setOnCloseRequest(Event::consume);
            
            mainStage.show();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
