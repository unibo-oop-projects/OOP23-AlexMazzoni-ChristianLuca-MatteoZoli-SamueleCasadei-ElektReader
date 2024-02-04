package elektreader.model;

import java.util.Iterator;

import elektreader.api.MediaControl;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3MediaControl implements MediaControl{

    private MediaPlayer mediaPlayer;
    private Iterable<Song> queue;
    Iterator<Song> iterator;
    static private final double SET_ZERO_VOLUME = 0.0;


    public Mp3MediaControl(final Iterable<Song> queue, final Song song) {
        this.queue = queue;
        this.iterator = this.queue.iterator();
        mediaPlayer = new MediaPlayer(new Media(song.getFile().toURI().toString()));
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
        this.mediaPlayer.stop();    //At this moment, mediaPlayer status will be set to status.STOPPED 
    }

    @Override
    public void next() {
        this.mediaPlayer = new MediaPlayer(new Media(this.iterator.next().getFile().toURI().toString()));
        this.play();
    }

    @Override
    public void prev() {
        
    }

    @Override
    public void loop() {
        Media media = this.mediaPlayer.getMedia();
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.play();
    }

    @Override
    public void setQueue(final Iterable<Song> queue) {
        this.queue = queue;
        this.iterator = queue.iterator();
    }

    @Override
    public void setSong(final Song song) {
        final Media media = new Media(song.getFile().toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
    }

    @Override
    public void setRepSpeed(final double rate) {
        this.mediaPlayer.setRate(rate);
    }

    @Override
    public void setProgress(final Duration duration) {
        this.mediaPlayer.seek(duration);
    }

    @Override
    public Duration getDuration() {
        return this.mediaPlayer.getMedia().getDuration();
    }

    @Override
    public void setVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
    }

    @Override
    public double getVolume() {
        return this.mediaPlayer.getVolume();
    }

    @Override
    public void mute() {
        this.mediaPlayer.setVolume(SET_ZERO_VOLUME);
    }
    
}
