package elektreader.controller;

import java.util.Optional;
import java.util.function.Predicate;

import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * this class is responsable to create a panel for search and, 
 * manipulate songs and playlist used by GUIController can update,
 * the GUIController reader.
 */
public final class FindController {

    /**
     * padding value for parent containers.
     */
    private static final Double PADDING = 20.0; 

    /**
     * padding value for label searched songs and playlists.
     */
    private static final Double LABEL_PADDING = 60.0; 

    /**
     * spacing value for label searched songs and playlists.
     */
    private static final Double SPACING = 15.0; 

    /**
     * container that contains all the result of the find query.
     */
    private VBox container;

    /**
     * @param findPane parent panels
     * show all the query need for search.
     */
    public void show(final Pane findPane) {
        final AnchorPane mainContainer = new AnchorPane();
        mainContainer.setPrefWidth(findPane.getWidth());
        final VBox centerContainer = new VBox();
        centerContainer.getStyleClass().add("root");
        mainContainer.getChildren().add(centerContainer);
        AnchorPane.setTopAnchor(centerContainer, PADDING);
        AnchorPane.setLeftAnchor(centerContainer, PADDING);
        AnchorPane.setRightAnchor(centerContainer, PADDING);
        AnchorPane.setBottomAnchor(centerContainer, PADDING);
        
        final AnchorPane anchorInputContainer = new AnchorPane();
        final TextField input = new TextField();
        input.setPromptText("Find...");
        anchorInputContainer.getChildren().add(input);
        AnchorPane.setTopAnchor(input, PADDING);
        AnchorPane.setLeftAnchor(input, PADDING);
        AnchorPane.setRightAnchor(input, PADDING);

        final AnchorPane anchorLabelContainer = new AnchorPane();
        this.container = new VBox();
        this.container.setSpacing(SPACING);
        anchorLabelContainer.getChildren().add(this.container);
        AnchorPane.setTopAnchor(this.container, PADDING);
        AnchorPane.setLeftAnchor(this.container, LABEL_PADDING);
        AnchorPane.setRightAnchor(this.container, LABEL_PADDING);
        AnchorPane.setBottomAnchor(this.container, PADDING);

        centerContainer.getChildren().addAll(anchorInputContainer, anchorLabelContainer);

        findPane.getChildren().add(centerContainer);

        input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                container.getChildren().clear();

                if (newValue.matches("^\\d+:\\d+(?::\\d+)?$")) { // ricerca per durata
                    find(t -> t.getTotalDuration().equals(newValue), s -> s.durationStringRep().equals(newValue));
                } else if (newValue.matches("^\\d+$")) { // ricerca per indice
                    find(t -> Integer.parseInt(newValue) == t.getSize(), t -> {
                        final var index = Song.getIndexFromName(t.getFile().getName());
                        if (index.isPresent()) {
                            return Integer.parseInt(newValue) == index.get();
                        } 
                        return index.isPresent();
                    });
                } else {    // ricerca per stringa generica (titolo)
                    find(t -> t.getName().startsWith(newValue), t -> t.getName().startsWith(newValue));
                }
            } else {
                container.getChildren().clear();
            }
        });
    }

    private void find(final Predicate<? super PlayList> playlistPredicate, final Predicate<? super Song> songPredicate) {
        container.getChildren().addAll(GUIController.READER.getPlaylists().stream()
            .filter(t -> playlistPredicate.test(t))
            .map(t -> createPlaylistBox(t))
            .toList()
        );

        container.getChildren().addAll(GUIController.READER.getPlaylists().stream()
            .flatMap(t -> t.getSongs().stream())
            .filter(t -> songPredicate.test(t))
            .map(t -> createSongBox(t))
            .toList()
        );
    }

    private HBox createSongBox(final Song song) {
        final HBox box = new HBox();
        final Label type = new Label("S");
        final Label name = new Label(song.getName() + "\t-\t"
            + GUIController.READER.getPlaylists().stream()
                .filter(t -> t.getSongs().stream().anyMatch(s -> s.equals(song)))
                .findFirst().get().getName());
        box.getChildren().addAll(type, name);
        box.setOnMouseClicked(event -> {
            GUIController.READER.setCurrentPlaylist(
                GUIController.READER.getPlaylists().stream()
                    .filter(t -> t.getSongs().stream().anyMatch(s -> s.equals(song)))
                    .findAny()
            );
            GUIController.READER.getPlayer().setSong(song);
        });
        return box;
    }

    private HBox createPlaylistBox(final PlayList playlist) {
        final HBox box = new HBox();
        final Label type = new Label("P");
        final Label name = new Label(playlist.getName());
        box.getChildren().addAll(type, name);
        box.setOnMouseClicked(event -> {
            GUIController.READER.setCurrentPlaylist(Optional.of(playlist));
        });
        return box;
    }
}
