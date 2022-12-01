package com.example.town_application.service;

import com.example.town_application.WIP.JwtUtils;
import com.example.town_application.WIP.MessageResponse;
import com.example.town_application.WIP.requests.personalData.AddPersonRequest;
import com.example.town_application.WIP.requests.personalData.UpdatePersonRequest;
import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.model.dto.PersonalDataWithoutID;
import com.example.town_application.model.dto.WorkerPersonalData;
import com.example.town_application.repository.MotionRepository;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PersonalDataService {
    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;


    public List<PersonalDataWithoutID> getAll(Integer page) {
        return personalDataRepository.findAll(PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, PersonalDataWithoutID.class))
                .collect(Collectors.toList());
    }


    public PersonalDataWithoutID getPersonalDataPesel(String pesel) {
        PersonalData personalData = personalDataRepository.findByPesel(pesel).orElseThrow(() -> new RuntimeException("Wrong pesel"));
        return modelMapper.map(personalData, PersonalDataWithoutID.class);
    }

    public PersonalDataWithoutID getPersonalDataToken(HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Wrong Login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        return modelMapper.map(personalData, PersonalDataWithoutID.class);
    }

    public ResponseEntity<?> registerUser(AddPersonRequest addPersonRequest) {

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

    public ResponseEntity<?> updateUser(UpdatePersonRequest updatePersonRequest) {
        if (!personalDataRepository.existsByPesel(updatePersonRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Person with this pesel not exist!"));
        }

        PersonalData personalData = personalDataRepository.findByPesel(updatePersonRequest.getPesel())
                .orElseThrow(() -> new RuntimeException(("Person Not Found with pesel: " + updatePersonRequest.getPesel())));
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

    public List<WorkerPersonalData> getAllWorkersForMotion(Integer page, Integer motionId, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Wrong Login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        Motion motion = motionRepository.findById(motionId).orElseThrow(() -> new RuntimeException(("Wrong Motion")));

        if (motion.getPersonalDataForMotions().getPersonalDataId() != users.getUserId()) {
            throw new RuntimeException("Wrong Motion");
        }

        List<WorkerPersonalData> workerPersonalDataList = personalDataRepository
                .findDistinctByActionTakenInMotionsByPersonalDataId_MotionForActionInMotions_MotionId(motionId, PageRequest.of(--page, 20))
                .stream()
                .map(person -> modelMapper.map(person, WorkerPersonalData.class))
                .toList();

        workerPersonalDataList.forEach(per ->
            per.setActionTakenInMotionsByPersonalDataId(per
                    .getActionTakenInMotionsByPersonalDataId()
                    .stream().filter(action -> action.getMotionForActionInMotions().getMotionId() == motionId)
                    .toList()
            )
        );

        return workerPersonalDataList;
    }

}
