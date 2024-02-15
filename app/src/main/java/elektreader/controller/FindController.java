package elektreader.controller;

import java.util.Optional;

import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FindController {
    
    private VBox container;

    public FindController() {}

    public void show(final Pane findPane) {
        AnchorPane mainContainer = new AnchorPane();
        mainContainer.setPrefWidth(findPane.getHeight());
        VBox centerContainer = new VBox();
        centerContainer.getStyleClass().add("root");
        mainContainer.getChildren().add(centerContainer);
        AnchorPane.setTopAnchor(centerContainer, (double) 20);
        AnchorPane.setLeftAnchor(centerContainer, (double) 20);
        AnchorPane.setRightAnchor(centerContainer, (double) 20);
        AnchorPane.setBottomAnchor(centerContainer, (double) 20);
        
        AnchorPane anchorInputContainer = new AnchorPane();
        TextField input = new TextField();
        input.setPromptText("Find...");
        anchorInputContainer.getChildren().add(input);
        AnchorPane.setTopAnchor(input, (double) 20);
        AnchorPane.setLeftAnchor(input, (double) 20);
        AnchorPane.setRightAnchor(input, (double) 20);

        AnchorPane anchorLabelContainer = new AnchorPane();
        this.container = new VBox();
        this.container.setSpacing(15);
        anchorLabelContainer.getChildren().add(this.container);
        AnchorPane.setTopAnchor(this.container, (double) 20);
        AnchorPane.setLeftAnchor(this.container, (double) 60);
        AnchorPane.setRightAnchor(this.container, (double) 60);
        AnchorPane.setBottomAnchor(this.container, (double) 20);

        centerContainer.getChildren().addAll(anchorInputContainer, anchorLabelContainer);
    
        findPane.getChildren().add(centerContainer);

        input.onInputMethodTextChangedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue!=null && newValue!=null) {
                if (oldValue.equals(newValue)) {
                    container.getChildren().clear();
                }
    
                for (final var element : GUIController.READER.getPlaylists()) {
                    final var box = createBox(find(newValue.toString()));
                }
            } else {
                container.getChildren().clear();
            }
        });
    
    }

    private <T extends PlayList & Song> Optional<HBox> createBox(final Optional<T> element) {
        if (element.isEmpty()) {
            return Optional.empty();
        } else {
            final HBox box = new HBox();

            return Optional.of(box);
        }
    }

    private <T extends PlayList & Song> T find(String string) {
        if(GUIController.READER.getPlaylists().stream().anyMatch(t -> t.getName().matches(string))) {
            return null;
        }
        return null;
    }
}
