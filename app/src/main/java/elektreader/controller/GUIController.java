package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


import elektreader.api.Reader;
import elektreader.model.ReaderImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;


public class GUIController {

	/* LOGICS */
	private final static Reader reader = new ReaderImpl();

	PlaylistsController controllerPlaylists;
	SongsController controllerSongs;

	/* MENU */
	@FXML
    private Button btnFile;

	@FXML
    private Button btnView;

	@FXML
    private Button btnFind;

	@FXML
    private Button btnHelp;

	/* DEBUG */
	@FXML
	private Button btnDebug1;
	@FXML
	private Button btnDebug2;
	@FXML
	private Button btnDebug3;
	@FXML
	private Button btnDebug4;

	/* PANEL */
	@FXML
	private SplitPane splitPlaylistsSongs;

	/* PLAYLISTS */
	@FXML
    private Label lblPlaylists;

	@FXML
    private Button btnPlaylistsShowPanel;

	@FXML
    private VBox playlistsList;

	/* SONGS */
	@FXML
    private Label lblSong;

	@FXML
    private TilePane songsList;

	/* MEDIA CONTROL */
	@FXML
	private HBox MediaControlPanel;


	/* EVENTS */
	/* logics */
	@FXML
	private void importFiles() throws IOException {
		try {
			DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Open Folder");
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
			Optional<File> res = Optional.of(chooser.showDialog(null));
			if(res.isPresent()) {
				if(reader.setCurrentEnvironment(res.get().toPath())) {
					System.out.println("environment loaded: " + reader.getCurrentEnvironment().get());
					loadPlaylists();
				}
			}
		} catch (Exception e) {}
	}

	@FXML
	private void view() { 
		//TODO - Matteo
	}

	@FXML
	private void find() { 
		//TODO - Alex
	}

	@FXML
	private void help() { 
		//TODO - anyone
	}

	/* DEBUG SECTION */
	@FXML
	private void debug1() { 
		//TODO
	}

	@FXML
	private void debug2() { 
		//TODO
	}

	@FXML
	private void debug3() { 
		//TODO
	}

	@FXML
	private void debug4() { 
		//TODO
	}

	/* only graphics */
	@FXML
	private void showPlaylists() {
		if(lblPlaylists.getText().equals("")) { //is hidden
			lblPlaylists.setText("Playlists");
			this.playlistsList.setVisible(true);
			this.splitPlaylistsSongs.setDividerPositions(0.31);
		} else {
			this.splitPlaylistsSongs.setDividerPositions(0);
			this.playlistsList.setVisible(false);
			lblPlaylists.setText("");
		}
	}

	/* PRIVATE METHODS */
	private void loadPlaylists() {
		this.playlistsList.getChildren().clear();
		this.controllerPlaylists = new PlaylistsController(this.playlistsList, this.songsList);
	}

	/* probabilmente il song va nel controller playlist */
	// private void loadSongs() {
	// 	if (this.reader.getCurrentPlaylist().isPresent()) {
	// 		this.controllerSongs = new SongsController(this.reader);
	// 	}
	// }

	/* probabilmente il media va nel controller song */
	// private void loadMediaControls() {
	// 	this.controllerMediaControls = new MediaControlsController();
	// }

	public GUIController() {}

	public static Reader getReader() {
		return reader;
	}
}
