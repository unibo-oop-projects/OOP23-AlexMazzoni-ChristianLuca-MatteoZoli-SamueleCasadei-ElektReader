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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    @SuppressFBWarnings(
        value = "LG",
        justification = "jaudiotagger prints make it harder to debug"
    )
    public Mp3Song(final Path songPath) {
        songFile = songPath.toFile();
        try {
            Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
            this.data = AudioFileIO.read(songFile);
            this.header = data.getAudioHeader();
            this.info = data.getTag();
        } catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException | IOException e) {
            System.out.println(songFile.toString() + " " + e);
        }
    }

    @Override
    public String getName() {
        return Song.getTitle(this.songFile.toPath());
    }

    @Override
    public Optional<String> getArtist() {
        return "".equals(info.getFirst(FieldKey.ARTIST)) ? Optional.empty() : Optional.of(info.getFirst(FieldKey.ARTIST));
    }

    @Override
    public Optional<String> getGenre() {
        return "".equals(info.getFirst(FieldKey.GENRE)) ? Optional.empty() : Optional.of(info.getFirst(FieldKey.GENRE));
    }

    @Override
    public int getDuration() {
        return header.getTrackLength();
    }

    @Override
    public Optional<String> getAlbumName() {
        return "".equals(info.getFirst(FieldKey.ALBUM)) ? Optional.empty() : Optional.of(info.getFirst(FieldKey.ALBUM));
    }

    @Override
    public File getFile() {
        return this.songFile;
    }

    @Override
    public String durationStringRep() {
        /* amount of hours */
        final long h = TimeUnit.SECONDS.toHours(getDuration());
        /* amount of minutes, less the hours */
        final long m = TimeUnit.SECONDS.toMinutes(getDuration() % (TIME_UNIT * TIME_UNIT));
        /* seconds left, less minutes, less hours */
        final long s = (getDuration() % (TIME_UNIT * TIME_UNIT)) % TIME_UNIT;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    @Override
    public String getFileFormat() {
        final Matcher match =  Pattern.compile(".+\\.(\\w+$)").matcher(getFile().getName());
        if (match.matches()) {
            return match.group(1);
        } else {
            return "";
        }
    }
}
