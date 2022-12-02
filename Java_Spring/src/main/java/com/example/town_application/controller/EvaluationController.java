package com.example.town_application.controller;

import com.example.town_application.WIP.requests.evaluation.AddEvaluationRequest;
import com.example.town_application.WIP.requests.evaluation.EditEvaluationRequest;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationMadeByUser;
import com.example.town_application.model.dto.evaluationDTOs.EvaluationPublicPersonalDTO;
import com.example.town_application.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {
    EvaluationService evaluationService;
    @Autowired
    public void setEvaluationService(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }
    @PostMapping("/addEvaluation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addEvaluation(
            @Valid @RequestBody AddEvaluationRequest addEvaluationRequest,
                                           HttpServletRequest request) {
        return evaluationService.addEvaluation(addEvaluationRequest, request );
    }

    @GetMapping("/getAllEvaluationsForWorker")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EvaluationPublicPersonalDTO> getAllEvaluationsForWorker(@RequestParam Integer page, @RequestParam Integer personaldataid, HttpServletRequest request) {
        return evaluationService.getAllEvaluationsForWorker(page, personaldataid, request);
    }

    @GetMapping("/getAllEvaluationsMadeByUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<EvaluationMadeByUser> getAllEvaluationsMadeByUser(@RequestParam Integer page,  HttpServletRequest request) {
        return evaluationService.getAllEvaluationsMadeByUser(page, request);
    }

    @PutMapping("/editEvaluation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> editEvaluation(@Valid @RequestBody EditEvaluationRequest editEvaluationRequest, HttpServletRequest request) {
        return evaluationService.editEvaluation(editEvaluationRequest, request );
    }


}
