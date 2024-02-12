package elektreader.controller;


import java.util.List;
import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/* a controller for songs graphics, link graphics and logics */
public class SongsController {
    
    private final MediaControlsController mediaControl;
    private List<VBox> btnSongs;
    private final VBox listContainer = new VBox();
    private final FlowPane songPane;
    private final ScrollPane pane;
    private final double CONTAINER_W = 120, CONTAINER_H = 140, BTN_W = 90, BTN_H = 80,
        IMGFIT_W = 75, IMGFIT_H = 85, DEF_SPACING = 2;

    /**
     * @param songContainer the pane that will graphically contain the songs
     * @param pane the scroll pane which will contain songContainer, i keep that in
     * order to resize
     */
    public SongsController(final FlowPane songContainer, final ScrollPane pane, final MediaControlsController mediaControl) {
        this.songPane = songContainer;
        songPane.setPrefWidth(pane.getWidth());
        this.pane = pane;
        this.mediaControl = mediaControl;
    }
    
    private VBox createButton(final Song song) {
        VBox container = new VBox();
        Button btn = new Button();
        ImageView img = new ImageView(ClassLoader.getSystemResource("icons/Light/Media/AudioWave.png").toString());
        Label duration = new Label(song.DurationStringRep());
        Label title = new Label(song.getName());

        container.setSpacing(DEF_SPACING);
        container.setPrefSize(CONTAINER_W, CONTAINER_H);
        container.getStyleClass().add("songcontainer");
        VBox.setMargin(container, new Insets(10));

        // adding a Tooltip in order to make possible to reade song titles if they're too long
        Tooltip btnTooltip = new Tooltip(duration.getText()+"\n"+title.getText());
        btnTooltip.setStyle("-fx-font-size: 12pt;");
        Tooltip.install(container, btnTooltip);

        container.setSpacing(DEF_SPACING);
        btn.setOnMouseClicked( event -> {
            this.btnSongs.stream()
                .forEach(button -> button.getStyleClass().removeIf(style -> style.equals("selected")));
            
            container.getStyleClass().add("selected");
            //this.mediaControl.loadSong(song);
            GUIController.getReader().getPlayer().setSong(song);
        });

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

    /**
     * @param playlist the playlist to be graphically loaded
     * @param onIcons a flag to know if the songs will be loaded as icons,
     * or as list
     */
    public void load(PlayList playlist, boolean onIcons){
        if (onIcons) {
            loadIcons(playlist);
        } else {
            loadList(playlist);
        }
    }

    private void loadIcons(PlayList playlist) {
        songPane.getChildren().clear();
        this.btnSongs = playlist.getSongs().stream()
            .map(s -> createButton(s))
            .toList();
        songPane.getChildren().addAll(btnSongs);
    }

    private void loadList(PlayList playList) {
        songPane.getChildren().clear();
        songPane.getChildren().add(listContainer);
        playList.getSongs().stream()
            .map(s -> createListButton(s))
            .forEach(b -> listContainer.getChildren().add(b));
        listContainer.setSpacing(2);
        listContainer.fillWidthProperty();
    }

    private Button createListButton(Song song) {
        Button btn = new Button(song.getName() 
            + "   |   " + (song.getArtist().isPresent() ? song.getArtist().get() : "Artist not found") 
            + "   |   " + song.DurationStringRep());
        btn.setOnMouseClicked(e -> {
            listContainer.getChildren().stream()
                .forEach(button -> button.getStyleClass().removeIf(style -> style.equals("selected")));

            var button = (Button)e.getSource();
            button.getStyleClass().add("selected");
            var player = GUIController.getReader().getPlayer();
            player.setSong(song);
            player.play();
        });
        return btn;
    }

    /**
     * this method adjusts the song pane to the size of its container 
     */
    public void responsive(){
        songPane.setPrefWidth(pane.getWidth());
        listContainer.setPrefWidth(songPane.getPrefWidth());
        listContainer.getChildren().stream().forEach(b -> b.prefWidth(listContainer.getPrefWidth()));
    }

}
