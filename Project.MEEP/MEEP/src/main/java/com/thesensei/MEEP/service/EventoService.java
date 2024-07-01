package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.Indirizzo;
import com.thesensei.MEEP.entity.User;
import com.thesensei.MEEP.repository.EventoRepository;
import com.thesensei.MEEP.repository.EventoTipologiaRepository;
import com.thesensei.MEEP.repository.IndirizzoRepository;
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

    private final IndirizzoRepository indirizzoRepository;

    public EventoService(EventoRepository eventoRepository, UserRepository userRepository, EventoTipologiaRepository eventoTipologiaRepository, ImageService imageService, IndirizzoRepository indirizzoRepository) {
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
        this.eventoTipologiaRepository = eventoTipologiaRepository;
        this.imageService = imageService;
        this.indirizzoRepository = indirizzoRepository;
    }



    /**
     * getEventiDisponibili -> restituisce tutti gli eventi a cui l'utente può partecipare
     *         (esegue dei controlli sul numero di partecipanti, non selezionando eventi con numero massimo partecipanti superato,
     *         e controlla la data e ora dell'evento, non selezionando gli eventi scaduti ).
     *         Non sono inclusi gli eventi di cui l'utente è amministratore
     * @param matricola
     * @return Response (List<Evento>)
     */
    public ResponseEntity<?> getEventiDisponibili(String matricola){
        LocalDateTime oggi = LocalDateTime.now();
        List<Evento> eventi = eventoRepository.getEventiDisponibiliNoAdmin(matricola,oggi.toLocalDate(), oggi.toLocalTime());
        return new ResponseEntity(eventi,HttpStatus.OK);
    }

    /**
     * getEventiFiltrati() -> permette di filtrare gli eventi diponibili in base ai filtri ricevuti nella request.
     *            E' possibile filtrare per: una data specifica, ora d'inizio, numero massimo di partecipanti e per n tipologie dell'evento
     *            (Possono essere applicati più di un filtro alla volta)
     * @param request (int[] idTipologia, LocalDate dataEvento, LocalTime oraEvento, int maxPartecipanti, boolean includeAdmin) i parametri non sono obbligatori
     * @param matricola
     * @return Response (list <Evento>  , contiene la lista degli eventi filtrati)
     */
    public ResponseEntity<?> getEventiFiltrati(FilterRequest request, String matricola){
        List<Evento> eventi;
        LocalDateTime oggi = LocalDateTime.now();

        LocalTime oraEvento = request.getOraEvento();
        int maxPartecipanti= request.getMaxPartecipanti();


        //filtra per data evento
        if(request.getDataEvento()==null){
            eventi = eventoRepository.getEventiDisponibili(oggi.toLocalDate(), oggi.toLocalTime());
        }else{
            eventi = eventoRepository.getEventiPerData(request.getDataEvento());
        }

        List<Evento> filtered = (List<Evento>) ((ArrayList<Evento>) eventi).clone();

        //filtra per includeAdmin (restituire gli eventi anche se l'utente è admin)
        if(!request.isIncludeAdmin()){
            filtered = eventi.stream().filter( e -> !e.getUtenteAdmin().getMatricola().equals(matricola)).collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        //filtra per ora inizio evento
        if(oraEvento!=null){
            filtered = eventi.stream().filter(e -> e.getOraInizio().equals(oraEvento)).collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        //filtra per n max partecipanti
        if(maxPartecipanti!=-1){
            filtered = eventi.stream().filter(e -> e.getMaxPartecipanti()==maxPartecipanti).collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        //filtra per tipologie
        if(request.getIdTipologia()!=null){
            int[] idTipologie=request.getIdTipologia();
            filtered = eventi.stream()
                    .filter(e -> Arrays.stream(idTipologie).allMatch(t -> eventoTipologiaRepository.existsByTipologiaIdTipologiaAndEventoCodiceEvento(t,e.getCodiceEvento())))
                    .collect(Collectors.toList());
            eventi  = (List<Evento>) ((ArrayList<Evento>) filtered).clone();
        }

        return new ResponseEntity(eventi,HttpStatus.OK);
    }

    /**
     * getEventoDetails() -> dato il codice dell'evento, se esiste, ne restituisce tutti i dati
     * @param codiceEvento
     * @return Response (oggetto Evento)
     */
    public ResponseEntity<?> getEventoDetails(int codiceEvento) {

        Optional<Evento> e = eventoRepository.findByCodiceEvento(codiceEvento);
        if (!e.isEmpty()) {
            Evento evento = e.get();
            return new ResponseEntity(evento, HttpStatus.OK);
        }
        return new ResponseEntity("Errore: evento non trovato", HttpStatus.NOT_FOUND);
    }

    /**
     * createEvento() -> Permette di creare un nuovo evento e salvarlo sul db.
     *                   Dati in input tutti i dati necessari per la creazione di un evento e la matricola dell'utente amministratore,
     *                   se i dati inseriti sono corretti, salva l'evento sul database
     * @param request (titolo, descrizione, data, oraInizio, oraFine, maxPartecipanti, maxAccompagnatori)
     * @param matricola
     * @return response (oggetto Evento creato)
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
        } else
            return new ResponseEntity("Errore nell'autenticazione: Non è stato possibile trovare l'utente, riesegui l'accesso.", HttpStatus.UNAUTHORIZED);
    }

    /**
     * updateEvento() -> Permette di modificare un evento salvato sul db.
     *                  Dati in input tutti i dati dell'evento da modificare, il codice dell'evento e la matricola dell'utente,
     *                  controlla se l'evento esiste, se l'utente autenticato è autorizzato a modificarlo (se è l'amministratore)
     *                  e, se non ci sono errori nei dati inseriti, modifica l'evento
     * @param request
     * @param codice_evento
     * @param matricola
     * @return Response (oggetto Evento modificato)
     */
    public ResponseEntity<?> updateEvento(CreateEventoRequest request, int codice_evento ,String matricola) {
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
                        return new ResponseEntity(evento, HttpStatus.OK);
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                        return new ResponseEntity("Errore nella modifica dell'evento", HttpStatus.BAD_REQUEST);
                    }
                } else
                    return new ResponseEntity("Errore: Utente non autorizzato", HttpStatus.UNAUTHORIZED);
            } else
                return new ResponseEntity("Errore: Impossibile trovare l'evento", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity("Errore nell'autenticazione: Non è stato possibile trovare l'utente, riesegui l'accesso.", HttpStatus.UNAUTHORIZED);
    }

    /**
     * deleteEvento() -> permette di cancellare un evento salvato sul db.
     *             Dati in input il codice dell'evento e la matricola dell'utente, controlla se l'evento esiste,
     *             se l'utente autenticato è autorizzato a cancellare l'evento (controlla se è l'amministratore) e,
     *             se non ci sono errori nei dati inseriti, elimina l'evento.
     * @param codice_evento
     * @param matricola
     * @return response
     */
    public ResponseEntity<?> deleteEvento(int codice_evento, String matricola){
        Optional<Evento> e = eventoRepository.findByCodiceEvento(codice_evento);
        if(!e.isEmpty()){
            Evento evento = e.get();
            if(evento.getUtenteAdmin().getMatricola().equals(matricola)){  //controlla se l'utente è l'amministratore
                eventoRepository.delete(evento);
                return new ResponseEntity("Cancellazione evento completata",HttpStatus.OK);
            }else
                return new ResponseEntity("Errore: Utente non autorizzato",HttpStatus.UNAUTHORIZED);
        }else
            return new ResponseEntity("Errore: Impossibile trovare l'evento",HttpStatus.NOT_FOUND);
    }


    /**
     * fromRequestToEntity() -> permette di creare un nuovo oggetto Evento prendendo i dati dalla request
     * @param request (titolo, descrizione, data, oraInizio, oraFine, maxPartecipanti, maxAccompagnatori)
     * @param utenteAdmin
     * @return Evento
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
        }else
            return null;
    }

    /**
     * img properties
     */
    @Value("${event.thumbnail.size}")
    private int imageSize;
    @Value("${event.thumbnail.width}")
    private int imageWidth;
    @Value("${event.thumbnail.height}")
    private int imageHeight;
    @Value("${event.thumbnail.path}")
    private String imagePath;

    /**
     * editThumbnail() -> permette di modificare l'immagine di copertina dell'evento.
     *          Dati in input codice evento, matricola e un file, effettua una serie di controlli sul file ricevuto (estensioni del file, dimensioni massime e corretto salvataggio sul server) e,
     *          se tutte le condizioni sono rispettate, permette di modificare il riferimento al path dell'immagine sul db (il campo "thumbnail")
     * @param codiceEvento
     * @param file
     * @param matricola
     * @return response
     */
    public ResponseEntity<?> editThumbnail(int codiceEvento, MultipartFile file ,String matricola){
        Optional<Evento> e = eventoRepository.findByCodiceEvento(codiceEvento);
        if(!e.isEmpty()){
            //controlla se l'utente loggato è l'amministratore dell'evento
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
            }else
                return new ResponseEntity("Errore: Utente non autorizzato",HttpStatus.UNAUTHORIZED);
        }else
            return new ResponseEntity("Errore: Impossibile trovare l'evento",HttpStatus.NOT_FOUND);
    }

    /**
     * getThumbnail() ->  dato in input il codice dell'evento, restituisce l'immagine della copertina se esiste sul server
     * @param codice_evento
     * @return response (byte[] dell'immagine trovata)
     * @throws IOException
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

    /**
     * getIndirizzoEvento() -> dato il codice di un evento, se esiste, ne restituisce l'indirizzo
     * @param codice_evento
     * @return response (oggetto Indirizzo)
     */
    public ResponseEntity<?> getIndirizzoEvento(int codice_evento){
        if(eventoRepository.existsById(codice_evento)){
            Optional<Indirizzo> indirizzo = indirizzoRepository.getIndirizzoByEvento(codice_evento);
            if(!indirizzo.isEmpty()){
                return new ResponseEntity(indirizzo.get(),HttpStatus.OK);
            }
            return new ResponseEntity("Errore: Non è stato possibile trovare l'indirizzo.", HttpStatus.NOT_FOUND);
        }else
            return new ResponseEntity("Errore: Non è stato possibile trovare l'evento.", HttpStatus.NOT_FOUND);
    }

}
