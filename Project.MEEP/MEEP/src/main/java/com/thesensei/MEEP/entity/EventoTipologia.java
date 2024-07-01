package com.thesensei.MEEP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "evento_tipologia")
public class EventoTipologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "codice_evento", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "id_tipologia", nullable = false)
    private Tipologia tipologia;

    public EventoTipologia(Evento evento, Tipologia tipologia) {
        this.evento = evento;
        this.tipologia = tipologia;
    }

}
