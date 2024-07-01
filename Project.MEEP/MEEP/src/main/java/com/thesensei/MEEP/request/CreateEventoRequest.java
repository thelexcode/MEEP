package com.thesensei.MEEP.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
public class CreateEventoRequest {

    @NotBlank
    @Size(min = 1, max = 30)
    private String titolo;

    @NotBlank
    @Size(min = 1)
    private String descrizione;

    @NotNull
    private LocalDate data;

    @NotNull
    private LocalTime oraInizio;

    @NotNull
    private LocalTime oraFine;

    @NotNull
    @Min(1)
    @Max(50)
    private int maxPartecipanti;

    @NotNull
    @Min(0)
    @Max(5)
    private int maxAccompagnatori;

}
