package elektreader.api;

import java.io.File;
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
    public boolean setCurrentEnvironment(final File folder);


    public Optional<File> getCurrentEnvironment();

    
    public boolean setCurrentPlaylist();
    
    
    public Optional<File> getCurrentPlaylist();

    
    public int getNPlaylists(); 

    
    public boolean setCurrentSong();
    
    
    public Optional<File> getCurrentSong();

    
    public int getNSongs();

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