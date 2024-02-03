package elektreader.model;

import java.io.File;
import java.nio.file.Path;
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
            
            this.data = AudioFileIO.read(songFile);
            this.header = data.getAudioHeader();
            this.info = data.getTag();
        } catch (Exception e) {
            System.out.println(songFile+"   "+e.toString());
            throw new IllegalStateException("file corrotto o non supportato");
        }
        Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
    }

    @Override
    public String getName() {
        return getPlainName(this.songFile.getName());
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

    /**
     * @param name the actual file name of the song
     * @return the actual name, knowing that every song file name format is
     * "index - actual name.mp3"
     */
    private String getPlainName(String name){
        Pattern pattern = Pattern.compile("\\d+\\s-\\s(.*?)\\.\\w+");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    public String DurationStringRep() {
        int h = getDuration()/3600;
        int m = (getDuration()%3600)/60;
        int s = (getDuration()%3600)%60;
        return h+":"+m+":"+s;

    }
    
}
