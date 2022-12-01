package com.example.town_application.controller;


import com.example.town_application.WIP.requests.motion.AddMotionRequest;
import com.example.town_application.WIP.requests.motion.UpdateMotionStateRequest;
import com.example.town_application.WIP.requests.personalData.AddPersonRequest;
import com.example.town_application.model.dto.MotionDetails;
import com.example.town_application.model.dto.PersonalDataWithoutID;
import com.example.town_application.service.MotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/motion")
public class MotionController {
    MotionService motionService;

    @Autowired
    public void setMotionService(MotionService motionService) {
        this.motionService = motionService;
    }

    @GetMapping("/getalldata")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MotionDetails> getAll(@RequestParam Integer page) {
        return motionService.getAll(page);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMotion(@Valid @RequestBody AddMotionRequest addMotionRequest, HttpServletRequest request) {
        return motionService.addMotion(addMotionRequest, request);
    }

    @PutMapping ("/updatestatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatemotionstatus(@RequestBody UpdateMotionStateRequest updateMotionStateRequest, HttpServletRequest request) {
        return motionService.updatemotionstatus(updateMotionStateRequest, request);
    }



}
