package elektreader.view;

import elektreader.api.TrackTrimmer;
import elektreader.model.TrackTrimmerImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TrimGUI {

    private static final int TEXTFIELD_WIDTH = 220;
	private static final int PANE_HEIGHT = 300;
	private static final int PANE_WIDTH = 270;
	private static final int GAP_UNITS = 10;
	private static final int PADDING_UNITS = 15;
	private final TrackTrimmer trimmer = new TrackTrimmerImpl();

    public TrimGUI(final Window primaryStage) {
        Stage trimStage = new Stage();
		trimStage.initModality(Modality.WINDOW_MODAL);
		trimStage.initOwner(primaryStage);
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(PADDING_UNITS));
		pane.setHgap(GAP_UNITS);
		pane.setVgap(GAP_UNITS);
		pane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
		Label firstLabel = new Label("1.");
		Label secondLabel = new Label("2.");
		Label thirdLabel = new Label("3.");
		Label fourthLabel = new Label("4.");
		Label fifthLabel = new Label("5.");
		Button fileBtn = new Button("Select track");
		fileBtn.setOnMouseClicked(e -> trimmer.chooseTrack());
		TextField startCut = new TextField("Insert start (hh:mm:ss or seconds)");
		TextField endCut = new TextField("Insert end (hh:mm:ss or seconds)");
		TextField newName = new TextField("Insert the name for the trimmed track");
		startCut.setPrefWidth(TEXTFIELD_WIDTH);
		endCut.setPrefWidth(TEXTFIELD_WIDTH);
		newName.setPrefWidth(TEXTFIELD_WIDTH);
		Button trimBtn = new Button("Trim");
		trimBtn.setOnMouseClicked(e -> trimmer.trim(startCut.getText(), endCut.getText(), newName.getText()));
		pane.add(firstLabel, 0, 0);
		pane.add(secondLabel, 0, 1);
		pane.add(thirdLabel, 0, 2);
		pane.add(fourthLabel, 0, 3);
		pane.add(fifthLabel, 0, 4);
		pane.add(fileBtn, 1, 0);
		pane.add(startCut, 1, 1);
		pane.add(endCut, 1, 2);
		pane.add(newName, 1, 3);
		pane.add(trimBtn, 1, 4);
		pane.setStyle("-fx-background-color: #000000");
		firstLabel.setStyle("-fx-mid-text-color: #ffffff");
		secondLabel.setStyle("-fx-mid-text-color: #ffffff");
		thirdLabel.setStyle("-fx-mid-text-color: #ffffff");
		fourthLabel.setStyle("-fx-mid-text-color: #ffffff");
		fifthLabel.setStyle("-fx-mid-text-color: #ffffff");
		Scene scene = new Scene(pane);
		trimStage.setScene(scene);
		trimStage.show();
    }

}
