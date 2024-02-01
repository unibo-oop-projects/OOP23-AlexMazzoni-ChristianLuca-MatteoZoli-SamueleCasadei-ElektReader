package elektreader.model;

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

    private Optional<PlayList> currentPlaylist = Optional.empty();
    private Optional<Song> currentSong = Optional.empty();

    private boolean mediaStatus=false;

    private void resetEnvironment() {
        this.root=Optional.empty();
        this.playlists=Optional.empty();
        this.currentPlaylist=Optional.empty();
        this.currentSong=Optional.empty();
        this.mediaStatus=false;
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
    public boolean setCurrentPlaylist(final Optional<PlayList> playlist) {    //da rifare TODO
        this.currentPlaylist = playlist;
        return true;
    }

    @Override
    public Optional<PlayList> getCurrentPlaylist() {
        return this.currentPlaylist;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlaylist'");
    }

    @Override
    public boolean setCurrentSong(final Optional<Song> song) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentSong'");
    }

    @Override
    public Optional<Song> getCurrentSong() {
        return this.currentSong;
    }

    @Override
    public boolean getStatusPlay() {
        return this.mediaStatus;
    }

    @Override
    public void saveCurrentEnvironment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveCurrentEnvironment'");
    }
}
