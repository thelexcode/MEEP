package com.thesensei.MEEP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "indirizzo")
public class Indirizzo {

    @EmbeddedId
    private IndirizzoId indirizzoId;

    @Column(name = "via", length = 50, nullable = false)
    private String via;

    @Column(name = "cap", length = 5, nullable = false)
    private String cap;

    @Column(name = "comune", length = 50, nullable = false)
    private String comune;

    @Column(name = "provincia", length = 2, nullable = false)
    private String provincia;

    @Override
    public String toString() {
        return via + ", "+comune+" "+provincia+", "+cap;
    }
}