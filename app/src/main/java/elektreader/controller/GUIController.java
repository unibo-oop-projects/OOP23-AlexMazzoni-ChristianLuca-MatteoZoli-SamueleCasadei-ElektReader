package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import elektreader.api.Controller;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class GUIController implements Controller {

	@FXML
    private Label lblPlaylists;

	@FXML
    private Label lblSongs;

    @FXML
	private VBox playlistsList;

	@FXML
	private VBox songsList;

	@FXML
	private Button btnPlaylistsShowPanel;

    @FXML
    private SplitPane splitPlayListsSongs;

	@FXML
	public void showPlayLists() {
		lblPlaylists.setVisible(false);
		splitPlayListsSongs.setDividerPosition(0, 0);
		splitPlayListsSongs.getDividers().get(0).setPosition(1);
	}

	@FXML
	public void importFiles() throws IOException {
		try {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Open Folder");
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
			Optional<File> res = Optional.of(chooser.showDialog(null));
			if(res.isPresent()) {
				System.out.println(res.get().getAbsolutePath());
			}
		} catch (Exception e) {
			throw new IOException("no file selected");
		}
	}

	@FXML
	public void riproduci() {
		System.out.println("canzone");
	}

	private void clearPlaylistsList() {
		this.playlistsList.getChildren().clear();	
	}

	
	public void setPlaylistsList(final Set<Node> playlists) {
		clearPlaylistsList();
		this.playlistsList.getChildren().addAll(playlists);
	}

	private void clearSongsList() {
		this.songsList.getChildren().clear();
	}

	@Override
	public void setSongsList(final Set<Node> songs) {
		clearSongsList();
		this.songsList.getChildren().addAll(songs);
	}	
}
