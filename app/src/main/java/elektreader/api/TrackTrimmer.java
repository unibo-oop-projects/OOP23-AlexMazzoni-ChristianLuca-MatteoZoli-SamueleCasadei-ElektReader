package elektreader.api;

import java.nio.file.Path;

/**
 * Interface that defines user's action to trim a track.
 */
public interface TrackTrimmer {

    /**
     * Trims a track based on the specified start and end times, and saves it with the given name.
     * @param start The start time of the trim.
     * @param end The end time of the trim.
     * @param name The name to be given to the trimmed track.
     * @return message regarding the end-status of the operation.
     */
    String trim(String start, String end, String name);

    /**
     * Sets a track.
     * @param path path to the specified file.
     */
    void setTrack(Path path);
}
