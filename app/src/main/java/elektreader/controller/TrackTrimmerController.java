package elektreader.controller;

import java.io.File;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import elektreader.api.TrimObserver;
import elektreader.api.TrackTrimmer;
import elektreader.model.TrackTrimmerImpl;
import elektreader.view.TrimGUIImpl;

import javafx.stage.Window;

/**
 * mvc controller for trimGui and TrackTrimmer.
 */
public class TrackTrimmerController implements TrimObserver {

    private final TrimGUIImpl view;
    private final TrackTrimmer trimmer;

    /**
     * Constructor for TrackTrimmerController.
     * @param window
     */
    @SuppressFBWarnings(
        value = "MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR",
        justification = "need to set the controller in the view"
    )
    public TrackTrimmerController(final Window window) {
        this.view = new TrimGUIImpl(window);
        this.trimmer = new TrackTrimmerImpl();
        this.view.setController(this);
    }

    // CHECKSTYLE: DesignForExtension OFF
    @Override
    public void chooseFile(final File track) {
        this.trimmer.setTrack(track.toPath());
        this.view.showFile(track.getName());
    }

    @Override
    public void retrieveParameters(final String start, final String end, final String name) {
        this.view.success(this.trimmer.trim(start, end, name));
    }
    // CHECKSTYLE: DesignForExtension ON

}
