# ElektReader - OOP project 

### TODO:
#### ALL: 
* relazione -> aprite il [https://github.com/pallax03/ElektReader/raw/master/GestioneProgetto.docx](file)
    * * 

* UML -> interfacce

#### alex:
* controller.GUIController -> to create: find, and rersponsive panels adjust
* FXML -> improve app.fxml, app.css -> playlist and songs | all the media controls

#### samuele:
* controller -> improve PlayListsController, SongsController. 

#### christian: 
* model.Mp3MediaControl -> attento con tutti qugli opzionali, guardando la classe singolarmente e dandola impasto a chi lo state non lo conosce devi un attimo iniziare a gestire i get che potrebbero ditruggere le cose, metti caso che io ti ho costruto la classe e non ti richiamo un set playlist, se rtichiamo qualsiasi altro metodo (dove i get hanno problemi), tu mi devi tirare un IllegalStateException, perche ovviamente prima bisogna settare una playlist.
te lo dico perche si parla di buona implementazione ovvio che nel programma la gui fa premere pulsanti in ordine quindi le eccezioni non verranno mai tirate.

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
