package elektreader.model;

import java.util.Iterator;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3MediaControl implements MediaControl{

    private MediaPlayer mediaPlayer;
    private Mp3PlayList myPlayList;
    private Iterable<Song> queue;
    private int queueIndex;
    static private final double SET_ZERO_VOLUME = 0.0;


    public Mp3MediaControl(final Mp3PlayList myPlayList) {
        this.myPlayList = myPlayList;
        this.queue = this.myPlayList.getQueue();        //Starting queue, given directly via playlist.
        this.queueIndex = 0;                            //internal index useful to check currentSong.
        this.initMediaPlayer();
    }

    private void initMediaPlayer() {
        Song initSong = this.getCurrentSong();
        this.mediaPlayer = new MediaPlayer(new Media(initSong.getFile().toURI().toString()));
        this.mediaPlayer.setOnEndOfMedia(this::nextSong);
    }

    private Song getCurrentSong() {
        int tempIndex = Math.min(queueIndex, 0);
        return this.getSongAtCertainIndex(tempIndex);
    }

    private Song getSongAtCertainIndex(final int index) {
        return this.myPlayList.getSong(index).get();
    }

    private int getQueueSize() {
        int queueSize = 0;
        for (Song elem : this.queue) {
            queueSize++;
        }
        return queueSize;
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

    public void currentSong() {
        this.mediaPlayer.stop();
        Song currSong = this.getCurrentSong();
        this.mediaPlayer = new MediaPlayer(new Media(currSong.getFile().toURI().toString()));
        this.mediaPlayer.setOnEndOfMedia(this::nextSong);
    }

    @Override
    public void nextSong() {
        this.queueIndex = (this.queueIndex + 1 + this.getQueueSize()) % this.getQueueSize();
        this.currentSong();
    }

    @Override
    public void prevSong() {
        this.queueIndex = (this.queueIndex - 1 + this.getQueueSize()) % this.getQueueSize();
        this.currentSong();
    }

    @Override
    public void loopSong() {
        Media media = this.mediaPlayer.getMedia();
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.play();
    }

    @Override
    public void setQueue(final Iterable<Song> queue) {
        this.queue = queue;
    }

    @Override
    public void setSong(final Song song) {
        this.queueIndex = 0;
        //this.queue = this.myPlayList.getIndexFromName(song.getName());
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
