package elektreader.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import elektreader.api.MediaControl;
import elektreader.api.Song;

public class MediaControlsController {
    private GridPane mediaCPanel;

    private Button play_pause;

    private Button prev_Song; 

    private Button next_Song;

    private Button loop;

    private Button rand;

    private Button stop;

    private Label current_meta_song;

    private Label next_meta_song;

    private Slider current_Volume;

    private Label volume;

    private ImageView volumeImage;

    private Slider progressBar;

    private MediaControl mediaControl;

    public MediaControlsController(final GridPane mediaControlGrid, final Slider progressBar) {
        mediaControlGrid.getChildren().clear();

        HBox base_controls = new HBox();

        this.play_pause = new Button();
        play_pause.setGraphic(new ImageView("icons/Light/Media/Play.png"));

        this.prev_Song = new Button();
        this.prev_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));

        this.next_Song = new Button();
        this.next_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));
        
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

        base_controls.getChildren().addAll(rand, prev_Song, play_pause, next_Song, loop, stop);
    
        this.current_meta_song = new Label();
        this.next_meta_song = new Label();

        this.current_Volume = new Slider(0, 1, 1);
        this.current_Volume.setStyle("-fx-text-fill: black");
        this.current_Volume.setPrefHeight(30.0);
        this.current_Volume.setPrefWidth(70.0);
        this.volumeImage = new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Audio.png").toString());
        this.progressBar = progressBar;
    
        this.mediaControl = GUIController.getReader().getPlayer();
        
        mediaControlGrid.add(current_meta_song, 0, 0);
        mediaControlGrid.add(base_controls, 1, 0);
        mediaControlGrid.add(next_meta_song, 2, 0);
        //mediaControlGrid.add(volume, 3, 0);
        //mediaControlGrid.add(volume, 3, 0);
    }

    public void loadSong(Song song) {
        this.mediaControl.getMediaControl().get().currentTimeProperty().addListener((observable, oldValue, newValue) -> 
            progressBar.setValue(newValue.toSeconds() / mediaControl.getMediaControl().get().getTotalDuration().toSeconds()));
            
        this.play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
        
        this.play_pause.setOnMouseClicked(event -> {
            if (this.mediaControl.getStatus().equals(MediaControl.Status.PLAYING)) {
                this.mediaControl.pause();
                play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Play.png").toString()));
            } else {
                this.mediaControl.play();
                play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
            }
        });
        this.prev_Song.setOnMouseClicked(event -> {
            this.mediaControl.prevSong();
            this.loadSong(mediaControl.getCurrentSong());
        });
        this.prev_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));
        this.next_Song.setOnMouseClicked(event -> {
            this.mediaControl.nextSong();
            this.loadSong(mediaControl.getCurrentSong());
        });
        this.next_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));
        this.current_meta_song.setText(song.getName() + 
            "\n" + (song.getArtist().isPresent() ? 
            song.getArtist().get() : "No artist found"));
        var next_Song = mediaControl.getNextSong().isPresent() ? mediaControl.getNextSong().get().getName() + "\n" +
        (mediaControl.getNextSong().get().getArtist().isPresent() ?
        mediaControl.getNextSong().get().getArtist().get() : " No artist found") : "End of playlist";
        this.next_meta_song.setText(next_Song);
        this.current_Volume.valueProperty().addListener((a, b, c) -> {
            mediaControl.setVolume(c.doubleValue());
        });
    }

    //This method will be used by GUIControllers to reload my components
    public void reload() {
        //Platform.runLater(() -> {
            mediaControl.getMediaControl().get().currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                progressBar.setValue(newValue.toSeconds() / mediaControl.getMediaControl().get().getTotalDuration().toSeconds());
            });
            
                this.play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
                this.prev_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));
                this.next_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));
                this.current_meta_song.setText(mediaControl.getCurrentSong().getName() + 
                    "\n" + (mediaControl.getCurrentSong().getArtist().isPresent() ? 
                    mediaControl.getCurrentSong().getArtist().get() : "No artist found"));
                var next_Song = mediaControl.getNextSong().isPresent() ? mediaControl.getNextSong().get().getName() + "\n" +
                (mediaControl.getNextSong().get().getArtist().isPresent() ?
                mediaControl.getNextSong().get().getArtist().get() : " No artist found") : "End of playlist";
                this.next_meta_song.setText(next_Song);

            this.play_pause.setOnMouseClicked(event -> {
                if (this.mediaControl.getStatus().equals(MediaControl.Status.PLAYING)) {
                    this.mediaControl.pause();
                    play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Play.png").toString()));
                } else {
                    this.mediaControl.play();
                    play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
                }
            });
            
            this.prev_Song.setOnMouseClicked(event -> {
                this.mediaControl.prevSong();
            });

            
            this.next_Song.setOnMouseClicked(event -> {
                this.mediaControl.nextSong();
            });

            this.current_Volume.valueProperty().addListener((a, b, c) -> {
                mediaControl.setVolume(c.doubleValue());
            });
        //});
    }
}
