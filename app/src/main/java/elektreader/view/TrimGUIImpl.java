package elektreader.view;

import elektreader.api.TrimGUI;
import elektreader.api.TrackTrimmer;
import elektreader.controller.TrackTrimmerController;
import elektreader.model.TrackTrimmerImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TrimGUIImpl implements TrimGUI{

    private static final int TEXTFIELD_WIDTH = 220;
	private static final int PANE_HEIGHT = 300;
	private static final int PANE_WIDTH = 270;
	private static final int GAP_UNITS = 10;
	private static final int PADDING_UNITS = 15;
	private static final String WHITE_STRING = "-fx-mid-text-color: #ffffff";

	Label resultLabel, trackLabel;

	private TrackTrimmerController controller;

    public TrimGUIImpl(final Window primaryStage) {
        Stage trimStage = new Stage();
		trimStage.initModality(Modality.WINDOW_MODAL);
		trimStage.initOwner(primaryStage);
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(PADDING_UNITS));
		pane.setHgap(GAP_UNITS);
		pane.setVgap(GAP_UNITS);
		pane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
		trackLabel = new Label();
		Label firstLabel = new Label("1.");
		Label secondLabel = new Label("2.");
		Label thirdLabel = new Label("3.");
		Label fourthLabel = new Label("4.");
		Label fifthLabel = new Label("5.");
		resultLabel = new Label();
		Button fileBtn = new Button("Select track");
		fileBtn.setOnMouseClicked(e -> {
			final FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(
				new ExtensionFilter("MP3, wav", "*.mp3", "*.wav"));
			controller.chooseFile(fileChooser.showOpenDialog(null));
		});
		TextField startCut = new TextField("Insert start (hh:mm:ss or seconds)");
		TextField endCut = new TextField("Insert end (hh:mm:ss or seconds)");
		TextField newName = new TextField("Insert the name for the trimmed track");
		startCut.setPrefWidth(TEXTFIELD_WIDTH);
		endCut.setPrefWidth(TEXTFIELD_WIDTH);
		newName.setPrefWidth(TEXTFIELD_WIDTH);
		Button trimBtn = new Button("Trim");
		trimBtn.setOnMouseClicked(e -> 
			this.controller.getParameters(startCut.getText(), endCut.getText(), newName.getText()));
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
		pane.add(trackLabel, 1, 5);
		pane.add(resultLabel, 1, 6);
		pane.setStyle("-fx-background-color: #000000");
		trackLabel.setStyle(WHITE_STRING);
		firstLabel.setStyle(WHITE_STRING);
		secondLabel.setStyle(WHITE_STRING);
		thirdLabel.setStyle(WHITE_STRING);
		fourthLabel.setStyle(WHITE_STRING);
		fifthLabel.setStyle(WHITE_STRING);
		resultLabel.setStyle(WHITE_STRING);
		Scene scene = new Scene(pane);
		trimStage.setScene(scene);
		trimStage.show();
    }

	@Override
	public void setController(final TrackTrimmerController controller) {
		this.controller = controller;
	}

	@Override
	public void showFile(String fileName) {
		this.trackLabel.setText("Selected: " + fileName);
	}

	@Override
	public void success(final boolean flag) {
		this.resultLabel.setText(flag ? "New track created" : "Operation failed");	
	}
}
