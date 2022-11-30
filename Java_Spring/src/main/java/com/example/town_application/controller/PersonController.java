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
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
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
    PersonalDataRepository personalDataRepository;
    JwtUtils jwtUtils;
    UsersRepository userRepository;



    @Autowired
    public void setPersonalDataRepository(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    @Autowired
    public void setUsersRepository(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }



    @GetMapping("/getalldata")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PersonalDataWithoutID> getAll(@RequestParam Integer page) {
        ModelMapper modelMapper=new ModelMapper();
        return personalDataRepository.findAll(PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, PersonalDataWithoutID.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/getdatapesel")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonalDataWithoutID getPersonalDataPesel(@RequestParam String pesel) {
        PersonalData personalData=personalDataRepository.findByPesel(pesel).orElseThrow(() -> new UsernameNotFoundException("Wrong pesel"));
        ModelMapper modelMapper=new ModelMapper();
        PersonalDataWithoutID personalDataWithoutID=modelMapper.map(personalData, PersonalDataWithoutID.class);
        return personalDataWithoutID;
    }

    @GetMapping("/getdatatoken")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PersonalDataWithoutID getPersonalDataToken(HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(parseJwt(request))).orElseThrow(() -> new UsernameNotFoundException("Wrong Login"));
        PersonalData personalData=users.getPersonalDataForUsers();
        ModelMapper modelMapper=new ModelMapper();
        PersonalDataWithoutID personalDataWithoutID=modelMapper.map(personalData, PersonalDataWithoutID.class);
        return personalDataWithoutID;
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


    @PutMapping("/update")
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

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
