package com.thesensei.MEEP.controller;

import com.thesensei.MEEP.request.CreateEventoRequest;
import com.thesensei.MEEP.request.FilterRequest;
import com.thesensei.MEEP.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    //Crea un nuovo evento
    @PostMapping("/create")
    public ResponseEntity<?> createEvento(@RequestBody @Valid CreateEventoRequest request, HttpServletRequest httpRequest){
        return eventoService.createEvento(request, httpRequest.getAttribute("matricola").toString());
    }

    //Cancella evento
    @DeleteMapping("/{codice_evento}")
    public ResponseEntity<?> deleteEvento(@PathVariable int codice_evento, HttpServletRequest httpRequest){
        return eventoService.deleteEvento(codice_evento, httpRequest.getAttribute("matricola").toString());
    }

    //Modifica evento
    @PutMapping("/edit/{codice_evento}")
    public ResponseEntity<?> updateEvento(@RequestBody @Valid CreateEventoRequest request, @PathVariable int codice_evento ,HttpServletRequest httpRequest){
        return eventoService.updateEvento(request, codice_evento, httpRequest.getAttribute("matricola").toString());
    }

    //Modifica copertina evento
    @PostMapping("/{codice_evento}/edit-foto")
    public ResponseEntity<?> editThumbnail(@PathVariable int codice_evento, @RequestParam MultipartFile file, HttpServletRequest httpRequest){
        return eventoService.editThumbnail(codice_evento,file,httpRequest.getAttribute("matricola").toString());
    }

    //Dettagli dell'evento
    @GetMapping("/{codiceEvento}")
    public ResponseEntity<?> getEventoDetails(@PathVariable int codiceEvento){
        return eventoService.getEventoDetails(codiceEvento);
    }

    //Get copertina dell'evento dato il suo codice evento
    @GetMapping("/{codice_evento}/foto")
    public ResponseEntity<?> getCopertinaEvento(@PathVariable int codice_evento) throws IOException {
        return eventoService.getThumbnail(codice_evento);
    }

    //Get eventi filtrati
    @PostMapping("/filter")
    public ResponseEntity<?> getEventiFiltati(@RequestBody FilterRequest request, HttpServletRequest httpRequest){
        return eventoService.getEventiFiltrati(request, httpRequest.getAttribute("matricola").toString());
    }

}
