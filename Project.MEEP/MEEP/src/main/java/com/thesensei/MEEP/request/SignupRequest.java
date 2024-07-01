package com.thesensei.MEEP.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank @Size(min = 8)
    private String newPassword;

    @NotBlank @Size(min = 8)
    private String checkPassword;

    @NotBlank @Size(min = 5, max = 50)
    private String username;

    @NotBlank @Size(min = 1)
    private String biografia="Let's Meet up!";   //biografia di default

}
