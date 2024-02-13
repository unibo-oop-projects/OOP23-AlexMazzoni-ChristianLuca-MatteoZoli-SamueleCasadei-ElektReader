package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

import elektreader.api.Reader;
import elektreader.model.ReaderImpl;
import elektreader.model.TrackTrimmerImpl;
import elektreader.api.TrackTrimmer;
import elektreader.view.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;



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
	private Button btnTrim;

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
    private ScrollPane playlistsScroll;

	/* SONGS */
	@FXML
    private Label lblSong;

	@FXML
    private Label lblSongDesc;
	
	@FXML
    private ScrollPane songsScroll;
	
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
		controllerPlayLists.switchView();
	}

	@FXML
	private void trim() {
		TrackTrimmer trimmer = new TrackTrimmerImpl();
		Stage trimStage = new Stage();
		trimStage.initModality(Modality.WINDOW_MODAL);
		trimStage.initOwner(root.getScene().getWindow());
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(15));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPrefSize(270, 300);
		Label firstLabel = new Label("1.");
		Label secondLabel = new Label("2.");
		Label thirdLabel = new Label("3.");
		Label fourthLabel = new Label("4.");
		Label fifthLabel = new Label("5.");
		Button fileBtn = new Button("Select track");
		fileBtn.setOnMouseClicked(e -> trimmer.chooseTrack());
		TextField startCut = new TextField("Insert start (hh:mm:ss or seconds)");
		TextField endCut = new TextField("Insert end (hh:mm:ss or seconds)");
		TextField newName = new TextField("Insert the name for the trimmed track");
		startCut.setPrefWidth(220);
		Button trimBtn = new Button("Trim");
		trimBtn.setOnMouseClicked(e -> trimmer.trim(startCut, endCut, newName));
		pane.add(firstLabel, 0, 0);
		pane.add(secondLabel, 0, 1);
		pane.add(thirdLabel, 0, 2);
		pane.add(fourthLabel, 0, 3);
		pane.add(fifthLabel, 0, 4);
		pane.add(fileBtn, 1, 0);
		pane.add(startCut, 1, 1);
		pane.add(endCut, 1, 2);
		pane.add(newName, 1, 3);
		pane.add(trimBtn, 1, 4);
		pane.setStyle("-fx-background-color: #000000");
		firstLabel.setStyle("-fx-mid-text-color: #ffffff");
		secondLabel.setStyle("-fx-mid-text-color: #ffffff");
		thirdLabel.setStyle("-fx-mid-text-color: #ffffff");
		fourthLabel.setStyle("-fx-mid-text-color: #ffffff");
		fifthLabel.setStyle("-fx-mid-text-color: #ffffff");
		Scene scene = new Scene(pane);
		trimStage.setScene(scene);
		trimStage.show();
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
			this.lblPlaylists.setPrefWidth(SCALE_PLAYLIST_SIZE*this.root.getWidth());
			this.playlistsScroll.setPrefWidth(this.lblPlaylists.getWidth()+this.btnPlaylists.getWidth());
			//this.playlistsList.setVisible(true);
			
		} else {
			//this.playlistsList.setVisible(false);
			this.playlistsScroll.setPrefWidth(SIZE_ZERO);
			this.lblPlaylists.setPrefWidth(SIZE_ZERO);
	
		}
		responsive();
	}

	private void responsive() {
		this.playlistsScroll.setPrefWidth(this.lblPlaylists.getWidth());
		this.songsScroll.setPrefWidth(this.lblSong.getWidth());
		this.controllerPlayLists.responsive();
	}

	/* PRIVATE METHODS */
	private void loadEnvironment(final Optional<Path> root) {
		if(reader.setCurrentEnvironment(root.get())) {
			System.out.println("environment loaded: " + reader.getCurrentEnvironment().get());
			this.lblSongDesc.setText("");
			loadPlayer();
			loadPlaylists();
		}	}

	private void loadPlayer() {
		this.mediaControlPanel.getChildren().clear();
		this.controllerMediaControls = new MediaControlsController(this.mediaControlPanel, this.progressBar);
	}

	//debug
	public MediaControlsController getMediaControl() {
		return this.controllerMediaControls;
	}

	private void loadPlaylists() {
		this.playlistsScroll.setContent(null);
		/* in order to keep constant track of the size of the two scrolls */
		this.playlistsScroll.setFitToWidth(true);
		this.songsScroll.setFitToWidth(true);
	
		this.controllerPlayLists = new PlayListsController(this.playlistsScroll, this.songsScroll, this.lblSongDesc, this.controllerMediaControls);
		responsive();
	}

	public static Reader getReader() {
		return reader;
	}

	//Functional interface to execute FXML methods via shortcuts
	private interface ShortcutAction {
		void execute();	
	}
	
	//adds to the scene's accelerators list the KeyCombination and the associated action to perform
	private static void createShortcut(final Scene scene, final KeyCodeCombination key, final ShortcutAction action) {
		scene.getAccelerators().put(key, () -> action.execute());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.root.setPrefSize(GUI.scaleToScreenSize().getKey(), GUI.scaleToScreenSize().getValue());
		
        root.sceneProperty().addListener((observableScene, oldScene, newScene) -> 
			createShortcut(newScene, new KeyCodeCombination(KeyCode.T), () -> trim()));
		root.sceneProperty().addListener((observableScene, oldScene, newScene) -> 
			createShortcut(newScene, new KeyCodeCombination(KeyCode.V), () -> view()));
		//NEED TO ADD MORE SHORTCUTS

		// this.root.heightProperty().addListener((observable, oldHeight, newHeight) -> {
		// 	responsive();
		// });

		// this.root.widthProperty().addListener((observable, oldWidth, newWidth) -> {
		// 	responsive();
		// });

		//loadEnvironment(Optional.of(elektreader.App.TEST_PATH));
	}
}
