package com.example.town_application.service;

import com.example.town_application.WIP.JwtUtils;
import com.example.town_application.WIP.MessageResponse;
import com.example.town_application.WIP.requests.evaluation.AddEvaluationRequest;
import com.example.town_application.model.Evaluation;
import com.example.town_application.model.Motion;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.model.dto.EvaluationPublicPersonalDTO;
import com.example.town_application.model.dto.MotionDetails;
import com.example.town_application.model.dto.PersonalDataWithoutID;
import com.example.town_application.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final MotionStateRepository motionStateRepository;
    private final MotionTypeRepository motionTypeRepository;
    private final ActionTakenInMotionRepository actionTakenInMotionRepository;
    private final ActionTypeRepository actionTypeRepository;
    private final EvaluationRepository evaluationRepository;

    public List<EvaluationPublicPersonalDTO> getAllEvaluationsForWorker(Integer page, Integer personaldataid, HttpServletRequest request) {
        PersonalData worker=personalDataRepository.findById(personaldataid).orElseThrow(() -> new RuntimeException(("Worker Doesnt' exist")));
        if (worker.getUsersByPersonalDataId().stream().filter(per->per.getPermissionLevel().equals("ROLE_ADMIN")).toList().isEmpty()){
            throw new RuntimeException("Person is not a worker");
        }
        return evaluationRepository.findByPersonalDataForEvaluation_PersonalDataId(personaldataid, PageRequest.of(--page, 20)
        )
                .stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, EvaluationPublicPersonalDTO.class))
                .collect(Collectors.toList());
    }


    public ResponseEntity<?> addEvaluation( AddEvaluationRequest addEvaluationRequest, HttpServletRequest request) {
        Users users = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(JwtUtils.parseJwt(request))).orElseThrow(() -> new RuntimeException(("Wrong Login")));
        PersonalData personalData = users.getPersonalDataForUsers();
        Motion motion = motionRepository.findById(addEvaluationRequest.getMotionId()).orElseThrow(() -> new RuntimeException(("Wrong Motion")));
        PersonalData worker=personalDataRepository.findById(addEvaluationRequest.getWorkerId()).orElseThrow(() -> new RuntimeException(("Worker Doesnt' exist")));



        if (motion.getPersonalDataForMotions().getPersonalDataId() != users.getUserId()) {
            throw new RuntimeException("Wrong Motion");
        }
        if (!actionTakenInMotionRepository.existsByPersonalDataForActionTakenInMotion_PersonalDataIdAndMotionForActionInMotions_MotionId(addEvaluationRequest.getWorkerId(),motion.getMotionId())){
            throw new RuntimeException("Error");
        }

        if (evaluationRepository.existsByMotionForEvaluation_MotionIdAndPersonalDataForEvaluation_PersonalDataId(addEvaluationRequest.getMotionId(),addEvaluationRequest.getWorkerId())){
            throw new RuntimeException("Worker already Evaluated");
        }


        if ((motion.getMotionStateByMotionStateId().getState().equals("Zatwierdzony")||motion.getMotionStateByMotionStateId().getState().equals("Odrzucony"))){
            Evaluation evaluation =new Evaluation(addEvaluationRequest.getGrade(),addEvaluationRequest.getDescription(),motion,worker);
            evaluationRepository.save(evaluation);
        }
        else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Motion is not finished"));
        }
        return ResponseEntity.ok(new MessageResponse("Evaluation added successfully!"));
    }


}
