package com.thesensei.MEEP.controller;

import com.thesensei.MEEP.service.TipologiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("tipologia")
public class TipologiaController {

    private final TipologiaService tipologiaService;

    public TipologiaController(TipologiaService tipologiaService) {
        this.tipologiaService = tipologiaService;
    }

    /**
     * getAllTipologie() -> restituisce tutti i record di Tipologia presenti sul db
     * @return response (lista di Tipologia)
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllTipologie(){
        return tipologiaService.getAllTipologie();
    }

}
