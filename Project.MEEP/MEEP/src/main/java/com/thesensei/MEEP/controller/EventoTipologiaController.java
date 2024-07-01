package com.thesensei.MEEP.controller;


import com.thesensei.MEEP.request.EventoTipologiaRequest;
import com.thesensei.MEEP.service.EventoTipologiaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("eventotipologia")
@CrossOrigin
public class EventoTipologiaController {
    private final EventoTipologiaService eventoTipologiaService;

    public EventoTipologiaController(EventoTipologiaService eventoTipologiaService) {
        this.eventoTipologiaService = eventoTipologiaService;
    }

    /**
     * addTipologiaEvento() -> dato l'id della tipologia e il codice evento, aggiunge il record della relazione (Evento-Tipologia) sul db
     * @param request (idTipologia, codiceEvento)
     * @return response
     */
    @PostMapping("/add")
    public ResponseEntity<?> addTipologiaEvento(@RequestBody @Valid EventoTipologiaRequest request){
        return eventoTipologiaService.addTipologiaevento(request);
    }


    /**
     * deleteTipologiaEvento() -> dato l'id della tipologia e il codice evento, rimuove il record della relazione (Evento-Tipologia) dal db
     * @param request (idTipologia, codiceEvento)
     * @return response
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTipologiaEvento(@RequestBody @Valid EventoTipologiaRequest request){
        return eventoTipologiaService.deleteTipologiaevento(request);
    }


}
