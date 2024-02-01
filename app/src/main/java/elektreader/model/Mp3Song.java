package elektreader.model;

import java.io.File;
import java.nio.file.Path;

import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.util.Duration;

public class Mp3Song implements Song{

    private final File songFile;
    private final Media data;

    /**
     * @param songPath the file path, already filtered from illegal arguments 
     */
    public Mp3Song(final Path songPath) {
        songFile = songPath.toFile();
        data = new Media(songPath.toUri().toString());
    }

    @Override
    public String getName() {
        var title =  this.data.getMetadata().entrySet().stream()
            .filter(e -> e.getKey().equals("title"))
            .map(e -> e.getValue())
            .findAny();
        return title.isPresent() ? title.get().toString() : songFile.getName();
    }

    @Override
    public String getArtist() {
        var artist = this.data.getMetadata().entrySet().stream()
        .filter(e -> e.getKey().equals("artist"))
        .map(e -> e.getValue())
        .findAny();
        return artist.isPresent() ? artist.get().toString() : "";
    }

    @Override
    public String getGenre() {
        var genre = this.data.getMetadata().entrySet().stream()
        .filter(e -> e.getKey().equals("genre"))
        .map(e -> e.getValue())
        .findAny();
        return genre.isPresent() ? genre.get().toString() : "";
    }

    @Override
    public Duration getDuration() {
        return this.data.getDuration();
    }

    @Override
    public String getAlbumName() {
        var album = this.data.getMetadata().entrySet().stream()
        .filter(e -> e.getKey().equals("album"))
        .map(e -> e.getValue())
        .findAny();
        return album.isPresent() ? album.get().toString() : "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((songFile == null) ? 0 : songFile.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mp3Song other = (Mp3Song) obj;
        if (songFile == null) {
            if (other.songFile != null)
                return false;
        } else if (!songFile.equals(other.songFile))
            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }
    
}
