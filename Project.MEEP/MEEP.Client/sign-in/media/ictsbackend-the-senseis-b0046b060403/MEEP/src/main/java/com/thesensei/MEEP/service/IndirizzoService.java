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


    /*
        Il metodo ModificaIndirizzo permette, una volta passati tutti i dati necessari nella request (matricola utente, via, cap, comune , provincia) ,
        di modificare e salvare i nuovi dati sul db
     */
    public ResponseEntity<?> editIndirizzo(IndirizzoRequest ir, String matricola){
        Optional <User> u = userRepository.findById(matricola);
        if(u.isEmpty())
            return new ResponseEntity("Errore: non è stato possibile trovare l' utente", HttpStatus.NOT_FOUND);
        else{
            try{
                indirizzoRepository.updateIndirizzo(matricola,ir.getVia(), ir.getCap(), ir.getComune(), ir.getProvincia());
                Optional<Indirizzo> i = indirizzoRepository.findByIndirizzoIdUserMatricola(matricola);
                if(i.isEmpty())
                    return new ResponseEntity("Errore impossibile trovare l'indirizzo", HttpStatus.OK);
                else{
                    Indirizzo indirizzo = i.get();
                    return new ResponseEntity(indirizzo, HttpStatus.OK);
                }
            } catch (Exception e){
                return new ResponseEntity("Errore: non è stato possibile modificare l' indirizzo", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
