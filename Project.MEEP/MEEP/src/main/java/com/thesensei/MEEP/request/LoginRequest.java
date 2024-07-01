package com.thesensei.MEEP.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
@Getter
public class LoginRequest {

    @NotBlank @Size(min = 5, max = 20)
    private String matricola;

    @NotBlank @Size(min = 10)
    private String password;

}
