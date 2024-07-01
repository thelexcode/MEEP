package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.Evento;
import com.thesensei.MEEP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    Optional<Evento> findByCodiceEvento(int codiceEvento);

    @Query(value = "SELECT e.max_accompagnatori FROM evento e WHERE e.codice_evento=:codice_evento",nativeQuery = true)
    int getNumeroAccompagnatori(@Param("codice_evento") int codiceEvento);

    @Query(value = "SELECT e.n_partecipanti FROM evento_partecipazioni e WHERE e.codice_evento=:codice_evento",nativeQuery = true)
    int getNumeroPartecipanti(@Param("codice_evento") int codiceEvento);

    @Modifying
    @Transactional
    @Query(value="UPDATE evento SET thumbnail= :foto WHERE codice_evento= :codice_evento", nativeQuery=true)
    void updateThumbnail(@Param("codice_evento") int codiceEvento, @Param("foto") String thumbnail);

    @Query(value = "SELECT * FROM evento_partecipazioni e " +
            "WHERE e.utente_admin!=:matricola AND " +
            "e.n_partecipanti<e.max_partecipanti AND " +
            "(e.data > :data_oggi OR (e.data= :data_oggi AND (e.ora_inizio > :ora_attuale OR e.ora_inizio LIKE ':ora_attuale%' ))) " +
            "ORDER BY e.data, e.ora_inizio, e.ora_fine ", nativeQuery = true)
    List<Evento> getEventiDisponibiliNoAdmin(@Param("matricola")String matricola, @Param("data_oggi")LocalDate dataOggi, @Param("ora_attuale")LocalTime oraAttuale);

    @Query(value = "SELECT * FROM evento_partecipazioni e " +
            "WHERE e.n_partecipanti<e.max_partecipanti  AND " +
            "(e.data > :data_oggi OR (e.data= :data_oggi AND (e.ora_inizio > :ora_attuale OR e.ora_inizio LIKE ':ora_attuale%' ))) " +
            "ORDER BY e.data, e.ora_inizio, e.ora_fine ", nativeQuery = true)
    List<Evento> getEventiDisponibili(@Param("data_oggi")LocalDate dataOggi, @Param("ora_attuale")LocalTime oraAttuale);


    @Query(value = "SELECT * FROM evento_partecipazioni e " +
            "WHERE e.n_partecipanti<e.max_partecipanti AND " +
            "e.data = :data_evento  " +
            "ORDER BY e.ora_inizio, e.ora_fine ", nativeQuery = true)
    List<Evento> getEventiPerData( @Param("data_evento")LocalDate dataEvento);

    List<Evento> findAllByUtenteAdmin(User utenteAdmin);

    @Query("SELECT e.thumbnail " +
            "FROM Evento e " +
            "WHERE e.codiceEvento=:codice_evento")
    String getThumbnail(@Param("codice_evento") int codice_evento);

}
