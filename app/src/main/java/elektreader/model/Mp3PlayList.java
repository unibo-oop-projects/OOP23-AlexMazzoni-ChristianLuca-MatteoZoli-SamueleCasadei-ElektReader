package elektreader.model;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import elektreader.api.PlayList;
import elektreader.api.Song;
import javafx.util.Duration;

public class Mp3PlayList implements PlayList{

    private final File playlistDir;
    private Set<Song> songs;
    private List<Song> queue;

    public Mp3PlayList(final Path playlist, final Collection<Path> tracks) {
        playlistDir = playlist.toFile();
        Set<Song> convertedMp3 = new HashSet<>();
        for(Path song : tracks){
            convertedMp3.add(new Mp3Song(song));
        }
        this.songs = convertedMp3;
        this.queue = convertedMp3.stream().collect(Collectors.toList());
    }

    @Override
    public Set<Song> getSongs() {
        return Set.copyOf(this.songs);
    }

    @Override
    public Iterable<Song> getQueue() {
        return this.queue;
    }

    @Override
    public void shuffleQueue() {
        Random rnd = new Random();
        this.queue = this.queue.stream()
            .sorted((a,b) -> rnd.nextInt()-rnd.nextInt())
            .collect(Collectors.toList());
    }

    @Override
    public Duration getTotalDuration() {
        return this.songs.stream()
            .map(Song::getDuration)
            .reduce((a,b) -> a.add(b))
            .get();
    }

    @Override
    public int getSize() {
        return this.songs.size();
    }

    @Override
    public boolean addSong(Song song) {
        return this.songs.add(song);
    }

    @Override
    public boolean removeSong(Song song) {
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mp3PlayList other = (Mp3PlayList) obj;
        if (playlistDir == null) {
            if (other.playlistDir != null)
                return false;
        } else if (!playlistDir.equals(other.playlistDir))
            return false;
        return true;
    }

    @Override
    public Optional<Song> getSong(int index) {
        Song searched;
        try {
            searched = this.queue.get(index);
            return Optional.of(searched);
        } catch (ArrayIndexOutOfBoundsException e) {
            if(index < getSize()){
                resetQueue();
                return getSong(index);
            }
            else {
                return Optional.empty();
            }
        }
        
    }

    public Optional<Song> getSong(Path path){
        return this.songs.stream()
            .filter(s -> s.getFile().toPath().equals(path)) /* filtro tutte le canzoni che corrispondono al percorso */
            .findAny(); /* dato che ci può essere solo un file con quel nome, se c'è lo trovo */

    }

    private void resetQueue() {
        this.queue = this.songs.stream()
            .collect(Collectors.toList());
    }

    @Override
    public Path getPath() {
        return this.playlistDir.toPath();
    }
    
}
