package elektreader.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import elektreader.api.MediaControl;
import elektreader.api.Song;

/**
 * This class is used to fill mediaControl graphic part of the software, 
 * with EventHandlers, JavaFX components and ActionListeners. 
 */
public class MediaControlsController {

    private Button playPause;

    private Button prevSong; 

    private Button nextSong;

    private Button loop;

    private Button rand;

    private Button stop;

    private Label currentMetaSong;

    private Label nextMetaSong;

    private Slider currentVolume;

    //private Label volume;

    //private ImageView volumeImage;

    private Slider progressBar;

    private MediaControl mediaControl;

    private final double height = 30.0;

    private final double width = 70.0;

    /**
     * @param mediaControlGrid the Parent that must be filled.
     * @param progressBar the Slider that must represent current Duration of current Song played.
     */
    public MediaControlsController(final GridPane mediaControlGrid, final Slider progressBar) {
        mediaControlGrid.getChildren().clear();

        HBox baseControls = new HBox();

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
        this.currentVolume.setStyle("-fx-text-fill: black");
        this.currentVolume.setPrefHeight(height);
        this.currentVolume.setPrefWidth(width);
        //this.volumeImage = new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Audio.png").toString());
        this.progressBar = progressBar;

        this.mediaControl = GUIController.getReader().getPlayer();

        mediaControlGrid.add(currentMetaSong, 0, 0);
        mediaControlGrid.add(baseControls, 1, 0);
        mediaControlGrid.add(nextMetaSong, 2, 0);
        //mediaControlGrid.add(volume, 3, 0);
    }

    /**
     * @param song the Song to be set as the current one into our graphic.
     */
    public void loadSong(final Song song) {
        this.mediaControl.getMediaControl().get().currentTimeProperty().addListener((observable, oldValue, newValue) -> 
            progressBar.setValue(newValue.toSeconds() / mediaControl.getMediaControl().get().getTotalDuration().toSeconds()));

        this.playPause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));

        this.playPause.setOnMouseClicked(event -> {
            if (this.mediaControl.getStatus().equals(MediaControl.Status.PLAYING)) {
                this.mediaControl.pause();
                playPause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Play.png").toString()));
            } else {
                this.mediaControl.play();
                playPause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
            }
        });
        this.prevSong.setOnMouseClicked(event -> {
            this.mediaControl.prevSong();
            this.loadSong(mediaControl.getCurrentSong());
        });
        this.prevSong.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));
        this.nextSong.setOnMouseClicked(event -> {
            this.mediaControl.nextSong();
            this.loadSong(mediaControl.getCurrentSong());
        });
        this.nextSong.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));
        this.currentMetaSong.setText(song.getName() 
            + "\n" 
            + (song.getArtist().isPresent() 
            ? song.getArtist().get() : "No artist found"));
        var nxtSong = mediaControl.getNextSong().isPresent() 
            ? mediaControl.getNextSong().get().getName() 
            + "\n" 
            + (mediaControl.getNextSong().get().getArtist().isPresent() 
            ? mediaControl.getNextSong().get().getArtist().get() 
            : " No artist found") 
            : "End of playlist";
        this.nextMetaSong.setText(nxtSong);
        this.currentVolume.valueProperty().addListener((a, b, c) -> {
            mediaControl.setVolume(c.doubleValue());
        });
    }

    /**
     * This method will be used by GUIControllers to reload my components.
     */
    public void reload() {
        //Platform.runLater(() -> {
            mediaControl.getMediaControl().get().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                progressBar.setValue(newValue.toSeconds() / mediaControl.getMediaControl().get().getTotalDuration().toSeconds());
            });
            this.playPause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
            this.prevSong.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));
            this.nextSong.setGraphic(new ImageView(
                ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));
            this.currentMetaSong.setText(mediaControl.getCurrentSong().getName() 
                + "\n" + (mediaControl.getCurrentSong().getArtist().isPresent() 
                ? mediaControl.getCurrentSong().getArtist().get() : "No artist found"));
            var nxtSong = mediaControl.getNextSong().isPresent() 
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
                    playPause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
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
        //});
    }
}
