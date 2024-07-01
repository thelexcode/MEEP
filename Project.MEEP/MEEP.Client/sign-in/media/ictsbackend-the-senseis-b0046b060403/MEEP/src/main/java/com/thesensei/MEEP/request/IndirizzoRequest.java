package com.thesensei.MEEP.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class IndirizzoRequest {


    @NotBlank
    @Size(min = 5, max = 50)
    private String via;

    @NotBlank
    @Size(min=5, max = 5)
    private String cap;

    @NotBlank
    @Size(min = 5, max = 50)
    private String comune;

    @NotBlank
    @Size(min = 2, max = 2)
    private String provincia;

}
