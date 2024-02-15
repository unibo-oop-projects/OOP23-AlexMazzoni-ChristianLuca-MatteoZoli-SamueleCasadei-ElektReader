package elektreader.view;

import elektreader.api.Song;
import elektreader.controller.GUIController;
import elektreader.model.Mp3PlayList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * class that create a new stage with the songs queue,
 * this class can be created only if READER.getPlayer is present.
 */
public class QueueGUI {

    private static final Double PADDING = 15.0; 

    private static final Double SPACING = 2.0; 

    /**
     * create in swing mode the new JavaFX stage.
     */
    public QueueGUI() {
        final Stage queueStage = new Stage();
        final VBox pane = new VBox();

        //pane.setPadding(new Insets(15));
        pane.setPadding(new Insets(PADDING));
        pane.setSpacing(SPACING);
        for (final var song : GUIController.READER.getPlayer().getPlaylist()) {
            pane.getChildren().add(createLabel(song));
        }
        final Scene scene = new Scene(pane);
        queueStage.setScene(scene);
        queueStage.show();
    }

    private Node createLabel(final Song song) {
        return new Label(String.format("%2s.\t%s\t-\t%s\t|\t%s\t|\t%s",
        Mp3PlayList.getIndexFromName(song.getFile().getName()).isPresent() 
            ? Mp3PlayList.getIndexFromName(song.getFile().getName()).get().toString()
            :   "  ",
        song.getName(),
        song.getArtist().isPresent() ? song.getArtist().get() : "no artist",
        song.durationStringRep(), song.getFileFormat()));
    }
}
