package elektreader.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;


/**
 * This interface represents the logic of a media player.
*/
public interface Reader {

    /**
     *  List of supported songs types.
     */
    Set<String> SUPPORTED_FILES = Set.of("mp3", "wav");

    /**
     * @return the path of the environment choosed.
    */
    Optional<Path> getCurrentEnvironment();


    /**
     * refresh always the current environment.
     * 
     * @param root passed the path specified.
     * the environment can only contain playlists (sub-directories),
     * playlists can only contain files, if a sub-directory contains a playlist.
     * is not considered a playlist anymore and it can only be a container of playlists,
     * (a sub-directory cannot contains playlists and files at the same time),
     * otherwise the whole environment is considered illegal.
    */
    void setCurrentEnvironment(Path root);


    /**
     * @return the current playlist if selected, 
     *  optional because it can be empty.
    */
    Optional<PlayList> getCurrentPlaylist();


    /**
     * @param playlist given a playlist check if the playlist is present, and set it as current playlist.
    */
    void setCurrentPlaylist(Optional<PlayList> playlist);


    /**
     * @return the list of the total playlists founded
    */
    List<PlayList> getPlaylists();


    /**
     * @return the total number of the playlists founds
    */
    int getNPlaylists();


    /**
     * @param path given a specified path
     * @return return the specified PlayList
     * Optional because if the path is invalid!
    */
    Optional<PlayList> getPlaylist(Path path);

    /**
     * @return the MediaControl
     * can throw an IllegalStateException, because a player
     * is always present if the root is present
    */
    MediaControl getPlayer();

    /**
     * @param song passed a song path
     * @return true if is a supported song, false otherwise.
     * song is valid only if:
     *  its format is SUPPORTED (check if is in SUPPORTED_FILES)
     *  its size is more of 100KB
     */
    static boolean isSupportedSong(final Path song) {
        try (FileInputStream ris = new FileInputStream(song.toFile())) {
            final int minimumSize = 100 * 1024; // 100KB
            return SUPPORTED_FILES.stream().anyMatch(type -> song.toString().matches(".*\\." + type)) 
                && song.toFile().length() > minimumSize;
        } catch (IOException e) {
            //System.out.println("la canzone " + song + " non e supportata: " + e.toString());
            return false;
        }
    }

    /**
     * @param playlist given a playlist, filter all the songs:
     * a songs need to be a supported file
     * a song title need to be unique so it filter every song and considarate only 1 file per name.
     * @return Optional: can be:
     *  empty       -> if the playlist is invalid (no songs found).
     *  List<Path>  -> if the playlist contain at least 1 song, so the playlist is valid. 
    */
    static Optional<List<Path>> getAndFilterSongs(final Path playlist) {
        /* given a playlist path filter all the possible songs: */
        final List<Path> songs = new ArrayList<>(Collections.emptyList());
        try (Stream<Path> filesPaths = Files.list(playlist).filter(Files::isRegularFile)) {
            final var files = filesPaths.filter(Reader::isSupportedSong).toList();
            for (final Path songPath : files) {
                if (!songs.stream().map(Song::getTitle).anyMatch(t -> t.equals(Song.getTitle(songPath)))) {
                    songs.add(songPath);
                }
            }
        } catch (IOException e) { 
            System.out.println("the current environment can't handle this playlist, this is not a valid playlist"); //NOPMD
        }
        return !songs.isEmpty() ? Optional.of(songs) : Optional.empty();
    }

    /**
     * @param oldFile given the old file
     * @param newFile given the new file
     * rename the old file with the new
     * @return true if the File is renamed successfully, false otherwise
     */
    static boolean saveFile(final File oldFile, final File newFile) {
        return oldFile.renameTo(newFile);
    }
}
