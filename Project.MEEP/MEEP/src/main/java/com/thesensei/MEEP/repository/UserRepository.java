package com.thesensei.MEEP.repository;

import com.thesensei.MEEP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    /**
     * findByMatricolaAndPassword() -> date le credenziali (matricola e password), restituisce l'utente
     * @param matricola
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByMatricolaAndPassword(String matricola, String password);

    /**
     * existByusername -> dato un username, restituisce true se già esiste sul db
     * @param username
     * @return boolean
     */
    boolean existsByusername(String username);

    /**
     * updateFotoProfilo() -> data la matricola, permette di modificare la foto profilo (nome del file) salvato sul db
     * @param matricola
     * @param fotoProfilo
     */
    @Modifying
    @Transactional
    @Query(value="UPDATE user SET foto_profilo= :fotoProfilo WHERE matricola= :matricola", nativeQuery=true)
    void updateFotoProfilo(@Param("matricola") String matricola,@Param("fotoProfilo") String fotoProfilo);

    /**
     * getFotoProfilo() -> data la matricola, restituisce il nome del file della foto profilo salvato sul db
     * @param matricola
     * @return
     */
    @Query("SELECT u.fotoProfilo " +
            "FROM User u " +
            "WHERE u.matricola=:matricola")
    String getFotoProfilo(@Param("matricola") String matricola);
}
