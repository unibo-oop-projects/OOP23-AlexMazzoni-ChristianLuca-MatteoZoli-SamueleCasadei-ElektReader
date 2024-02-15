package elektreader.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Reader;

/**
 * this class implement the main app logics,
 * includes all the other logic classes, like:
 * - playlists
 * - songs
 * - mediacontrol 
 * from this class is possible to load, reproduce and manipulate an environment.
*/
public final class ReaderImpl implements Reader {

    private Optional<Path> root = Optional.empty();

    private Optional<List<PlayList>> playlists = Optional.empty();

    private Optional<PlayList> currentPlaylist = Optional.empty();

    private Optional<MediaControl> player = Optional.empty();

    @Override
    public void setCurrentEnvironment(final Path root) {
        try (Stream<Path> paths = Files.walk(root)) {
            final var tmpPlaylist = paths.filter(Files::isDirectory)
                .map(t -> {
                    final var songs = Reader.getAndFilterSongs(t);
                    if (songs.isPresent()) {
                        return Optional.of(new Mp3PlayList(t, songs.get()));
                    }
                    return Optional.empty();
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PlayList.class::cast) // Cast the list of Mp3PlayList to a list of PlayList (because is Object)
                .toList();
                this.playlists = tmpPlaylist.isEmpty() ? Optional.empty() : Optional.of(tmpPlaylist);
        } catch (IOException e) {
            System.out.println("root -> " + e.toString() + " is not valid");
        }

        if (this.playlists.isEmpty()) {
            this.root = Optional.empty();
        } else {
            this.root = Optional.of(root);
        }
        resetEnvironment();
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
    public void setCurrentPlaylist(final Optional<PlayList> playlist) {
        if (!this.playlists.isPresent() || !playlist.isPresent()) {
            this.currentPlaylist = Optional.empty();
        } else {
            this.currentPlaylist = this.playlists.get().stream().filter(t -> t.equals(playlist.get())).findFirst();
            if (this.currentPlaylist.isPresent()) {
                this.player.get().setPlaylist(this.currentPlaylist.get());
            }
        }
    }

    @Override
    public List<PlayList> getPlaylists() {
        if (this.playlists.isEmpty()) {
            return new ArrayList<>(Collections.emptyList());
        }
        return this.playlists.get();
    }

    @Override
    public int getNPlaylists() {
        return this.playlists.isPresent() ? this.playlists.get().size() : 0;
    }

    @Override
    public Optional<PlayList> getPlaylist(final Path path) {
        if (!this.playlists.isPresent()) {
            return Optional.empty();
        }
        return this.playlists.get().stream().filter(t -> t.getPath().equals(path)).findFirst();
    }

    @Override
    public MediaControl getPlayer() {
        if (this.root.isEmpty()) {
            throw new IllegalStateException("need to set an environment");
        }
        return this.player.get();
    }

    private void resetEnvironment() {
        /* needs to be intialized before calling setCurrentPlaylist */
        this.player = this.root.isEmpty() ? Optional.empty() : Optional.of(new Mp3MediaControl());
        setCurrentPlaylist(Optional.empty());
    }
}
