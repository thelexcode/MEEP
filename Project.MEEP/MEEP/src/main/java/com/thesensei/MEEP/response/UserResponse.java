package com.thesensei.MEEP.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor


public class UserResponse {

    private String matricola;

    private String username;


    private String email;


    private String nome;


    private String cognome;


    private String telefono;


    private LocalDateTime dataNascita;


    private String biografia;



    private String via;


    private String cap;


    private String comune;


    private String provincia;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponse that)) return false;
        return Objects.equals(matricola, that.matricola) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(nome, that.nome) && Objects.equals(cognome, that.cognome) && Objects.equals(telefono, that.telefono) && Objects.equals(dataNascita, that.dataNascita) && Objects.equals(biografia, that.biografia) && Objects.equals(via, that.via) && Objects.equals(cap, that.cap) && Objects.equals(comune, that.comune) && Objects.equals(provincia, that.provincia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricola, username, email, nome, cognome, telefono, dataNascita, biografia, via, cap, comune, provincia);
    }

    public UserResponse(String matricola, String username, String email, String nome, String cognome, String telefono, LocalDateTime dataNascita, String biografia, String via, String cap, String comune, String provincia) {
        this.matricola = matricola;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.telefono = telefono;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.biografia=biografia;
        this.via=via;
        this.cap = cap;
        this.comune = comune;
        this.provincia = provincia;

    }


}
