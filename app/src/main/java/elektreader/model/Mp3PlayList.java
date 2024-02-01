package elektreader.model;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
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
    private final int FIRST = 0;

    public Mp3PlayList(final Path directory) {
        playlistDir = directory.toFile();
        Set<Song> convertedMp3 = new HashSet<>();
        for(File file : playlistDir.listFiles()){
            convertedMp3.add(new Mp3Song(file.toPath()));
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
    public void playQueue() {
        while(this.queue.size() > 0){
            this.queue.get(FIRST).play();
            this.queue.remove(FIRST);
        }
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
    
}
