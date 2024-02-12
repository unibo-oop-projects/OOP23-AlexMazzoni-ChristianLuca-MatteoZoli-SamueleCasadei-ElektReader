package elektreader.api;

import java.util.List;

import javafx.util.Duration;


/*
 * This interface models the logic about MediaControl aspect of our MP3Reader. It consists of a lot of utility methods that
 * are useful about retrieving data, manipulating reproduction, eccetera.
 */
public interface MediaControl {

    static enum Status {
        PLAYING, PAUSED;
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
    public Song getNextSong();

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
     * Changes current played song with the next in the playlist.
     */
    public void nextSong();

    /**
     * Changes current played song with the previous in the playlist.
     */
    public void prevSong();
    
    /**
     * Creates a playing loop with the current song played by our mediaPLayer.
     */
    public void loopSong();

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
     * Sets the Volume of our mediaPlayer to 0.0.
     */
    public void mute();

    public double getProgress();

    public Status getStatus();

}

