package com.thesensei.MEEP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
@CrossOrigin
public class User {

    @Id
    @Column(length = 20)
    private String matricola;

    @Column(name = "username",length = 50)
    private String username;

    @Column(name = "email",length = 50, nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "nome",length = 50,nullable = false)
    private String nome;

    @Column(name = "cognome",length = 50,nullable = false)
    private String cognome;

    @Column(name = "telefono",length = 15,nullable = false)
    private String telefono;

    @Column(name = "data_nascita",nullable = false)
    private LocalDateTime dataNascita;

    @Column(name = "foto_profilo",length = 100,nullable = false)
    private String fotoProfilo ="defaultfoto.jpg";

    @Column(name = "biografia",nullable = false)
    private String biografia;

}
