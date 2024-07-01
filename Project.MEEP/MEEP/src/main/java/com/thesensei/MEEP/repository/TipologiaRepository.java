package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.EventoTipologia;
import com.thesensei.MEEP.entity.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TipologiaRepository extends JpaRepository<Tipologia, Integer> {

    /**
     * findByidTipologia() -> dato l'id, restituisce la tipologia
     * @param idtipologia
     * @return Optional<Tipologia> (vuoto se non trova l'indirizzo)
     */
    Optional<Tipologia> findByidTipologia(int idtipologia);

}
