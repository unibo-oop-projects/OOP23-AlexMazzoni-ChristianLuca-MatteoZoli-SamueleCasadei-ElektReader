package elektreader.controller;


import java.util.List;
import elektreader.api.PlayList;
import elektreader.api.Song;
import elektreader.model.Mp3PlayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * a controller for songs graphics, link graphics and logics.
 */
public class SongsController {

    private List<VBox> btnSongs;
    private final VBox listContainer = new VBox();
    private final FlowPane songPane;
    private final ScrollPane pane;
    private final double containerW = 120, containerH = 140, btnW = 50, btnH = 50,
        imgfitW = 50, imgfitH = 56, defSpace = 2, defGap = 15, insets = 5, margin = 10;

    /**
     * @param songContainer the pane that will graphically contain the songs
     * @param pane the scroll pane which will contain songContainer, i keep that in
     * order to resize
     */
    public SongsController(final FlowPane songContainer, final ScrollPane pane) {
        this.songPane = songContainer;
        this.songPane.setHgap(defGap);
        this.songPane.setVgap(defGap);
        songPane.setPrefWidth(pane.getWidth());
        this.pane = pane;
    }
 
    private VBox createButton(final Song song) {
        VBox container = new VBox();
        Label icon = new Label();
        ImageView img = new ImageView(ClassLoader.getSystemResource("icons/Light/Media/AudioWave.png").toString());
        Label duration = new Label(song.durationStringRep());
        Label title = new Label(song.getName());
        container.setPrefSize(containerW, containerH);
        container.getStyleClass().add("songcontainer");
        VBox.setMargin(container, new Insets(margin));
        container.setPadding(new Insets(insets));

        // adding a Tooltip in order to make possible to reade song titles if they're too long
        Tooltip btnTooltip = new Tooltip(duration.getText() + "\n" + title.getText());
        btnTooltip.setStyle("-fx-font-size: 12pt;");
        Tooltip.install(container, btnTooltip);

        container.setSpacing(defSpace);
        container.setOnMouseClicked(event -> {
            this.btnSongs.stream()
                .forEach(button -> button.getStyleClass().removeIf(style -> style.equals("selected")));
            container.getStyleClass().add("selected");
            GUIController.getReader().getPlayer().setSong(song);
             //GUIController.getReader().getPlayer().setSong(song);
        });

        icon.setPrefSize(btnW, btnH);
        icon.setPadding(new Insets(insets));
        icon.getStyleClass().add("songbtn");

        img.setFitHeight(imgfitH);
        img.setFitWidth(imgfitW);
        img.setPreserveRatio(true);
        icon.setGraphic(img);

        duration.getStyleClass().add("songduration");
        title.getStyleClass().add("songtitle");

        container.getChildren().addAll(icon, duration, title);
        return container;
    }

    /**
     * @param playlist the playlist to be graphically loaded
     * @param onIcons a flag to know if the songs will be loaded as icons,
     * or as list
     */
    public void load(final PlayList playlist, final boolean onIcons) {
        if (onIcons) {
            loadIcons(playlist);
        } else {
            loadList(playlist);
        }
    }

    private void loadIcons(final PlayList playlist) {
            songPane.getChildren().clear();
            this.btnSongs = playlist.getSongs().stream()
                .map(s -> {
                    if (s.equals(GUIController.getReader().getPlayer().getCurrentSong())) {
                        var songView = createButton(s);
                        songView.getStyleClass().add("selected");
                        return songView;
                    }
                    return createButton(s);
                })
                .toList();
            songPane.getChildren().addAll(btnSongs);
    }

    private void loadList(final PlayList playList) {
            songPane.getChildren().clear();
            listContainer.getChildren().clear();
            songPane.getChildren().add(listContainer);
            playList.getSongs().stream()
                .map(s -> {
                    if (s.equals(GUIController.getReader().getPlayer().getCurrentSong())) {
                        var songView = createListButton(s);
                        songView.getStyleClass().add("selected");
                        return songView;
                    }
                    return createListButton(s);
                })
                .forEach(b -> listContainer.getChildren().add(b));
            listContainer.setSpacing(defSpace);
            listContainer.fillWidthProperty();
    }

    private Button createListButton(final Song song) {
        Button btn = new Button(String.format("%2s.\t%s\t-\t%s\t|\t%s\t|\t%s",
            Mp3PlayList.getIndexFromName(song.getFile().getName()).isPresent() 
                ? Mp3PlayList.getIndexFromName(song.getFile().getName()).get().toString()
                :   "  ",
            song.getName(),
            song.getArtist().isPresent() ? song.getArtist().get() : "no artist",
            song.durationStringRep(), song.getFileFormat()));

            btn.setOnMouseClicked(e -> {
            listContainer.getChildren().stream()
                .forEach(button -> button.getStyleClass().removeIf(style -> style.equals("selected")));

            var button = (Button) e.getSource();
            button.getStyleClass().add("selected");
            GUIController.getReader().getPlayer().setSong(song);
        });
        return btn;
    }

    /**
     * this method adjusts the song pane to the size of its container.
     */
    public void responsive() {
        songPane.setPrefWidth(pane.getWidth());
        listContainer.setPrefWidth(songPane.getPrefWidth());
        listContainer.getChildren().stream().forEach(b -> b.prefWidth(listContainer.getPrefWidth()));
    }
}
