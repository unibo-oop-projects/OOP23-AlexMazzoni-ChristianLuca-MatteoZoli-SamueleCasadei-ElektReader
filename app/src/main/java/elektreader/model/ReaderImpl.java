package elektreader.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;

public class ReaderImpl implements Reader{

    private static final String SUPPORTED_FILE = "mp3"; 

    private Optional<Path> root = Optional.empty();
    private Optional<List<PlayList>> playlists = Optional.empty();

    private Optional<PlayList> currentPlaylist = Optional.empty();
    private Optional<Song> currentSong = Optional.empty();

    private Optional<MediaControl> player = Optional.empty();

    private void resetEnvironment() {
        this.root = Optional.empty();
        this.playlists = Optional.empty();
        this.currentPlaylist = Optional.empty();
        this.currentSong = Optional.empty();
        this.player = Optional.empty();
    }

    public static boolean isSong(final Path s) {
        /* is a song, if is a supported file */
        return s.toString().matches(".*\\."+SUPPORTED_FILE);
    }

    public static List<Path> getAndFilterSongs(final Path playlist) {
        /* given a playlist path filter all the possible songs: */
        List<Path> songs = new ArrayList<>(Collections.emptyList());
        try (Stream<Path> filesPaths = Files.list(playlist)) {
            /*
             * a songs need to be a supported file
             * a song title need to be unique so it filter every song and considarate only 1 file per name
             */
            var files = filesPaths.filter(ReaderImpl::isSong).toList();
            songs = files.stream().filter(t -> {
                var newSong = new Mp3Song(t);
                return files.stream()
                        .map(Mp3Song::new)
                        .map(Mp3Song::getName)
                        .anyMatch(s -> s.equals(newSong.getName()));
            }).toList();
        } catch (IOException e) { 
            System.out.println("the current environment can't handle this playlist, this is not a valid playlist");
        }
        return songs;
    }

    @Override
    public boolean setCurrentEnvironment(final Path root) {
        try (Stream<Path> paths = Files.walk(root)) {
            var tmpPlaylist = paths.filter(Files::isDirectory)
                .map(t -> {
                    var songs = getAndFilterSongs(t);
                    if(!songs.isEmpty()) {
                        return Optional.of(new Mp3PlayList(t, songs));
                    }
                    return Optional.empty();
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PlayList.class::cast) // Cast the list of Mp3PlayList to a list of PlayList (because is Object)
                .toList();
            this.playlists = tmpPlaylist.isEmpty() ? Optional.empty() : Optional.of(tmpPlaylist);
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
    public Optional<MediaControl> getPlayer() {
        return this.player;
    }
}