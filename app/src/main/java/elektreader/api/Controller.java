package elektreader.api;

import java.io.IOException;
import java.util.Set;

import javafx.scene.Node;

/**
 * GUI in development
 * da modificare
 */
public interface Controller {
    void importFiles() throws IOException;
    public void setSongsList(final Set<Node> songs);
    public void setPlaylistsList(final Set<Node> playlists);
    // Parent getMediaPane();
}
