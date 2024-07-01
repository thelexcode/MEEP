package com.thesensei.MEEP.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeRequest {
    private int n_accompagnatori=0;

    @NotNull
    private int codice_evento;

    private String[] nomiAccompagnatori;

    private String[]  emailAccompagnatori;
}
