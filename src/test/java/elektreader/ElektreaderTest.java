package elektreader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import elektreader.api.MediaControl;
import elektreader.api.PlayList;
import elektreader.api.Reader;
import elektreader.api.Song;
import elektreader.api.TrackTrimmer;
import elektreader.model.Mp3MediaControl;
import elektreader.model.Mp3PlayList;
import elektreader.model.Mp3Song;
import elektreader.model.ReaderImpl;
import elektreader.model.TrackTrimmerImpl;
import javafx.application.Platform;

/*
 * Tests per testare la logica generale dell lettore
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class ElektreaderTest {
    /* mi raccomando per i test posizionare le cartelle nel percorso specificato "user.home/elektreaderTEST/${ENVIRONMENT}" */
    /* il piu leggero 'environment'     : https://drive.google.com/file/d/17WTQxkTmtdlTbGVU20vWhc3YzxP1zktA/view?usp=sharing */
    /* quello realistico 'MUSICA'       : https://drive.google.com/file/d/1d6UC0DYF2jKUqH-y1h0XfPCt3EnY_g7O/view?usp=sharing */
    /* quello impossibile 'ECCEZIONI'   : https://drive.google.com/file/d/1p5uUBHkpwvWBuhb3-DAqtkr6cMcKpsSN/view?usp=sharing */

    private final Path testPath;
    private final Path testInvalidPath;
    private final Path testInvalidPlaylistPath;
    private final Path testPlaylistPath;
    private final Path testSong1Path;
    private final Path testSong2Path;
    private final Path testSong3Path;
    private final Path testSong4Path;
    private final Path testSong5Path;

    private static final String OPERATION_SUCCESSFULL = "Operation successfull"; // NOPMD
    ElektreaderTest() throws URISyntaxException, IOException {
        this.testPath = Paths.get(System.getProperty("user.home") + File.separator + ".ElektReader");

        this.testInvalidPath = Paths.get(System.getProperty("user.home"), "Desktop", "Musica");

        this.testInvalidPlaylistPath = Paths.get(this.testPath.toString(), "balli di coppia");

        this.testPlaylistPath = Paths.get(this.testPath.toString());

        this.testSong1Path = Paths.get(this.testPlaylistPath.toString(), "01 - bachata.mp3");

        this.testSong2Path = Paths.get(this.testPlaylistPath.toString(), 
            "02 - Becky G, NATTI NATASHA - Sin Pijama (Official Video).mp3");

        this.testSong3Path = Paths.get(this.testPlaylistPath.toString(), 
            "03 - Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video).mp3");

        this.testSong4Path = Paths.get(this.testPlaylistPath.toString(), "04 - la bomba.mp3");

        this.testSong5Path = Paths.get(this.testPlaylistPath.toString(), "05 - ritmo vuelta.mp3");
    }

    /*
     * If you are using JavaFX components in a non-GUI application or a unit test,
     *  you need to call the Platform.startup(Runnable) method with an empty runnable before using any JavaFX classes.
     *  This will initialize the JavaFX toolkit without creating a stage or a scene.
     */
    @BeforeAll
    private void setup() { // NOPMD
        Platform.startup(() -> { }); 
    }

    /* TESTS */
    @Test void testEnvironment() throws Exception { /* test all the environment */
        final Reader app = new ReaderImpl();
        /* test environment */
        /* test invalid path */
        app.setCurrentEnvironment(this.testInvalidPath);
        Assertions.assertEquals(Optional.empty(), app.getCurrentEnvironment());

        /* test valid path */
        app.setCurrentEnvironment(this.testPath);
        Assertions.assertEquals(this.testPath, app.getCurrentEnvironment().get());

        /* test playlist */ 
        //test n playlists
        Assertions.assertEquals(app.getPlaylists().size(), app.getNPlaylists());

        // test invalid playlist 
        app.setCurrentPlaylist(app.getPlaylist(this.testInvalidPlaylistPath));
        Assertions.assertEquals(Optional.empty(), app.getCurrentPlaylist());

        // test valid playlist 1
        app.setCurrentPlaylist(app.getPlaylist(this.testPlaylistPath));
        Assertions.assertEquals(this.testPlaylistPath, app.getCurrentPlaylist().get().getPath());

        /* test player */
        app.getPlayer().play();
        app.getPlayer().setSong(app.getCurrentPlaylist().get().getSong(this.testSong3Path).get());
        Assertions.assertEquals(this.testSong3Path, app.getPlayer().getCurrentSong().getFile().toPath());
        app.getPlayer().play();
    }

    @Test void testPlaylists() {
        final Reader app = new ReaderImpl();
        app.setCurrentEnvironment(this.testPlaylistPath);

        final PlayList plist1 = new Mp3PlayList(this.testPlaylistPath, app.getPlaylist(this.testPlaylistPath)
            .get()
            .getSongs()
            .stream()
            .map(s -> s.getFile().toPath())
            .toList());

        /* test on playlist with dynamic and small size */

        //CHECKSTYLE: MagicNumber OFF
        //'5' is a magic number.
        Assertions.assertEquals(5, plist1.getSize());
        //CHECKSTYLE: MagicNumber ON

        Assertions.assertEquals("00:18:07", plist1.getTotalDuration());
        Assertions.assertEquals(".ElektReader", plist1.getName());
    }

    @Test void testSongs() throws Exception {
        final Song s1 = new Mp3Song(this.testSong1Path);
        final Song s3 = new Mp3Song(this.testSong3Path);
        final Song s5 = new Mp3Song(this.testSong5Path);
        final Song s4 = new Mp3Song(this.testSong4Path);

        Assertions.assertEquals("bachata", s1.getName());
        Assertions.assertEquals("00:04:15", s1.durationStringRep());

        Assertions.assertEquals("Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video)", s3.getName());
        Assertions.assertEquals("00:03:23", s3.durationStringRep());

        Assertions.assertEquals("la bomba", s4.getName());
        Assertions.assertEquals("00:03:21", s4.durationStringRep());

        Assertions.assertEquals("ritmo vuelta", s5.getName());
        Assertions.assertEquals("00:03:32", s5.durationStringRep());
    }

    @Test void testMediaControl() throws Exception {
        //Declaring a new MediaControl instance
        final MediaControl mC1;
        final double volume = 0.015;
        mC1 = new Mp3MediaControl();
        mC1.setPlaylist(new Mp3PlayList(this.testPlaylistPath, Reader.getAndFilterSongs(this.testPlaylistPath).get()));
        mC1.play();
        mC1.setVolume(volume);
        Assertions.assertEquals(volume, mC1.getVolume());
        //Testing various void method useful for reproduction, song choice, queue gestion ecc.
        mC1.nextSong();
        mC1.prevSong();
        mC1.setVolume(volume);
        mC1.setSong(mC1.getPlaylist().get(1));
        Assertions.assertEquals(
            new Mp3Song(mC1.getPlaylist().get(1).getFile().toPath()).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(this.testSong1Path).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(this.testSong2Path).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(this.testSong3Path).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(this.testSong2Path).getName(), mC1.getCurrentSong().getName());
        mC1.setSong(mC1.getPlaylist().get(0));
        Assertions.assertEquals(new Mp3Song(this.testSong1Path).getName(), mC1.getCurrentSong().getName());
        mC1.setRepSpeed(2.0);
        mC1.setSong(mC1.getPlaylist().get(mC1.getPlaylist().size() - 1));
        mC1.loopSong();
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(this.testSong1Path).getName(), mC1.getCurrentSong().getName());
        mC1.loopSong();
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(this.testSong1Path).getName(), mC1.getCurrentSong().getName());
        mC1.loopSong();
        mC1.rand();
        mC1.nextSong();
        mC1.rand();
        final int actualSong = mC1.getPlaylist().indexOf(mC1.getCurrentSong());
        mC1.nextSong();
        Assertions.assertEquals(actualSong + 1, mC1.getPlaylist().indexOf(mC1.getCurrentSong()));
        mC1.stop();
    }

    @Test void testTrim() {
        final TrackTrimmer trimmer = new TrackTrimmerImpl();

        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:25", "FirstTestMp3"));

        trimmer.setTrack(this.testSong1Path);
        Assertions.assertEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:20", "SecondTestMp3"));
        final File secondTestMp3 = new File(this.testPath.toString() 
            + FileSystems.getDefault().getSeparator() + "SecondTestMp3.mp3");
        Assertions.assertTrue(secondTestMp3.exists());
        Assertions.assertTrue(secondTestMp3.delete());
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("", "", "ThirdTestMp3"));
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:40", "0:30", "FourthTestMp3"));
        Assertions.assertThrows(NumberFormatException.class, () -> trimmer.trim("ciao", "0:40", "FifthTestMp3"));
    }
}
