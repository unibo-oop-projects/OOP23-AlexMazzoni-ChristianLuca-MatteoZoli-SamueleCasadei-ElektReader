package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Arrays;

import elektreader.api.Reader;
import elektreader.api.Song;
import elektreader.model.ReaderImpl;
import elektreader.view.GUI;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;


public class GUIController implements Initializable {

	public static final double SIZE_ZERO = 0.0;
	public static final double SCALE_PLAYLIST_SIZE = 0.3;
	public static final double SCALE_SONG_SIZE = 0.7;
	

	/* LOGICS */
	private final static Reader reader = new ReaderImpl();

	PlayListsController controllerPlayLists;
	MediaControlsController controllerMediaControls;

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

	/* PLAYLISTS */
	@FXML
    private Label lblPlaylists;

	@FXML
    private Button btnPlaylists;

	@FXML
    private ImageView imgPlaylistsShowPanel;

	@FXML
    private ScrollPane playlistsList;

	/* SONGS */
	@FXML
    private Label lblSong;

	@FXML
    private Label lblSongDesc;

	@FXML
	private StackPane songsContainer;
	
	@FXML
    private ScrollPane songsList;

	@FXML
	private TableView<Song> songsListView;
	
	@FXML
    private ScrollPane songsIcon;
	
	/* MEDIA CONTROL */
	@FXML
	private Slider progressBar;

	@FXML
	private HBox mediaControlPanel;


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
		//Setting the TableView for the current playlist (if one has been selected)
		if (getReader().getCurrentPlaylist().isPresent()){
			var currentPlaylist = getReader().getCurrentPlaylist().get();
			ObservableList<Song> listaCoda = FXCollections.observableArrayList(currentPlaylist.getSongs());
			TableColumn<Song, String> nameColumn = new TableColumn<>("Song Name");
			nameColumn.setCellValueFactory(cellData -> {
				var name = cellData.getValue().getName();
				return new SimpleStringProperty(name);
			});
			TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
			artistColumn.setCellValueFactory(cellData -> {
				var artist = cellData.getValue().getArtist();
				return new SimpleStringProperty(artist.isPresent() ? artist.get() : "");
			});
			TableColumn<Song, String> durationColumn = new TableColumn<>("Duration");
			durationColumn.setCellValueFactory(cellData -> {
				var duration = cellData.getValue().DurationStringRep();
				return new SimpleStringProperty(duration);
			});
			songsListView.setItems(listaCoda);
			songsListView.getColumns().addAll(Arrays.asList(nameColumn, artistColumn, durationColumn));
			songsListView.setOnMouseClicked(e -> {
				var selectedSong = songsListView.getSelectionModel().getSelectedItem();
				reader.getPlayer().setSong(selectedSong);
				reader.getPlayer().play();
			});
		}
		//Switching the pane that is been shown in the StackPane
		var tmp = songsContainer.getChildren().get(0);
		songsContainer.getChildren().remove(0);
		songsContainer.getChildren().add(tmp);
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
		/* codice per trovare le canzoni duplicate (inutile e poco efficente) */
		// songs = files.stream().filter(t -> {
        //     if((int)files.stream()
        //             .map(Song::getTitle)
        //             .filter(s -> s.equals(Song.getTitle(t))).count() > 1) {
        //                 System.out.println("DUPLICATO DI "+Song.getTitle(t));
        //         return false;
        //     }
        //     return true;
        // }).toList();
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
		if(this.lblPlaylists.getPrefWidth()==SIZE_ZERO) { //is hidden
			Platform.runLater(() -> {
				this.lblPlaylists.setPrefWidth(SCALE_PLAYLIST_SIZE*this.root.getWidth());
				this.playlistsList.setPrefWidth(this.lblPlaylists.getWidth()+this.btnPlaylists.getWidth());
				//this.playlistsList.setVisible(true);
			});
		} else {
			Platform.runLater(() -> {
				//this.playlistsList.setVisible(false);
				this.playlistsList.setPrefWidth(SIZE_ZERO);
				this.lblPlaylists.setPrefWidth(SIZE_ZERO);
			});
		}
		responsive();
	}

	private void responsive() {
		Platform.runLater(() -> {
			this.controllerPlayLists.responsive();
		});
	}

	/* PRIVATE METHODS */
	private void loadEnvironment(final Optional<Path> root) {
		if(reader.setCurrentEnvironment(root.get())) {
			System.out.println("environment loaded: " + reader.getCurrentEnvironment().get());
			loadPlayer();
			loadPlaylists();
		}
	}

	private void loadPlayer() {
		this.mediaControlPanel.getChildren().clear();
		this.controllerMediaControls = new MediaControlsController(this.mediaControlPanel, this.progressBar);
	}

	private void loadPlaylists() {
		Platform.runLater(()-> {
			this.playlistsList.setContent(null);
			this.controllerPlayLists = new PlayListsController(this.playlistsList, this.songsIcon);
			responsive();
		});
	}

	public static Reader getReader() {
		return reader;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.root.setPrefSize(GUI.scaleToScreenSize().getKey(), GUI.scaleToScreenSize().getValue());

		// this.root.heightProperty().addListener((observable, oldHeight, newHeight) -> {
		// 	responsive();
		// });

		// this.root.widthProperty().addListener((observable, oldWidth, newWidth) -> {
		// 	responsive();
		// });

		loadEnvironment(Optional.of(elektreader.App.TEST_PATH));
	}
}
