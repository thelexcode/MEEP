package com.thesensei.MEEP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tipologia")
public class Tipologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincrement
    @Column(name = "id_tipologia")
    private int idTipologia;

    @Column(name = "denominazione", length = 50, nullable = false)
    private String denominazione;
}