package com.example.town_application.controller;


import com.example.town_application.WIP.requests.personalData.AddPersonRequest;
import com.example.town_application.WIP.requests.personalData.UpdatePersonRequest;
import com.example.town_application.model.dto.PersonalDataWithoutID;
import com.example.town_application.model.dto.WorkerData;
import com.example.town_application.model.dto.WorkerPersonalDataWithMotionAction;
import com.example.town_application.service.PersonalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

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
    public List<WorkerPersonalDataWithMotionAction> getAllWorkersForMotion(@RequestParam Integer page, @RequestParam Integer motionId, HttpServletRequest request) {
        return personalDataService.getAllWorkersForMotion(page, motionId, request);
    }


    @GetMapping("/getAllWorkers")
    public List<WorkerData> getAllWorkers(@RequestParam Integer page) {
        return personalDataService.getAllWorkers(page);
    }


}
