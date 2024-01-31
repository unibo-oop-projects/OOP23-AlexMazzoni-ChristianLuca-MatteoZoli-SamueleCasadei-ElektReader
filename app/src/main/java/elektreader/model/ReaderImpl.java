package elektreader.model;

import java.io.File;
import java.util.Optional;

import elektreader.api.Reader;

public class ReaderImpl implements Reader{

    Optional<File> root = Optional.empty();

    @Override
    public boolean setCurrentEnvironment(File folder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentEnvironment'");
    }

    @Override
    public Optional<File> getCurrentEnvironment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentEnvironment'");
    }

    @Override
    public boolean setCurrentPlaylist() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentPlaylist'");
    }

    @Override
    public Optional<File> getCurrentPlaylist() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPlaylist'");
    }

    @Override
    public int getNPlaylists() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNPlaylists'");
    }

    @Override
    public boolean setCurrentSong() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCurrentSong'");
    }

    @Override
    public Optional<File> getCurrentSong() {
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
