package elektreader.model;

import elektreader.api.MediaControl;
import elektreader.api.Song;
import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3MediaControl implements MediaControl{

    private MediaPlayer mediaPlayer;
    private Iterable<Song> queue;


    public Mp3MediaControl(Iterable<Song> queue, final Media song) {
        this.queue = queue;
        mediaPlayer = new MediaPlayer(song);
    }

    @Override
    public void play() {
        this.mediaPlayer.play();     //At this moment, mediaPlayer status will be set to status.PLAYING
    }

    @Override
    public void pause() {
        this.mediaPlayer.pause();   //At this moment, mediaPlayer status will be set to status.PAUSED
    }

    @Override
    public void stop() {
        this.mediaPlayer.stop();
    }

    @Override
    public boolean next() {
        
    }

    @Override
    public boolean prev() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prev'");
    }

    @Override
    public boolean loop(Song song) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loop'");
    }

    @Override
    public void setQueue(Iterable<Song> queue) {
       this.queue = queue;
    }

    @Override
    public void setSong(Media song) {
        this.mediaPlayer = new MediaPlayer(song);
    }

    @Override
    public boolean setRepSpeed() {
        /* TODO */
    }

    @Override
    public boolean setProgress(Duration duration) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProgress'");
    }

    @Override
    public Duration getDuration() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDuration'");
    }

    @Override
    public boolean setVolume(DoubleProperty volume) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVolume'");
    }

    @Override
    public DoubleProperty getVolume() {
        return this.mediaPlayer.
    }

    @Override
    public void mute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mute'");
    }
    
}
