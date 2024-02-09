package elektreader.controller;

import elektreader.model.Mp3MediaControl;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;

public class MediaControlsController {

    private HBox mediaCPanel;

    private Mp3MediaControl mediaControl;

    private Button play_pause;

    private Button prev_Song; 

    private Button next_Song;

    private Label current_meta_Song;

    private Label next_meta_Song;

    private Label current_Volume;

    private ProgressBar progressBar;

    public MediaControlsController(HBox mediaControlPanel, Mp3MediaControl mediaControl) {
        this.mediaCPanel = mediaControlPanel;
        this.mediaControl = mediaControl;
        this.play_pause = new Button();
        this.prev_Song = new Button();
        this.next_Song = new Button();
        this.current_meta_Song = new Label();
        this.next_meta_Song = new Label();
        this.current_Volume = new Label();
        this.progressBar = new ProgressBar();
    }

    public void full_Labels() {
        this.prev_Song.setOnMouseClicked(event -> this.mediaControl.nextSong());
        this.next_Song.setOnMouseClicked(event -> this.mediaControl.nextSong());
        this.current_meta_Song.setText(this.mediaControl.getCurrentSong().getName() + 
            this.mediaControl.getCurrentSong().getArtist().get());
        this.next_meta_Song.setText(this.mediaControl.getNextSong().getName() +
            this.mediaControl.getNextSong().getArtist());
        this.current_Volume.setText(Double.toString(this.mediaControl.getVolume()));
        this.progressBar.setProgress(this.mediaControl.getProgress());
    }
}
