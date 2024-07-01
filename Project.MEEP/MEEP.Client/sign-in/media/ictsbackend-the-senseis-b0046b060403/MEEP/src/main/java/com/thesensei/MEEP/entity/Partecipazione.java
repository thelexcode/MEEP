package com.thesensei.MEEP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "partecipazione")
public class Partecipazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_qr", columnDefinition = "TEXT", length = 2500)
    private String codiceQr;

    @ManyToOne
    @JoinColumn(name = "codice_evento", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "codice_utente", nullable = false)
    private User user;

    @Column(name = "is_accompagnatore", nullable = false)
    private boolean isAccompagnatore;
}
