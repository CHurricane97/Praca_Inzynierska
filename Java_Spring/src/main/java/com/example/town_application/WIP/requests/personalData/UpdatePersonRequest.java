package com.example.town_application.WIP.requests.personalData;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatePersonRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    @Size(min = 11, max = 11)
    private String pesel;

    @NotBlank
    private String city;

    @NotBlank
    private String city_code;

    @NotBlank
    private String street;

    @NotBlank
    private String house_number;

    @NotBlank
    private String flat_number;
}
