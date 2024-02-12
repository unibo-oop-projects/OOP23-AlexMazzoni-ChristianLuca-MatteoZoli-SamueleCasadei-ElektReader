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
    private double currentVolume;
    static private final double SET_ZERO_VOLUME = 0.0;

    public Mp3MediaControl() {
        this.mediaPlayer = Optional.empty();
        this.playlist = Optional.empty();
        this.index = 0;
    }

    //This method set the current song as the Media given to our Mediaplyer.
    private void currentSong() {
        Song currSong = this.getCurrentSong();
        this.stop();
        this.mediaPlayer = Optional.of(new MediaPlayer(new Media(currSong.getFile().toURI().toString())));
        this.mediaPlayer.get().setVolume(this.currentVolume);
        this.play();
        this.mediaPlayer.get().setOnEndOfMedia(this::nextSong);   //If media ends, the next song in the queue will be played.
    }

    //This method return the Song found at the index passed as a parameter, in the current playlist. 
    private Song getSongAtCertainIndex(final int index) {
        if (this.playlist.isPresent()) {
            return this.playlist.get().get(index);
        } else {
            throw new IllegalStateException("No playlist set.");
        }   
    }

    //This method returns the size of the current playlist.
    private int getPlaylistSize() {
        if (this.playlist.isEmpty()) {
            throw new IllegalStateException("Playlist is currently empty.");
        }
        return this.playlist.get().size();
    }

    public boolean setPlaylist(final PlayList playList) {
        if (this.mediaPlayer.isPresent()) {
            this.stop();
        }
        this.index = 0;
        this.playlist = Optional.of(playList.getSongs());
        this.mediaPlayer = Optional.of(new MediaPlayer(new Media(this.getCurrentSong().getFile().toURI().toString())));
        this.mediaPlayer.get().setOnEndOfMedia(this::nextSong);
        return this.playlist.isPresent() ? true : false;
    }

    public Song getCurrentSong() {
        return this.getSongAtCertainIndex(this.index);
    }

    //ONLY DEBUG VIA TEST!
    public List<Song> getPlaylist() {
        if (this.playlist.isEmpty()) {
            throw new IllegalStateException("Playlist is currently empty.");
        }
        return this.playlist.get();
    }

    @Override
    public void play() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().play(); //At this moment, mediaPlayer status will be set to status.PLAYING
        }
    }

    @Override
    public void pause() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().pause(); //At this moment, mediaPlayer status will be set to status.PAUSED
        }
    }

    @Override
    public void stop() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().stop(); //At this moment, mediaPlayer status will be set to status.STOPPED
        }
    }

    @Override
    public void nextSong() {
        if (this.mediaPlayer.isPresent()) {
            if (this.index == (this.getPlaylistSize()-1)) {
                return;
            }
            this.index++;
            this.currentSong();
        }
    }

    @Override
    public void prevSong() {
        if (this.mediaPlayer.isPresent()) {
            if (this.index == 0) {
                return;
            }
            this.index--;
            this.currentSong();
        }
    }

    @Override
    public void loopSong() {
        if (this.mediaPlayer.isPresent()) {
            this.stop();
            Media media = this.mediaPlayer.get().getMedia();
            this.mediaPlayer = Optional.of(new MediaPlayer(media));
            this.mediaPlayer.get().setCycleCount(MediaPlayer.INDEFINITE);
            this.play();
        }
    }

    @Override
    public boolean setSong(final Song song) {
        if (this.playlist.isPresent()) {
            if (!(this.playlist.get().stream().anyMatch(t -> t.equals(song)))) {
                return false;
            }
            this.index = playlist.get().indexOf(song);
            this.currentSong();
            return true;
        } else {
            throw new IllegalStateException("Playlist is currently empty.");
        }
    }

    @Override
    public void setRepSpeed(final double rate) {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().setRate(rate);
        }
    }

    @Override
    public void setProgress(final Duration duration) {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().seek(duration);
        }
    }

    @Override
    public double getDuration() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get().getMedia().getDuration().toSeconds();
        }
        return MediaPlayer.INDEFINITE;
    }

    @Override
    public void setVolume(final double volume) {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().setVolume(volume);
            this.currentVolume = volume;
        }
    }

    @Override
    public double getVolume() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get().getVolume();
        }
        return 0.0;
    }

    @Override
    public void mute() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().setVolume(SET_ZERO_VOLUME);
        }
    }

    @Override
    public Song getNextSong() {
        if (this.playlist.isPresent() && this.index < getPlaylistSize()) {
            return this.playlist.get().get(index + 1);
        } else {
            return null;
        }
    }

    @Override
    public double getProgress() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get().getBufferProgressTime().toMinutes();
        }
        return -1;
    }

    public MediaPlayer getMediaControl() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get();
        } else {
            throw new IllegalStateException("MediaPlayer is currently unset.");
        }
    }

    public Status getStatus() {
        if (this.mediaPlayer.isPresent()) {
            if (this.mediaPlayer.get().getStatus().equals(MediaPlayer.Status.PLAYING)) {
                return Status.PLAYING;
            } else {
                return Status.PAUSED;
            }
        }
        return null;
    }
}
