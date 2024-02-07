package elektreader.model;

import java.util.*;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3MediaControl implements MediaControl{

    private Optional<MediaPlayer> mediaPlayer;
    private Optional<List<Song>> playlist;
    private int index;
    static private final double SET_ZERO_VOLUME = 0.0;


    public Mp3MediaControl() {
        this.mediaPlayer = Optional.empty();
        this.playlist = Optional.empty();
        this.index = 0;
    }

    public void setPlaylist(PlayList playList) {
        this.playlist = Optional.of(playList.getSongs());
        this.mediaPlayer = Optional.of(new MediaPlayer(new Media(this.getCurrentSong().getFile().toURI().toString())));
        this.mediaPlayer.get().setOnEndOfMedia(this::nextSong);
    }

    public Song getCurrentSong() {
        return this.getSongAtCertainIndex(this.index);
    }

    private Song getSongAtCertainIndex(final int index) {
        return this.playlist.get().get(index);
    }

    private int getPlaylistSize() {
        return this.playlist.get().size();
    }

    //Only debug
    public List<Song> getPlaylist() {
        return this.playlist.get();
    }

    @Override
    public void play() {
        this.mediaPlayer.get().play();     //At this moment, mediaPlayer status will be set to status.PLAYING
    }

    @Override
    public void pause() {
        this.mediaPlayer.get().pause();   //At this moment, mediaPlayer status will be set to status.PAUSED
    }

    @Override
    public void stop() {
        this.mediaPlayer.get().stop();    //At this moment, mediaPlayer status will be set to status.STOPPED 
    }

    public void currentSong() {
        this.stop();
        Song currSong = this.getCurrentSong();
        this.mediaPlayer = Optional.of(new MediaPlayer(new Media(currSong.getFile().toURI().toString())));
        this.play();
        this.mediaPlayer.get().setOnReady(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        });
        this.mediaPlayer.get().setOnEndOfMedia(this::nextSong);   //If media ends, the next song in the queue will be played.
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
        Media media = this.mediaPlayer.get().getMedia();
        this.mediaPlayer = Optional.of(new MediaPlayer(media));
        this.mediaPlayer.get().setCycleCount(MediaPlayer.INDEFINITE);
        this.play();
    }

    @Override
    public void setSong(final Song song) {
        if (!(this.playlist.get().contains(song))) {
            return;
        }
        this.index = playlist.get().indexOf(song);
        this.currentSong();
    }

    @Override
    public void setRepSpeed(final double rate) {
        this.mediaPlayer.get().setRate(rate);
    }

    @Override
    public void setProgress(final Duration duration) {
        this.mediaPlayer.get().seek(duration);
    }

    @Override
    public Duration getDuration() {
        return this.mediaPlayer.get().getMedia().getDuration();
    }

    @Override
    public void setVolume(double volume) {
        this.mediaPlayer.get().setVolume(volume);
    }

    @Override
    public double getVolume() {
        return this.mediaPlayer.get().getVolume();
    }

    @Override
    public void mute() {
        this.mediaPlayer.get().setVolume(SET_ZERO_VOLUME);
    }
    
}
