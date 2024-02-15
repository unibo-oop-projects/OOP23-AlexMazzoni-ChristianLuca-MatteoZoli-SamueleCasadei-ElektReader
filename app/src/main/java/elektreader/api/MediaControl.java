package elektreader.api;

import java.util.List;
import java.util.Optional;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


/**
 * This interface models the logic about MediaControl aspect of our MP3Reader. It consists of a lot of utility methods that
 * are useful about retrieving data, manipulating reproduction, eccetera.
 */
public interface MediaControl {

    /**
     * This enum is used to check the current status of our mediaPlayer.
     */
    enum Status {
        /**
         *  PLAYING is the status of the mediaControl when a media is currently played.
         */
        PLAYING, /**
         *  PAUSED is the status of the mediaControl when a media is currently paused.
         */
        PAUSED;
    }

    /**
     * TO DO: MATTEO.
     */
    enum LoopStatus {
        /**
         * TO DO MATTEO.
         */
        OFF, /**
         * TO DO MATTEO.
         */
        PLAYLIST, /**
         * TO DO MATTEO.
         */
        TRACK
    }

    /**
     * @param playList the playlist to be set as the current one.
     * @return true if the playlist is set as the current one correctly, false otherwise.
     */
    boolean setPlaylist(PlayList playList);
    /**
     * @return current played song by our MediaPlayer.
     */
    Song getCurrentSong();
    /**
     * @return the next Song to be set.
     */
    Optional<Song> getNextSong();
    /**
     * @return Playlist currently set as the current one. ONLY DEBUG!
     */
    List<Song> getPlaylist();
    /**
     * Starts MediaPlayer execution.
     */
    void play();
    /**
     * Pauses MediaPlayer execution.
     */
    void pause();
    /**
     * Stops MediaPlayer execution.
     */
    void stop();
    /**
     * Changes current played song according to the current loop status.
     */
    void nextSong();
    /**
     * Changes current played song with the previous in the playlist.
     */
    void prevSong();
    /**
     * Sets the loop status either on true for the playlist or true for the current song or turns the loop off.
     */
    void loopSong();
    /**
     * @return the current status of the loop functionality.
     */
    LoopStatus getLoopStatus();
    /**
     * Shuffles the current playlist or resets it to its normal behaviour.
     */
    void rand();
    /**
     * @return true if random functionality is enabled, false otherwise.
     */
    boolean getRandStatus();
    /**
     * @param song the song to be set as the current one.
     * @return true if the operation ends with a success, false otherwise.
     */
    boolean setSong(Song song);
    /**
     * @param rate the speed rate to be assigned to our mediaPlayer.
     */
    void setRepSpeed(double rate);
    /**
     * @param duration the new playback time that must be assigned to our mediaPlayer
     */
    void setProgress(Duration duration);
    /**
     * @return the Duration of song currently played by our mediaPlayer.
     */
    double getDuration();
    /**
     * @param volume the Volume that must be assigned to our mediaPlayer. 
     */
    void setVolume(double volume);
    /**
     * @return the current Volume assigned to our mediaPlayer in double.
     */
    double getVolume();
    /**
     * @return Prgoress done by our mediaPlayer.
     */
    double getProgress();
    /**
     * @return our mediaPlayer.
     */
    Optional<MediaPlayer> getMediaControl();
    /**
     * @return current mediaPlayer status.
     */
    Status getStatus();

}

