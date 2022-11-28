package com.example.town_application.WIP.requests.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String login;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 1)
    private String role;

    @NotBlank
    @Size(min = 11, max = 11)
    private String pesel;




//    @NotBlank
//    @Size(min = 1, max = 15)
//    private String name;
//
//    @NotBlank
//    @Size(min = 1, max = 15)
//    private String surname;

}
