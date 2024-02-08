/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package elektreader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
        Assertions.assertEquals(app.getCurrentEnvironment(), Optional.empty());
        
        /* test valid path */
        Assertions.assertTrue(app.setCurrentEnvironment(TEST_PATH));
        Assertions.assertEquals(app.getCurrentEnvironment().get(), TEST_PATH);
        

        /* test playlist */ 
        // test valid playlist 1
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST1)));
        Assertions.assertEquals(app.getCurrentPlaylist().get().getPath(), TEST_PATH_PLAYLIST1);

        // test invalid playlist 
        Assertions.assertFalse(app.setCurrentPlaylist(app.getPlaylist(TEST_INVALID_PLAYLIST)));
        Assertions.assertEquals(app.getCurrentPlaylist(), Optional.empty());
        
        // test valid playlist 2
        Assertions.assertTrue(app.setCurrentPlaylist(app.getPlaylist(TEST_PATH_PLAYLIST2)));
        Assertions.assertEquals(app.getCurrentPlaylist().get().getPath(), TEST_PATH_PLAYLIST2);

        System.out.println(app.getPlaylist(TEST_PATH_PLAYLIST1).get().getSize());

        // // test invalid song - current playlist 1
        // Assertions.assertFalse(app.setCurrentSong(app.getPlaylist(TEST_PATH_PLAYLIST2).get().getSong(getASong(TEST_PATH_PLAYLIST1, 0))));
        // Assertions.assertEquals(app.getCurrentSong(), Optional.empty());

        // //test valid song - current playlist 2
        // Assertions.assertTrue(app.setCurrentSong(app.getCurrentPlaylist().get().getSong(getASong(TEST_PATH_PLAYLIST2, 0))));
        // Assertions.assertEquals(app.getCurrentSong().get().getFile().toPath(), getASong(TEST_PATH_PLAYLIST2, 0));
    

        /* test player - playlist 2 */
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
        MediaControl mC1;
        mC1 = new Mp3MediaControl();
        mC1.setPlaylist(new Mp3PlayList(TEST_PATH_PLAYLIST2, Reader.getAndFilterSongs(TEST_PATH_PLAYLIST2).get()));
        //Volume methods tests
        double volume = mC1.getVolume();
        Assertions.assertEquals(volume, mC1.getVolume());
        mC1.mute();
        Assertions.assertEquals(0.0, mC1.getVolume());
        mC1.setVolume(0.2);
        Assertions.assertEquals(0.2, mC1.getVolume());

        //Testing various void method useful for reproduction, song choice, queue gestion ecc.
        mC1.getDuration();
        mC1.play();
        Thread.sleep(2000);
        mC1.nextSong();
        Thread.sleep(2000);
        mC1.prevSong();
        Thread.sleep(2000);
        mC1.mute();
        //mC1.setSong(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 1)));
        mC1.setSong(mC1.getPlaylist().get(1));
        System.out.println(mC1.getCurrentSong().getName());
        Thread.sleep(2000);
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG18).getName(), mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG10).getName(), mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG18).getName(), mC1.getCurrentSong().getName());
        //mC1.setSong(new Mp3Song(getASong(TEST_PATH_PLAYLIST2, 0)));
        mC1.setSong(mC1.getPlaylist().get(0));
        System.out.println(mC1.getCurrentSong().getName());
        Thread.sleep(2000);
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG10).getName(), mC1.getCurrentSong().getName());
        //Changing reference of my mediaControl to another object of the same class.
        mC1 = new Mp3MediaControl();
        mC1.setPlaylist(new Mp3PlayList(TEST_PATH_PLAYLIST2, Arrays.asList(TEST_PATH_PLAYLIST2_SONG10, TEST_PATH_PLAYLIST2_SONG18)));
        mC1.mute();
        Assertions.assertEquals(0.0, mC1.getVolume());
        mC1.setVolume(0.5);
        Assertions.assertEquals(0.5, mC1.getVolume());
        mC1.play();

        //Testing basic reproduction methods (GUI implementation will be still more accurate to be sure that everything works correctly.).
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG10).getName(), mC1.getCurrentSong().getName());
        System.out.println(mC1.getCurrentSong().getName());
        mC1.nextSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG10).getName(), mC1.getCurrentSong().getName());
        System.out.println(mC1.getCurrentSong().getName());
        mC1.prevSong();
        Assertions.assertEquals(new Mp3Song(TEST_PATH_PLAYLIST2_SONG10).getName(), mC1.getCurrentSong().getName());
        System.out.println(mC1.getCurrentSong().getName());
        mC1.stop();
    }
}