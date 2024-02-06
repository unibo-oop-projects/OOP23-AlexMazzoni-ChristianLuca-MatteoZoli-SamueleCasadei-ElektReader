/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package elektreader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;
import elektreader.model.Mp3MediaControl;
import elektreader.model.Mp3PlayList;
import elektreader.model.Mp3Song;
import elektreader.model.ReaderImpl;
import javafx.application.Platform;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ElektreaderTest {

    /* CONSTANT */
    
    /* mi raccomando per i test posizionare la cartella nel percorso specificato */
    /* cartella: https://drive.google.com/file/d/1b5JAQ3Hc6FRwvO2BjIb7olaxOApJDrfp/view?usp=sharing */
    final Path TEST_PATH = Paths.get(System.getProperty("user.home"),"elektreaderTEST","Environment");
    final Path TEST_INVALID_PATH = Paths.get(System.getProperty("user.home"),"Desktop", "Musica");

    final Path TEST_INVALID_PLAYLIST = Paths.get(TEST_PATH.toString(), "GENERI"); 

    final Path TEST_PATH_PLAYLIST1 = Paths.get(TEST_PATH.toString(), "tutta la musica");
    
    final Path TEST_PATH_PLAYLIST2 = Paths.get(TEST_PATH.toString(), "GENERI", "MUSICA ROMAGNOLA");

    final Path TEST_INVALID_SONG = Paths.get(TEST_PATH_PLAYLIST1.toString(), "31 - video flashmob balla.mp4"); 

    private Path getASong(final Path playlist, final int order) throws Exception {
        return Reader.getAndFilterSongs(playlist).get().stream()
            .sorted((o1, o2) -> (Integer.valueOf(o1.getFileName().toString().split(" ")[0]) >= Integer.valueOf(o2.getFileName().toString().split(" ")[0])) ? 1 : -1)
            .toList()
        .get(order);
    }
    
    /*
     * If you are using JavaFX components in a non-GUI application or a unit test,
     *  you need to call the Platform.startup(Runnable) method with an empty runnable before using any JavaFX classes.
     *  This will initialize the JavaFX toolkit without creating a stage or a scene.
     */
    @BeforeAll
    private void setup() {
        Platform.startup(() -> {});
    }   

    /* TESTS */
    @Test void testEnvironment() throws Exception { /* test all the environment */
        Reader app = new ReaderImpl();

        /* test environment */
        Assertions.assertTrue(app.setCurrentEnvironment(TEST_PATH));
        Assertions.assertEquals(app.getCurrentEnvironment().get(), TEST_PATH);
        /* test invalid path */
        Assertions.assertFalse(app.setCurrentEnvironment(TEST_INVALID_PATH));
        Assertions.assertEquals(app.getCurrentEnvironment(), Optional.empty());
        
        /* test valid */
        Assertions.assertTrue(app.setCurrentEnvironment(TEST_PATH));
        Assertions.assertEquals(app.getCurrentEnvironment().get(), TEST_PATH);
        

        /* test playlist - song */ 
        //test valid playlist 1
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST1)));
        Assertions.assertEquals(app.getCurrentPlaylist().get().getPath(), TEST_PATH_PLAYLIST1);
        
        //test invalid song - current playlist 1
        Assertions.assertFalse(app.setCurrentSong(app.getPlaylist(TEST_PATH_PLAYLIST2).get().getSong(getASong(TEST_PATH_PLAYLIST1, 0))));
        Assertions.assertEquals(app.getCurrentSong(), Optional.empty());

        //test valid playlist 2
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST2)));
        Assertions.assertEquals(app.getCurrentPlaylist().get().getPath(), TEST_PATH_PLAYLIST2);

        //test valid song - current playlist 2
        Assertions.assertTrue(app.setCurrentSong(app.getCurrentPlaylist().get().getSong(getASong(TEST_PATH_PLAYLIST2, 0))));
        Assertions.assertEquals(app.getCurrentSong().get().getFile().toPath(), getASong(TEST_PATH_PLAYLIST2, 0));

        // test invalid playlist
        Assertions.assertFalse(app.setCurrentPlaylist(app.getPlaylist(TEST_INVALID_PLAYLIST)));
        Assertions.assertEquals(app.getCurrentPlaylist(), Optional.empty());
    }

    @Test void testPlaylists() {
        Reader app = new ReaderImpl();

        app.setCurrentEnvironment(TEST_PATH_PLAYLIST2);
        PlayList plist1 = new Mp3PlayList(TEST_PATH_PLAYLIST2, app.getPlaylist(TEST_PATH_PLAYLIST2).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        app.setCurrentEnvironment(TEST_PATH_PLAYLIST1);
        PlayList plist2 = new Mp3PlayList(TEST_PATH_PLAYLIST1, app.getPlaylist(TEST_PATH_PLAYLIST1).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        /* test on playlist with static and small size */
        Assertions.assertEquals(Reader.getAndFilterSongs(plist1.getPath()).get().size(), plist1.getSize());
        Assertions.assertEquals("00:11:13", plist1.getTotalDuration());
        Assertions.assertEquals("MUSICA ROMAGNOLA", plist1.getName());
        /* test on a playlist with dynamic and big size */
        Assertions.assertEquals(Reader.getAndFilterSongs(plist2.getPath()).get().size(), plist2.getSize());
        Assertions.assertEquals("01:56:44", plist2.getTotalDuration());
        Assertions.assertEquals("tutta la musica", plist2.getName());
        
    }

    @Test void testSongs() throws Exception {

        Song s1 = new Mp3Song(getASong(TEST_PATH_PLAYLIST1, 0));
        Song s3 = new Mp3Song(getASong(TEST_PATH_PLAYLIST1, 2));
        Song s5 = new Mp3Song(getASong(TEST_PATH_PLAYLIST1, 4));
        Song s4 = new Mp3Song(getASong(TEST_PATH_PLAYLIST1, 3));

        Assertions.assertEquals("flashmob", s1.getName());
        Assertions.assertEquals("00:03:21", s1.DurationStringRep());

        Assertions.assertEquals("despacito", s3.getName());
        Assertions.assertEquals("00:03:38", s3.DurationStringRep());

        Assertions.assertEquals("la bomba", s4.getName());
        Assertions.assertEquals("00:03:21", s4.DurationStringRep());

        Assertions.assertEquals("ritmo vuelta", s5.getName());
        Assertions.assertEquals("00:03:32", s5.DurationStringRep());
    }

    @Test void testMediaControl() throws Exception {
        //Declaring a new MediaControl instance
        MediaControl mC1;
        mC1 = new Mp3MediaControl(new Mp3PlayList(TEST_PATH_PLAYLIST2, Arrays.asList(TEST_PATH_PLAYLIST2.toFile().listFiles()).stream()
            .map(f -> f.toPath())
            .toList()));

        //Volume methods tests
        double volume = mC1.getVolume();
        Assertions.assertEquals(volume, mC1.getVolume());
        mC1.mute();
        Assertions.assertEquals(0.0, mC1.getVolume());
        mC1.setVolume(1.0);
        Assertions.assertEquals(1.0, mC1.getVolume());

        //Testing various void method useful for reproduction, song choice, queue gestion ecc.
        mC1.getDuration();
        mC1.play();
        Thread.sleep(2000);
        //mC1.loopSong();
        mC1.nextSong();
        Thread.sleep(2000);
        mC1.prevSong();
        Thread.sleep(2000);
        mC1.setSong(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 1)));
        Thread.sleep(2000);
        //Reminder: setSong(final Song song) still to be tested successfully! Test that must pass here below (Still commented).
        Assertions.assertEquals(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 1)).getName(), mC1.getCurrentSong().getName());

        //Changing reference of my mediaControl to another object of the same class.
        mC1 = new Mp3MediaControl(new Mp3PlayList(TEST_PATH_PLAYLIST2, Arrays.asList(getASong(TEST_PATH_PLAYLIST2, 0), getASong(TEST_PATH_PLAYLIST2, 1))));
        mC1.mute();
        Assertions.assertEquals(0.0, mC1.getVolume());
        mC1.play();

        //Testing basic reproduction methods (GUI implementation will be still more accurate to be sure that everything works correctly.).
        Assertions.assertEquals(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 0)).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 1)).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 0)).getName(), mC1.getCurrentSong().getName());
    }
}