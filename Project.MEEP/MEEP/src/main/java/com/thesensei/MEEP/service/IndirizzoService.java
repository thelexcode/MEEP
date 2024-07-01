package com.thesensei.MEEP.service;

import com.thesensei.MEEP.entity.Indirizzo;
import com.thesensei.MEEP.entity.User;
import com.thesensei.MEEP.repository.IndirizzoRepository;
import com.thesensei.MEEP.repository.UserRepository;
import com.thesensei.MEEP.request.IndirizzoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IndirizzoService {

    private final UserRepository userRepository;
    private final IndirizzoRepository indirizzoRepository;


    public IndirizzoService(UserRepository userRepository, IndirizzoRepository indirizzoRepository) {
        this.indirizzoRepository = indirizzoRepository;
        this.userRepository = userRepository;
    }

    /**
     * editIndirizzo() -> passati tutti i dati del nuovo indirizzo e la matricola dell'utente, permette di modificare i dati dell'indirizzo salvati sul db
     * @param request ( via, comune, provincia, cap)
     * @param matricola
     * @return Indirizzo
     */
    public ResponseEntity<?> editIndirizzo(IndirizzoRequest request, String matricola) {
        if (!userRepository.existsById(matricola))
            return new ResponseEntity("Errore: non è stato possibile trovare l' utente", HttpStatus.NOT_FOUND);
        else {
            Optional<Indirizzo> i = indirizzoRepository.findByIndirizzoIdUserMatricola(matricola);
            if (i.isEmpty())
                return new ResponseEntity("Errore: non è stato possibile trovare l' indirizzo", HttpStatus.NOT_FOUND);
            else {
                Indirizzo indirizzo = i.get();
                indirizzo.setVia(request.getVia());
                indirizzo.setComune(request.getComune());
                indirizzo.setProvincia(request.getProvincia());
                indirizzo.setCap(request.getCap());
                try {
                    indirizzoRepository.save(indirizzo);
                    return new ResponseEntity(indirizzo, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity("Errore: non è stato possibile modificare l' indirizzo", HttpStatus.BAD_REQUEST);
                }

            }
        }
    }
}