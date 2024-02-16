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
    final Path TEST_PATH_PLAYLIST1;
    final Path TEST_PATH_PLAYLIST1_SONG1;
    final Path TEST_PATH_PLAYLIST1_SONG2;
    final Path TEST_PATH_PLAYLIST1_SONG3;
    final Path TEST_PATH_PLAYLIST1_SONG4;
    final Path TEST_PATH_PLAYLIST1_SONG5;
    final Path TEST_PATH_PLAYLIST2;
    final Path TEST_PATH_PLAYLIST2_SONG1;
    final Path TEST_PATH_PLAYLIST2_SONG2;
    final Path TEST_INVALID_SONG;
    final Path TEST_TRIM_MP3;
    final Path TEST_TRIM_WAV;

    ElektreaderTest() throws URISyntaxException, IOException {
        // final InputStream in = Objects.requireNonNull(
        //     ClassLoader.getSystemResourceAsStream("")
        // ); 
        // System.out.println(in.toString());
        // String line;
        // try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
        //     while (br.ready()) {                
        //         line = br.readLine();
        //         System.out.println(line);
        //     }
        // }

        

        //TEST_PATH = Paths.get(getClass().getResource("MUSICA").toURI()).toFile().toPath();
        TEST_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "MUSICA");
        TEST_INVALID_PATH = Paths.get(System.getProperty("user.home"),"Desktop", "Musica");

        TEST_INVALID_PLAYLIST = Paths.get(TEST_PATH.toString(), "balli di coppia"); 
        
        TEST_PATH_PLAYLIST1 = Paths.get(TEST_PATH.toString(), "italiani");
        
        TEST_PATH_PLAYLIST1_SONG1 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "01 - Fedez, Annalisa, Articolo 31 - DISCO PARADISE (Visual Video).mp3");
        
        TEST_PATH_PLAYLIST1_SONG2 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "02 - Claudio Baglioni - Avrai [Testo - 4K].mp3");
        
        TEST_PATH_PLAYLIST1_SONG3 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "03 - Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video).mp3");
        
        TEST_PATH_PLAYLIST1_SONG4 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "04 - Fabio Rovazzi Orietta Berti La discoteca italiana.mp3");
        
        TEST_PATH_PLAYLIST1_SONG5 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "05 - Mr.Rain - SUPEREROI (Official Video) [Sanremo 2023].mp3");
        
        TEST_PATH_PLAYLIST2 = Paths.get(TEST_PATH.toString(), "Saggio EXPLORA");
        
        TEST_PATH_PLAYLIST2_SONG1 = Paths.get(TEST_PATH_PLAYLIST2.toString(), "01 - flashmob.mp3");
        
        TEST_PATH_PLAYLIST2_SONG2 = Paths.get(TEST_PATH_PLAYLIST2.toString(), "02 - la bomba.mp3");
        
        TEST_INVALID_SONG = Paths.get(TEST_PATH_PLAYLIST1.toString(), "05 - Lo Stato Sociale - Una Vita In Vacanza (Sanremo 2018).mp3");
        
        TEST_TRIM_MP3 = Paths.get(TEST_PATH.toString(), "01 - bachata.mp3");
        
        TEST_TRIM_WAV = Paths.get(TEST_PATH.toString(), "03 - MAZURKA- EULALIA.wav");
    }

    final String OPERATION_SUCCESSFULL = "Operation successfull";
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

        // test valid playlist 2
        app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST2));
        Assertions.assertEquals(TEST_PATH_PLAYLIST2, app.getCurrentPlaylist().get().getPath());

        // test invalid playlist 
        app.setCurrentPlaylist(app.getPlaylist(TEST_INVALID_PLAYLIST));
        Assertions.assertEquals(Optional.empty(), app.getCurrentPlaylist());
        
        // test valid playlist 1
        app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST1));
        Assertions.assertEquals(TEST_PATH_PLAYLIST1, app.getCurrentPlaylist().get().getPath());

        /* test player */
        app.getPlayer().setVolume(0.2);
        app.getPlayer().play();
        app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST2));
        app.getPlayer().setSong(app.getCurrentPlaylist().get().getSong(TEST_PATH_PLAYLIST2_SONG2).get());
        Assertions.assertEquals(TEST_PATH_PLAYLIST2_SONG2, app.getPlayer().getCurrentSong().getFile().toPath());
        app.getPlayer().play();
    }

    @Test void testPlaylists() {
        final Reader app = new ReaderImpl();
        app.setCurrentEnvironment(TEST_PATH_PLAYLIST1);

        final PlayList plist1 = new Mp3PlayList(TEST_PATH_PLAYLIST1, app.getPlaylist(TEST_PATH_PLAYLIST1).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        app.setCurrentEnvironment(TEST_PATH_PLAYLIST2);
        final PlayList plist2 = new Mp3PlayList(TEST_PATH_PLAYLIST2, app.getPlaylist(TEST_PATH_PLAYLIST2).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        /* test on playlist with dynamic and small size */
        Assertions.assertEquals(6, plist1.getSize());
        Assertions.assertEquals("00:21:57", plist1.getTotalDuration());
        Assertions.assertEquals("italiani", plist1.getName());

        /* test on a playlist with static and big size */
        Assertions.assertEquals(2, plist2.getSize());
        Assertions.assertEquals("00:06:42", plist2.getTotalDuration());
        Assertions.assertEquals("Saggio EXPLORA", plist2.getName());
        
    }

    @Test void testSongs() throws Exception {
        final Song s1 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG1);
        final Song s3 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG3);
        final Song s5 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG5);
        final Song s4 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG4);

        Assertions.assertEquals("Fedez, Annalisa, Articolo 31 - DISCO PARADISE (Visual Video)", s1.getName());
        Assertions.assertEquals("00:03:18", s1.durationStringRep());

        Assertions.assertEquals("Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video)", s3.getName());
        Assertions.assertEquals("00:03:23", s3.durationStringRep());

        Assertions.assertEquals("Fabio Rovazzi Orietta Berti La discoteca italiana", s4.getName());
        Assertions.assertEquals("00:03:10", s4.durationStringRep());

        Assertions.assertEquals("Mr.Rain - SUPEREROI (Official Video) [Sanremo 2023]", s5.getName());
        Assertions.assertEquals("00:03:24", s5.durationStringRep());
    }

    @Test void testMediaControl() throws Exception {
        //Declaring a new MediaControl instance
        MediaControl mC1;
        mC1 = new Mp3MediaControl();mC1.setPlaylist(new Mp3PlayList(TEST_PATH_PLAYLIST1, Reader.getAndFilterSongs(TEST_PATH_PLAYLIST1).get()));
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
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG2).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG3).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG2).getName(), mC1.getCurrentSong().getName());
        mC1.setSong(mC1.getPlaylist().get(0));
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.setRepSpeed(2.0);
        mC1.setSong(mC1.getPlaylist().get(mC1.getPlaylist().size() - 1));
        mC1.loopSong();
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.loopSong();
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG1).getName(), mC1.getCurrentSong().getName());
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
        
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:20", "FirstTestMp3"));

        trimmer.setTrack(TEST_TRIM_MP3);
        Assertions.assertEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:20", "SecondTestMp3"));
        final File secondTestMp3 = new File(TEST_PATH.toString() + FileSystems.getDefault().getSeparator() + "SecondTestMp3.mp3");
        Assertions.assertTrue(secondTestMp3.exists());
        secondTestMp3.delete();
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("", "", "ThirdTestMp3"));
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:25", "0:20", "FourthTestMp3"));
        Assertions.assertThrows(NumberFormatException.class, () -> trimmer.trim("ciao", "0:20", "FifthTestMp3"));

        trimmer.setTrack(TEST_TRIM_WAV);
        Assertions.assertEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:00", "0:20", "SecondTestWav"));
        File secondTestWav = new File(TEST_PATH.toString() + FileSystems.getDefault().getSeparator() + "SecondTestWav.wav");
        Assertions.assertTrue(secondTestWav.exists());
        secondTestWav.delete();
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("", "", "ThirdTestWav"));
        Assertions.assertNotEquals(OPERATION_SUCCESSFULL, trimmer.trim("0:25", "0:20", "FourthTestWav"));
        Assertions.assertThrows(NumberFormatException.class, () -> trimmer.trim("ciao", "0:20", "FifthTestWav"));
    }
}