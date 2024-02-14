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
import elektreader.view.TrimGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.MediaPlayer;


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
	private GridPane mediaControlGrid;


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
		if(GUIController.getReader().getCurrentPlaylist().isPresent()) {
			controllerPlayLists.switchView();
		}
	}

	@FXML
	private void trim() {
		TrimGUI trim = new TrimGUI(this.root.getScene().getWindow());
	}

	@FXML
	private void find() { 
		//TODO - Alex
	}

	@FXML
	private void help() { 
		//TODO - anyone
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

	/* only graphics */
	@FXML
	private void showPlaylists() {
		if(GUIController.getReader().getCurrentEnvironment().isPresent()) {
			Platform.runLater(() -> {
				if(this.lblPlaylists.getPrefWidth()==SIZE_ZERO) { //is hidden
					this.imgPlaylistsShowPanel.setImage(new Image(ClassLoader.getSystemResource("icons/Light/UI/HideSidepanel.png").toString())); 
					this.lblPlaylists.setPrefWidth(SCALE_PLAYLIST_SIZE*this.root.getWidth());
					this.playlistsScroll.setPrefWidth(this.lblPlaylists.getWidth()+this.btnPlaylists.getWidth());
					this.playlistsScroll.setVisible(true);
				} else {
					this.imgPlaylistsShowPanel.setImage(new Image(ClassLoader.getSystemResource("icons/Light/UI/ShowSidepanel.png").toString())); 
					this.playlistsScroll.setVisible(false);
					this.playlistsScroll.setPrefWidth(SIZE_ZERO);
					this.lblPlaylists.setPrefWidth(SIZE_ZERO);
				}
			});
			responsive();
		}
	}

	private void responsive() {
		this.playlistsScroll.setPrefWidth(this.lblPlaylists.getWidth());
		this.songsScroll.setPrefWidth(this.lblSong.getWidth());
		this.controllerPlayLists.responsive();
	}

	private void reload() {
		this.controllerPlayLists.reload();
		this.controllerMediaControls.reload();
	}

	private Runnable GUIReaderListener() {
		return new Runnable() {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						if(GUIController.getReader().getPlayer().getMediaControl().isPresent()) {
							GUIController.getReader().getPlayer().getMediaControl().get().statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
								@Override
								public void changed(ObservableValue<? extends MediaPlayer.Status> observable, MediaPlayer.Status oldValue, MediaPlayer.Status newValue) {
									reload();
								}
							});
						}
					});
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
				
			}
		};	
	}

	/* PRIVATE METHODS */
	private void loadEnvironment(final Optional<Path> root) {
		if(reader.setCurrentEnvironment(root.get())) {
			System.out.println("environment loaded: " + reader.getCurrentEnvironment().get());
			//this.lblSongDesc.setText("");
			loadPlaylists();
			loadPlayer();
			Thread statusCheckerThread = new Thread(GUIReaderListener());
			//statusCheckerThread.setDaemon(false);
			statusCheckerThread.start();
		}	
	}

	private void loadPlayer() {
		Platform.runLater(() -> {
			this.mediaControlGrid.getChildren().clear();
			this.controllerMediaControls = new MediaControlsController(this.mediaControlGrid, this.progressBar);
		});
	}

	private void loadPlaylists() {
		Platform.runLater(() -> {
			this.playlistsScroll.setContent(null);
			/* in order to keep constant track of the size of the two scrolls */
			this.playlistsScroll.setFitToWidth(true);
			this.songsScroll.setFitToWidth(true);
			this.controllerPlayLists = new PlayListsController(this.playlistsScroll, this.songsScroll, this.lblSongDesc);
		});
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
		
		root.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
			createShortcut(newScene, new KeyCodeCombination(KeyCode.F), () -> find());
			createShortcut(newScene, new KeyCodeCombination(KeyCode.T), () -> trim());
			createShortcut(newScene, new KeyCodeCombination(KeyCode.V), () -> view());
		});

		Platform.runLater(() -> loadEnvironment(Optional.of(elektreader.App.TEST_PATH)));
	}
}
