package com.thesensei.MEEP.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Embeddable
public class IndirizzoId implements Serializable {

    @OneToOne
    @JoinColumn(name="matricola")
    private User user;
}
