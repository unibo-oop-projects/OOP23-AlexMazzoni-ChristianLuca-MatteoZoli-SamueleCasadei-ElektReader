package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

import elektreader.api.Reader;
import elektreader.model.ReaderImpl;
import elektreader.view.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;


public class GUIController implements Initializable {

	public static final double SCALE_PLAYLIST_SIZE = 0.3;
	public static final double SCALE_SONG_SIZE = 0.7;

	/* LOGICS */
	private final static Reader reader = new ReaderImpl();

	PlayListsController controllerPlayLists;
	SongsController controllerSongs;

	/* MAIN PARENT */
	@FXML
    private GridPane root;

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

	/* LIST */

	/* PLAYLISTS */
	@FXML
    private Label lblPlaylists;

	@FXML
    private ImageView imgPlaylistsShowPanel;

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
				loadEnvironment(Optional.of(res.get().toPath()));
			}
		} catch (Exception e) { throw e;}
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
		// Group g = groupAll();
		// for (var element : g.getChildren()) {
			
		// 	//element.getStyleClass().
		// }
	}

	/* only graphics */
	@FXML
	private void showPlaylists() {
		if(this.lblPlaylists.getPrefWidth()==0.0) { //is hidden
			this.lblPlaylists.setPrefWidth(SCALE_PLAYLIST_SIZE*GUI.scaleToScreenSize().getKey());
			this.playlistsList.setVisible(true);
		} else {
			this.playlistsList.setVisible(false);
			this.lblPlaylists.setText("");
		}
	}

	private void responsive() {
		this.root.setPrefSize(GUI.scaleToScreenSize().getKey(), GUI.scaleToScreenSize().getValue());
	}
	
	private Group groupAll() {
		return null;
		// return new Group(this.btnFile, this.btnFind, this.btnView, this.btnHelp,
		// 	this.btnDebug1, this.btnDebug2, this.btnDebug3, this.btnDebug4, 
		// 	this.lblPlaylists, this.imgPlaylistsShowPanel, this.playlistsList,
		// 	this.lblSong, this.songsList,
		// 	this.MediaControlPanel);
	}

	/* PRIVATE METHODS */
	private void loadEnvironment(final Optional<Path> root) {
		if(reader.setCurrentEnvironment(root.get())) {
			System.out.println("environment loaded: " + reader.getCurrentEnvironment().get());
			loadPlaylists();
		}
	}

	private void loadPlaylists() {
		this.playlistsList.getChildren().clear();
		this.controllerPlayLists = new PlayListsController(this.playlistsList, this.songsList);
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

	public static Reader getReader() {
		return reader;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadEnvironment(Optional.of(elektreader.App.TEST_PATH));
		responsive();
	}
}
