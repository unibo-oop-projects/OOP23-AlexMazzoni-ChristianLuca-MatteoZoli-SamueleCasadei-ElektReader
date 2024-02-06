package elektreader.view;

import java.awt.Toolkit;

import elektreader.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GUI extends Application {

    public static final double SCALE_SCREEN_SIZE_PERCENT = 0.8;
    public static Pair<Double, Double> scaleToScreenSize() {
        return new Pair<Double,Double>(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*SCALE_SCREEN_SIZE_PERCENT,
            Toolkit.getDefaultToolkit().getScreenSize().getHeight()*SCALE_SCREEN_SIZE_PERCENT);
    }


    @Override
    public void start(Stage mainStage) {
        try {
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("layouts/app.fxml"));
            Scene scene = new Scene(root);
            mainStage.setTitle(App.class.getPackageName());
            mainStage.setScene(scene);
            mainStage.setResizable(true);
            //mainStage.setOnCloseRequest(Event::consume);
            mainStage.show();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
