package com.thesensei.MEEP.service;


import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.EventoTipologia;
import com.thesensei.MEEP.entity.Tipologia;
import com.thesensei.MEEP.repository.EventoRepository;
import com.thesensei.MEEP.repository.EventoTipologiaRepository;
import com.thesensei.MEEP.repository.TipologiaRepository;
import com.thesensei.MEEP.request.EventoTipologiaRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventoTipologiaService {

    private final EventoTipologiaRepository eventoTipologiaRepository;
    private final EventoRepository eventoRepository;
    private final TipologiaRepository tipologiaRepository;

    public EventoTipologiaService(EventoTipologiaRepository eventoTipologiaRepository, EventoRepository eventoRepository, TipologiaRepository tipologiaRepository) {
        this.eventoTipologiaRepository = eventoTipologiaRepository;
        this.eventoRepository = eventoRepository;
        this.tipologiaRepository = tipologiaRepository;
    }

    /**
     * addTipologiaEvento() -> permette di aggiungere le tipologie agli eventi
     *             Dato in input il codice dell'evento e l'id della tipologia, dopo averne controllato l'esistenza, crea un nuovo riferimento sul db.
     * @param request
     * @return response
     */
    public ResponseEntity<?> addTipologiaevento(EventoTipologiaRequest request){
            Optional<EventoTipologia> et = eventoTipologiaRepository.findByTipologiaIdTipologiaAndEventoCodiceEvento(request.getIdTipologia(), request.getCodiceEvento());   //controlla che sul db non esiste già un record con gli stessi dati
            if(et.isEmpty()) {
                EventoTipologia e = fromRequestToEntity(request);        //dalla richiesta viene creata una nuova entità EventoTipologia per salvare il record sul db
                if(e != null) {
                    eventoTipologiaRepository.save(e);
                    return new ResponseEntity("Riferimento evento e tipologia creato", HttpStatus.CREATED);
                } else
                    return new ResponseEntity("L' evento o la Tipologia non esistono", HttpStatus.BAD_REQUEST);
            } else{
                return new ResponseEntity("Errore: Il riferimento evento tipologia esiste già", HttpStatus.BAD_REQUEST);
            }
    }

    /**
     * deleteTipologiaEvento() -> permette di cancellare le tipologie di un evento.
     *             Dato in input il codice dell'evento e l'id della tipologia, si controlla l'esistenza sul database di un record con gli stessi e,
     *             in caso positivo, elimina il riferimento.
     * @param request
     * @return Response
     */
    public ResponseEntity<?> deleteTipologiaevento(EventoTipologiaRequest request){

        Optional<EventoTipologia> et = eventoTipologiaRepository.findByTipologiaIdTipologiaAndEventoCodiceEvento(request.getIdTipologia(), request.getCodiceEvento());
        if(!et.isEmpty()) {
            eventoTipologiaRepository.delete(et.get());
            return new ResponseEntity("Riferimento evento e tipologia eliminato", HttpStatus.OK);
        } else
            return new ResponseEntity("Errore: Impossibile eliminare, Riferimento evento e tipologia non trovato", HttpStatus.NOT_FOUND);
    }

    /**
     * fromRequestToEntity() -> permette di creare un nuovo oggetto EventoTipologia prendendo i dati dalla request
     * @param request (idTipologia, codiceEvento)
     * @return EventoTipologia
     */
    public EventoTipologia fromRequestToEntity(EventoTipologiaRequest request){
        Optional<Evento> evento = eventoRepository.findByCodiceEvento(request.getCodiceEvento());
        if(evento.isEmpty()){
            return null;
        }else{
            Optional<Tipologia> tipologia = tipologiaRepository.findByidTipologia(request.getIdTipologia());
            if(tipologia.isEmpty()){
                return null;
            }else{
                EventoTipologia et = new EventoTipologia(evento.get(),tipologia.get());
                return et;
            }
        }
    }

}