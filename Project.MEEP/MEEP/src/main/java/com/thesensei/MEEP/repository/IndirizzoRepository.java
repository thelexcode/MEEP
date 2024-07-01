package com.thesensei.MEEP.repository;
import com.thesensei.MEEP.entity.Indirizzo;
import com.thesensei.MEEP.entity.IndirizzoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, IndirizzoId> {

    /**
     * findByIndirizzoIdUserMatricola() -> data la matricola dell'utente, restituisce il suo indirizzo
     * @param matricola
     * @return Optional<Indirizzo> (vuoto se non trova l'indirizzo)
     */
    Optional<Indirizzo> findByIndirizzoIdUserMatricola(String matricola);

    /**
     * getIndirizzoByEvento() -> dato il codice dell'evento, restituisce il suo indirizzo
     * @param codiceEvento
     * @return Optional<Indirizzo> (vuoto se non trova l'indirizzo)
     */
    @Query(value = "SELECT i.* FROM indirizzo i " +
            "INNER JOIN user u ON i.matricola=u.matricola " +
            "INNER JOIN evento e ON e.utente_admin=u.matricola " +
            "WHERE e.codice_evento=:codice_evento ", nativeQuery = true)
    Optional<Indirizzo> getIndirizzoByEvento(@Param("codice_evento") int codiceEvento );

}
