package elektreader.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import elektreader.api.MediaControl;

public class MediaControlsController {

    private HBox mediaCPanel;

    private GridPane gridPane;

    private Button play_pause;

    private Button prev_Song; 

    private Button next_Song;

    private Label current_meta_Song;

    private Label next_meta_Song;

    private Label current_Volume;

    private Slider progressBar;

    MediaControl mediaControl;

    public MediaControlsController(final HBox mediaControlPanel, final Slider progressBar) {
        this.gridPane = new GridPane();
        this.mediaCPanel = mediaControlPanel;
        this.mediaCPanel.getChildren().add(gridPane);

        this.gridPane.setPrefHeight(mediaControlPanel.getHeight());
        this.gridPane.setPrefWidth(mediaControlPanel.getWidth());

        this.play_pause = new Button();
        this.prev_Song = new Button();
        this.next_Song = new Button();
        this.current_meta_Song = new Label();
        this.next_meta_Song = new Label();
        this.current_Volume = new Label();
        this.progressBar = progressBar;
        this.mediaControl = GUIController.getReader().getPlayer();
        fillMediaControlPanel();
        insert_in_Panel();
    }
    
    //This method will be used to insert components into MediaControlPanel. 
    private void insert_in_Panel() {
        this.gridPane.getChildren().clear();
        this.gridPane.getChildren().addAll(current_meta_Song, prev_Song, play_pause, next_Song, next_meta_Song, current_Volume);
        this.gridPane.setVisible(true);
    }

    //This method will be used to insert infos into the components.
    private void fillMediaControlPanel() {
        this.play_pause.setOnMouseClicked(event -> {
            if (this.mediaControl.getStatus().equals(MediaControl.Status.PLAYING)) {
                this.mediaControl.pause();
                play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Pause.png").toString()));
            } else {
                this.mediaControl.play();
                play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Play.png").toString()));
            }
        });
        this.prev_Song.setOnMouseClicked(event -> this.mediaControl.prevSong());
        this.prev_Song.setGraphic(current_Volume);
        this.next_Song.setOnMouseClicked(event -> this.mediaControl.nextSong());
        this.current_meta_Song.setText(this.mediaControl.getCurrentSong().getName() + 
            this.mediaControl.getCurrentSong().getArtist().get());
        //this.next_meta_Song.setText(this.mediaControl.getNextSong().getName() +
        //    this.mediaControl.getNextSong().getArtist());
        this.current_Volume.setText(Double.toString(this.mediaControl.getVolume()));
    }
}
