package com.example.town_application.WIP.requests.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class ChangePasswordUserReqest {


    @NotBlank
    private String password;

    @NotBlank
    private String newpassword;


}
