package elektreader.controller;

import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.scene.Node;
import javafx.scene.control.TextField;
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
        
        AnchorPane inputContainer = new AnchorPane();
        TextField input = new TextField();
        input.setPromptText("Find...");
        inputContainer.getChildren().add(input);
        AnchorPane.setTopAnchor(input, (double) 20);
        AnchorPane.setLeftAnchor(input, (double) 20);
        AnchorPane.setRightAnchor(input, (double) 20);

        AnchorPane labelContainer = new AnchorPane();
        this.container = new VBox();
        this.container.setSpacing(15);
        labelContainer.getChildren().add(this.container);
        AnchorPane.setTopAnchor(this.container, (double) 20);
        AnchorPane.setLeftAnchor(this.container, (double) 60);
        AnchorPane.setRightAnchor(this.container, (double) 60);
        AnchorPane.setBottomAnchor(this.container, (double) 20);

        centerContainer.getChildren().addAll(inputContainer, labelContainer);
    }

    private <T extends PlayList & Song> HBox createBox(final T element) {
        return null;
    }

    public Node load(final Pane panel) {
        return null;
    }

}
