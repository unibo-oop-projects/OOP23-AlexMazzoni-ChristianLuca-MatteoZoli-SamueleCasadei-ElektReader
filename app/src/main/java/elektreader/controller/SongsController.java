package elektreader.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import elektreader.api.Song;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class SongsController {
    private List<VBox> btnSongs;

    public SongsController(final TilePane songsList) {
        this.btnSongs = new ArrayList<>(Collections.emptyList());
        for (var song : GUIController.getReader().getCurrentPlaylist().get().getSongs()) {
            btnSongs.add(createButton(song));
        }
        songsList.getChildren().addAll(btnSongs);
    }
    
    private VBox createButton(final Song song) {
        VBox container = new VBox();
        container.setPrefSize(100, 125);
        container.setStyle("-fx-alignment: center;");
        container.setStyle("-fx-background-color: d3d3d3;");
        Button btn = new Button();
        btn.setAlignment(Pos.CENTER);
        btn.setPrefSize(90, 80);
        btn.setStyle("-fx-background-color: transparent;");
        btn.setTextAlignment(TextAlignment.JUSTIFY); 
        ImageView img = new ImageView(ClassLoader.getSystemResource("icons/Dark/Media/AudioWave.png").toString());
        img.setFitHeight(75.0);
        img.setFitWidth(85.0);
        img.setPreserveRatio(true);
        btn.setGraphic(img);
        Label duration = new Label(String.valueOf(song.getDuration()));
        Label title = new Label(song.getName());
        container.getChildren().addAll(btn, duration, title);
        return container;
    }



    public List<VBox> getBtnSongs() {
        return this.btnSongs;
    }
}
