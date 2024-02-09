package elektreader.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class MediaControlsController {

    private HBox mediaCPanel;

    private Button play_pause;

    private Button prev_Song; 

    private Button next_Song;

    private Label current_meta_Song;

    private Label next_meta_Song;

    private Label current_Volume;

    private Slider progressBar;

    public MediaControlsController(final HBox mediaControlPanel, final Slider progressBar) {
        this.mediaCPanel = mediaControlPanel;
        this.play_pause = new Button();
        this.prev_Song = new Button();
        this.next_Song = new Button();
        this.current_meta_Song = new Label();
        this.next_meta_Song = new Label();
        this.current_Volume = new Label();
        this.progressBar = progressBar;
    }
    
    //This method will be used to insert components into MediaControlPanel. 
    private void insert_in_Panel() {
        mediaCPanel.getChildren().addAll(play_pause, prev_Song, next_Song, current_meta_Song, next_meta_Song,
            current_Volume, progressBar);
        mediaCPanel.getChildren().forEach(t -> t.setVisible(true));
    }

    //This method will be used to insert infos into the components.
    public void fillMediaControlPanel() {
        this.prev_Song.setOnMouseClicked(event -> GUIController.getReader().getPlayer().nextSong());
        this.next_Song.setOnMouseClicked(event -> GUIController.getReader().getPlayer().prevSong());
        this.current_meta_Song.setText(GUIController.getReader().getPlayer().getCurrentSong().getName() + 
            GUIController.getReader().getPlayer().getCurrentSong().getArtist().get());
        this.next_meta_Song.setText(GUIController.getReader().getPlayer().getNextSong().getName() +
            GUIController.getReader().getPlayer().getNextSong().getArtist());
        this.current_Volume.setText(Double.toString(GUIController.getReader().getPlayer().getVolume()));
        //this.progressBar;
        this.insert_in_Panel();
    }

    //This method is used to return the updated MediaControlPanel.
    public HBox getMediaControlPanel() {
        return this.mediaCPanel;
    }
    

    
}
