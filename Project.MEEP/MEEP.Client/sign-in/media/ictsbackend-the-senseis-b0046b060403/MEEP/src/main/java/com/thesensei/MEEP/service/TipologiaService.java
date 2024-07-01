package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Tipologia;
import com.thesensei.MEEP.repository.TipologiaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipologiaService {

    private final TipologiaRepository tipologiaRepository;

    public TipologiaService(TipologiaRepository tipologiaRepository) {
        this.tipologiaRepository = tipologiaRepository;
    }

    public ResponseEntity<?> getAllTipologie(){
        List<Tipologia> tipologie = tipologiaRepository.findAll();
        if(tipologie.isEmpty())
            return new ResponseEntity("Errore: non sono state trovate tipologie", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity(tipologie,HttpStatus.OK);
    }
}
