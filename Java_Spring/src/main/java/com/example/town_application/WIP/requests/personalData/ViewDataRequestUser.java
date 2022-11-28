package com.example.town_application.WIP.requests.personalData;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ViewDataRequestUser {

    @NotBlank
    private String token;


}
