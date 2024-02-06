package elektreader.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import elektreader.model.Mp3Song;

/**
 * This interface represents the logic of a 
 * media player 
*/
public interface Reader {

    /**
     *  List of supported songs types
     */
    public static final Set<String> SUPPORTED_FILES = Set.of("mp3", "wav");

    /**
     * @return the path of the environment choosed
    */
    public Optional<Path> getCurrentEnvironment();


    /**
     * refresh always the current environment
     * 
     * @param root passed the path specified
     * the environment can only contain playlists (sub-directories),
     * playlists can only contain files, if a sub-directory contains a playlist
     * is not considered a playlist anymore and it can only be a container of playlists
     * (a sub-directory cannot contains playlists and files at the same time),
     * otherwise the whole environment is considered illegal.
     * 
     * @return true if it can be used as current environment so if it contain at least 1 playlist,
     * false otherwise (if the environment not contain any playlist)
    */
    public boolean setCurrentEnvironment(final Path root);
    

    /**
     * @return the current playlist if selected, 
     *  optional because it can be empty.
    */
    public Optional<PlayList> getCurrentPlaylist();


    /**
     * @param playlist given a playlist check if the playlist is present, and set it as current playlist
     * @return true if the playlist is set as current playlist,
     *  false if cannot set as a current playlist (playlist can be empty).
    */
    public boolean setCurrentPlaylist(final Optional<PlayList> playlist);

    
    /**
     * @return the list of the total playlists founded
    */
    public List<PlayList> getPlaylists();


    /**
     * @return the total number of the playlists founds
    */
    public int getNPlaylists();


    /**
     * @param path given a specified path
     * @return return the specified PlayList
     * Optional because if the path is invalid!
    */
    public Optional<PlayList> getPlaylist(final Path path);
    
    
    /**
     * @return the current song is selected
     * (it can be empty)
    */
    public Optional<Song> getCurrentSong();


    /**
     * @param song given a song if is valid
     * @return true if is set ad surrent, false otherwise
    */
    public boolean setCurrentSong(final Optional<Song> song);

    /**
     * @return the MediaControl, optional because can be in a init state
    */
    public Optional<MediaControl> getPlayer();

    /**
     * @param song passed a song path
     * @return true if is a supported song, false otherwise.
     */
    public static boolean isSupportedSong(final Path song) {
        return SUPPORTED_FILES.stream().anyMatch(type -> song.toString().matches(".*\\."+type));
    }

    /**
     * @param playlist given a playlist, filter all the songs:
     * a songs need to be a supported file
     * a song title need to be unique so it filter every song and considarate only 1 file per name.
     * @return Optional: can be:
     *  empty       -> if the playlist is invalid (no songs found).
     *  List<Path>  -> if the playlist contain at least 1 song, so the playlist is valid. 
    */
    public static Optional<List<Path>> getAndFilterSongs(final Path playlist) {
        /* given a playlist path filter all the possible songs: */
        List<Path> songs = new ArrayList<>(Collections.emptyList());
        try (Stream<Path> filesPaths = Files.list(playlist)) {
            var files = filesPaths.filter(Reader::isSupportedSong).toList();
            songs = files.stream().filter(t -> {
                return files.stream()
                        .map(Song::getTitle)
                        .anyMatch(s -> s.equals(Song.getTitle(t)));
            }).toList();
        } catch (IOException e) { 
            System.out.println("the current environment can't handle this playlist, this is not a valid playlist");
        }
        return !songs.isEmpty() ? Optional.of(songs) : Optional.empty();
    }

    /**
     * @param oldFile given the old file
     * @param newFile given the new file
     * rename the old file with the new
     * @retur true if the File is renamed successfully, false otherwise
     */
    public static boolean saveFile(final File oldFile, final File newFile) {
        try {
            oldFile.renameTo(newFile);
        } catch (Exception e) { 
            return false;
        }
        return true;
    }
}