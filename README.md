# ElektReader - OOP project 

### TODO:
#### ALL: 
* relazione -> aprite il [https://github.com/pallax03/ElektReader/raw/master/GestioneProgetto.docx](file)
    * * 

* UML -> interfacce

* 

#### alex:
* FXML -> improve app.fxml, app.css
* controller.GUIController -> to create: responsive, find
* api.Reader, model.ReaderImpl -> adds field Mp3MediaControl 

#### samuele:
* controller -> improve PlayListsController, SongsController
* model -> Mp3PlayList MI RACCOMANDO DOPO AVER CLICCATO UNA PLAYLIST DEVI RESETTARE LA CODA (non so se lo hai gia gestito)
* BUG:
    attento perchè il metodo getIndexFromName se non è messo un index lancia un eccezione spaventosa tu devi assolutamente gestire l'evento e sicuramente ti conviene gestirlo  internamente ovvero non cmabiando il nome del file ma semplicemente butti via l'indice e ordini per cazzi loro però poi dobbiamo comunque essere in grado di poterle ordinare cambindo il nome del file oppure ti complichi la vita cambiando il nome del file e mettendolo tu (questa é una bella bega purtroppo ordini in maniera causale per come vengono carcate ma quello non importa ma devi assolutamente, cmabiare il nome del file in quel caso, vedi se farlo con la libreria Files o addirittura con la libreria jaudiotagger ho visto che è stra utilizzata per la manipolazione dei file mp3), SICURO NE DOBBIAMO PARLARE INSIEME se vuoi iniziare a fare qualcosa ti consiglio di fare un metodo setIndex che cambia il nome del file e mette un nuovo indice (ATTENTO E SE UN'ALTRA CANZONE HA QUEL NUMERO!!!!!!!!! (DEVI SHIFTARLE TUTTE DI UNO DA DOPO IL NUMERO NUOVO SOVRASCRITTO))

#### christian: 
* model.Mp3MediaControl -> devi cambiare l'implementazione 
    nel costruttore devi inizializzare tutti i campi per definire uno stato di inizializzazione
    abbiamo bisogno di una funzione setPlaylist(), che effettivamente ti carica tutti i campi che 
    stai gestendo ora nel costruttore.
    sicuramente avrai bisogno di un optional per il MediaPlayer dato che inizialmente nel costruttore non
    hai nessuna canzone da riprodurre, verrà creato nuovo ad ogni setPlaylist, deve essere un po la tua funzione di inizializzazione.

    !perchè questo -> ovviamente perchè nell'implementazione generale del lettore il lettore avrà sempre il campo per riprodurre inizializzato io non voglio che lui si resetta ogni volta che cambio playlist, ma semplicemente cambi il cmapo quando richiamo la determinata funzione

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
