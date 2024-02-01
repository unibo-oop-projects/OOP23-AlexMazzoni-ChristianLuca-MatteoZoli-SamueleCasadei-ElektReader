package elektreader.model;

import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Mp3Song implements Song{

    private final MediaPlayer player;
    private final Media data;

    public Mp3Song(String path) {
        this.data = new Media(path);
        this.player = new MediaPlayer(data);
    }

    @Override
    public boolean isPlaying() {
        return this.player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    @Override
    public void play() {
        this.player.play();
    }

    @Override
    public void pause() {
        this.player.pause();
    }

    @Override
    public String getName() {
        return this.data.getMetadata().get("Title").toString();
    }

    @Override
    public String getArtist() {
        return this.data.getMetadata().get("Artist").toString();
    }

    @Override
    public String getGenre() {
        return this.data.getMetadata().get("Genre").toString();
    }

    @Override
    public Duration getDuration() {
        return this.data.getDuration();
    }

    @Override
    public String getAlbumName() {
        return this.data.getMetadata().get("Album").toString();
    }

    @Override
    public Double getVolume() {
        return isPlaying() ? this.player.getVolume() : 0;
    }
    
    @Override
    public void setVolume(Double amount) {
        this.player.setVolume(amount);
    }
    
}
