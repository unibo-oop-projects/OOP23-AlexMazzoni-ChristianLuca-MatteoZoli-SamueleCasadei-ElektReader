package elektreader.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
//import javafx.scene.layout.ColumnConstraints;
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

    private TextArea current_Volume;

    private Slider progressBar;

    MediaControl mediaControl;

    public MediaControlsController(final HBox mediaControlPanel, final Slider progressBar) {
        this.gridPane = new GridPane(10, 10);
        this.mediaCPanel = mediaControlPanel;
        this.mediaCPanel.getChildren().add(gridPane);

        this.gridPane.setPrefHeight(mediaControlPanel.getHeight());
        this.gridPane.setPrefWidth(mediaControlPanel.getWidth());

        this.play_pause = new Button();
        play_pause.setGraphic(new ImageView("icons/Light/Media/Play.png"));
        this.prev_Song = new Button();
        this.prev_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/Rewind.png").toString()));
        this.next_Song = new Button();
        this.next_Song.setGraphic(new ImageView(ClassLoader.getSystemResource("icons/Light/Media/FastForward.png").toString()));
        this.current_meta_Song = new Label();
        this.next_meta_Song = new Label();
        this.current_Volume = new TextArea("Write here");
        this.current_Volume.setStyle("-fx-text-fill: black");
        this.current_Volume.positionCaret(current_Volume.getText().length() / 2);
        this.current_Volume.setPrefHeight(30.0);
        this.current_Volume.setPrefWidth(70.0);
        this.progressBar = progressBar;
        this.mediaControl = GUIController.getReader().getPlayer();
        insert_in_Panel();
    }
    
    //This method will be used to insert components into MediaControlPanel. 
    private void insert_in_Panel() {
        this.gridPane.getChildren().clear();
        //Still to be understood
        /*
        this.gridPane.getColumnConstraints().addAll(new ColumnConstraints().setPercentWidth(50), 
            new ColumnConstraints().setPercentWidth(50));
        */
        this.gridPane.add(current_meta_Song, 3, 2);
        this.gridPane.add(prev_Song, 50, 2);
        this.gridPane.add(play_pause, 55, 2);
        this.gridPane.add(next_Song, 60, 2);
        this.gridPane.add(next_meta_Song, 80, 2);
        this.gridPane.add(current_Volume, (gridPane.getColumnCount() - 3), 2);
        this.gridPane.setVisible(true);
    }

    //This method is used to load song infos into the view.
    public void loadSong(Song song) {

        mediaControl.getMediaControl().currentTimeProperty().addListener((observable, oldValue, newValue) -> 
            progressBar.setValue(newValue.toSeconds() / mediaControl.getMediaControl().getTotalDuration().toSeconds()));
            
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
        this.current_meta_Song.setText(song.getName() + 
            "\n" + (song.getArtist().isPresent() ? 
            song.getArtist().get() : "No artist found"));
        //Debug
        this.current_Volume.setOnInputMethodTextChanged(event -> {
            this.mediaControl.setVolume(Double.parseDouble(current_Volume.getText()));
        });  
        //End debug
        var next_Song = mediaControl.getNextSong().isPresent() ? mediaControl.getNextSong().get().getName() + "\n" +
        (mediaControl.getNextSong().get().getArtist().isPresent() ?
        mediaControl.getNextSong().get().getArtist().get() : " No artist found") : "end of playlist";
        this.next_meta_Song.setText(next_Song);
        this.current_Volume.setText(Double.toString(this.mediaControl.getVolume()));
        //this.current_Volume.positionCaret(current_Volume.getText().length() / 2);
    }
}
