package com.example.town_application.service;

import com.example.town_application.WIP.JwtUtils;
import com.example.town_application.WIP.MessageResponse;
import com.example.town_application.WIP.requests.actionTakenInMotion.ActionTakenInMotionBaseRequest;
import com.example.town_application.model.*;
import com.example.town_application.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ActionTakenInMotionService {

    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final MotionStateRepository motionStateRepository;
    private final MotionTypeRepository motionTypeRepository;
    private final ActionTakenInMotionRepository actionTakenInMotionRepository;
    private final ActionTypeRepository actionTypeRepository;





    public ResponseEntity<?> approveMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();
        MotionState motionState = motionStateRepository.findByState("Zatwierdzony").
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        Motion motion = motionRepository.findById(actionTakenInMotionBaseRequest.getMotionID()).
                orElseThrow(() -> new RuntimeException("Motion Error"));
        MotionState beforestate = motion.getMotionStateByMotionStateId();
        if (beforestate.getState().equals("Zatwierdzony") || beforestate.getState().equals("Odrzucony")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Motion procedings finished!"));
        }



        motion.setMotionStateByMotionStateId(motionState);
        motionRepository.save(motion);

        ActionType actionType = actionTypeRepository.findByType("Zatwierdzenie wniosku").
                orElseThrow(() -> new RuntimeException("Action Type Error"));
        ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
        actionTakenInMotionRepository.save(actionTakenInMotion);
        return ResponseEntity.ok(new MessageResponse("Motion approved successfully!"));




    }


    public ResponseEntity<?> denyMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();
        MotionState motionState = motionStateRepository.findByState("Odrzucony").
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        Motion motion = motionRepository.findById(actionTakenInMotionBaseRequest.getMotionID()).
                orElseThrow(() -> new RuntimeException("Motion Error"));
        MotionState beforestate = motion.getMotionStateByMotionStateId();
        if (beforestate.getState().equals("Zatwierdzony") || beforestate.getState().equals("Odrzucony")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Motion procedings finished!"));
        }

        motion.setMotionStateByMotionStateId(motionState);
        motionRepository.save(motion);

        ActionType actionType = actionTypeRepository.findByType("Odrzucenie wniosku").
                orElseThrow(() -> new RuntimeException("Action Type Error"));
        ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
        actionTakenInMotionRepository.save(actionTakenInMotion);
        return ResponseEntity.ok(new MessageResponse("Motion denied successfully!"));

    }


    public ResponseEntity<?> consultMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();
        MotionState motionState = motionStateRepository.findByState("Przetwarzany").
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        Motion motion = motionRepository.findById(actionTakenInMotionBaseRequest.getMotionID()).
                orElseThrow(() -> new RuntimeException("Motion Error"));
        MotionState beforestate = motion.getMotionStateByMotionStateId();
        if (beforestate.getState().equals("Zatwierdzony") || beforestate.getState().equals("Odrzucony")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Motion procedings finished!"));
        }
        if (beforestate.getMotionStateId() != motionState.getMotionStateId()) {
            motion.setMotionStateByMotionStateId(motionState);
            motionRepository.save(motion);
        }

        ActionType actionType = actionTypeRepository.findByType("Konsultacje z wnioskodawcą").
                orElseThrow(() -> new RuntimeException("Action Type Error"));
        if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotionAndMotionForActionInMotionsAndActionTypeByActionTypeId(
                workerPersonalData, motion, actionType
        )) {
            ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
            actionTakenInMotionRepository.save(actionTakenInMotion);


        }
        return ResponseEntity.ok(new MessageResponse("Motion proceded successfully!"));
    }


    public ResponseEntity<?> proceedMotion(
            @Valid @RequestBody ActionTakenInMotionBaseRequest actionTakenInMotionBaseRequest,
            HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).
                orElseThrow(() -> new RuntimeException("Wrong Login"));

        PersonalData workerPersonalData = users.getPersonalDataForUsers();
        MotionState motionState = motionStateRepository.findByState("Przetwarzany").
                orElseThrow(() -> new RuntimeException("Motion State Error"));
        Motion motion = motionRepository.findById(actionTakenInMotionBaseRequest.getMotionID()).
                orElseThrow(() -> new RuntimeException("Motion Error"));
        MotionState beforestate = motion.getMotionStateByMotionStateId();
        if (beforestate.getState().equals("Zatwierdzony") || beforestate.getState().equals("Odrzucony")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Motion procedings finished!"));
        }
        if (beforestate.getMotionStateId() != motionState.getMotionStateId()) {
            motion.setMotionStateByMotionStateId(motionState);
            motionRepository.save(motion);
        }

        ActionType actionType = actionTypeRepository.findByType("Przetwarzanie wniosku").
                orElseThrow(() -> new RuntimeException("Action Type Error"));
        if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotionAndMotionForActionInMotionsAndActionTypeByActionTypeId(
                workerPersonalData, motion, actionType
        )) {
            ActionTakenInMotion actionTakenInMotion = new ActionTakenInMotion(motion, workerPersonalData, actionType);
            actionTakenInMotionRepository.save(actionTakenInMotion);

        }
        return ResponseEntity.ok(new MessageResponse("Motion proceded successfully!"));
    }




}
