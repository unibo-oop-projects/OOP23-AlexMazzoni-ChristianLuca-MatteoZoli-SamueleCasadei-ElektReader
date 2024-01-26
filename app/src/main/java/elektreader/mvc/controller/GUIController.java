package elektreader.mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class GUIController {
    @FXML
	private VBox playlistsList;

	@FXML
	private Button btnPlaylistsShowPanel;

    @FXML
    private SplitPane splitPlayListsSongs;

	@FXML
	public void showPlayLists() {
		splitPlayListsSongs.setDividerPosition(0, 0);
	}
}
