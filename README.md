# ElektReader - OOP project 

### TODO:
#### ALL: 
* relazione -> aprite il [https://github.com/pallax03/ElektReader/raw/master/GestioneProgetto.docx](file)
    * * 

* UML -> interfacce

* 

#### alex:
* api.Reader, model.ReaderImpl -> adds field Mp3MediaControl 
* controller.GUIController -> to create: find
* FXML -> improve app.fxml, app.css

#### samuele:
* controller -> improve PlayListsController, SongsController. 
    sam attento allo split guarda la canzone in generi musica romagnola polka ha un -
    devi assolutamente gestire e usare bene il regex

#### christian: 
* model.Mp3MediaControl -> devi cambiare l'implementazione 
    nel costruttore devi inizializzare tutti i campi per definire uno stato di inizializzazione
    abbiamo bisogno di una funzione setPlaylist(), che effettivamente ti carica tutti i campi che 
    stai gestendo ora nel costruttore.
    sicuramente avrai bisogno di un optional per il MediaPlayer dato che inizialmente nel costruttore non
    hai nessuna canzone da riprodurre, verrà creato nuovo ad ogni setPlaylist, deve essere un po la tua funzione di inizializzazione.

    !perchè questo -> ovviamente perchè nell'implementazione generale del lettore il lettore avrà sempre il campo per riprodurre inizializzato io non voglio che lui si resetta ogni volta che cambio playlist, ma semplicemente cambi il cmapo quando richiamo la determinata funzione

    Osservazione: ottenere le canzoni usando il metodo di Mazzo (getASong) non capisco il perché ma non mi fa ottenere il corretto inidice della canzone che  va impostata. Il test passa correttamente se mi recupero la playlist attraverso un getter!

#### matteo:
* ancora non é pronto il lavoro per te

# ---------------------------------------------

# da tradurre e sistemare:
## Introduction
Abbiamo deciso di creare un Lettore MP3, che gestisce le canzoni scaricate nel computer,
ogni cartella è una playlist ed essa può contenere dei brani *mp3*.

### Library:
Abbiamo utilizzato la libreria [https://openjfx.io/](javaFX) per l’implementazione grafica e per la manipolazione di generale,
e la libreria [https://jthink.net/jaudiotagger/](jaudiotagger) per leggere i metadata dei file.
