package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.EventoTipologia;
import com.thesensei.MEEP.entity.Tipologia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EventoTipologiaRepository extends JpaRepository<EventoTipologia, Integer> {

    /**
     * findByTipologiaIdTipologiaAndEventoCodiceEvento() -> dato l'id della Tipologia e il codice dell'evento, recupera il record della relazione (Evento-Tipologia) salvato sul db
     * @param idTipologia
     * @param codiceEvento
     * @return Optional<EventoTipologia>
     */
    Optional<EventoTipologia> findByTipologiaIdTipologiaAndEventoCodiceEvento(int idTipologia, int codiceEvento);

    /**
     * existsByTipologiaIdTipologiaAndEventoCodiceEvento() -> dato l'id della Tipologia e il codice dell'evento, restituisce true se esiste sul db
     * @param idTipologia
     * @param codiceEvento
     * @return boolean
     */
    boolean existsByTipologiaIdTipologiaAndEventoCodiceEvento(int idTipologia, int codiceEvento);

}
