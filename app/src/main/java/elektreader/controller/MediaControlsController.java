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

    private HBox mediaCPanel;

    private GridPane gridPane;

    private Button play_pause;

    private Button prev_Song; 

    private Button next_Song;

    private Label current_meta_Song;

    private Label next_meta_Song;

    private Label current_Volume;

    //private Slider progressBar;

    MediaControl mediaControl;

    public MediaControlsController(final HBox mediaControlPanel, final Slider progressBar) {
        this.gridPane = new GridPane(10, 10);
        this.mediaCPanel = mediaControlPanel;
        this.mediaCPanel.getChildren().add(gridPane);

        this.gridPane.setPrefHeight(mediaControlPanel.getHeight());
        this.gridPane.setPrefWidth(mediaControlPanel.getWidth());

        this.play_pause = new Button(/*"Avvio/Pausa"*/);
        play_pause.setGraphic(new ImageView("icons/Light/Media/Play.png"));
        this.prev_Song = new Button("Previous");
        this.next_Song = new Button("Next");
        this.current_meta_Song = new Label();
        this.next_meta_Song = new Label();
        this.current_Volume = new Label();
        //this.progressBar = progressBar;
        this.mediaControl = GUIController.getReader().getPlayer();
        insert_in_Panel();
    }
    
    //This method will be used to insert components into MediaControlPanel. 
    private void insert_in_Panel() {
        this.gridPane.getChildren().clear();
        this.gridPane.add(current_meta_Song, 3, 2);
        this.gridPane.add(prev_Song, 50, 2);
        this.gridPane.add(play_pause, 55, 2);
        this.gridPane.add(next_Song, 60, 2);
        this.gridPane.add(next_meta_Song, 80, 2);
        this.gridPane.add(current_Volume, (gridPane.getColumnCount() - 3), 2);
        this.gridPane.setVisible(true);
    }

    public void loadSong(Song song) {
        //this.progressBar.valueProperty().bindBidirectional(GUIController.getReader().getPlayer().getMediaControl().currentTimeProperty().);
        /*DoubleProperty currTimeProperty = new SimpleDoubleProperty();
        DoubleBinding currTimeBinding = Bindings.createDoubleBinding(
            () -> mediaControl.getMediaControl().getCurrentTime().toSeconds(), 
            mediaControl.getMediaControl().currentTimeProperty());
        currTimeProperty.bind(currTimeBinding);
        Bindings.bindBidirectional(progressBar.valueProperty(), currTimeProperty);
        */
        this.play_pause.setOnMouseClicked(event -> {
            if (this.mediaControl.getStatus().equals(MediaControl.Status.PLAYING)) {
                this.mediaControl.pause();
                play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Pause.png").toString()));
            } else {
                this.mediaControl.play();
                play_pause.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Play.png").toString()));
            }
        });
        this.prev_Song.setOnMouseClicked(event -> this.mediaControl.prevSong());
        //this.prev_Song.setGraphic();
        this.next_Song.setOnMouseClicked(event -> this.mediaControl.nextSong());
        //this.next_Song.setGraphic();
        this.current_meta_Song.setText(song.getName() + 
            "\n" + (song.getArtist().isPresent() ? 
            song.getArtist().get() : "No artist found"));
            
            var next_Song = mediaControl.getNextSong().isPresent() ? mediaControl.getNextSong().get().getName() + " " +
            (mediaControl.getNextSong().get().getArtist().isPresent() ?
            mediaControl.getNextSong().get().getArtist().get() : " No artist found") : "end of playlist";
            this.next_meta_Song.setText(next_Song);
        this.current_Volume.setText(Double.toString(this.mediaControl.getVolume()));
    }
}
