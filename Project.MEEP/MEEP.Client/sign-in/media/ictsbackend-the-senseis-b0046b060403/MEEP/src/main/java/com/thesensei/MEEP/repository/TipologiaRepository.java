package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.EventoTipologia;
import com.thesensei.MEEP.entity.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TipologiaRepository extends JpaRepository<Tipologia, Integer> {
    Optional<Tipologia> findByidTipologia(int idtipologia);

    @Query("SELECT t.denominazione FROM Tipologia t")
    List<String> getAllDenominazioneTipologia();

}
