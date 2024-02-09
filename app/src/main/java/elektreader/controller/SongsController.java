package elektreader.controller;


import java.util.List;
import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class SongsController {
    
    private List<VBox> btnSongs;
    private final FlowPane songPane;
    private final ScrollPane pane;
    private final double CONTAINER_W = 100, CONTAINER_H = 125, BTN_W = 90, BTN_H = 80,
        IMGFIT_W = 75, IMGFIT_H = 85, DEF_SPACING = 5;

    public SongsController(final FlowPane songContainer, final ScrollPane pane) {
        this.songPane = songContainer;
        this.pane = pane;
    }
    
    private VBox createButton(final Song song) {
        VBox container = new VBox();
        Button btn = new Button();
        ImageView img = new ImageView(ClassLoader.getSystemResource("icons/Dark/Media/AudioWave.png").toString());
        Label duration = new Label(song.DurationStringRep());
        Label title = new Label(song.getName());
        System.out.println(song.getName());
        container.setSpacing(DEF_SPACING);
        container.setMinSize(CONTAINER_W, CONTAINER_H);
        container.getStyleClass().add("songcontainer");

        btn.setPrefSize(BTN_W, BTN_H);
        btn.getStyleClass().add("songbtn");

        img.setFitHeight(IMGFIT_W);
        img.setFitWidth(IMGFIT_H);
        img.setPreserveRatio(true);
        btn.setGraphic(img);

        duration.getStyleClass().add("songduration");
        title.getStyleClass().add("songtitle");

        container.getChildren().addAll(btn, duration, title);
        return container;
    }

    public void load(PlayList playlist){
        songPane.getChildren().clear();
        this.btnSongs = playlist.getSongs().stream()
            .map(s -> createButton(s))
            .toList();
        songPane.getChildren().addAll(btnSongs);
    }

    public void responsive(){
        songPane.setPrefSize(pane.getWidth(), pane.getHeight());
    }
}
