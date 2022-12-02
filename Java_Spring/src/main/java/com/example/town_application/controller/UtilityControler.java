package com.example.town_application.controller;

import com.example.town_application.model.MotionState;
import com.example.town_application.model.dto.ActionTypeDTO;
import com.example.town_application.model.dto.EvaluationPublicPersonalDTO;
import com.example.town_application.model.dto.MotionStateDTO;
import com.example.town_application.model.dto.MotionTypeDTO;
import com.example.town_application.service.ActionTypeService;
import com.example.town_application.service.MotionStateService;
import com.example.town_application.service.MotionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/utility")
public class UtilityControler {
    ActionTypeService actionTypeService;
    MotionStateService motionStateService;
    MotionTypeService motionTypeService;

    @Autowired
    public void setActionTypeService(ActionTypeService actionTypeService) {
        this.actionTypeService = actionTypeService;
    }
    @Autowired
    public void setMotionStateService(MotionStateService motionStateService) {
        this.motionStateService = motionStateService;
    }
    @Autowired
    public void setMotionTypeService(MotionTypeService motionTypeService) {
        this.motionTypeService = motionTypeService;
    }

    @GetMapping("/getAllMotionStates")
    public List<MotionStateDTO> getAllMotionStates() {
        return motionStateService.getAllMotionStates();
    }
    @GetMapping("/getAllMotionTypes")
    public List<MotionTypeDTO> getAllMotionTypes() {
        return motionTypeService.getAllMotionTypes();
    }
    @GetMapping("/getAllActionTypes")
    public List<ActionTypeDTO> getAllActionTypes() {
        return actionTypeService.getAllActionTypes();
    }



}
