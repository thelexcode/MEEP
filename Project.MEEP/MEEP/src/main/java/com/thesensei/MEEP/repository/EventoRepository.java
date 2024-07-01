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

    /**
     * findByCodiceEvento() -> recupera l'evento dal db dato il suo codice evento
     * @param codiceEvento
     * @return Optional<Evento> (vuoto se non trova l'evento)
     */
    Optional<Evento> findByCodiceEvento(int codiceEvento);


    /**
     * getNumeroAccompagnatori() -> dato il codice di un evento, ne resituisce il numero massimo di accompagnatori
     * @param codiceEvento
     * @return int max_accompagnatori (numero max di accompagnatori che gli utenti possono portare all'evento)
     */
    @Query(value = "SELECT e.max_accompagnatori FROM evento e WHERE e.codice_evento=:codice_evento",nativeQuery = true)
    int getNumeroAccompagnatori(@Param("codice_evento") int codiceEvento);


    /**
     * getNumeroPartecipanti() -> dato il codice di un evento, ne resituisce il numero di partecipanti attuale
     * @param codiceEvento
     * @return int n_partecipanti
     */
    @Query(value = "SELECT e.n_partecipanti FROM evento_partecipazioni e WHERE e.codice_evento=:codice_evento",nativeQuery = true)
    int getNumeroPartecipanti(@Param("codice_evento") int codiceEvento);


    /**
     * updateThumbnail() -> dato il codice dell'evento, permette di modificare la foto della copertina (nome del file) salvato sul db
     * @param codiceEvento
     * @param thumbnail (nome del file da salvare sul db)
     */
    @Modifying
    @Transactional
    @Query(value="UPDATE evento SET thumbnail= :foto WHERE codice_evento= :codice_evento", nativeQuery=true)
    void updateThumbnail(@Param("codice_evento") int codiceEvento, @Param("foto") String thumbnail);


    /**
     * getEventiDisponibiliNoAdmin() -> restituisce una lista di eventi disponibili (eventi non scaduti e di cui il numero di partecipanti non ha superato il massimo)
     *                                  non includendo gli eventi di cui l'utente è amministratore
     * @param matricola
     * @param dataOggi
     * @param oraAttuale
     * @return List<Evento>
     */
    @Query(value = "SELECT * FROM evento_partecipazioni e " +
            "WHERE e.utente_admin!=:matricola AND " +
            "e.n_partecipanti<e.max_partecipanti AND " +
            "(e.data > :data_oggi OR (e.data= :data_oggi AND (e.ora_inizio > :ora_attuale OR e.ora_inizio LIKE ':ora_attuale%' ))) " +
            "ORDER BY e.data, e.ora_inizio, e.ora_fine ", nativeQuery = true)
    List<Evento> getEventiDisponibiliNoAdmin(@Param("matricola")String matricola, @Param("data_oggi")LocalDate dataOggi, @Param("ora_attuale")LocalTime oraAttuale);


    /**
     * getEventiDisponibili() -> restituisce una lista di eventi disponibili (eventi non scaduti e di cui il numero di partecipanti non ha superato il massimo),
     *                           gli eventi di cui l'utente è amministratore sono inclusi.
     * @param dataOggi
     * @param oraAttuale
     * @return List<Evento>
     */
    @Query(value = "SELECT * FROM evento_partecipazioni e " +
            "WHERE e.n_partecipanti<e.max_partecipanti  AND " +
            "(e.data > :data_oggi OR (e.data= :data_oggi AND (e.ora_inizio > :ora_attuale OR e.ora_inizio LIKE ':ora_attuale%' ))) " +
            "ORDER BY e.data, e.ora_inizio, e.ora_fine ", nativeQuery = true)
    List<Evento> getEventiDisponibili(@Param("data_oggi")LocalDate dataOggi, @Param("ora_attuale")LocalTime oraAttuale);


    /**
     * getEventiPerData() -> restituisce una lista con tutti gli eventi di una specifica data
     * @param dataEvento
     * @return List<Evento>
     */
    @Query(value = "SELECT * FROM evento_partecipazioni e " +
            "WHERE e.n_partecipanti<e.max_partecipanti AND " +
            "e.data = :data_evento  " +
            "ORDER BY e.ora_inizio, e.ora_fine ", nativeQuery = true)
    List<Evento> getEventiPerData( @Param("data_evento")LocalDate dataEvento);


    /**
     * findAllByUtenteAdmin() -> restituisce una lista di tutti gli eventi organizzati da un certo utente
     * @param utenteAdmin (oggetto User)
     * @return List<Evento>
     */
    List<Evento> findAllByUtenteAdmin(User utenteAdmin);


    /**
     * getThumbnail() -> dato il codice dell'evento, restituisce il nome del file dell' immagine copertina salvato sul db
     * @param codice_evento
     * @return String nomefile
     */
    @Query("SELECT e.thumbnail " +
            "FROM Evento e " +
            "WHERE e.codiceEvento=:codice_evento")
    String getThumbnail(@Param("codice_evento") int codice_evento);



}
