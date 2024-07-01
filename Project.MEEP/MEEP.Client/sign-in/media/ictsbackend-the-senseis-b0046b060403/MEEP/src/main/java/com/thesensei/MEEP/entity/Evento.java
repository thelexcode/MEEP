package com.thesensei.MEEP.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    @Column(name = "codice_evento")
    private int codiceEvento;

    @ManyToOne
    @JoinColumn(name = "utente_admin", nullable = false)
    private User utenteAdmin;

    @Column(name = "titolo", length = 30, nullable = false)
    private String titolo;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "thumbnail", length = 100)
    private String thumbnail="copertina.jpg";

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "ora_inizio", nullable = false)
    private LocalTime oraInizio;

    @Column(name = "ora_fine", nullable = false)
    private LocalTime oraFine;

    @Column(name = "max_partecipanti", nullable = false)
    private int maxPartecipanti;

    @Column(name = "max_accompagnatori", nullable = false)
    private int maxAccompagnatori;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evento evento)) return false;
        return codiceEvento == evento.codiceEvento && maxPartecipanti == evento.maxPartecipanti && maxAccompagnatori == evento.maxAccompagnatori && Objects.equals(utenteAdmin, evento.utenteAdmin) && Objects.equals(titolo, evento.titolo) && Objects.equals(descrizione, evento.descrizione) && Objects.equals(thumbnail, evento.thumbnail) && Objects.equals(data, evento.data) && Objects.equals(oraInizio, evento.oraInizio) && Objects.equals(oraFine, evento.oraFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceEvento, utenteAdmin, titolo, descrizione, thumbnail, data, oraInizio, oraFine, maxPartecipanti, maxAccompagnatori);
    }

    public Evento(User utenteAdmin, String titolo, String descrizione, LocalDate data, LocalTime oraInizio, LocalTime oraFine, int maxPartecipanti, int maxAccompagnatori) {
        this.utenteAdmin = utenteAdmin;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.maxPartecipanti = maxPartecipanti;
        this.maxAccompagnatori = maxAccompagnatori;
    }
}


