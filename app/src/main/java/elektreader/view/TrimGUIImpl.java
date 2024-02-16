package elektreader.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import elektreader.api.TrimGUI;
import elektreader.controller.TrackTrimmerController;
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

/**
 * Gui implementation used to trim tracks.
 */
public class TrimGUIImpl implements TrimGUI {

    private static final int TEXTFIELD_WIDTH = 220;
    private static final int PANE_HEIGHT = 300;
    private static final int PANE_WIDTH = 270;
    private static final int GAP_UNITS = 10;
    private static final int PADDING_UNITS = 15;
    private static final String WHITE_STRING = "-fx-mid-text-color: #ffffff";

    private final Label resultLabel, trackLabel;

    private TrackTrimmerController controller;

    /**
	 * Constructor for TrimGui.
	 * @param primaryStage
	 */
    public TrimGUIImpl(final Window primaryStage) {
        final Stage trimStage = new Stage();
		trimStage.initModality(Modality.WINDOW_MODAL);
		trimStage.initOwner(primaryStage);
		final GridPane pane = new GridPane();
		pane.setPadding(new Insets(PADDING_UNITS));
		pane.setHgap(GAP_UNITS);
		pane.setVgap(GAP_UNITS);
		pane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
		trackLabel = new Label();
		final Label firstLabel = new Label("1.");
		final Label secondLabel = new Label("2.");
		final Label thirdLabel = new Label("3.");
		final Label fourthLabel = new Label("4.");
		final Label fifthLabel = new Label("5.");
		resultLabel = new Label();
		final Button fileBtn = new Button("Select track");
		fileBtn.setOnMouseClicked(e -> this.openFileChooser());
		final TextField startCut = new TextField("Insert start (hh:mm:ss or seconds)");
		final TextField endCut = new TextField("Insert end (hh:mm:ss or seconds)");
		final TextField newName = new TextField("Insert the name for the trimmed track");
		startCut.setPrefWidth(TEXTFIELD_WIDTH);
		endCut.setPrefWidth(TEXTFIELD_WIDTH);
		newName.setPrefWidth(TEXTFIELD_WIDTH);
		final Button trimBtn = new Button("Trim");
		trimBtn.setOnMouseClicked(e -> 
			this.retrieveParameters(startCut.getText(), endCut.getText(), newName.getText()));
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
		// CHECKSTYLE: MagicNumber OFF
		//5 and 6 are not magic numbers: they're used to place two nodes on the pane 
		//and they're used only once so it isn't useful replacing them with a constant.
		pane.add(trackLabel, 1, 5);
		pane.add(resultLabel, 1, 6);
		// CHECKSTYLE: MagicNumber ON
		pane.setStyle("-fx-background-color: #000000");
		trackLabel.setStyle(WHITE_STRING);
		firstLabel.setStyle(WHITE_STRING);
		secondLabel.setStyle(WHITE_STRING);
		thirdLabel.setStyle(WHITE_STRING);
		fourthLabel.setStyle(WHITE_STRING);
		fifthLabel.setStyle(WHITE_STRING);
		resultLabel.setStyle(WHITE_STRING);
		final Scene scene = new Scene(pane);
		trimStage.setScene(scene);
		trimStage.show();
    }

	// CHECKSTYLE: DesignForExtension OFF
	@Override
	public void setController(final TrackTrimmerController controller) {
		this.controller = controller;
	}

	@Override
	public void showFile(final String fileName) {
		this.trackLabel.setText("Selected: " + fileName);
	}

	@Override
	public void success(final String message) {	
		this.resultLabel.setText(message);
	}
	// CHECKSTYLE: DesignForExtension ON

	@SuppressFBWarnings(
		value = "UwF",
		justification = "controller is initialized in its constructor"
	)
	private void openFileChooser() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
			new ExtensionFilter("MP3, wav", "*.mp3", "*.wav"));
		this.controller.chooseFile(fileChooser.showOpenDialog(null));
	}

	@SuppressFBWarnings(
		value = "UwF",
		justification = "controller is initialized in its constructor"
	)
	private void retrieveParameters(final String start, final String end, final String name) {
		this.controller.retrieveParameters(start, end, name);
	}
}
