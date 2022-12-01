package com.example.town_application.controller;


import com.example.town_application.WIP.JwtResponse;
import com.example.town_application.WIP.JwtUtils;
import com.example.town_application.WIP.MessageResponse;
import com.example.town_application.WIP.requests.account.SignupRequest;
import com.example.town_application.WIP.requests.personalData.AddPersonRequest;
import com.example.town_application.WIP.requests.personalData.UpdatePersonRequest;
import com.example.town_application.WIP.requests.personalData.ViewDataRequestAdmin;
import com.example.town_application.WIP.requests.personalData.ViewDataRequestUser;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.model.dto.PersonalDataWithoutID;
import com.example.town_application.model.dto.WorkerPersonalData;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import com.example.town_application.service.PersonalDataService;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/person")
public class PersonController {

    PersonalDataService personalDataService;

    @Autowired
    public void setPersonalDataService(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    @GetMapping("/getalldata")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PersonalDataWithoutID> getAll(@RequestParam Integer page) {
        return personalDataService.getAll(page);
    }

    @GetMapping("/getdatapesel")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonalDataWithoutID getPersonalDataPesel(@RequestParam String pesel) {
        return personalDataService.getPersonalDataPesel(pesel);
    }

    @GetMapping("/getdatatoken")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PersonalDataWithoutID getPersonalDataToken(HttpServletRequest request) {
        return personalDataService.getPersonalDataToken(request);
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AddPersonRequest addPersonRequest) {
        return personalDataService.registerUser(addPersonRequest);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdatePersonRequest updatePersonRequest) {
        return personalDataService.updateUser(updatePersonRequest);
    }

    @GetMapping("/getAllWorkersForMotion")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<WorkerPersonalData> getAllWorkersForMotion(@RequestParam Integer page, @RequestParam Integer motionId, HttpServletRequest request) {
        return personalDataService.getAllWorkersForMotion(page, motionId, request);
    }


}
