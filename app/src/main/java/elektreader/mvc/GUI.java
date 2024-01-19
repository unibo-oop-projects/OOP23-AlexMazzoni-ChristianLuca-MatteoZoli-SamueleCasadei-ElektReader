package elektreader.mvc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Label label = new Label("Hello, World!");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
