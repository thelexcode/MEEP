package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.Partecipazione;
import jakarta.mail.Part;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartecipazioneRepository extends JpaRepository<Partecipazione, String> {

    /**
     * saveToDatabase() -> crea un nuovo record "Partecipazione" salvando sul db il Qrcode,
     *                     il codice dell'evento, il codice del dipendente e l'eventuale id dell'accompagnatore
     * @param id_accompagnatore  (può essere null in caso il partecipante non è un accompagnatore)
     * @param codice_evento
     * @param codice_utente
     * @param codice_qr
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO partecipazione (id_accompagnatore, codice_evento, codice_utente, codice_qr) " +
            "VALUES (:id_accompagnatore, :codice_evento, :codice_utente, :codice_qr)",
            nativeQuery = true)
    void saveToDatabase(
            @Param("id_accompagnatore") Integer id_accompagnatore,
            @Param("codice_evento") int codice_evento,
            @Param("codice_utente") String codice_utente,
            @Param("codice_qr") String codice_qr);

    /**
     * getPartecipazioneDipendente() -> dato il codice dell'evento e la matricola, seleziona dal db il record relativo alla partecipazione del dipendente
     *                                  (non seleziona i record di partecipazione degli accompagnatori)
     * @param matricola
     * @return Optional<Partecipazione>
     */
    @Query(value = "SELECT * FROM partecipazione p WHERE p.codice_evento=:codice_evento AND p.codice_utente = :matricola AND id_accompagnatore IS NULL", nativeQuery = true)
    Optional<Partecipazione> getPartecipazioneDipendente(@Param("codice_evento") int codiceEvento, @Param("matricola")String matricola);


}
