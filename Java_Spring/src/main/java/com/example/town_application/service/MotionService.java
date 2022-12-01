package com.example.town_application.service;


import com.example.town_application.WIP.JwtUtils;
import com.example.town_application.WIP.MessageResponse;
import com.example.town_application.WIP.requests.motion.AddMotionRequest;
import com.example.town_application.WIP.requests.motion.UpdateMotionStateRequest;
import com.example.town_application.model.*;
import com.example.town_application.model.dto.MotionDetails;

import com.example.town_application.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotionService {

    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final MotionStateRepository motionStateRepository;
    private final MotionTypeRepository motionTypeRepository;
    private final ActionTakenInMotionRepository actionTakenInMotionRepository;
    private final ActionTypeRepository actionTypeRepository;

    public List<MotionDetails> getAll(Integer page) {
        return motionRepository.findAll(PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, MotionDetails.class))
                .collect(Collectors.toList());
    }


    public List<MotionDetails> getAllForUser(Integer page, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException(("Wrong Login")));


        PersonalData userPersonalData = users.getPersonalDataForUsers();

        return motionRepository.findAllByPersonalDataForMotions(userPersonalData, PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, MotionDetails.class))
                .collect(Collectors.toList());
    }


    public List<MotionDetails> getAllFinishedForUser(Integer page, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));
        MotionState motionState1 = motionStateRepository.findByState("Zatwierdzony").
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        MotionState motionState2 = motionStateRepository.findByState("Odrzucony").
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        PersonalData userPersonalData = users.getPersonalDataForUsers();

        return motionRepository.findAllByPersonalDataForMotionsAndMotionStateByMotionStateIdOrMotionStateByMotionStateId(userPersonalData,
                        motionState1, motionState2, PageRequest.of(--page, 20)
                )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, MotionDetails.class))
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> addMotion(AddMotionRequest addMotionRequest, HttpServletRequest request) {
        if (!personalDataRepository.existsByPesel(addMotionRequest.getPesel())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Person with this pesel doesn't exist!"));
        }
        PersonalData userPersonalData = personalDataRepository.findByPesel(addMotionRequest.getPesel()).
                orElseThrow(() -> new RuntimeException("Wrong Pesel"));

        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();

        MotionState motionState = motionStateRepository.findByState("Przyjęty").
                orElseThrow(() -> new RuntimeException("Motion State Error"));

        ActionType actionType = actionTypeRepository.findByType("Przyjęcie wniosku").
                orElseThrow(() -> new RuntimeException("Action Type Error"));

        MotionType motionType = motionTypeRepository.findById(addMotionRequest.getMotiontype()).
                orElseThrow(() -> new RuntimeException("Motion Type Error"));
        Motion motion = new Motion(userPersonalData, motionType, motionState);
        ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
        motionRepository.save(motion);
        actionTakenInMotionRepository.save(actionTakenInMotion);
        return ResponseEntity.ok(new MessageResponse("Motion added successfully!"));
    }

    public ResponseEntity<?> updatemotionstatus(@RequestBody UpdateMotionStateRequest updateMotionStateRequest, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();
        MotionState motionState = motionStateRepository.findById(updateMotionStateRequest.getNewmotionstate()).
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        Motion motion = motionRepository.findById(updateMotionStateRequest.getMotionid()).
                orElseThrow(() -> new RuntimeException("Motion Error"));

        MotionState beforestate = motion.getMotionStateByMotionStateId();
        if (beforestate.getState().equals("Zatwierdzony") || beforestate.getState().equals("Odrzucony")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Motion procedings finished!"));
        }
        if (beforestate.getMotionStateId() == updateMotionStateRequest.getNewmotionstate()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Motion State already set at demanded value!"));
        }


        motion.setMotionStateByMotionStateId(motionState);
        motionRepository.save(motion);


        if (motionState.getState().equals("Zatwierdzony")) {
            ActionType actionType = actionTypeRepository.findByType("Zatwierdzenie wniosku").
                    orElseThrow(() -> new UsernameNotFoundException("Action Type Error"));
            ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
            actionTakenInMotionRepository.save(actionTakenInMotion);
            return ResponseEntity.ok(new MessageResponse("Motion updated successfully!"));

        } else if (motionState.getState().equals("Odrzucony")) {
            ActionType actionType = actionTypeRepository.findByType("Odrzucenie wniosku").
                    orElseThrow(() -> new RuntimeException("Action Type Error"));
            ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
            actionTakenInMotionRepository.save(actionTakenInMotion);
            return ResponseEntity.ok(new MessageResponse("Motion updated successfully!"));
        } else {
            ActionType actionType = actionTypeRepository.findByType("Przetwarzanie wniosku").
                    orElseThrow(() -> new RuntimeException("Action Type Error"));
            if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotionAndMotionForActionInMotionsAndActionTypeByActionTypeId(
                    workerPersonalData, motion, actionType
            )) {
                ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
                actionTakenInMotionRepository.save(actionTakenInMotion);
                return ResponseEntity.ok(new MessageResponse("Motion updated successfully!"));

            }

        }
        return ResponseEntity.ok(new MessageResponse("Motion updated successfully!"));
    }


}
