package com.thesensei.MEEP.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EventoTipologiaRequest {

        @NotNull
        private int idTipologia;

        @NotNull
        private int codiceEvento;

}
