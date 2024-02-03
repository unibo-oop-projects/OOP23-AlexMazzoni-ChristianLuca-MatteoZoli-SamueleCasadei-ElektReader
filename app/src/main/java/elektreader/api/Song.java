package elektreader.api;

import java.io.File;
import java.util.Optional;

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
     * (with the possibility it's not present, so Optional)
     */
    Optional<String> getArtist();

    /**
     * @return song's album, read through metadata
     * (with the possibility it's not present, so Optional)
     */
    Optional<String> getAlbumName();

    /**
     * @return song's genre, read through metadata
     * (with the possibility it's not present, so Optional)
     */
    Optional<String> getGenre();

    /**
     * @return the file which represents the song, the file is not an Optional
     * it's always present.
     */
    File getFile();

    /**
     * @return song's duration in seconds, the duration is not optional
     */
    int getDuration();

    /**
     * @return song's duration in the formato h:mm:ss 
     */
    String DurationStringRep();

}