package com.thesensei.MEEP.controller;

import com.thesensei.MEEP.request.CreateEventoRequest;
import com.thesensei.MEEP.request.FilterRequest;
import com.thesensei.MEEP.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("evento")
@CrossOrigin
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }



    /**
     * createEvento() -> permette di creare un nuovo evento dai dati passati nella request e salvarli sul db
     * @param request  (titolo, descrizione, data, oraInizio, oraFine, maxPartecipanti, maxAccompagnatori)
     * @param httpRequest
     * @return response (restituisce l'oggetto Evento creato)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createEvento(@RequestBody @Valid CreateEventoRequest request, HttpServletRequest httpRequest){
        return eventoService.createEvento(request, httpRequest.getAttribute("matricola").toString());
    }

    /**
     * deleteEvento() -> dato il codice dell'evento, ne esegue la cancellazione dal db
     * @param codice_evento
     * @param httpRequest
     * @return response
     */
    @DeleteMapping("/{codice_evento}")
    public ResponseEntity<?> deleteEvento(@PathVariable int codice_evento, HttpServletRequest httpRequest){
        return eventoService.deleteEvento(codice_evento, httpRequest.getAttribute("matricola").toString());
    }

    /**
     * updateEvento() -> dato il codice di un evento, permette di modificarne i dati
     * @param request (titolo, descrizione, data, oraInizio, oraFine, maxPartecipanti, maxAccompagnatori)
     * @param codice_evento
     * @param httpRequest
     * @return response (restituisce il nuovo oggetto Evento con modifiche apportate)
     */
    @PutMapping("/edit/{codice_evento}")
    public ResponseEntity<?> updateEvento(@RequestBody @Valid CreateEventoRequest request, @PathVariable int codice_evento ,HttpServletRequest httpRequest){
        return eventoService.updateEvento(request, codice_evento, httpRequest.getAttribute("matricola").toString());
    }

    /**
     * editThumbnail() -> dato il codice dell'evento, permette di sostituire la copertina (immagine) dell'evento con un nuovo file
     * @param codice_evento
     * @param file (il nuovo file da caricare sul server)
     * @param httpRequest
     * @return response
     */
    @PostMapping("/{codice_evento}/edit-foto")
    public ResponseEntity<?> editCopertina(@PathVariable int codice_evento, @RequestParam MultipartFile file, HttpServletRequest httpRequest){
        return eventoService.editThumbnail(codice_evento,file,httpRequest.getAttribute("matricola").toString());
    }


    /**
     * getEventoDetails() -> dato il codice di un evento, ne restituisce tutti i dati
     * @param codiceEvento
     * @return response (restituisce l'oggetto Evento)
     */
    @GetMapping("/{codiceEvento}")
    public ResponseEntity<?> getEventoDetails(@PathVariable int codiceEvento){
        return eventoService.getEventoDetails(codiceEvento);
    }


    /**
     * getCopertinaEvento() -> dato il codice di un evento, ne restituisce l'immagine di copertina
     * @param codice_evento
     * @return response (byte[] dell'immagine di copertina salvata sul server)
     * @throws IOException
     */
    @GetMapping("/{codice_evento}/foto")
    public ResponseEntity<?> getCopertinaEvento(@PathVariable int codice_evento) throws IOException {
        return eventoService.getThumbnail(codice_evento);
    }

    /**
     * getEventiFiltrati() -> permette di ottenere una lista di eventi filtrata in base ai parametri inseriti
     * @param request (int[] idTipologia, LocalDate dataEvento, LocalTime oraEvento, int maxPartecipanti, boolean includeAdmin)
     * @param httpRequest
     * @return response (lista di eventi filtrati)
     */
    @PostMapping("/filter")
    public ResponseEntity<?> getEventiFiltati(@RequestBody FilterRequest request, HttpServletRequest httpRequest){
        return eventoService.getEventiFiltrati(request, httpRequest.getAttribute("matricola").toString());
    }

    /**
     * getIndirizzoEvento() -> dato il codice dell'evento, restituisce l'indirizzo
     * @param codice_evento
     * @return response (oggetto Indirizzo)
     */
    @GetMapping("/{codice_evento}/indirizzo")
    public ResponseEntity<?> getIndirizzoEvento(@PathVariable int codice_evento){
        return eventoService.getIndirizzoEvento(codice_evento);
    }

}
