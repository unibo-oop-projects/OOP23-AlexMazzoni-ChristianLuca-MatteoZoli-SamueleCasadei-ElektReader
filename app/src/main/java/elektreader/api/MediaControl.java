package elektreader.api;

import java.util.List;
import java.util.Optional;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


/*
 * This interface models the logic about MediaControl aspect of our MP3Reader. It consists of a lot of utility methods that
 * are useful about retrieving data, manipulating reproduction, eccetera.
 */
public interface MediaControl {

    static enum Status {
        PLAYING, PAUSED;
    }

    static enum LoopStatus {
        OFF, PLAYLIST, TRACK
    }


    /**
     * @param playList the playlist to be set as the current one.
     * @return true if the playlist is set as the current one correctly, false otherwise.
     */
    public boolean setPlaylist(PlayList playList);

    /**
     * @return current played song by our MediaPlayer.
     */
    public Song getCurrentSong();
    
    /**
     * @return the next Song to be set.
     */
    public Optional<Song> getNextSong();

    /**
     * @return Playlist currently set as the current one. ONLY DEBUG!
     */
    public List<Song> getPlaylist();

    /**
     * Starts MediaPlayer execution.
     */
    public void play();

    /**
     * Pauses MediaPlayer execution.
     */
    public void pause();

    /**
     * Stops MediaPlayer execution
     */
    public void stop();

    /**
     * Changes current played song according to the current loop status.
     */
    public void nextSong();

    /**
     * Changes current played song with the previous in the playlist.
     */
    public void prevSong();
    
    /**
     * Sets the loop status either on true for the playlist or true for the current song or turns the loop off.
     */
    public void loopSong();

    /**
     * Returns the current status of the loop functionality.
     */
    public LoopStatus getLoopStatus();

    /**
     * Shuffles the current playlist or resets it to its normal behaviour.
     */
    public void rand();

    /**
     * Returns true if random functionality is enabled, false otherwise.
     */
    public boolean getRandStatus();

    /**
     * @param song the song to be set as the current one.
     * @return true if the operation ends with a success, false otherwise.
     */
    public boolean setSong(final Song song);

    /**
     * @param rate the speed rate to be assigned to our mediaPlayer.
     */
    public void setRepSpeed(final double rate);

    /**
     * @param duration the new playback time that must be assigned to our mediaPlayer
     */
    public void setProgress(final Duration duration);

    /**
     * @return the Duration of song currently played by our mediaPlayer.
     */
    public double getDuration();

    /**
     * @param volume the Volume that must be assigned to our mediaPlayer. 
     */
    public void setVolume(final double volume);

    /**
     * @return the current Volume assigned to our mediaPlayer in double.
     */
    public double getVolume();

    /**
     * @return Prgoress done by our mediaPlayer.
     */
    public double getProgress();

    /**
     * @return our mediaPlayer.
     */
    public MediaPlayer getMediaControl();

    /**
     * @return current mediaPLayer status.
     */
    public Status getStatus();

}

