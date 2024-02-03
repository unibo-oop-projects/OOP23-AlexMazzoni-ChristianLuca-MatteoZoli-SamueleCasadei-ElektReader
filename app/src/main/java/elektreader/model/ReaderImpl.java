package elektreader.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ReaderImpl implements Reader{

    private final String SUPPORTED_FILE = "mp3"; 

    private Optional<Path> root = Optional.empty();
    private Optional<List<PlayList>> playlists = Optional.empty();

    private Optional<PlayList> currentPlaylist = Optional.empty();
    private Optional<Song> currentSong = Optional.empty();

    private Optional<MediaPlayer> player;

    private void resetEnvironment() {
        this.root=Optional.empty();
        this.playlists=Optional.empty();
        this.currentPlaylist=Optional.empty();
        this.currentSong=Optional.empty();
    }

    private boolean isSong(final Path s) {
        return s.toString().matches(".*\\."+SUPPORTED_FILE);
    }

    private boolean isPlaylist(final Path p) {
        try (Stream<Path> paths = Files.list(p)) {
            return paths.anyMatch(this::isSong);
        } catch (Exception e) {}
        return false;
    }

    private List<Path> getFiles(final Path playlist) {
        List<Path> songs = new ArrayList<>(Collections.emptyList());
        try (Stream<Path> filesPaths = Files.list(playlist)) {
            songs = filesPaths.filter(t -> t.toString().matches(".*\\."+SUPPORTED_FILE)).toList();
        } catch (IOException e) { throw new IllegalStateException("class doesn't support it or invalid state"); }
        return songs;
    }

    @Override
    public boolean setCurrentEnvironment(final Path root) {
        try (Stream<Path> paths = Files.walk(root)) {
            var tmpPlaylist = paths.filter(Files::isDirectory)
                .filter(this::isPlaylist)
                .map(t -> new Mp3PlayList(t, getFiles(t)))
                .map(PlayList.class::cast) // Cast the list of Mp3PlayList to a list of PlayList
                .toList();
            this.playlists = tmpPlaylist.isEmpty() ? Optional.empty() : Optional.of(tmpPlaylist);
            System.out.println(this.playlists);
        } catch (IOException e) {
            e.printStackTrace();
            resetEnvironment(); //reset all the environment 
            return false;
        }
        if(this.playlists.isEmpty()) {
            return false; 
        }
        this.root = Optional.of(root);
        setCurrentPlaylist(Optional.empty());
        return true;
    }

    @Override
    public Optional<Path> getCurrentEnvironment() {
        return this.root;
    }

    @Override
    public Optional<PlayList> getCurrentPlaylist() {
        return this.currentPlaylist;
    }

    @Override
    public boolean setCurrentPlaylist(final Optional<PlayList> playlist) {
        if(!playlist.isPresent() || !this.playlists.isPresent()) {
            this.currentPlaylist = Optional.empty();
            return false;
        }
        this.currentPlaylist = this.playlists.get().stream().filter(t -> t.equals(playlist.get())).findFirst();
        return this.currentPlaylist.isPresent();
    }

    @Override
    public List<PlayList> getPlaylists() {
        return this.playlists.get();
    }

    @Override
    public int getNPlaylists() {
        return this.playlists.isPresent() ? this.playlists.get().size() : 0;
    }

    @Override
    public Optional<PlayList> getPlaylist(final Path path) {
        if(!this.playlists.isPresent()) return Optional.empty();
        return this.playlists.get().stream().filter(t -> t.getPath().equals(path)).findFirst();
    }

    @Override
    public Optional<Song> getCurrentSong() {
        return this.currentSong;
    }

    @Override
    public boolean setCurrentSong(final Optional<Song> song) {
        if(!song.isPresent() || !this.playlists.isPresent() || !this.currentPlaylist.isPresent()) {
            this.currentSong = Optional.empty();
            return false;
        } 
        this.currentSong = this.playlists.get().stream()
            .filter(p -> p.equals(this.currentPlaylist.get()))
            .flatMap(p -> p.getSongs().stream())
            .filter(t -> t.equals(song.get()))
            .findFirst();
        return this.currentSong.isPresent();
    }

    @Override
    public boolean setPlayer(Media song) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPlayer'");
    }

    @Override
    public Optional<MediaPlayer> getPlayer() {
        return this.player;
    }

    @Override
    public boolean getStatusPlay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveCurrentEnvironment'");
    }

    @Override
    public void saveCurrentEnvironment(final Path toModidy, final File modified) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveCurrentEnvironment'");
    }
}