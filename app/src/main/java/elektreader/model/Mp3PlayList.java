package elektreader.model;

import java.io.File;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Optional;
import java.util.Random;
import elektreader.api.PlayList;
import elektreader.api.Song;

public class Mp3PlayList implements PlayList{

    private final File playlistDir;
    private List<Song> songs;
    private Queue<Song> queue;

    public Mp3PlayList(final Path playlist, final Collection<Path> tracks) {
        playlistDir = playlist.toFile();
        List<Song> convertedMp3 = establishOrder(tracks).stream()
            .map(p -> new Mp3Song(p))
            .map(Song.class::cast)
            .toList();
        this.songs = convertedMp3;
        //this.queue.addAll(convertedMp3);
    }

    @Override
    public List<Song> getSongs() {
        return List.copyOf(this.songs);
    }

    /* potrebbe essere rimosso */
    @Override
    public Iterable<Song> getQueue() {
        return this.queue;
    }

    @Override
    public void shuffleQueue() {
        Random rnd = new Random();
        /* i'm using a temporary variable due to the fact that Collectors
         * doesn't have a toQueue() method.
         */
        Collection<Song> tmp = this.queue.stream()
            .sorted((s1,s2) -> rnd.nextInt()-rnd.nextInt())
            .toList();
        /* the queue needs to be empty before adding the new order */
        this.queue.clear();
        this.queue.addAll(tmp);
    }

    @Override
    public String getTotalDuration() {
        var secs = this.songs.stream()
            .mapToInt(Song::getDuration) /* map on duration to get every song's duration */
            .sum(); /* sum every duration */
        return String.format("%02d:%02d:%02d" ,secs/3600,(secs%3600)/60,(secs%3600)%60);
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
            searched = this.songs.get(index);
            return Optional.of(searched);
        } catch (Exception e) {
            return Optional.empty();
        }
        
    }

    public Optional<Song> getSong(Path path){
        return this.songs.stream()
            .filter(s -> s.getFile().toPath().equals(path)) /* filtro tutte le canzoni che corrispondono al percorso */
            .findAny(); /* dato che ci può essere solo un file con quel nome, se c'è lo trovo */
    }


    @Override
    public Path getPath() {
        return this.playlistDir.toPath();
    }

    private List<Path> establishOrder(Collection<Path> songs){
        int counter = 1;
        List<Path> withIndex = songs.stream()
            .filter(p -> getIndexFromName(p.toFile().getName()).isPresent())
            .sorted((p1,p2) -> getIndexFromName(p1.toFile().getName()).get()-getIndexFromName(p2.toFile().getName()).get())
            .collect(Collectors.toList());
        for (Path song : withIndex){
            song = setIndex(song, counter++);
        }
        for(Path song : songs.stream()
            .filter(p -> getIndexFromName(p.toFile().getName()).isEmpty())
            .toList()
        ){
            song = setIndex(song, counter++);
            withIndex.add(song);
        }
        return withIndex;
    }

    private Path setIndex(Path p, int i) {
        /* utilizzo un pattern per capire se l'indice di p va modificato o aggiunto */
            Pattern pattern = Pattern.compile("\\d+\\s*-\\s*.+");
            Matcher match = pattern.matcher(p.toFile().getName());

            if(match.matches()){
                /* se p rispetta il pattern del tipo "numero - nome" allora semplicemente rimpiazzo l'indice */
                String newName = p.toFile().getName().replaceFirst("\\d+", Integer.toString(i));
                File newFile = new File(p.toFile().getParent()+File.separator+newName);
                p.toFile().renameTo(newFile);
                return newFile.toPath();
                
            }
            else{
                /* se p non rispetta il pattern, semplicemente lo aggiungo all'inizio del nome */
                File newFile = new File(p.toFile().getParent()+File.separator+i+" - "+p.toFile().getName());
                p.toFile().renameTo(newFile);
                return newFile.toPath();
            }
    }

    /**
     * @param name the name of the song file
     * @return the index of the file, knowing every file name is structured
     * like "index - actual name.mp3"
     */
    private Optional<Integer> getIndexFromName(String name){
        Pattern pattern = Pattern.compile("\\d+\\s*-\\s*\\w+");
        Matcher match = pattern.matcher(name);
        /* se il file rispetta il formato standard */
        if(match.matches()){
            /* ricava l'indice e lo ritorna */
            return Optional.of(Integer.valueOf(name.split(" ")[0]));
        }
        else {
            return Optional.empty();
        }
    }
    
}
