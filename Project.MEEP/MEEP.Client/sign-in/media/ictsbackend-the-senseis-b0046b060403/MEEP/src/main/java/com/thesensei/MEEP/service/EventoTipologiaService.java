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

    /*
            Il metodo addTipologiaEvento serve ad aggiungere agli eventi la tipologia
            Dato in input il codice dell'evento e l'id della tipologia,
            si controlla l'esistenza sul database di un record con gli stessi e, in caso negativo, crea un nuovo riferimento.
    */

    public ResponseEntity<?> addTipologiaevento(EventoTipologiaRequest ev){
            Optional<EventoTipologia> et = eventoTipologiaRepository.findByTipologiaIdTipologiaAndEventoCodiceEvento(ev.getIdTipologia(), ev.getCodiceEvento());
            if(et.isEmpty()) {
                EventoTipologia e = fromRequestToEntity(ev);        //dalla richiesta viene creata una nuova entità EventoTipologia per salvare il record sul db
                if(e != null) {
                    eventoTipologiaRepository.save(e);
                    return new ResponseEntity("Riferimento evento e tipologia creato", HttpStatus.CREATED);
                } else
                    return new ResponseEntity("L' evento o la Tipologia non esistono", HttpStatus.BAD_REQUEST);
            } else{
                return new ResponseEntity("Errore: Il riferimento evento tipologia esiste già", HttpStatus.BAD_REQUEST);
            }
    }

    /*
            Il metodo deleteTipologiaEvento serve a cancellare la tipologia di un evento
            Dato in input il codice dell'evento e l'id della tipologia,
            si controlla l'esistenza sul database di un record con gli stessi e, in caso positivo, elimina il riferimento.
    */

    public ResponseEntity<?> deleteTipologiaevento(EventoTipologiaRequest evr){

        Optional<EventoTipologia> et = eventoTipologiaRepository.findByTipologiaIdTipologiaAndEventoCodiceEvento(evr.getIdTipologia(), evr.getCodiceEvento());
        if(!et.isEmpty()) {
            eventoTipologiaRepository.delete(et.get());
            return new ResponseEntity("Riferimento evento e tipologia eliminato", HttpStatus.OK);
        } else
            return new ResponseEntity("Errore: Impossibile eliminare, Riferimento evento e tipologia non trovato", HttpStatus.NOT_FOUND);
    }

    /*
            Il metodo fromRequestToEntity permette di creare l'entità EventoTipologia prendendo i dati dalla request (codiceEvento e idTipologia)
    */
    public EventoTipologia fromRequestToEntity(EventoTipologiaRequest request){
        int codiceEvento= request.getCodiceEvento();
        int idTipologia= request.getIdTipologia();
        Optional<Evento> e = eventoRepository.findByCodiceEvento(codiceEvento);
        if(e.isEmpty()){
            return null;
        }else{
            Optional<Tipologia> t = tipologiaRepository.findByidTipologia(idTipologia);
            if(t.isEmpty()){
                return null;
            }else{
                Evento evento = e.get();   //Estrapolazione di evento da option
                Tipologia tipologia = t.get();   //Estrapolazione di tipologia da option
                EventoTipologia et = new EventoTipologia(evento,tipologia);
                return et;
            }
        }
    }

}