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
    public Set<Song> getSongs();

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

    Duration getTotalDuration();

    int getSize();

    boolean addSong(Song song);

    boolean removeSong(Song song);
    
}
