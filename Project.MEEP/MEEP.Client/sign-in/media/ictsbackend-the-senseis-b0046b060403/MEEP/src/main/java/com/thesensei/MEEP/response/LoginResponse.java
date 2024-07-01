package com.thesensei.MEEP.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginResponse {

    private String matricola;
    private String token;
    private boolean isFirstLogin = false;

    public LoginResponse(String matricola, String token) {
        this.matricola = matricola;
        this.token = token;
    }
}
