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

    //Aggiungi tipologia Evento
    @PostMapping("/add")
    public ResponseEntity<?> addTipologiaEvento(@RequestBody @Valid EventoTipologiaRequest request){
        return eventoTipologiaService.addTipologiaevento(request);
    }

    //Cancella tipologia Evento
    @DeleteMapping("/delete")
    public ResponseEntity<?> DeleteTipologiaEvento(@RequestBody @Valid EventoTipologiaRequest request){
        return eventoTipologiaService.deleteTipologiaevento(request);
    }


}
