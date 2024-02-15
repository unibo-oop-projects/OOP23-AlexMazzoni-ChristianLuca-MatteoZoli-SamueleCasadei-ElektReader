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
    private final List<Song> songs;

    /**
     * @param playlist the filesystem path of the playlist to be set
     * @param tracks the paths of the songs contained in playlist, filtered by corrupted and non-supported files
     */
    public Mp3PlayList(final Path playlist, final Collection<Path> tracks) {
        playlistDir = playlist.toFile();
        final List<Song> convertedMp3 = establishOrder(tracks).stream()
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
        final var secs = this.songs.stream()
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
        final Mp3PlayList other = (Mp3PlayList) obj;
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
    @Override
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
            .filter(p -> Song.getIndexFromName(p.toFile().getName()).isPresent())
            .sorted((p1, p2) -> Song.getIndexFromName(p1.toFile().getName()).get() - Song.getIndexFromName(p2.toFile().getName()).get())
            .collect(Collectors.toList());

        /* i need to use a temporary list in order to avoid concurrent modification and mantain counter in the scope
         * which would be impossible using streams
         */
        final List<Path> tmp = new ArrayList<>();
        for (final Path song : withIndex) {
            tmp.add(withIndex.indexOf(song), changeIndex(song, counter++));
        }
        withIndex = tmp;
        for (final Path path : songs.stream()
            .filter(p -> Song.getIndexFromName(p.toFile().getName()).isEmpty())
            .toList()
        ) {
            final Path newPath = changeIndex(path, counter++);
            withIndex.add(newPath);
        }
        return withIndex;
    }

    private Path changeIndex(final Path p, final int i) {
        /* i use pattern to know if the file name matches the pattern, if it does, it means that the index must be overwritten.
         * otherwise it means that the index must be added
         */
        final Pattern pattern = Pattern.compile("\\d+\\s*-\\s*.+");
        final Matcher match = pattern.matcher(p.toFile().getName());
        if (match.matches()) {
            /* in this case, p matches the pattern, so the index will be replaced */
            final String newName = p.toFile().getName().replaceFirst("\\d+",
                i >= 100 ? String.valueOf(i) : String.format("%02d", i));
            final File newFile = new File(p.toFile().getParent() + File.separator + newName);
            Reader.saveFile(p.toFile(), newFile);
            return newFile.toPath();
        } else {
            /* here, the index is added to the start of the name */
            final File newFile = new File(p.toFile().getParent() + File.separator
                + (i >= 100 ? String.valueOf(i) : String.format("%02d", i)) + " - " + p.toFile().getName());
            Reader.saveFile(p.toFile(), newFile);
            return newFile.toPath();
        }
    }
}
