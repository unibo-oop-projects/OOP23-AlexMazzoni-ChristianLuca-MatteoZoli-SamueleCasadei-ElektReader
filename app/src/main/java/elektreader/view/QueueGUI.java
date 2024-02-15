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

public class QueueGUI {
    public QueueGUI() {
        Stage queueStage = new Stage();
		VBox pane = new VBox();
		//pane.setPadding(new Insets(15));
        pane.setPadding(new Insets(15));
		pane.setSpacing(2);
        for (var song : GUIController.getReader().getPlayer().getPlaylist()) {
               pane.getChildren().add(createLabel(song));
        }
		Scene scene = new Scene(pane);
		queueStage.setScene(scene);
		queueStage.show();
    }

    private Node createLabel(final Song song) {
        Label label = new Label(String.format("%2s.\t%s\t-\t%s\t|\t%s\t|\t%s",
        Mp3PlayList.getIndexFromName(song.getFile().getName()).isPresent() ?
            Mp3PlayList.getIndexFromName(song.getFile().getName()).get().toString()
            :   "  ",
        song.getName(),
        song.getArtist().isPresent() ? song.getArtist().get() : "no artist",
        song.durationStringRep(), song.getFileFormat()) );
        return label;
    }
}
