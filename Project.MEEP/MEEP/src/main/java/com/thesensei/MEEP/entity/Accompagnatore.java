package com.thesensei.MEEP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accompagnatore")
@Getter @Setter @NoArgsConstructor
public class Accompagnatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nominativo", nullable = false, length = 100)
    private String nominativo;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    public Accompagnatore(String nominativo, String email) {
        this.nominativo = nominativo;
        this.email = email;
    }
}
