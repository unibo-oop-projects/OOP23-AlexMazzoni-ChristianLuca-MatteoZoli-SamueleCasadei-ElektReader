package elektreader.api;

import javafx.util.Duration;


/*
 * This interface models the logic about MediaControl aspect of our MP3Reader. It consists of a lot of helper methods that
 * are useful about retrieving data, aminpulating reproduction eccetera.
 */
public interface MediaControl {

    //private boolean isOver(final Song song);

    /**
     * Starts mediaPlayer execution.
     */
    public void play();

    /**
     * Pauses mediaPlayer execution.
     */
    public void pause();

    /**
     * Stops mediaPlayer execution
     */
    public void stop();

    /**
     * Changes current played song to the next in the queue.
     */
    public void next();

    /**
     * Changes current played song in the previous in the queue
     */
    public void prev();
    
    /**
     * Creates a playing loop with the current song played by our mediaPLayer.
     */
    public void loop();

    /**
     * @param queue the queue to be set as the current one.
     */
    public void setQueue(final Iterable<Song> queue);

    /**
     * @param song the song to be set as the current one.
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
