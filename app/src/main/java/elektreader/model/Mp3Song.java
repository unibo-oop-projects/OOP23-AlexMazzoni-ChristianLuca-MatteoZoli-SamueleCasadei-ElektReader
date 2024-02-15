package elektreader.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import elektreader.api.Song;
/**
 * this class represents the abstraction of a song data.
 */
public final class Mp3Song implements Song {

    private final File songFile;
    private AudioFile data;
    private AudioHeader header;
    private Tag info;
    static final int TIME_UNIT = 60;

    /**
     * @param songPath the file path, already filtered from illegal arguments 
     */
    public Mp3Song(final Path songPath) {
        songFile = songPath.toFile();
        try {
            Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
            this.data = AudioFileIO.read(songFile);
            this.header = data.getAudioHeader();
            this.info = data.getTag();
        } catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException | IOException e) {
            System.out.println(songFile + "   " + e.toString());
        }
    }

    @Override
    public String getName() {
        return Song.getTitle(this.songFile.toPath());
    }

    @Override
    public Optional<String> getArtist() {
        return info.getFirst(FieldKey.ARTIST).equals("") ? Optional.empty() : Optional.of(info.getFirst(FieldKey.ARTIST));
    }

    @Override
    public Optional<String> getGenre() {
        return info.getFirst(FieldKey.GENRE).equals("") ? Optional.empty() : Optional.of(info.getFirst(FieldKey.GENRE));
    }

    @Override
    public int getDuration() {
        return header.getTrackLength();
    }

    @Override
    public Optional<String> getAlbumName() {
        return info.getFirst(FieldKey.ALBUM).equals("") ? Optional.empty() : Optional.of(info.getFirst(FieldKey.ALBUM));
    }

    @Override
    public File getFile() {
        return this.songFile;
    }

    @Override
    public String durationStringRep() {
        long h = TimeUnit.SECONDS.toHours(getDuration()); /* amount of hours */
        long m = TimeUnit.SECONDS.toMinutes(getDuration() % (TIME_UNIT * TIME_UNIT)); /* amount of minutes, less the hours */
        long s = (getDuration() % (TIME_UNIT * TIME_UNIT)) % TIME_UNIT; /* seconds left, less minutes, less hours */
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    @Override
    public String getFileFormat() {
        Matcher match =  Pattern.compile(".+\\.(\\w+$)").matcher(getFile().getName());
        if (match.matches()) {
            return match.group(1);
        } else {
            return "";
        }
    }
}
