package elektreader.api;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.util.Duration;

public interface MediaControl {

    //private boolean isOver(final Song song);

    public void play();

    public void pause();

    public void stop();

    public boolean next();

    public boolean prev();
    
    public boolean loop(final Song song);

    public void setQueue(final Iterable<Song> queue);

    public void setSong(final Media song);

    public boolean setRepSpeed();

    public boolean setProgress(final Duration duration);

    public Duration getDuration();

    public boolean setVolume(final DoubleProperty volume);

    public DoubleProperty getVolume();

    public void mute();


}
