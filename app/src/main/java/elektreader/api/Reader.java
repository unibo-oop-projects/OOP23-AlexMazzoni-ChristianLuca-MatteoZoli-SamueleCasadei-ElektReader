package elektreader.api;


import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


/**
 * This interface represents the logic of a 
 * media player 
 */
public interface Reader {

    /**
     * @param folder passed the path specified
     * the environment can only contain playlists (sub-directories),
     * playlists can only contain files, if a sub-directory contains a playlist
     * is not considered a playlist anymore and it can only be a container of playlists
     * (a sub-directory cannot contains playlists and files at the same time),
     * otherwise the whole environment is considered illegal.
     * 
     * @return true if it can be used as current environment, 
     * else wait for a valid folder and false is return (the program doesn't stop if is invalid).
     */
    public boolean setCurrentEnvironment(final Path folder);


    /**
     * @return the path of the environment choosed
     */
    public Optional<Path> getCurrentEnvironment();

    
    /**
     * @param playlist given a playlist check if the playlist is present, and set it as current playlist
     * @return true if the playlist is set as current playlist,
     *  false if cannot set as a current playlist (playlist can be empty).
     */
    public boolean setCurrentPlaylist(final Optional<PlayList> playlist);
    
    
    /**
     * @return the current playlist if selected, 
     *  optional because it can be empty.
     */
    public Optional<PlayList> getCurrentPlaylist();

    
    /**
     * @return the list of the total playlists founded
     */
    public Optional<List<PlayList>> getPlaylists();


    /**
     * @return the total number of the playlists founds
     */
    public int getNPlaylists();


    /**
     * non molto utile
     * @param path given a specified path
     * @return return the specified PlayList
     */
    public Optional<PlayList> getPlaylist(final Path path);

    
    /**
     * @param song given a song if is valid
     * @return true if is set ad surrent, false otherwise
     */
    public boolean setCurrentSong(final Optional<Song> song);
    
    
    /**
     * @return the current song is selected
     * (it can be empty)
     */
    public Optional<Song> getCurrentSong();


    /**
     * @return true if current song is playing, false otherwise.
     */
    public boolean getStatusPlay();





    //Add - on

    /**
     * salva tutte le modifiche che sono state
     * effettuate alle canzoni nel file system
     */
    public void saveCurrentEnvironment();

}