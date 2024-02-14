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

/* a controller for songs graphics, link graphics and logics */
public class SongsController {
    
    private List<VBox> btnSongs;
    private final VBox listContainer = new VBox();
    private final FlowPane songPane;
    private final ScrollPane pane;
    private final double CONTAINER_W = 120, CONTAINER_H = 140, BTN_W = 50, BTN_H = 50,
        IMGFIT_W = 75, IMGFIT_H = 85, DEF_SPACING = 2, DEF_GAP = 15;

    /**
     * @param songContainer the pane that will graphically contain the songs
     * @param pane the scroll pane which will contain songContainer, i keep that in
     * order to resize
     */
    public SongsController(final FlowPane songContainer, final ScrollPane pane) {
        this.songPane = songContainer;
        this.songPane.setHgap(DEF_GAP);
        this.songPane.setVgap(DEF_GAP);
        songPane.setPrefWidth(pane.getWidth());
        this.pane = pane;
    }
    
    private VBox createButton(final Song song) {
        VBox container = new VBox();
        Label icon = new Label();
        ImageView img = new ImageView(ClassLoader.getSystemResource("icons/Light/Media/AudioWave.png").toString());
        Label duration = new Label(song.DurationStringRep());
        Label title = new Label(song.getName());
        container.setPrefSize(CONTAINER_W, CONTAINER_H);
        container.getStyleClass().add("songcontainer");
        VBox.setMargin(container, new Insets(10));

        // adding a Tooltip in order to make possible to reade song titles if they're too long
        Tooltip btnTooltip = new Tooltip(duration.getText()+"\n"+title.getText());
        btnTooltip.setStyle("-fx-font-size: 12pt;");
        Tooltip.install(container, btnTooltip);

        container.setSpacing(DEF_SPACING);
        container.setOnMouseClicked( event -> {
            this.btnSongs.stream()
                .forEach(button -> button.getStyleClass().removeIf(style -> style.equals("selected")));
            
            container.getStyleClass().add("selected");
            GUIController.getReader().getPlayer().setSong(song);
             //GUIController.getReader().getPlayer().setSong(song);
        });

        icon.setPrefSize(BTN_W, BTN_H);
        icon.setPadding(new Insets(5));
        icon.getStyleClass().add("songbtn");

        img.setFitHeight(IMGFIT_W);
        img.setFitWidth(IMGFIT_H);
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
    public void load(final PlayList playlist, final boolean onIcons){
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
                if(s.equals(GUIController.getReader().getPlayer().getCurrentSong())){
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
                /* tmp */
                if(s.equals(GUIController.getReader().getPlayer().getCurrentSong())){
                    var songView = createListButton(s);
                    songView.getStyleClass().add("selected");
                    return songView;
                }
                return createListButton(s);
            })
            .forEach(b -> listContainer.getChildren().add(b));
        listContainer.setSpacing(DEF_SPACING);
        listContainer.fillWidthProperty();
    }

    private Button createListButton(final Song song) {
        Button btn = new Button(String.format("%2d.\t%s\t-\t%s\t|\t%s\t|\t%s",
            Mp3PlayList.getIndexFromName(song.getFile().getName()),
            song.getName(),
            song.getArtist().isPresent() ? song.getArtist().get() : "no artist",
            song.DurationStringRep(), song.getFileFormat()));
        
            btn.setOnMouseClicked(e -> {
            listContainer.getChildren().stream()
                .forEach(button -> button.getStyleClass().removeIf(style -> style.equals("selected")));

            var button = (Button)e.getSource();
            button.getStyleClass().add("selected");
            GUIController.getReader().getPlayer().setSong(song);
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
