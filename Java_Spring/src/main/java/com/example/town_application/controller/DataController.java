package com.example.town_application.controller;

import com.example.town_application.repository.MotionTypeRepository;
import com.example.town_application.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO: Verify permission level for every method
@RestController
@RequestMapping(path = "/data")
public class DataController {

    private final DataService dataService;
    private final MotionTypeRepository motionTypeRepository;

    @Autowired
    public DataController(DataService dataService, MotionTypeRepository motionTypeRepository) {
        this.dataService = dataService;
        this.motionTypeRepository = motionTypeRepository;
    }

    @GetMapping("/motionTypes")
    public ResponseEntity<Object> getMotionTypes(@RequestParam String authToken){
        return dataService.getAll(motionTypeRepository, authToken, 0);
    }

}