package elektreader.model;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import elektreader.api.PlayList;
import elektreader.api.Song;

public class Mp3PlayList implements PlayList{

    private final File playlistDir;
    private List<Song> songs;
    private List<Song> queue;

    public Mp3PlayList(final Path playlist, final Collection<Path> tracks) {
        playlistDir = playlist.toFile();
        List<Song> convertedMp3 = tracks.stream()
            .sorted((p1,p2) -> getIndexFromName(p1.toFile().getName())-getIndexFromName(p2.toFile().getName()))
            .map(p -> new Mp3Song(p))
            .map(Song.class::cast)
            .peek(s -> System.out.println(s.getName()+"\n"))
            .toList();
        this.songs = convertedMp3;
        this.queue = convertedMp3;
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
    public int getTotalDuration() {
        return this.songs.stream()
            .map(Song::getDuration)
            .reduce((a,b) -> a+b)
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

    /**
     * @param name the name of the song file
     * @return the index of the file, knowing every file name is structured
     * like "index - actual name.mp3"
     */
    private int getIndexFromName(String name){
        System.out.println(name.split(" ")[0]);
        return Integer.valueOf(name.split(" ")[0]);

    }
    
}
