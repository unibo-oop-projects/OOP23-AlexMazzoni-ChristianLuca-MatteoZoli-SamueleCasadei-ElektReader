package elektreader.model;

import java.util.*;

import elektreader.api.MediaControl;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3MediaControl implements MediaControl{

    private MediaPlayer mediaPlayer;
    private List<Song> playlist = new ArrayList<>();
    private int index;
    static private final double SET_ZERO_VOLUME = 0.0;


    public Mp3MediaControl(final Mp3PlayList myPlayList) {
        this.playlist = myPlayList.getSongs();          //Taking a copy of entire playlist.
        this.index = 0;                            //internal index useful to check currentSong.
        this.initMediaPlayer();
    }

    private void initMediaPlayer() {
        Song initSong = this.getCurrentSong();
        this.mediaPlayer = new MediaPlayer(new Media(initSong.getFile().toURI().toString()));
        this.mediaPlayer.setOnEndOfMedia(this::nextSong);
    }

    public Song getCurrentSong() {
        return this.getSongAtCertainIndex(this.index);
    }

    private Song getSongAtCertainIndex(final int index) {
        return this.playlist.get(index);
    }

    private int getPlaylistSize() {
        return this.playlist.size();
    }

    //Only debug
    public List<Song> getPlaylist() {
        return this.playlist;
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
        this.stop();
        Song currSong = this.getCurrentSong();
        this.mediaPlayer = new MediaPlayer(new Media(currSong.getFile().toURI().toString()));
        this.play();
        this.mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        });
        this.mediaPlayer.setOnEndOfMedia(this::nextSong);   //If media ends, the next song in the queue will be played.
    }

    @Override
    public void nextSong() {
        if (this.index == (this.getPlaylistSize()-1)) {
            return;
        }
        this.index++;
        this.currentSong();
    }

    @Override
    public void prevSong() {
        if (this.index == 0) {
            return;
        }
        this.index--;
        this.currentSong();
    }

    @Override
    public void loopSong() {
        this.stop();
        Media media = this.mediaPlayer.getMedia();
        this.mediaPlayer = new MediaPlayer(media);
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.play();
    }

    @Override
    public void setSong(final Song song) {
        if (!(this.playlist.contains(song))) {
            return;
        }
        this.index = playlist.indexOf(song);
        this.currentSong();
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
