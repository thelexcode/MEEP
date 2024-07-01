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

    Optional<User> findByMatricolaAndPassword(String matricola, String password);
    boolean existsByusername(String username);

    @Modifying
    @Transactional
    @Query(value="UPDATE user SET foto_profilo= :fotoProfilo WHERE matricola= :matricola", nativeQuery=true)
    void updateFotoProfilo(@Param("matricola") String matricola,@Param("fotoProfilo") String fotoProfilo);

    @Query("SELECT u.fotoProfilo " +
            "FROM User u " +
            "WHERE u.matricola=:matricola")
    String getFotoProfilo(@Param("matricola") String matricola);
}
