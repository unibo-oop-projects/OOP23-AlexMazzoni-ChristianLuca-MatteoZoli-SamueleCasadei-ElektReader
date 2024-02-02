package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


import elektreader.api.Reader;
import elektreader.model.ReaderImpl;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;


public class GUIController {

	/* LOGICS */
	private Reader reader = new ReaderImpl();
	
	PlaylistsController controllerPlaylists;

	/* MENU */
	@FXML
    private Button btnFile;

	@FXML
    private Button btnView;

	@FXML
    private Button btnFind;

	@FXML
    private Button btnHelp;

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
    private VBox songsList;

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
				this.reader.setCurrentEnvironment(res.get().toPath());
			}
		} catch (Exception e) {}
		if(this.reader.getCurrentEnvironment().isPresent()) {
			System.out.println("environment loaded: " + this.reader.getCurrentEnvironment().get());
			loadPlaylists();
		}
	}

	@FXML
	private void view() { 
		//TODO - Matteo
	}

	@FXML
	private void find() { 
		//TODO - Alex
	}

	/* DEBUG SECTION */
	@FXML
	private void debug() { 
		//TODO
		//MATTEO QUESTO è PER TE da qua aggancia un nuovo stage e fa quello che vuoi ti do il pieno controllo
		//ti consiglio di mettere lo stage che sti creando nella cartella View
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

	private void loadPlaylists() {
		if(this.reader.getPlaylists().isPresent()) {
			this.controllerPlaylists = new PlaylistsController(this.reader);
			this.playlistsList.getChildren().addAll(this.reader.getPlaylists().get().stream().map(t -> new Label(t.getName())).map(Node.class::cast).toList());
		}
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

	public Reader getReader() {
		return this.reader;
	}
}
