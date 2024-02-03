package elektreader.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import elektreader.api.PlayList;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PlaylistsController {
    private List<Button> btnPlaylists;
    private SongsController controllerSongs;

    public PlaylistsController(final VBox playlistsList, final TilePane songsList) {
        this.btnPlaylists = new ArrayList<>(Collections.emptyList());
        /* here i reference to a static method that will be in the next push */
        for (var playlist : GUIController.getReader().getPlaylists().get()) {
            btnPlaylists.add(createButton(playlist, songsList));
        }
        playlistsList.getChildren().addAll(btnPlaylists);
    }

    private Button createButton(final PlayList p, final TilePane songsList) {
        Button btnPlaylist = new Button("#"+p.getName());
        btnPlaylist.setMinSize(150, 15);
        btnPlaylist.setStyle("-fx-background-color: transparent;");
        btnPlaylist.setStyle("-fx-border-color: black;");
        btnPlaylist.setStyle("-fx-border-width: 4px;");
        btnPlaylist.setFont(new Font(18.0));
        btnPlaylist.setOnMouseClicked(event -> {
            /* here i reference to a static method that will be in the next push */
					if(GUIController.getReader().setCurrentPlaylist(Optional.of(p))); {
                        songsList.getChildren().clear();
                        this.controllerSongs = new SongsController(songsList); 
                    }
				});
        return btnPlaylist;
    }

    public List<Button> getBtnPlaylists() {
        return this.btnPlaylists;
    }
}
