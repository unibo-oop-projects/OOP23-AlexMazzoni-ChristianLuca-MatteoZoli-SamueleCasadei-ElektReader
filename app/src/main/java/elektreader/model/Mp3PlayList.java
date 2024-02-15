package elektreader.model;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;

/**
 * This class represents a playlist of only mp3 songs, is physical image is a directory
 * contained in the environment.
 */
public final class Mp3PlayList implements PlayList {

    private final File playlistDir;
    private List<Song> songs;

    /**
     * @param playlist the filesystem path of the playlist to be set
     * @param tracks the paths of the songs contained in playlist, filtered by corrupted and non-supported files
     */
    public Mp3PlayList(final Path playlist, final Collection<Path> tracks) {
        playlistDir = playlist.toFile();
        List<Song> convertedMp3 = establishOrder(tracks).stream()
            .map(p -> new Mp3Song(p))
            .map(Song.class::cast)
            .toList();
        this.songs = convertedMp3;
    }

    @Override
    public List<Song> getSongs() {
        return List.copyOf(this.songs);
    }

    @Override
    public String getTotalDuration() {
        var secs = this.songs.stream()
            .mapToInt(Song::getDuration) /* map on duration to get every song's duration */
            .sum(); /* sum every duration */
        return String.format("%02d:%02d:%02d", secs / (Mp3Song.TIME_UNIT * Mp3Song.TIME_UNIT),
            (secs % (Mp3Song.TIME_UNIT * Mp3Song.TIME_UNIT))
            / Mp3Song.TIME_UNIT, (secs % (Mp3Song.TIME_UNIT * Mp3Song.TIME_UNIT)) % Mp3Song.TIME_UNIT);
    }

    @Override
    public int getSize() {
        return this.songs.size();
    }

    /* needs further implementation */
    @Override
    public boolean addSong(final Song song) {
        return this.songs.add(song);
    }

    /* needs further implementation */
    @Override
    public boolean removeSong(final Song song) {
        return this.songs.remove(song);
    }

    @Override
    public String getName() {
        return playlistDir.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playlistDir == null) ? 0 : playlistDir.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Mp3PlayList other = (Mp3PlayList) obj;
        if (playlistDir == null) {
            if (other.playlistDir != null) {
                return false;
            }
        } else if (!playlistDir.equals(other.playlistDir)) {
            return false;
        }
        return true;
    }

    @Override
    public Optional<Song> getSong(final int index) {
        Song searched;
        try {
            searched = this.songs.get(index);
            return Optional.of(searched);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /** 
     * @param path the path of the song to be found in this playlist
     * @return the song, Optional.empty() if not present 
     */
    public Optional<Song> getSong(final Path path) {
        return this.songs.stream()
            .filter(s -> s.getFile().toPath().equals(path)) /* filter all the songs that match with the specifed path */
            .findAny(); /* since, in a directory, two files with the same name cannot exist i search for one*/
    }


    @Override
    public Path getPath() {
        return this.playlistDir.toPath();
    }

    private List<Path> establishOrder(final Collection<Path> songs) {
        int counter = 1;
        List<Path> withIndex = songs.stream()
            .filter(p -> getIndexFromName(p.toFile().getName()).isPresent())
            .sorted((p1, p2) -> getIndexFromName(p1.toFile().getName()).get() - getIndexFromName(p2.toFile().getName()).get())
            .collect(Collectors.toList());

        /* i need to use a temporary list in order to avoid concurrent modification and mantain counter in the scope
         * which would be impossible using streams
         */
        List<Path> tmp = new ArrayList<>();
        for (Path song : withIndex) {
            tmp.add(withIndex.indexOf(song), setIndex(song, counter++));
        }
        withIndex = tmp;
        for (Path song : songs.stream()
            .filter(p -> getIndexFromName(p.toFile().getName()).isEmpty())
            .toList()
        ) {
            song = setIndex(song, counter++);
            withIndex.add(song);
        }
        return withIndex;
    }

    private Path setIndex(final Path p, final int i) {
        /* i use pattern to know if the file name matches the pattern, if it does, it means that the index must be overwritten.
         * otherwise it means that the index must be added
         */
        Pattern pattern = Pattern.compile("\\d+\\s*-\\s*.+");
        Matcher match = pattern.matcher(p.toFile().getName());
        if (match.matches()) {
            /* in this case, p matches the pattern, so the index will be replaced */
            String newName = p.toFile().getName().replaceFirst("\\d+",
                (i >= 100 ? String.valueOf(i) : String.format("%02d", i)));
            File newFile = new File(p.toFile().getParent() + File.separator + newName);
            Reader.saveFile(p.toFile(), newFile);
            return newFile.toPath();
        } else {
            /* here, the index is added to the start of the name */
            File newFile = new File(p.toFile().getParent() + File.separator
                + (i >= 100 ? String.valueOf(i) : String.format("%02d", i)) + " - " + p.toFile().getName());
            Reader.saveFile(p.toFile(), newFile);
            return newFile.toPath();
        }
    }

    /**
     * @param name the name of the song file
     * @return the index of the file, knowing every file name is structured
     * like "index - actual name.mp3"
     */
    public static Optional<Integer> getIndexFromName(final String name) {
        Pattern pattern = Pattern.compile("\\d+\\s*-\\s*.+$");
        Matcher match = pattern.matcher(name);
        /* if the filename matches the standard pattern... */
        if (match.matches()) {
            /* the index can be picked and returned */
            return Optional.of(Integer.valueOf(name.split(" ")[0]));
        } else {
            /* if it doesn't match, that means i can't read the index */
            return Optional.empty();
        }
    }
}
