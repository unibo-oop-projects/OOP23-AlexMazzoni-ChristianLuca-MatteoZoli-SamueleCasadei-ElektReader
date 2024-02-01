package elektreader.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ReaderImpl implements Reader{

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

    private boolean isPlaylist(final Path t) {
        try (Stream<Path> paths = Files.list(t)) {
            return paths.allMatch(Files::isRegularFile);
        } catch (Exception e) {}
        return false;
    }

    @Override
    public boolean setCurrentEnvironment(final Path folder) {
        try (Stream<Path> paths = Files.walk(folder)) {
            this.playlists = Optional.of(paths.filter(Files::isDirectory)
                    .filter(this::isPlaylist)
                    .map(Mp3PlayList::new)
                    .map(PlayList.class::cast) // Cast the list of Mp3PlayList to a list of PlayList
                    .toList());
        } catch (IOException e) {
            e.printStackTrace();
            resetEnvironment(); //reset all the environment 
            return false;
        }

        this.root = Optional.of(folder);
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
        return true;
    }

    @Override
    public Optional<List<PlayList>> getPlaylists() {
        return this.playlists;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentSong'");
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
