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

public class FindController {
    
    private VBox container;

    public FindController() {}

    public void show(final Pane findPane) {
        AnchorPane mainContainer = new AnchorPane();
        mainContainer.setPrefWidth(findPane.getWidth());
        VBox centerContainer = new VBox();
        centerContainer.getStyleClass().add("root");
        mainContainer.getChildren().add(centerContainer);
        AnchorPane.setTopAnchor(centerContainer, (double) 20);
        AnchorPane.setLeftAnchor(centerContainer, (double) 20);
        AnchorPane.setRightAnchor(centerContainer, (double) 20);
        AnchorPane.setBottomAnchor(centerContainer, (double) 20);
        
        AnchorPane anchorInputContainer = new AnchorPane();
        TextField input = new TextField();
        input.setPromptText("Find...");
        anchorInputContainer.getChildren().add(input);
        AnchorPane.setTopAnchor(input, (double) 20);
        AnchorPane.setLeftAnchor(input, (double) 20);
        AnchorPane.setRightAnchor(input, (double) 20);

        AnchorPane anchorLabelContainer = new AnchorPane();
        this.container = new VBox();
        this.container.setSpacing(15);
        anchorLabelContainer.getChildren().add(this.container);
        AnchorPane.setTopAnchor(this.container, (double) 20);
        AnchorPane.setLeftAnchor(this.container, (double) 60);
        AnchorPane.setRightAnchor(this.container, (double) 60);
        AnchorPane.setBottomAnchor(this.container, (double) 20);

        centerContainer.getChildren().addAll(anchorInputContainer, anchorLabelContainer);

        findPane.getChildren().add(centerContainer);

        input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue!=null && newValue!=null) {
                container.getChildren().clear();

                if (newValue.toString().matches("^\\d+:\\d+(?::\\d+)?$")) { // ricerca per durata
                    find(t -> t.getTotalDuration().equals(newValue.toString()), s -> s.durationStringRep().equals(newValue.toString()));
                } else if (newValue.toString().matches("^\\d+$")) { // ricerca per indice
                    find(t -> false, t -> {
                        var index = Song.getIndexFromName(t.getFile().getName());
                        if (index.isPresent()) {
                            return String.valueOf(index.get()).equals(newValue.toString());
                        } 
                        return index.isPresent();
                    });
                } else {    // ricerca per stringa generica (titolo)
                    find(t -> t.getName().startsWith(newValue.toString()), t -> t.getName().startsWith(newValue.toString()));
                }
            } else {
                container.getChildren().clear();
            }
        });
    }

    private void find(Predicate<? super PlayList> playlistPredicate, Predicate<? super Song> songPredicate) {
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
        Label type = new Label("S");
        Label name = new Label(song.getName() + "" + 
            GUIController.READER.getPlaylists().stream()
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
        Label type = new Label("P");
        Label name = new Label(playlist.getName());
        box.getChildren().addAll(type, name);
        box.setOnMouseClicked(event -> {
            GUIController.READER.setCurrentPlaylist(Optional.of(playlist));
        });
        return box;
    }
}
