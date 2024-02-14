package elektreader;

import java.io.File;
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
class ElektreaderTest { 
    /* mi raccomando per i test posizionare le cartelle nel percorso specificato "user.home/elektreaderTEST/${ENVIRONMENT}" */
    /* il piu leggero 'environment'     : https://drive.google.com/file/d/17WTQxkTmtdlTbGVU20vWhc3YzxP1zktA/view?usp=sharing */
    /* quello realistico 'MUSICA'       : https://drive.google.com/file/d/1d6UC0DYF2jKUqH-y1h0XfPCt3EnY_g7O/view?usp=sharing */
    /* quello impossibile 'ECCEZIONI'   : https://drive.google.com/file/d/1p5uUBHkpwvWBuhb3-DAqtkr6cMcKpsSN/view?usp=sharing */
    
    final Path TEST_PATH = Paths.get(System.getProperty("user.home"),"elektreaderTEST","Environment");
    final Path TEST_INVALID_PATH = Paths.get(System.getProperty("user.home"),"Desktop", "Musica");

    final Path TEST_INVALID_PLAYLIST = Paths.get(TEST_PATH.toString(), "balli di coppia"); 

    final Path TEST_PATH_PLAYLIST1 = Paths.get(TEST_PATH.toString(), "italiani");
    
    final Path TEST_PATH_PLAYLIST1_SONG1 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "01 - Fedez, Annalisa, Articolo 31 - DISCO PARADISE (Visual Video).mp3");

    final Path TEST_PATH_PLAYLIST1_SONG2 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "02 - Claudio Baglioni - Avrai [Testo - 4K].mp3");

    final Path TEST_PATH_PLAYLIST1_SONG3 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "03 - Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video).mp3");

    final Path TEST_PATH_PLAYLIST1_SONG4 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "04 - Fabio Rovazzi Orietta Berti La discoteca italiana.mp3");

    final Path TEST_PATH_PLAYLIST1_SONG5 = Paths.get(TEST_PATH_PLAYLIST1.toString(), "05 - Mr.Rain - SUPEREROI (Official Video) [Sanremo 2023].mp3");

    final Path TEST_PATH_PLAYLIST2 = Paths.get(TEST_PATH.toString(), "Saggio EXPLORA");

    final Path TEST_PATH_PLAYLIST2_SONG10 = Paths.get(TEST_PATH_PLAYLIST2.toString(), "10 - farfalle.mp3");

    final Path TEST_PATH_PLAYLIST2_SONG18 = Paths.get(TEST_PATH_PLAYLIST2.toString(), "18 - latinos.mp3");

    final Path TEST_INVALID_SONG = Paths.get(TEST_PATH_PLAYLIST1.toString(), "05 - Lo Stato Sociale - Una Vita In Vacanza (Sanremo 2018).mp3");
    
    final Path TEST_TRIM_MP3 = Paths.get(TEST_PATH.toString(), "01 - bachata.mp3");

    final Path TEST_TRIM_WAV = Paths.get(TEST_PATH.toString(), "03 - MAZURKA- EULALIA.wav");
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
        /* test invalid path */
        Assertions.assertFalse(app.setCurrentEnvironment(TEST_INVALID_PATH));
        Assertions.assertEquals(Optional.empty(), app.getCurrentEnvironment());
        
        /* test valid path */
        Assertions.assertTrue(app.setCurrentEnvironment(TEST_PATH));
        Assertions.assertEquals(TEST_PATH, app.getCurrentEnvironment().get());
        
        /* test playlist */ 
        //test n playlists
        Assertions.assertEquals(app.getPlaylists().size(), app.getNPlaylists());

        // test valid playlist 2
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST2)));
        Assertions.assertEquals(TEST_PATH_PLAYLIST2, app.getCurrentPlaylist().get().getPath());

        // test invalid playlist 
        Assertions.assertFalse(app.setCurrentPlaylist(app.getPlaylist(TEST_INVALID_PLAYLIST)));
        Assertions.assertEquals(Optional.empty(), app.getCurrentPlaylist());
        
        // test valid playlist 1
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST1)));
        Assertions.assertEquals(TEST_PATH_PLAYLIST1, app.getCurrentPlaylist().get().getPath());

        /* test player */
        app.getPlayer().setVolume(0.2);
        app.getPlayer().play();
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST2)));
        Assertions.assertTrue(app.getPlayer().setSong(app.getCurrentPlaylist().get().getSong(TEST_PATH_PLAYLIST2_SONG18).get()));
        Assertions.assertEquals(TEST_PATH_PLAYLIST2_SONG18, app.getPlayer().getCurrentSong().getFile().toPath());
        app.getPlayer().play();
        Thread.sleep(6000);
    }

    @Test void testPlaylists() {
        Reader app = new ReaderImpl();
        app.setCurrentEnvironment(TEST_PATH_PLAYLIST1);

        PlayList plist1 = new Mp3PlayList(TEST_PATH_PLAYLIST1, app.getPlaylist(TEST_PATH_PLAYLIST1).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        app.setCurrentEnvironment(TEST_PATH_PLAYLIST2);
        PlayList plist2 = new Mp3PlayList(TEST_PATH_PLAYLIST2, app.getPlaylist(TEST_PATH_PLAYLIST2).get().getSongs().stream()
            .map(s -> s.getFile().toPath())
            .toList());

        /* test on playlist with dynamic and small size */
        Assertions.assertEquals(13, plist1.getSize());
        Assertions.assertEquals("00:45:08", plist1.getTotalDuration());
        Assertions.assertEquals("italiani", plist1.getName());

        /* test on a playlist with static and big size */
        Assertions.assertEquals(24, plist2.getSize());
        Assertions.assertEquals("01:26:09", plist2.getTotalDuration());
        Assertions.assertEquals("Saggio EXPLORA", plist2.getName());
        
    }

    @Test void testSongs() throws Exception {
        Song s1 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG1);
        Song s3 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG3);
        Song s5 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG5);
        Song s4 = new Mp3Song(TEST_PATH_PLAYLIST1_SONG4);

        Assertions.assertEquals("Fedez, Annalisa, Articolo 31 - DISCO PARADISE (Visual Video)", s1.getName());
        Assertions.assertEquals("00:03:18", s1.DurationStringRep());

        Assertions.assertEquals("Fabio Rovazzi - ANDIAMO A COMANDARE (Official Video)", s3.getName());
        Assertions.assertEquals("00:03:23", s3.DurationStringRep());

        Assertions.assertEquals("Fabio Rovazzi Orietta Berti La discoteca italiana", s4.getName());
        Assertions.assertEquals("00:03:10", s4.DurationStringRep());

        Assertions.assertEquals("Mr.Rain - SUPEREROI (Official Video) [Sanremo 2023]", s5.getName());
        Assertions.assertEquals("00:03:24", s5.DurationStringRep());
    }

    @Test void testMediaControl() throws Exception {
        //Declaring a new MediaControl instance
        boolean flag;
        MediaControl mC1;
        mC1 = new Mp3MediaControl();
        flag = mC1.setPlaylist(new Mp3PlayList(TEST_PATH_PLAYLIST1, Reader.getAndFilterSongs(TEST_PATH_PLAYLIST1).get()));
        Assertions.assertEquals(true, flag);
        mC1.play();
        System.out.println(mC1.getCurrentSong().getName());
        mC1.setVolume(0.015);
        Assertions.assertEquals(0.015, mC1.getVolume());
        //Testing various void method useful for reproduction, song choice, queue gestion ecc.
        System.out.println(mC1.getDuration());
        mC1.nextSong();
        System.out.println(mC1.getCurrentSong().getName());
        mC1.prevSong();
        System.out.println(mC1.getCurrentSong().getName());
        mC1.setVolume(0.015);
        flag = mC1.setSong(mC1.getPlaylist().get(1));
        Assertions.assertEquals(true, flag);
        System.out.println(mC1.getCurrentSong().getName());
        Assertions.assertEquals(new Mp3Song(mC1.getPlaylist().get(1).getFile().toPath()).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        System.out.println(mC1.getCurrentSong().getName());
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        System.out.println(mC1.getCurrentSong().getName());
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG2).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        System.out.println(mC1.getCurrentSong().getName());
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG3).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG2).getName(), mC1.getCurrentSong().getName());
        flag = mC1.setSong(mC1.getPlaylist().get(0));
        System.out.println(mC1.getCurrentSong().getName());
        Assertions.assertEquals(true, flag);
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST1_SONG1).getName(), mC1.getCurrentSong().getName());
        mC1.setRepSpeed(2.0);
        flag = mC1.setSong(mC1.getPlaylist().get(mC1.getPlaylist().size() - 1));
        System.out.println(mC1.getCurrentSong().getName());
        Assertions.assertEquals(true, flag);
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
        TrackTrimmer trimmer = new TrackTrimmerImpl();
        
        Assertions.assertFalse( trimmer.trim("0:00", "0:20", "FirstTestMp3"));

        trimmer.setTrack(TEST_TRIM_MP3);
        Assertions.assertTrue(trimmer.trim("0:00", "0:20", "SecondTestMp3"));
        File secondTestMp3 = new File(TEST_PATH.toString() + FileSystems.getDefault().getSeparator() + "SecondTestMp3.mp3");
        Assertions.assertTrue(secondTestMp3.exists());
        secondTestMp3.delete();
        Assertions.assertFalse(trimmer.trim("", "", "ThirdTestMp3"));
        Assertions.assertFalse(trimmer.trim("0:25", "0:20", "FourthTestMp3"));
        Assertions.assertThrows(NumberFormatException.class, () -> trimmer.trim("ciao", "0:20", "FifthTestMp3"));

        trimmer.setTrack(TEST_TRIM_WAV);
        Assertions.assertTrue(trimmer.trim("0:00", "0:20", "SecondTestWav"));
        File secondTestWav = new File(TEST_PATH.toString() + FileSystems.getDefault().getSeparator() + "SecondTestWav.wav");
        Assertions.assertTrue(secondTestWav.exists());
        secondTestWav.delete();
        Assertions.assertFalse(trimmer.trim("", "", "ThirdTestWav"));
        Assertions.assertFalse(trimmer.trim("0:25", "0:20", "FourthTestWav"));
        Assertions.assertThrows(NumberFormatException.class, () -> trimmer.trim("ciao", "0:20", "FifthTestWav"));
    }
}