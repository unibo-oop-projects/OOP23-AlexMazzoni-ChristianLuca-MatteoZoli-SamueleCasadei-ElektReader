package elektreader.controller;

import java.io.File;

import elektreader.api.TrackGUIObserver;
import elektreader.api.TrackTrimmer;
import elektreader.model.TrackTrimmerImpl;
import elektreader.view.TrimGUIImpl;

import javafx.stage.Window;

public class TrackTrimmerController implements TrackGUIObserver{

    private final TrimGUIImpl view;
    private final TrackTrimmer trimmer;
    
    public TrackTrimmerController(final Window window) {
        this.view = new TrimGUIImpl(window);
        this.trimmer = new TrackTrimmerImpl();
        this.view.setController(this);
    }

    @Override
    public void chooseFile(final File track) {
        this.trimmer.setTrack(track.toPath());
        this.view.showFile(track.getName());
    }

    @Override
    public void getParameters(final String start, final String end, final String name) {
        if (this.trimmer.trim(start, end, name)) {
            this.view.success(true);
        } else {
            this.view.success(false);
        }
    }

}
