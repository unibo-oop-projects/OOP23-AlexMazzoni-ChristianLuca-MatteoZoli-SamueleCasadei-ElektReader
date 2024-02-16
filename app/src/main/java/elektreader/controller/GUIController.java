package elektreader.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import elektreader.api.Reader;
import elektreader.model.ReaderImpl;
import elektreader.view.GUI;
import elektreader.view.QueueGUI;
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
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.media.MediaPlayer;

/**
 * Controller of the main GUI, he use the app.FXML,
 * its design is inspired by an MVC like Ajax,
 * it has a base structure that is the app.FXML,
 * and some panels need to be updated during the execution,
 * we have split it that every dynamic panel have is classes,
 * and all of this panels are sync in this main controller class,
 * using a thread.
 */
public final class GUIController implements Initializable {
    /**
     * constant for the value 0, used for resize gui panels.
     */
    public static final double SIZE_ZERO = 0.0;

	/**
	 * constant used for resize playlists panel of 0.3 of the total window.
	 */
	public static final double SCALE_PLAYLIST_SIZE = 0.3;

	/**
	 * constant used for resize songs panel of 0.7 of the total window.
	 */
	public static final double SCALE_SONG_SIZE = 0.7;

	/**
	 * constant used for resize find separator a panel,
	 * that is resized when the user want to find anything.
	 */
	public static final double MIN_FIND_SIZE = 4;
	
	/**
	 * constant used for resize find separator a panel,
	 * that is resized when the user want to find anything.
	 */
	public static final double MAX_FIND_SIZE = 800;

	/**
	 * constant used for put in sleep the thread that sync all the GUI's with logics.
	 */
	public static final long THREAD_SLEEP = 1000L;
	
	/**
	 * must be public for all the classes he contains all the logic,
	 * and from it is possible to know all the status of logics app.
	 */
	public static final Reader READER = new ReaderImpl();

	/* LOGICS */
	private final FindController find = new FindController();

	private PlayListsController controllerPlayLists;

	private MediaControlsController controllerMediaControls;

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
	private Button btnQueue;

	@FXML
    private Button btnHelp;

	@FXML
	private Pane findPane;

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

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		this.root.setPrefSize(GUI.scaleToScreenSize().getKey(), GUI.scaleToScreenSize().getValue());

		root.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
			createShortcut(newScene, new KeyCodeCombination(KeyCode.F), () -> find());
			createShortcut(newScene, new KeyCodeCombination(KeyCode.T), () -> trim());
			createShortcut(newScene, new KeyCodeCombination(KeyCode.V), () -> view());
		});

		Platform.runLater(() -> {
			try {
				loadEnvironment(Optional.of(Paths.get(getClass().getResource("/" + "MUSICA").toURI())));
			} catch (URISyntaxException e) { } //NOPMD
		});
	}

	/* EVENTS */
	/* logics */
	@FXML
	private void importFiles() { //NOPMD
		try {
			final DirectoryChooser chooser = new DirectoryChooser();
			chooser.setTitle("Open Folder");
			chooser.setInitialDirectory(new File(System.getProperty("user.home")));
			final Optional<File> res = Optional.of(chooser.showDialog(null));
			if (res.isPresent()) {
				loadEnvironment(Optional.of(res.get().toPath()));
			}
		} catch (IllegalArgumentException e) { } //NOPMD
	}

	@FXML
	private void view() { //NOPMD
		if (GUIController.READER.getCurrentPlaylist().isPresent()) {
			controllerPlayLists.switchView();
		}
	}

	@FXML
	private void trim() { //NOPMD
		new TrackTrimmerController(this.root.getScene().getWindow());
	}

	@FXML
	private void find() { //NOPMD
		if (GUIController.READER.getCurrentEnvironment().isPresent()) {
			Platform.runLater(() -> {
				if (this.root.getRowConstraints().get(1).getMaxHeight() <= MIN_FIND_SIZE) { //pane is closed, so open it
					this.root.getRowConstraints().get(1).setMaxHeight(MAX_FIND_SIZE);
					find.show(this.findPane);
				} else {
					this.findPane.getChildren().clear();
					this.root.getRowConstraints().get(1).setMaxHeight(MIN_FIND_SIZE);
				}
			});
			responsive();
		}
	}

	@FXML
	private void queue() { //NOPMD
		if (GUIController.READER.getCurrentPlaylist().isPresent()) {
			new QueueGUI();
		}
	}

	@FXML
	private void help() { //NOPMD
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
	private void showPlaylists() { //NOPMD
		if (GUIController.READER.getCurrentEnvironment().isPresent()) {
			Platform.runLater(() -> {
				if (this.lblPlaylists.getPrefWidth() == SIZE_ZERO) { //is hidden
					this.imgPlaylistsShowPanel.setImage(new Image(
						ClassLoader.getSystemResource("icons/Light/UI/HideSidepanel.png").toString())); 

					this.lblPlaylists.setPrefWidth(SCALE_PLAYLIST_SIZE * this.root.getWidth());
					this.playlistsScroll.setPrefWidth(this.lblPlaylists.getWidth()
						+ this.btnPlaylists.getWidth());
					this.playlistsScroll.setVisible(true);
				} else {
					this.imgPlaylistsShowPanel.setImage(new Image(
						ClassLoader.getSystemResource("icons/Light/UI/ShowSidepanel.png").toString())); 

					this.playlistsScroll.setVisible(false);
					this.playlistsScroll.setPrefWidth(SIZE_ZERO);
					this.lblPlaylists.setPrefWidth(SIZE_ZERO);
				}
			});
			responsive();
		}
	}

	private void responsive() {
		//this.findPane.setPrefWidth(this.root.getWidth());
		this.playlistsScroll.setPrefWidth(this.lblPlaylists.getWidth());
		this.songsScroll.setPrefWidth(this.lblSong.getWidth());
		this.controllerPlayLists.responsive();
	}

	private void reload() {
		this.controllerPlayLists.reload();
		this.controllerMediaControls.reload();
	}

	private Runnable guiReaderListener() {
		return new Runnable() {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						if (GUIController.READER.getPlayer().getMediaControl().isPresent()) {
							GUIController.READER.getPlayer().getMediaControl().get().statusProperty()
								.addListener(new ChangeListener<MediaPlayer.Status>() {
								@Override
								public void changed(
								 final ObservableValue<? extends MediaPlayer.Status> observable,
								 final MediaPlayer.Status oldValue,
								 final MediaPlayer.Status newValue) {
									reload();
								}
							});
						}
					});
					try {
						Thread.sleep(THREAD_SLEEP);
					} catch (InterruptedException e) {
						Logger.getLogger(e.toString());
					}
				}
			}
		};
	}

	/* PRIVATE METHODS */
	private void loadEnvironment(final Optional<Path> root) {
		GUIController.READER.setCurrentEnvironment(root.get());
		if (GUIController.READER.getCurrentEnvironment().isPresent()) {
			System.out.println("environment loaded: " + GUIController.READER.getCurrentEnvironment().get()); //NOPMD
			loadPlaylists();
			loadPlayer();
			final Thread statusCheckerThread = new Thread(guiReaderListener());
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
			this.controllerPlayLists = new PlayListsController(this.playlistsScroll,
			 this.songsScroll, this.lblSongDesc);
		});
	}

	//
	/**
	 * adds to the scene's accelerators list the KeyCombination and the associated action to perform.
	 */
	private static void createShortcut(final Scene scene, final KeyCodeCombination key, final ShortcutAction action) {
		scene.getAccelerators().put(key, () -> action.execute());
	}

	/**
     * Functional interface to execute FXML methods via shortcuts.
     */
    private interface ShortcutAction {
		void execute();
    }
}
