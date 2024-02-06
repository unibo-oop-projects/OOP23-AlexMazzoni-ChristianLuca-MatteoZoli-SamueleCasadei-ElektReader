package elektreader.api;

import java.util.List;

import javafx.util.Duration;


/*
 * This interface models the logic about MediaControl aspect of our MP3Reader. It consists of a lot of helper methods that
 * are useful about retrieving data, aminpulating reproduction eccetera.
 */
public interface MediaControl {

    /**
     * @return current played song by our MediaPlayer.
     */
    public Song getCurrentSong();
    
    //Only debug
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
     * Plays current song selected.
     */
    public void currentSong();

    /**
     * Changes current played song to the next in the queue.
     */
    public void nextSong();

    /**
     * Changes current played song in the previous in the queue
     */
    public void prevSong();
    
    /**
     * Creates a playing loop with the current song played by our mediaPLayer.
     */
    public void loopSong();

    /**
     * @param song the song to be set as the current one. Internal implementation works with the 
     * reproduction queue too. Look at the implementation for further details.
     */
    public void setSong(final Song song);

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
    public Duration getDuration();

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

}

