package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.Partecipazione;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartecipazioneRepository extends JpaRepository<Partecipazione, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO partecipazione (is_accompagnatore, codice_evento, codice_utente, codice_qr) " +
            "VALUES (:is_accompagnatore, :codice_evento, :codice_utente, :codice_qr)",
            nativeQuery = true)
    void saveToDatabase(
            @Param("is_accompagnatore") boolean is_accompagnatore,
            @Param("codice_evento") int codice_evento,
            @Param("codice_utente") String codice_utente,
            @Param("codice_qr") String codice_qr);
}
