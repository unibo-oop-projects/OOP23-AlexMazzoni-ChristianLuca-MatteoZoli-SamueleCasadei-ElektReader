package elektreader.api;

import java.io.File;

public interface TrackGUIObserver {
    
    
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
    void getParameters(String start, String end, String name);
}
