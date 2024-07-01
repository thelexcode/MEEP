package com.thesensei.MEEP.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @NoArgsConstructor
public class FilterRequest {

    private int[] idTipologia={};

    private LocalDate dataEvento=null;

    private LocalTime oraEvento=null;

    private int maxPartecipanti=-1;

    private boolean includeAdmin=false;
}
