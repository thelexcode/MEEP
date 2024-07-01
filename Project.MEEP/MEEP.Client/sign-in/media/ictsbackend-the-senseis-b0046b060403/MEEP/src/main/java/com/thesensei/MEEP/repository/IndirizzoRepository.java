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

    Optional<Indirizzo> findByIndirizzoIdUserMatricola(String matricola);

    @Modifying
    @Transactional
    @Query(value="UPDATE indirizzo SET via= :via, cap= :cap, comune= :comune, provincia= :provincia WHERE matricola= :matricola", nativeQuery=true)
    void updateIndirizzo(@Param("matricola") String matricola, @Param("via") String via,@Param("cap") String cap,@Param("comune") String comune, @Param("provincia") String provincia );



}
