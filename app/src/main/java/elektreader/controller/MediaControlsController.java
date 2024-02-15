package elektreader.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import elektreader.api.MediaControl;

/**
 * This class is used to fill mediaControl graphic part of the software, 
 * with EventHandlers, JavaFX components and ActionListeners. 
 */
public class MediaControlsController {

    private final Button playPause;

    private final ImageView pausePng = new ImageView(ClassLoader.getSystemResource(
        "icons/Light/Media/Pause.png").toString());

    private final Button prevSong; 

    private final Button nextSong;

    private final Button loop;

    private final Button rand;

    private final Button stop; // NOPMD suppressed as it is a false positive

    private final Label currentMetaSong;

    private final Label nextMetaSong;

    private final Slider setRepSpeed;

    private final Slider currentVolume;

    private final Slider progressBar;

    private final MediaControl mediaControl;

    static final double HEIGHT = 30.0;

    static final double WIDTH = 70.0;

    /**
     * @param mediaControlGrid the Parent that must be filled.
     * @param progressBar the Slider that must represent current Duration of current Song played.
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "i need the parameters to be modifiable from external classes"
    )
    public MediaControlsController(final GridPane mediaControlGrid, final Slider progressBar) {
        mediaControlGrid.getChildren().clear();

        final HBox baseControls = new HBox();

        this.mediaControl = GUIController.READER.getPlayer();

        this.playPause = new Button();
        playPause.setGraphic(new ImageView("icons/Light/Media/Play.png"));

        this.prevSong = new Button();
        this.prevSong.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));

        this.nextSong = new Button();
        this.nextSong.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));

        this.loop = new Button();
        loop.setGraphic(new ImageView("icons/Light/Media/Repeat.png"));

        loop.setOnMouseClicked(e -> {
            mediaControl.loopSong();
            switch (mediaControl.getLoopStatus()) {
                case OFF -> {
                    loop.setGraphic(new ImageView("icons/Light/Media/Repeat.png"));
                    break;
                }
                case PLAYLIST -> {
                    loop.setGraphic(new ImageView("icons/Light/Media/RepeatActive.png"));
                    break;
                }
                case TRACK -> {
                    loop.setGraphic(new ImageView("icons/Light/Media/RepeatOneActive.png"));
                    break;
                }
                default -> {
                    break;
                }
            }
        });

        this.rand = new Button();
        rand.setOnMouseClicked(e -> {
            mediaControl.rand();
            if (mediaControl.getRandStatus()) {
                rand.setGraphic(new ImageView("icons/Light/Media/ShuffleActive.png"));
            } else {
                rand.setGraphic(new ImageView("icons/Light/Media/Shuffle.png"));
            }
        });
        rand.setGraphic(new ImageView("icons/Light/Media/Shuffle.png"));

        this.stop = new Button();
        stop.setGraphic(new ImageView("icons/Light/Media/Stop.png"));

        baseControls.getChildren().addAll(rand, prevSong, playPause, nextSong, loop, stop);

        this.currentMetaSong = new Label();
        this.nextMetaSong = new Label();

        this.currentVolume = new Slider(0, 1, 1);
        this.setRepSpeed = new Slider(0.5, 2, 1);
        this.setRepSpeed.setMaxWidth(WIDTH);
        this.currentVolume.setStyle("-fx-text-fill: black");
        this.currentVolume.setPrefHeight(HEIGHT);
        this.currentVolume.setPrefWidth(WIDTH);
        this.progressBar = progressBar;

        mediaControlGrid.add(currentMetaSong, 0, 0);
        mediaControlGrid.add(setRepSpeed, 2, 0);
        mediaControlGrid.add(baseControls, 1, 0);
        mediaControlGrid.add(nextMetaSong, 4, 0);
        mediaControlGrid.add(currentVolume, 3, 0);
    }

    /**
     * This method will be used by GUIControllers to reload my components.
     */
    public void reload() {
        //Platform.runLater(() -> {
            mediaControl.getMediaControl().get().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                progressBar.setValue(newValue.toSeconds() / mediaControl.getMediaControl().get().getTotalDuration().toSeconds());
            });
            this.currentMetaSong.setText(mediaControl.getCurrentSong().getName() 
                + "\n" + (mediaControl.getCurrentSong().getArtist().isPresent() 
                ? mediaControl.getCurrentSong().getArtist().get() : "No artist found"));
            final var nxtSong = mediaControl.getNextSong().isPresent() 
                ? mediaControl.getNextSong().get().getName() 
                + "\n" 
                + (mediaControl.getNextSong().get().getArtist().isPresent() 
                ? mediaControl.getNextSong().get().getArtist().get() 
                : " No artist found") 
                : "End of playlist";
            this.nextMetaSong.setText(nxtSong);

            this.playPause.setOnMouseClicked(event -> {
                if (this.mediaControl.getStatus().equals(MediaControl.Status.PLAYING)) {
                    this.mediaControl.pause();
                    playPause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Play.png").toString()));
                } else {
                    this.mediaControl.play();
                    playPause.setGraphic(pausePng);
                }
            });
            this.prevSong.setOnMouseClicked(event -> {
                this.mediaControl.prevSong();
            });

            this.nextSong.setOnMouseClicked(event -> {
                this.mediaControl.nextSong();
            });

            this.currentVolume.valueProperty().addListener((a, b, c) -> {
                mediaControl.setVolume(c.doubleValue());
            });

            this.setRepSpeed.valueProperty().addListener((a, b, c) -> {
                mediaControl.setRepSpeed(c.doubleValue());
            });
    }
}
