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

public class ReaderImpl implements Reader{

    private Optional<Path> root = Optional.empty();
    private Optional<List<PlayList>> playlists = Optional.empty();

    private void resetEnvironment() {
        this.playlists=Optional.empty();
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
            // this.playlists = Optional.of(paths.filter(Files::isDirectory)
            //         .filter(this::isPlaylist)
            //         .map(Mp3PlayList::new)
            //         .toList());
        } catch (IOException e) {
            e.printStackTrace();
            resetEnvironment();
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
    public boolean setCurrentPlaylist(final Optional<PlayList> playlist) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentPlaylist'");
    }

    @Override
    public Optional<PlayList> getCurrentPlaylist() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPlaylist'");
    }

    @Override
    public int getNPlaylists() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNPlaylists'");
    }

    @Override
    public boolean setCurrentSong(final Optional<Song> song) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentSong'");
    }

    @Override
    public Optional<Song> getCurrentSong() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSong'");
    }

    @Override
    public int getNSongs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNSongs'");
    }

    @Override
    public boolean getStatusPlay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusPlay'");
    }

    @Override
    public void saveCurrentEnvironment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveCurrentEnvironment'");
    }
}
