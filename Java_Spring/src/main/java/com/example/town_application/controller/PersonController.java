package com.example.town_application.controller;


import com.example.town_application.WIP.MessageResponse;
import com.example.town_application.WIP.requests.account.SignupRequest;
import com.example.town_application.WIP.requests.personalData.AddPersonRequest;
import com.example.town_application.WIP.requests.personalData.UpdatePersonRequest;
import com.example.town_application.model.PersonalData;
import com.example.town_application.repository.PersonalDataRepository;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/person")
public class PersonController {
    PersonalDataRepository personalDataRepository;

    @Autowired
    public void setPersonalDataRepository(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AddPersonRequest addPersonRequest) {
        if (personalDataRepository.existsByPesel(addPersonRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Person with this pesel already exist!"));
        }
        if (!GenericValidator.isDate(addPersonRequest.getDate_of_birth(), "yyyy-MM-dd", true)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid Date of birth!"));
        }
        LocalDate dateOfBirth = LocalDate.parse(addPersonRequest.getDate_of_birth());
        PersonalData personalData = new PersonalData(
                addPersonRequest.getName(),
                addPersonRequest.getSurname(),
                addPersonRequest.getPesel(),
                java.sql.Date.valueOf(dateOfBirth),
                addPersonRequest.getCity(),
                addPersonRequest.getCity_code(),
                addPersonRequest.getStreet(),
                addPersonRequest.getHouse_number(),
                addPersonRequest.getFlat_number());
        personalDataRepository.save(personalData);

        return ResponseEntity.ok(new MessageResponse("Person added successfully!"));
    }


    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UpdatePersonRequest updatePersonRequest) {
        if (!personalDataRepository.existsByPesel(updatePersonRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Person with this pesel not exist!"));
        }

        PersonalData personalData = personalDataRepository.findByPesel(updatePersonRequest.getPesel())
                .orElseThrow(() -> new UsernameNotFoundException("Person Not Found with pesel: " + updatePersonRequest.getPesel()));
        personalData.setName(updatePersonRequest.getName());
        personalData.setSurname(updatePersonRequest.getSurname());
        personalData.setCity(updatePersonRequest.getCity());
        personalData.setCityCode(updatePersonRequest.getCity_code());
        personalData.setSurname(updatePersonRequest.getStreet());
        personalData.setHouseNumber(updatePersonRequest.getHouse_number());
        personalData.setFlatNumber(updatePersonRequest.getFlat_number());
        personalDataRepository.save(personalData);

        return ResponseEntity.ok(new MessageResponse("Person Updated successfully!"));
    }

    @GetMapping("/getdata")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserData(@Valid @RequestBody UpdatePersonRequest updatePersonRequest) {
        if (!personalDataRepository.existsByPesel(updatePersonRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Person with this pesel not exist!"));
        }

        PersonalData personalData = personalDataRepository.findByPesel(updatePersonRequest.getPesel())
                .orElseThrow(() -> new UsernameNotFoundException("Person Not Found with pesel: " + updatePersonRequest.getPesel()));
        personalData.setName(updatePersonRequest.getName());
        personalData.setSurname(updatePersonRequest.getSurname());
        personalData.setCity(updatePersonRequest.getCity());
        personalData.setCityCode(updatePersonRequest.getCity_code());
        personalData.setSurname(updatePersonRequest.getStreet());
        personalData.setHouseNumber(updatePersonRequest.getHouse_number());
        personalData.setFlatNumber(updatePersonRequest.getFlat_number());
        personalDataRepository.save(personalData);

        return ResponseEntity.ok(new MessageResponse("Person Updated successfully!"));
    }

}
