package elektreader.api;

import javafx.util.Duration;

public interface MediaControl {

    //private boolean isOver(final Song song);

    public void play();

    public void pause();

    public void stop();

    public void next();

    public void prev();
    
    public void loop();

    public void setQueue(final Iterable<Song> queue);

    public void setSong(final Song song);

    public void setRepSpeed(final double rate);

    public void setProgress(final Duration duration);

    public Duration getDuration();

    public void setVolume(final double volume);

    public double getVolume();

    public void mute();


}
