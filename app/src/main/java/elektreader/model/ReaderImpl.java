package elektreader.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;

public class ReaderImpl implements Reader{

    private Optional<Path> root = Optional.empty();
    private Optional<List<PlayList>> playlists = Optional.empty();

    private Optional<PlayList> currentPlaylist = Optional.empty();
    private Optional<Song> currentSong = Optional.empty();

    private Optional<MediaControl> player = Optional.empty();

    private void resetEnvironment() {
        setCurrentPlaylist(Optional.empty());
        setCurrentSong(Optional.empty());
        //this.player = Optional.empty(); BOH
    }

    @Override
    public boolean setCurrentEnvironment(final Path root) {
        try (Stream<Path> paths = Files.walk(root)) {
            var tmpPlaylist = paths.filter(Files::isDirectory)
                .map(t -> {
                    var songs = Reader.getAndFilterSongs(t);
                    if(songs.isPresent()) {
                        return Optional.of(new Mp3PlayList(t, songs.get()));
                    }
                    return Optional.empty();
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PlayList.class::cast) // Cast the list of Mp3PlayList to a list of PlayList (because is Object)
                .toList();
                this.playlists = tmpPlaylist.isEmpty() ? Optional.empty() : Optional.of(tmpPlaylist);
        } catch (IOException e) { System.out.println("root -> "+e.toString() + " is not valid"); }
        if(this.playlists.isEmpty()) {
            this.root = Optional.empty();
        } else {
            this.root = Optional.of(root);
        }
        resetEnvironment();
        return this.root.isPresent();
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
    public Optional<MediaControl> getPlayer() {
        return this.player;
    }
}