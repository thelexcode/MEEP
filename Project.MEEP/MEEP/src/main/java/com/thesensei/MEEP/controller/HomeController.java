package com.thesensei.MEEP.controller;


import com.thesensei.MEEP.service.EventoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("home")
@CrossOrigin
public class HomeController {

    private final EventoService eventoService;

    public HomeController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * getEventi() -> restiuisce la lista di tutti gli eventi a cui è possibile partecipare
     * @param httpRequest
     * @return response (lista di Eventi disponibli)
     */
    @GetMapping("/eventi")
    public ResponseEntity<?> getEventi(HttpServletRequest httpRequest){
        return eventoService.getEventiDisponibili(httpRequest.getAttribute("matricola").toString());
    }

}
