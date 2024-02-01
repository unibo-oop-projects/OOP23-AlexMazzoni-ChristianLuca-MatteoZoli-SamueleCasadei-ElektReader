package elektreader.api;

import javafx.util.Duration;
/**
 * An interface representing the idea of a track which is playable by ElektReader.
 * Creating an interface to program against makes the project way more flexible and
 * leaves space for eventual future implementations.
 */
public interface Song {

    /**
     * @return true if this song is currently playing, false otherwise
     */
    boolean isPlaying();

    /**
     * Plays this song only if it is already playing
     */
    void play();

    /**
     * Pauses this song only if it is playing
     */
    void pause();

    /**
     * @return song's title, read through metadata
     */
    String getName();

    /**
     * @return song's author, read through metadata
     */
    String getArtist();

    /**
     * @return song's album, read through metadata
     */
    String getAlbumName();

    /**
     * @return song's genre, read through metadata
     */
    String getGenre();

    /**
     * @return song's duration
     */
    Duration getDuration();

    /**
     * @return the volume of the song, if it's currently playing.
     * otherwise zero
     */
    Double getVolume();

    /**
     * Sets the volume of the song to a Defined amount
     * @param amount the amount of volume that the song will have, if it's currently
     * playing
     */
    void setVolume(Double amount);
    
}