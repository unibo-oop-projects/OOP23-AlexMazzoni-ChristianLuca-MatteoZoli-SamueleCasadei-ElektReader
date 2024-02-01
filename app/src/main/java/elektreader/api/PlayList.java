package elektreader.api;

import java.util.Set;
import javafx.util.Duration;

/**
 * A class which represents the concept of a playlist of songs
 */
public interface PlayList {

    /**
     * @return the set of songs contained in the playlist, this is not used in
     * the actual reproduction
     */
    Set<Song> getSongs();

    /**
     * @return the name of the current playlist, which is also the directory name
     */
    String getName();

    /**
     * @return the queue of songs used for the actual reproduction of tracks
     */
    Iterable<Song> getQueue();

    /**
     * shuffles the reproduction queue
     */
    void shuffleQueue();

    /**
     * starts to play the queue from the first song
     */
    void playQueue();

    /**
     * @return the complessive duration of all the songs in this playlist
     */
    Duration getTotalDuration();

    /**
     * @return the number of songs contained in this playlist
     */
    int getSize();

    /**
     * @param song the track to add
     * @return true if the song has been added, false otherwise
     */
    boolean addSong(Song song);

    /**
     * @param song the song to be removed from the playlist
     * @return true if the song has been removed (it was present), false otherwise
     */
    boolean removeSong(Song song);
    
}
