package com.example.town_application.WIP.requests.personalData;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
public class ViewDataRequestAdmin {


    @NotBlank
    @Size(min = 11, max = 11)
    private String pesel;


}
