package elektreader.model;

import java.io.File;
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
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import elektreader.api.Song;

public class Mp3Song implements Song{

    private final File songFile;
    private AudioFile data;
    private AudioHeader header;
    private Tag info;

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
        } catch (Exception e) {
            System.out.println(songFile+"   "+e.toString());
            throw new IllegalStateException("file corrotto o non supportato");
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
        return info.getFirst(FieldKey.GENRE).equals("") ? Optional.empty() :Optional.of(info.getFirst(FieldKey.GENRE));
    }

    @Override
    public int getDuration() {
        return header.getTrackLength();
    }

    @Override
    public Optional<String> getAlbumName() {
        return info.getFirst(FieldKey.ALBUM).equals("") ? Optional.empty() :Optional.of(info.getFirst(FieldKey.ALBUM));
    }

    @Override
    public File getFile() {
        return this.songFile;
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
    public String DurationStringRep() {
        long h = TimeUnit.SECONDS.toHours(getDuration()); /* amount of hours */
        long m = TimeUnit.SECONDS.toMinutes(getDuration()%3600); /* amount of minutes, less the hours */
        long s = (getDuration()%3600)%60; /* seconds left, less minutes, less hours */
        return String.format("%02d:%02d:%02d",h,m,s);
    }

    @Override
    public String getFileFormat() {
        Matcher match =  Pattern.compile(".*.(\\w+)$").matcher(getFile().getName());
        if(match.matches()){
            return match.group(1);
        }
        else {
            return "";
        }
    }
    
}
