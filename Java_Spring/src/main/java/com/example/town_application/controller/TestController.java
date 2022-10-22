package com.example.town_application.controller;

import com.example.town_application.model.AccountType;
import com.example.town_application.service.TestServis;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class TestController {


    private final TestServis orderService;

    @GetMapping("/statuses")
    public List<AccountType> getStatuses(){
        return orderService.getOrderStatuses();
    }
}
