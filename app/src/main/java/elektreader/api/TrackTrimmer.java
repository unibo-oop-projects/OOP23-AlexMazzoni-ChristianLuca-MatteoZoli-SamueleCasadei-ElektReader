package elektreader.api;

import java.nio.file.Path;

/**
 * Interface that defines user's action to trim a track
 */
public interface TrackTrimmer {

    /**
     * Opens a file chooser dialog for the user to select an MP3 or WAV track file.
     */
    void chooseTrack();

    /**
     * Trims a track based on the specified start and end times, and saves it with the given name.
     * @param start The start time of the trim.
     * @param end The end time of the trim.
     * @param name The name to be given to the trimmed track.
     * @return true if a new track has been created.
     */
    boolean trim(String start, String end, String name);

    /**
     * Test-only method to set a track without using a FileChooser.
     * @param path
     */
    void setTrack(Path path);
}
