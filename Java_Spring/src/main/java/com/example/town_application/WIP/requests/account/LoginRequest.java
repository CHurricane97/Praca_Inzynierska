package com.example.town_application.WIP.requests.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
