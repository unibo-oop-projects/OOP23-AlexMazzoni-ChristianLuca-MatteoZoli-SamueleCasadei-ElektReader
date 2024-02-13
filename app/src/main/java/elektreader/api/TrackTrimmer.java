package elektreader.api;

import javafx.scene.control.TextField;

public interface TrackTrimmer {

    /**
     * Opens a file chooser dialog for the user to select an MP3 or WAV track file.
     */
    void chooseTrack();

    /**
     * Trims a track based on the specified start and end times, and saves it with the given name.
     * @param start The TextField that contains the start time of the trim.
     * @param end The TextField that contains the end time of the trim.
     * @param name The TextField that contains the name to be given to the trimmed track.
     */
    void trim(TextField start, TextField end, TextField name);
}
