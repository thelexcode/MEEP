package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.User;
import com.thesensei.MEEP.repository.EventoRepository;
import com.thesensei.MEEP.repository.EventoTipologiaRepository;
import com.thesensei.MEEP.repository.UserRepository;
import com.thesensei.MEEP.request.CreateEventoRequest;
import com.thesensei.MEEP.request.FilterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;

    private final EventoTipologiaRepository eventoTipologiaRepository;
    private final ImageService imageService;

    public EventoService(EventoRepository eventoRepository, UserRepository userRepository, EventoTipologiaRepository eventoTipologiaRepository, ImageService imageService) {
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
        this.eventoTipologiaRepository = eventoTipologiaRepository;
        this.imageService = imageService;
    }


    /*
        Il metodo getEventiDisponibili, restituisce tutti gli eventi a cui è possibile partecipare
        (esegue dei controlli sul numero dei partecipanti, non selezionando eventi con numero massimo partecipanti superato,
        e controllando la data e ora dell'evento, non selezionando gli eventi scaduti )
    */
    public ResponseEntity<?> getEventiDisponibili(String matricola){
        LocalDateTime oggi = LocalDateTime.now();
        List<Evento> eventi = eventoRepository.getEventiDisponibiliNoAdmin(matricola,oggi.toLocalDate(), oggi.toLocalTime());

        if (eventi.isEmpty())
            return new ResponseEntity("Non sono stati trovati eventi",HttpStatus.NOT_FOUND);
        else{
            return new ResponseEntity(eventi,HttpStatus.OK);
        }
    }

    /*
           Il metodo getEventiFiltrati permette di filtrare gli eventi diponibili dati i filtri da applicare ricevuti nella request.
           E' possibile filtrare per: una data specifica, orario d'inizio specifico, numero massimo di partecipanti e per n tipologie
           (Possono essere applicato più di un filtro alla volta)
     */

    public ResponseEntity<?> getEventiFiltrati(FilterRequest request, String matricola){
        List<Evento> eventi;
        LocalDateTime oggi = LocalDateTime.now();

        LocalTime oraEvento = request.getOraEvento();
        int[] idTipologie=request.getIdTipologia();
        int maxPartecipanti= request.getMaxPartecipanti();


        //filtra per data evento
        if(request.getDataEvento()==null){
            eventi = eventoRepository.getEventiDisponibili(oggi.toLocalDate(), oggi.toLocalTime());
        }else{
            eventi = eventoRepository.getEventiPerData(request.getDataEvento());
        }

        List<Evento> filtered = (List<Evento>) ((ArrayList<Evento>) eventi).clone();

        //filtra per includeAdmin (restituire gli eventi anche se l'utente è admin)
        if(!request.isIncludeAdmin())
            filtered = eventi.stream().filter( e -> e.getUtenteAdmin().getMatricola()!=matricola).collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();

        //filtra per ora inizio evento
        if(oraEvento!=null){
            filtered = eventi.stream().filter(e -> e.getOraInizio()==oraEvento).collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        //filtra per n max partecipanti
        if(maxPartecipanti!=-1){
            filtered = eventi.stream().filter(e -> e.getMaxPartecipanti()==maxPartecipanti).collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        filtered.clear();

        //filtra per tipologie
        if(idTipologie.length!=0){
            filtered = eventi.stream()
                    .filter(e -> Arrays.stream(idTipologie).allMatch(t -> eventoTipologiaRepository.existsByTipologiaIdTipologiaAndEventoCodiceEvento(t,e.getCodiceEvento())))
                    .collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        return new ResponseEntity(eventi,HttpStatus.OK);
    }

    /*

        il metodo getEventoDetails, dato il codice dell'evento, se esiste, ne restituisce tutti i dati

     */

    public ResponseEntity<?> getEventoDetails(int codiceEvento) {

        Optional<Evento> e = eventoRepository.findByCodiceEvento(codiceEvento);
        if (!e.isEmpty()) {
            Evento evento = e.get();
            return new ResponseEntity(evento, HttpStatus.OK);
        }
        return new ResponseEntity("Errore: evento non trovato", HttpStatus.NOT_FOUND);
    }

    /*
            Il metodo createEvento permette di creare un nuovo evento.
            Dati in input tutti i dati necessari per la creazione dell'evento (della classe CreateEventoRequest) e la matricola dell'utente amministratore evento,
            se i dati inseriti sono corretti, salva l'evento sul database
    */

    public ResponseEntity<?> createEvento(CreateEventoRequest request, String matricola) {
        Optional<User> u = userRepository.findById(matricola);
        if (!u.isEmpty()) {
            Evento evento = fromRequestToEntity(request, u.get());
            if (evento == null) {
                return new ResponseEntity("Errore: i dati inseriti sono errati", HttpStatus.BAD_REQUEST);
            }else{
                try {
                    eventoRepository.save(evento);
                    return new ResponseEntity(evento, HttpStatus.CREATED);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return new ResponseEntity("Errore nella creazione dell'evento", HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            return new ResponseEntity("Errore nell'autenticazione: Non è stato possibile trovare l'utente, riesegui l'accesso.", HttpStatus.UNAUTHORIZED);
        }
    }

    /*
            Il metodo updateEvento permette di modificare i dati di un evento.
            Dati in input tutti i dati dell'evento da modificare (della classe CreateEventoRequest), il codice dell'evento e la matricola dell'utente,
            controlla se l'evento esiste, se l'utente autenticato è autorizzato a modificarlo (se è l'amministratore) e, se non ci sono errori nei dati inseriti, modifica l'evento
    */

    public ResponseEntity<?> updateEvento(CreateEventoRequest request, int codice_evento ,String matricola){
        Optional<User> u = userRepository.findById(matricola);
        if (!u.isEmpty()) {
            Optional<Evento> e = eventoRepository.findByCodiceEvento(codice_evento);
            if (!e.isEmpty()) {
                User utente = u.get();
                if (e.get().getUtenteAdmin().equals(utente)) {    //controlla se l'utente è l'amministratore
                    Evento evento = fromRequestToEntity(request, utente);
                    evento.setCodiceEvento(codice_evento);
                    try {
                        eventoRepository.save(evento);
                        return new ResponseEntity("Modifica evento eseguita", HttpStatus.CREATED);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                        return new ResponseEntity("Errore nella modifica dell'evento", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity("Errore: Utente non autorizzato", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity("Errore: Impossibile trovare l'evento", HttpStatus.NOT_FOUND);
            }
        }else {
                return new ResponseEntity("Errore nell'autenticazione: Non è stato possibile trovare l'utente, riesegui l'accesso.", HttpStatus.UNAUTHORIZED);
            }
    }

    /*
            Il metodo deleteEvento permette di cancellare un evento.
            Dati in input il codice dell'evento e la matricola dell'utente, controlla se l'evento esiste,
            se l'utente autenticato è autorizzato a cancellare l'evento (controlla se è l'amministratore) e,
            se non ci sono errori nei dati inseriti, elimina l'evento.
    */

    public ResponseEntity<?> deleteEvento(int codice_evento, String matricola){
        Optional<Evento> e = eventoRepository.findByCodiceEvento(codice_evento);
        if(!e.isEmpty()){
            Evento evento = e.get();
            if(evento.getUtenteAdmin().getMatricola().equals(matricola)){  //controlla se l'utente è l'amministratore
                eventoRepository.delete(evento);
                return new ResponseEntity("Cancellazione evento completata",HttpStatus.OK);
            }else{
                return new ResponseEntity("Errore: Utente non autorizzato",HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity("Errore: Impossibile trovare l'evento",HttpStatus.NOT_FOUND);
        }
    }

    /*
            Il metodo fromRequestToEntity permette di creare l'entità Evento prendendo i dati dalla request (tra cui il Titolo, descrizione, data e ora, n partecipanti,..)
    */

    private Evento fromRequestToEntity(CreateEventoRequest request, User utenteAdmin){
        LocalDateTime dataInizio = LocalDateTime.from(request.getData().atTime(request.getOraInizio()));
        LocalDateTime dataFine = LocalDateTime.from(request.getData().atTime(request.getOraFine()));
        if(dataInizio.isAfter(LocalDateTime.now())&&dataFine.isAfter(dataInizio)) {   //controlli sulla data e l'ora di inizio e fine evento
            Evento evento = new Evento(
                    utenteAdmin,
                    request.getTitolo(),
                    request.getDescrizione(),
                    request.getData(),
                    request.getOraInizio(),
                    request.getOraFine(),
                    request.getMaxPartecipanti(),
                    request.getMaxAccompagnatori()
            );
            return evento;
        }else{
            return null;
        }
    }

    @Value("${event.thumbnail.size}")
    private int imageSize;
    @Value("${event.thumbnail.width}")
    private int imageWidth;
    @Value("${event.thumbnail.height}")
    private int imageHeight;
    @Value("${event.thumbnail.path}")
    private String imagePath;


    /*
        il metodo editThumbnail, ricevuto un file in input ed effettuati  gli opportuni controlli
        (estensioni del file, dimensioni, salvataggio sul disco), consente di  cambiare la copertina dell'evento e modificare il suo riferimento sul db

    */

    public ResponseEntity<?> editThumbnail(int codiceEvento, MultipartFile file ,String matricola){
        Optional<Evento> e = eventoRepository.findByCodiceEvento(codiceEvento);
        if(!e.isEmpty()){
            //controlla se l'utente loggato è amministratore dell'evento
            if(e.get().getUtenteAdmin().getMatricola().equals(matricola)){
                if(!imageService.checkExtensions(file))
                    return new ResponseEntity("Errore: Il file non rispetta le estensioni consentite",HttpStatus.BAD_REQUEST);
                if(!imageService.checkImageSize(file,imageSize))
                    return new ResponseEntity("Errore: Il file inserito è maggiore di "+imageSize+" bytes oppure è vuoto",HttpStatus.BAD_REQUEST);
                BufferedImage image = imageService.fromMultipartFileToBufferedImage(file);
                if(!imageService.checkDimensioni(image,imageHeight,imageWidth))
                    return new ResponseEntity("Errore: l'immagine ha dimensioni errate",HttpStatus.BAD_REQUEST);
                String nomeFile=String.valueOf(codiceEvento)+"_"+file.getOriginalFilename();
                if(!imageService.uploadFile(file,nomeFile,imagePath))
                    return new ResponseEntity("Errore nell'upload dell'immagine",HttpStatus.BAD_REQUEST);
                else{
                    eventoRepository.updateThumbnail(codiceEvento,nomeFile);
                    return new ResponseEntity("Copertina Evento cambiata con successo",HttpStatus.OK);
                }
            }else {
                return new ResponseEntity("Errore: Utente non autorizzato",HttpStatus.UNAUTHORIZED);
            }
        }else {
            return new ResponseEntity("Errore: Impossibile trovare l'evento",HttpStatus.NOT_FOUND);
        }
    }

    /*
        Il metodo getThumbnail permette (passato in input il codice dell'evento) di restituire l'immmagie della copertina (thumbnail) se esiste
    */

    public ResponseEntity<?> getThumbnail(int codice_evento) throws IOException {
        if(eventoRepository.existsById(codice_evento)){
            String imgName = eventoRepository.getThumbnail(codice_evento);
            Path pathImage = Paths.get(imagePath+imgName);
            try{
                byte[] bytes = Files.readAllBytes(pathImage);
                return new ResponseEntity(bytes,HttpStatus.OK);
            }catch (IOException e){
                return new ResponseEntity("Errore: Immagine non trovata",HttpStatus.NOT_FOUND);
            }
        }else
            return new ResponseEntity("Errore: Non è stato possibile trovare l'evento.", HttpStatus.NOT_FOUND);
    }

}
