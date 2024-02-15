package elektreader.api;

import java.io.File;

/**
 * Interface to describe the behaviour of the trim observer.
 */
public interface TrimObserver {

    /**
     * Chooses a file to pass to the trimmer.
     *
     * @param track the file selected
     */
    void chooseFile(File track);

    /**
     * Retrieves the parameters for a track.
     *
     * @param start the start parameter of the track
     * @param end the end parameter of the track
     * @param name the name parameter of the track
     */
    void retrieveParameters(String start, String end, String name);
}
