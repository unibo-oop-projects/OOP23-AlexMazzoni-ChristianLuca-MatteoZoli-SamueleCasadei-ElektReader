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
    
    final Path TEST_PATH;
    final Path TEST_INVALID_PATH;
    final Path TEST_INVALID_PLAYLIST; 
    final Path TEST_PATH_PLAYLIST;
    final Path TEST_PATH_SONG1;
    final Path TEST_PATH_SONG2;
    final Path TEST_PATH_SONG3;
    final Path TEST_PATH_SONG4;
    final Path TEST_PATH_SONG5;

    ElektreaderTest() throws URISyntaxException, IOException {
        TEST_PATH = Paths.get(System.getProperty("user.home") + File.separator + ".ElektReader");
        TEST_INVALID_PATH = Paths.get(System.getProperty("user.home"),"Desktop", "Musica");

        TEST_INVALID_PLAYLIST = Paths.get(TEST_PATH.toString(), "balli di coppia"); 
        
        TEST_PATH_PLAYLIST = Paths.get(TEST_PATH.toString());
        
        TEST_PATH_SONG1 = Paths.get(TEST_PATH_PLAYLIST.toString(), "01 - bachata.mp3");
        
        TEST_PATH_SONG2 = Paths.get(TEST_PATH_PLAYLIST.toString(), "02 - Becky G, NATTI NATASHA - Sin Pijama (Official Video).mp3");
        
        TEST_PATH_SONG3 = Paths.get(TEST_PATH_PLAYLIST.toString(), "03 - Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video).mp3");
        
        TEST_PATH_SONG4 = Paths.get(TEST_PATH_PLAYLIST.toString(), "04 - la bomba.mp3");
        
        TEST_PATH_SONG5 = Paths.get(TEST_PATH_PLAYLIST.toString(), "05 - ritmo vuelta.mp3");
    }

    final static String OPERATION_SUCCESSFULL = "Operation successfull";
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
        final Reader app = new ReaderImpl();

        /* test environment */
        /* test invalid path */
        app.setCurrentEnvironment(TEST_INVALID_PATH);
        Assertions.assertEquals(Optional.empty(), app.getCurrentEnvironment());
        
        /* test valid path */
        app.setCurrentEnvironment(TEST_PATH);
        Assertions.assertEquals(TEST_PATH, app.getCurrentEnvironment().get());
        
        /* test playlist */ 
        //test n playlists
        Assertions.assertEquals(app.getPlaylists().size(), app.getNPlaylists());

        // test invalid playlist 
        app.setCurrentPlaylist(app.getPlaylist(TEST_INVALID_PLAYLIST));
        Assertions.assertEquals(Optional.empty(), app.getCurrentPlaylist());
        
        // test valid playlist 1
        app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST));
        Assertions.assertEquals(TEST_PATH_PLAYLIST, app.getCurrentPlaylist().get().getPath());

        /* test player */
        app.getPlayer().setVolume(0.2);
        app.getPlayer().play();
        app.getPlayer().setSong(app.getCurrentPlaylist().get().getSong(TEST_PATH_SONG3).get());
        Assertions.assertEquals(TEST_PATH_SONG3, app.getPlayer().getCurrentSong().getFile().toPath());
        app.getPlayer().play();
    }

    @Test void testPlaylists() {
        final Reader app = new ReaderImpl();
        app.setCurrentEnvironment(TEST_PATH_PLAYLIST);

        final PlayList plist1 = new Mp3PlayList(TEST_PATH_PLAYLIST, app.getPlaylist(TEST_PATH_PLAYLIST).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        /* test on playlist with dynamic and small size */
        Assertions.assertEquals(5, plist1.getSize());
        Assertions.assertEquals("00:18:07", plist1.getTotalDuration());
        Assertions.assertEquals(".ElektReader", plist1.getName());       
    }

    @Test void testSongs() throws Exception {
        final Song s1 = new Mp3Song(TEST_PATH_SONG1);
        final Song s3 = new Mp3Song(TEST_PATH_SONG3);
        final Song s5 = new Mp3Song(TEST_PATH_SONG5);
        final Song s4 = new Mp3Song(TEST_PATH_SONG4);

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
        MediaControl mC1;
        mC1 = new Mp3MediaControl();mC1.setPlaylist(new Mp3PlayList(TEST_PATH_PLAYLIST, Reader.getAndFilterSongs(TEST_PATH_PLAYLIST).get()));
        mC1.play();
        mC1.setVolume(0.015);
        Assertions.assertEquals(0.015, mC1.getVolume());
        //Testing various void method useful for reproduction, song choice, queue gestion ecc.
        mC1.nextSong();
        mC1.prevSong();
        mC1.setVolume(0.015);
        mC1.setSong(mC1.getPlaylist().get(1));
        Assertions.assertEquals(new Mp3Song(mC1.getPlaylist().get(1).getFile().toPath()).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG2).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG3).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG2).getName(), mC1.getCurrentSong().getName());
        mC1.setSong(mC1.getPlaylist().get(0));
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.setRepSpeed(2.0);
        mC1.setSong(mC1.getPlaylist().get(mC1.getPlaylist().size() - 1));
        mC1.loopSong();
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.loopSong();
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.loopSong();
        mC1.rand();
        mC1.nextSong();
        mC1.rand();
        int actualSong = mC1.getPlaylist().indexOf(mC1.getCurrentSong());
        mC1.nextSong();
        Assertions.assertEquals(actualSong + 1, mC1.getPlaylist().indexOf(mC1.getCurrentSong()));
        //Thread.sleep(2000);
        mC1.stop();
    }

    
    @Test void testTrim() {
        final TrackTrimmer trimmer = new TrackTrimmerImpl();

        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:25", "FirstTestMp3"));

        trimmer.setTrack(TEST_PATH_SONG1);
        Assertions.assertEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:20", "SecondTestMp3"));
        final File secondTestMp3 = new File(TEST_PATH.toString() + FileSystems.getDefault().getSeparator() + "SecondTestMp3.mp3");
        Assertions.assertTrue(secondTestMp3.exists());
        Assertions.assertTrue(secondTestMp3.delete());
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("", "", "ThirdTestMp3"));
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:40", "0:30", "FourthTestMp3"));
        Assertions.assertThrows(NumberFormatException.class, () -> trimmer.trim("ciao", "0:40", "FifthTestMp3"));
    }
}