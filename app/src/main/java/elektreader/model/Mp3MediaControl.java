package elektreader.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * This is an implementation of our mediaPlayer, used to manipulate songs.
 */
public class Mp3MediaControl implements MediaControl {

    private Optional<MediaPlayer> mediaPlayer;
    private Optional<List<Song>> playlist, playlistCopy;
    private int index;
    private boolean randOn;
    private LoopStatus loop = LoopStatus.OFF;
    private double currentVolume;

    /**
     * At the moment of creation, our fields will 
     * be set at empty or default values. 
     */
    public Mp3MediaControl() {
        this.mediaPlayer = Optional.empty();
        this.playlist = Optional.empty();
        this.playlistCopy = Optional.empty();
        this.currentVolume = 1.0;
        this.index = 0;
    }

    //This method set the current song as the Media given to our Mediaplyer.
    private void currentSong() {
        final Song currSong = this.getCurrentSong();
        this.stop();
        this.mediaPlayer = Optional.of(new MediaPlayer(new Media(currSong.getFile().toURI().toString())));
        this.mediaPlayer.get().setVolume(this.currentVolume);
        this.play();
        this.mediaPlayer.get().setOnEndOfMedia(this::nextSong);
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

    /**
     * @param playList the playlist to be set as the current one.
     */
    @Override
    public void setPlaylist(final PlayList playList) {
        if (this.mediaPlayer.isPresent()) {
            this.stop();
        }
        this.index = 0;
        this.playlist = Optional.of(playList.getSongs());
        this.playlistCopy = Optional.of(playList.getSongs());
        this.mediaPlayer = Optional.of(new MediaPlayer(new Media(this.getCurrentSong().getFile().toURI().toString())));
        /*Debug*/this.mediaPlayer.get().setVolume(currentVolume);
        this.mediaPlayer.get().setOnEndOfMedia(this::nextSong);
    }

    /**
     * @return the song set as the current one.
     */
    @Override
    public Song getCurrentSong() {
        return this.getSongAtCertainIndex(this.index);
    }

    //ONLY DEBUG VIA TEST!
    /**
     * @return the current playlist set (if present).
     */
    @Override
    public List<Song> getPlaylist() {
        if (this.playlist.isEmpty()) {
            throw new IllegalStateException("Playlist is currently empty.");
        }
        return this.playlist.get();
    }

    /**
     * Starts the mediaPlayer.
     */
    @Override
    public void play() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().play();
        }
    }

    /**
     * Pauses the mediaPlayer.
     */
    @Override
    public void pause() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().pause();
        }
    }

    /**
     * Stops the mediaPlayer.
     */
    @Override
    public void stop() {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().stop();
        }
    }

    /**
     * Increments the index to set the current song to be player. It stares at loop status too.
     */
    @Override
    public void nextSong() {
        if (this.mediaPlayer.isPresent()) {
            switch (loop) {
                case OFF -> {
                    if (this.index == this.getPlaylistSize() - 1) {
                        return;
                    }
                    this.index++;
                    break;
                }
                case PLAYLIST -> {
                    this.index = (this.index + 1) % this.getPlaylistSize();
                    break;
                }
                case TRACK -> {
                    break;
                }
                default -> {
                    break;
                }
            }
            this.currentSong();
        }
    }

    /**
     * Reduces the index to set the prev song as the current one.
     */
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

    /**
     * Sets loop to the song or to the playlist.
     */
    @Override
    public void loopSong() {
        switch (loop) {
            case OFF -> {
                loop = LoopStatus.PLAYLIST;
                break;
            }
            case PLAYLIST -> {
                loop = LoopStatus.TRACK;
                break;
            }
            case TRACK -> {
                loop = LoopStatus.OFF;
                break;
            }
            default -> {
                break;
            }
        }
    }

    /**
     * @return teh loop status.
     */
    @Override
    public LoopStatus isLoopStatus() {
        return loop;
    }

    /**
     * Sets the rand status.
     */
    @Override
    public void rand() {
        if (!randOn) {
            index = 0;
            final List<Song> tmpList = new ArrayList<>();
            tmpList.addAll(playlist.get());
            Collections.shuffle(tmpList);
            playlist = Optional.of(tmpList);
        } else {
            index = playlistCopy.get().indexOf(getCurrentSong());
            playlist = playlistCopy;
        }
        randOn = !randOn;
    }

    /**
     * @return true if randOn is true, false otherwise.
     */
    @Override
    public boolean getRandStatus() {
        return randOn;
    }

    /**
     * @param song the song to be set as the current one.
     */
    @Override
    public void setSong(final Song song) {
        if (this.playlist.isPresent()) {
            if (!(this.playlist.get().stream().anyMatch(t -> t.equals(song)))) {
                return;
            }
            this.index = playlist.get().indexOf(song);
            this.currentSong();
            return;
        } else {
            throw new IllegalStateException("Playlist is currently empty.");
        }
    }

    /**
     * @param rate sets the current rep speed of our mediaPlayer (if present).
     */
    @Override
    public void setRepSpeed(final double rate) {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().setRate(rate);
        }
    }

    /**
     * @param duration the progress to be set as the current one.
     */
    @Override
    public void setProgress(final Duration duration) {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().seek(duration);
        }
    }

    /**
     * @return the current duration of our mediaPlayer (if present).
     */
    @Override
    public double getDuration() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get().getMedia().getDuration().toSeconds();
        }
        return MediaPlayer.INDEFINITE;
    }

    /**
     * @param volume the value to be set as the current one.
     */
    @Override
    public void setVolume(final double volume) {
        if (this.mediaPlayer.isPresent()) {
            this.mediaPlayer.get().setVolume(volume);
            this.currentVolume = volume;
        }
    }

    /**
     * @return current volume (if mediaPlayer is present).
     */
    @Override
    public double getVolume() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get().getVolume();
        }
        return 0.0;
    }

    /**
     * @return the next song (if the playlist is present).
     */
    @Override
    public Optional<Song> getNextSong() {
        if (this.playlist.isPresent() && this.index < getPlaylistSize() - 1) {
            return Optional.of(this.playlist.get().get(index + 1));
        } else {
            return Optional.empty();
        }
    }

    /**
     * @return current progress of our mediaPlayer (if this one is present).
     */
    @Override
    public double getProgress() {
        if (this.mediaPlayer.isPresent()) {
            return this.mediaPlayer.get().getBufferProgressTime().toMinutes();
        }
        return -1;
    }

    /**
     * @return our mediaPlayer (If present).
     */
    @Override
    public Optional<MediaPlayer> getMediaControl() {
        return this.mediaPlayer;
    }

    /**
     * @return Current status of our mediaPlayer.
     */
    @Override
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
