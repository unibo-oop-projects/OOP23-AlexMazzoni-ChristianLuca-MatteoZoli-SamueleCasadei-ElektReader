package elektreader.api;

import java.io.File;

import javafx.util.Duration;
/**
 * An interface representing the idea of a track which is playable by ElektReader.
 * Creating an interface to program against makes the project way more flexible and
 * leaves space for eventual future implementations.
 */
public interface Song {

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
     * @return the file which represents the song
     */
    File getFile();

    /**
     * @return song's duration
     */
    Duration getDuration();

}